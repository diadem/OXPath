/*
 * Copyright (c)2011, DIADEM Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the DIADEM team nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DIADEM Team BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Package containing core OXPath functionality
 */
package uk.ac.ox.comlab.diadem.oxpath.core;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;

import diadem.common.web.WebBrowser;
import diadem.common.web.dom.DOMDocument;
import diadem.common.web.dom.DOMElement;
import diadem.common.web.dom.DOMNode;
import diadem.common.web.dom.DOMWindow;

import uk.ac.ox.comlab.diadem.oxpath.core.domlookup.DOMLookup;
import uk.ac.ox.comlab.diadem.oxpath.core.domlookup.DOMLookupDocumentPosition;
import uk.ac.ox.comlab.diadem.oxpath.core.domlookup.NodeReference;
import uk.ac.ox.comlab.diadem.oxpath.core.extraction.Extractor;
import uk.ac.ox.comlab.diadem.oxpath.core.extraction.OXPathExtractor;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATState;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalIterative;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalSet;
import uk.ac.ox.comlab.diadem.oxpath.dom.ActionEngine;
import uk.ac.ox.comlab.diadem.oxpath.dom.ActionKeywords;
import uk.ac.ox.comlab.diadem.oxpath.dom.FieldTypes;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathNodeList;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes;
import uk.ac.ox.comlab.diadem.oxpath.model.language.Action;
import uk.ac.ox.comlab.diadem.oxpath.model.language.ActionType;
import uk.ac.ox.comlab.diadem.oxpath.model.language.OXPathExtractionMarker;
import uk.ac.ox.comlab.diadem.oxpath.parser.OXPathParser;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTBinaryOpExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTExpression;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathActionPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathExtractionMarker;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathKleeneStarPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathNodeTestOp;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTRelativeOXPathLocationPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTSimpleOXPathStepPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathFunctionCall;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathLiteral;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathNumber;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPathExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPredicate;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPrimaryExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathUnaryExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.Node;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.SimpleNode;
import uk.ac.ox.comlab.diadem.oxpath.parser.visitor.OXPathVisitorGenericAdaptor;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathMemoizer;


/**
 * 
 * Class for evaluating OXPath queries over Abstract Syntax Trees.  Used
 * by the OXPathNavigator objects for query evaluation.  Encodes the eval 
 * function of the PAAT Visitor (and calls eval_)
 * @author AndrewJSel
 *
 */
public class PAATEvalVisitor extends OXPathVisitorGenericAdaptor<PAATStateEvalSet, OXPathType> {

	/**
	 * Call this method to instantiate a new {@code PAATEvalVisitor} instance to evaluate OXPath expressions.  Implements the PAAT algorithm detailed in the OXPath VLDB '11 paper.  
	 * While possible to evaluate multiple OXPath expressions with the same 
	 * object, <b>HIGHLY RECOMMEND</b> using a new TreeWalker object for each expression evaluated through calls to this method, even if the browser, logger, and output stream are all 
	 * reused.
	 * @param browser browser object to evaluate on OXPath
	 * @param iLogger the logging environment to pass messages to
	 * @param os the output stream to pipe away any {@code OXPathExtractionNode} instances
	 * @return a new {@code PAATEvalVisitor} instance for evaluating an OXPath expression 
	 */
	public static PAATEvalVisitor newInstance(WebBrowser browser, Logger iLogger, ObjectOutputStream os) {
		return new PAATEvalVisitor(browser, iLogger, os);
	}


	/**
	 * Constructor for initiating new PAATEvalVisitor object; must pass the PAAT Visitor a {@code WebBrowser} object to evaluate the expression over, 
	 * a {@code Logger} environment to pass logging information, and {@code ObjectOutputStream} to pipe away any {@code OXPathExtractionNode} instances.
	 * @param browser browser object to evaluate on OXPath
	 * @param iLogger the logging environment to pass messages to
	 * @param os the output stream to pipe away any {@code OXPathExtractionNode} instances
	 */
	private PAATEvalVisitor(WebBrowser browser, Logger iLogger, ObjectOutputStream os) {
		this.webclient = browser;
		this.logger = iLogger;
		this.extractor = OXPathMemoizer.memoize(new OXPathExtractor(os));
		this.eval_visitor = OXPathMemoizer.memoize(new PAATEval_Visitor(this,this.extractor));
	}

	/**
	 * Evaluates <tt>SimpleNode</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(SimpleNode node, PAATStateEvalSet data) throws OXPathException {
		//this should never happen
		throw new OXPathException("Evaluated SimpleNode visit in PAATEvalVisitor - unexpected AST node" + node.getClass() +" parsed!");
	}

	/**
	 * Evaluates <tt>ASTScript</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTExpression node, PAATStateEvalSet data) throws OXPathException {
		//decorate the tree with positional function information
		new PrePAATVisitor().accept(node, null);
		OXPathType result = this.accept(node.jjtGetChild(0), data);
		this.endWalk();
		return result;
	}

	/**
	 * Evaluates <tt>ASTRelativeOXPathLocationPath</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTRelativeOXPathLocationPath node,
			PAATStateEvalSet data) throws OXPathException {
		int numChild = 0;
		if (data.getContextSet().isEmpty()) return OXPathType.EMPTYRESULT;
		//first handle, if this is an absolute path
		OXPathNodeList context;
		if (node.isAbsolutePath()) {
			context = new OXPathNodeList();
			OXPathContextNode firstDomNode = data.getContextSet().first();
			context.add(new OXPathContextNode(firstDomNode.getNode().getOwnerDocument().getDocumentElement(),firstDomNode.getParent(),firstDomNode.getLast()));
		} 
		else context = data.getContextSet();
		//next, handle any simple expression via eval_
		OXPathNodeList simpleResult;
		if (node.hasSimplePath()) {
			simpleResult = new OXPathNodeList();
			int astSimple = numChild++;
			Iterator<OXPathContextNode> iterator = context.iterator();
			while (iterator.hasNext()) {
				OXPathContextNode c = iterator.next();
				boolean newProtect = (iterator.hasNext())?true:data.isDocumentProtected();
				PAATStateEvalIterative newState = new PAATState.Builder(data).setContextNode(c).setDocumentProtect(newProtect).buildNode();
				simpleResult.addAll(
						this.eval_visitor.eval_(c.getNode(), node.jjtGetChild(astSimple), newState).nodeList());
			}
		}
		else simpleResult = context;
		//finally, handle any complex expression
		OXPathType complexResult;
		if (node.hasComplexPath()) {
			complexResult = this.accept(node.jjtGetChild(numChild++), new PAATState.Builder(data).setContextSet(simpleResult).buildSet());
		}
		else complexResult = new OXPathType(simpleResult);
		return complexResult;
	}

	/**
	 * Evaluates <tt>ASTSimpleOXPathStepPathAspect</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTSimpleOXPathStepPath node, PAATStateEvalSet data) throws OXPathException {
		//this should never happen
		throw new OXPathException("Evaluated simple oxpath evaluation with set-based eval function in PAAT");
	}

	/**
	 * Evaluates <tt>ASTOXPathKleeneStarPath</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTOXPathKleeneStarPath node, PAATStateEvalSet data) throws OXPathException {
		OXPathNodeList context = data.getContextSet();
		OXPathNodeList result = new OXPathNodeList();
		if (context.isEmpty()) return OXPathType.EMPTYRESULT;
		if (OXPathParser.hasActionOnMainPath(node.jjtGetChild(0))) {
			int lower = node.getLowerBound();
			int higher = node.getUpperBound();
			//the only time we evaluate following, otherwise the inner actions do
			if (lower==0 && node.hasFollowingPath()) result.addAll(this.accept(node.jjtGetChild(1), new PAATState.Builder(data).setContextSet(context).setDocumentProtect(true).buildSet()).nodeList());
			//the inner actions evaluate the rest
			
			result.addAll(this.accept(node.jjtGetChild(0), new PAATState.Builder(data).setContextSet(context).setNumKleeneStarIterations(higher).buildSet()).nodeList());
			return new OXPathType(result);
		}
		else {
			if (node.getLowerBound()<1) result.addAll(context);
			for (int i=0; i<node.getUpperBound() && !context.isEmpty(); i++) {
				PAATStateEvalSet state = new PAATState.Builder(data).setContextSet(context).setDocumentProtect((node.hasFollowingPath() || i < node.getUpperBound()-1)?true:data.isDocumentProtected()).buildSet();
				context = this.accept(node.jjtGetChild(0), state).nodeList();
				if (i>=node.getLowerBound()) {
					context.removeAll(result);
					result.addAll(context) ;
				}
			}
			if (!node.hasFollowingPath()) return new OXPathType(result);
			else return this.accept(node.jjtGetChild(1), new PAATState.Builder(data).setContextSet(result).buildSet());
		}
	}

	/**
	 * Evaluates <tt>ASTOXPathActionPath</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTOXPathActionPath node, PAATStateEvalSet data) throws OXPathException {
		if (data.isActionFreeNavigation()) {
			if (data.getActionFreePrefixEnd().equals(node) || !node.hasTail()) return new OXPathType(data.getContextSet());
			else return this.accept(node.jjtGetChild(0), data);
		}
		else {
			OXPathNodeList context = data.getContextSet();
			OXPathNodeList result = new OXPathNodeList();
			if (context.isEmpty()) return OXPathType.EMPTYRESULT;
			WebBrowser actionSetBrowser = (node.getAction().getActionType().equals(ActionType.URL))?null:context.first().getNode().getOwnerDocument().getEnclosingWindow().getBrowser();
			ArrayList<NodeReference> references = this.domlookup.getNodeReferences(context);
			for (int i=0; i<context.size(); i++) {
				OXPathContextNode c = (node.getAction().getActionType().equals(ActionType.URL))?OXPathContextNode.getNotionalContext():references.get(i).getRenderedNode(actionSetBrowser.getContentDOMWindow().getDocument());
				boolean newProtect = (i<context.size()-1)?true:data.isDocumentProtected();
				OXPathContextNode newNode = this.takeAction(c, node.getAction(), newProtect, data.getCurrentAction());
				final int newCurrentAction = this.currentAction;
				if (!node.getAction().isAbsoluteAction()) {//calculate AFP
					PAATStateEvalSet afpState = new PAATState.Builder(data).setContextSet(new OXPathNodeList(newNode)).setIsActionFreeNavigation(true).setCurrentAction(newCurrentAction).setActionFreePrefixEnd(node).buildSet();
					OXPathNodeList afpSet = this.accept(data.getActionFreePrefix(), afpState).nodeList();
					//multi-way set based evaluation doesn't happen often and aren't big sets, but they are expensive
					if (afpSet.isEmpty()) continue;//we continue if there is no element after this AFP
					Iterator<OXPathContextNode> iterator = afpSet.iterator();
					OXPathContextNode afpNode = iterator.next();
					try {
						for (int j=0; j<i;j++) {
							afpNode=iterator.next();
						}
					} catch (NoSuchElementException e) {
						continue;//we continue if there is no element after this AFP
					}
					//because we don't do the extraction markers, these won't come back correct if there are extraction markers in the AFP
					newNode = new OXPathContextNode(afpNode.getNode(),c.getParent(),c.getLast());
				}
				PAATStateEvalSet actionState;
				if (node.getAction().isAbsoluteAction()) actionState = new PAATState.Builder(data).setContextSet(new OXPathNodeList(newNode)).setDocumentProtect(false).setActionFreePrefix(node).setCurrentAction(newCurrentAction).buildSet();
				else actionState = new PAATState.Builder(data).setContextSet(new OXPathNodeList(newNode)).setDocumentProtect(false).setCurrentAction(newCurrentAction).buildSet();
				OXPathNodeList predResult;
				final boolean evalAsKleene = node.isInsideKleeneStar() && (data.getNumKleeneStarIterations() > 0);
				if (node.hasTail()) {
					predResult = this.accept(node.jjtGetChild(0), actionState).nodeList();
					if (!evalAsKleene) result.addAll(predResult);
				}
				else {
					predResult = new OXPathNodeList(newNode);
					if (!evalAsKleene) result.add(newNode);
				} 
				if (evalAsKleene && !predResult.isEmpty()) {
					//we do the rest from the Kleene-star; this is another area where we break compositionality of the language; we additionally protect the page as it is part of the Kleene's recurring context
					ASTOXPathKleeneStarPath containingKleene = (ASTOXPathKleeneStarPath)node.insideKleeneStar();
					//we only do the following if we've done lower the specified number of times (since we already checked for the 0 unwinding in the Kleene node, we've done 1 unwinding at this recursion level
					boolean doneLower = containingKleene.getLowerBound()-(containingKleene.getUpperBound()-data.getNumKleeneStarIterations())<=1;
					if (containingKleene.hasFollowingPath()  && doneLower) {
						result.addAll(this.accept(node.insideKleeneStar().jjtGetChild(1), new PAATState.Builder(actionState).setDocumentProtect(true).setContextSet(predResult).setNumKleeneStarIterations(0).setCurrentAction(newCurrentAction).buildSet()).nodeList());
					}
					else result.addAll(predResult);
					int newNumKleeneStarIterations = data.getNumKleeneStarIterations()-1;
					if (newNumKleeneStarIterations>0) this.accept(containingKleene.jjtGetChild(0), 
							new PAATState.Builder(actionState).setDocumentProtect(false).setContextSet(predResult).setNumKleeneStarIterations(newNumKleeneStarIterations).setCurrentAction(newCurrentAction).buildSet());
				}
				if (this.openActions.contains(newCurrentAction)) this.freeMem(newNode,newCurrentAction);
			}
			return new OXPathType(result);
		}
	}

	/**
	 * Evaluates <tt>ASTOXPathNodeTestOp</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTOXPathNodeTestOp node, PAATStateEvalSet data) throws OXPathException {
		//because this node can't contain a position function, we assume a later node contains the function and make the set call of the next node 
		if (!node.hasList() || !node.getSetBasedEval().equals(PositionFuncEnum.NEITHER)) throw new OXPathException("Unexpected call in PAAT to set-based OXPathNodeTestOp node");
		OXPathNodeList context = data.getContextSet();
		OXPathType result = node.getSelectorPredicate().evaluateSet(context);
		PAATStateEvalSet newState = new PAATState.Builder(data).setContextSet(result.nodeList()).buildSet();
		return this.accept(node.jjtGetChild(0), newState);
	}

	/**
	 * Evaluates <tt>ASTXPathLiteral</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathLiteral node, PAATStateEvalSet data) throws OXPathException {
		return new OXPathType(node.getValue());
	}

	/**
	 * Evaluates <tt>ASTXPathPredicate</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathPredicate node, PAATStateEvalSet data) throws OXPathException {
		//since we are doing set-based predicate eval, each node will need position and last assignment
		OXPathNodeList context = data.getContextSet();
		OXPathNodeList result = new OXPathNodeList();
		Iterator<OXPathContextNode> iteratorContext = context.iterator();
		int positionCount = 1;
		while (iteratorContext.hasNext()) {
			OXPathContextNode c = iteratorContext.next();
			//position is i+1 because XPath counting begins at 1, not 0
			PAATStateEvalSet predState = new PAATState.Builder(data).setPosition(positionCount).setLast(context.size()).setDocumentProtect((node.hasList()||iteratorContext.hasNext())?true:data.isDocumentProtected())
			.setContextSet(new OXPathNodeList(new OXPathContextNode(c.getNode(),c.getLast(),c.getLast())))
			.setDocumentProtect((node.hasList())?true:data.isDocumentProtected()).setNumKleeneStarIterations(0).buildSet();
			OXPathType predResult = this.accept(node.jjtGetChild(0), predState);
			if (predResult.isType().equals(OXPathTypes.NUMBER)) {
				if (new Integer(positionCount).doubleValue()==predResult.number()) result.add(c);
			}
			else if (predResult.booleanValue() || node.isOptional()) result.add(c);
			positionCount++;
		}
		//what we do with the result set depends on the whether the next node exists and if it is set-based or not
		if (!node.hasList() || result.isEmpty()) return new OXPathType(result);
		else if (!node.getSetBasedEval().equals(PositionFuncEnum.NEITHER)) return this.accept(node.jjtGetChild(1), new PAATState.Builder(data).setContextSet(result).buildSet());
		else {
			OXPathNodeList finalResult = new OXPathNodeList();
			Iterator<OXPathContextNode> iteratorResult = result.iterator();
			while (iteratorResult.hasNext()) {
				OXPathContextNode r = iteratorResult.next();
				//we need to account for the last in the set as this wasn't done in the step
				PAATStateEvalIterative listState = new PAATState.Builder(data).setContextNode(r).setDocumentProtect((iteratorResult.hasNext())?true:data.isDocumentProtected()).buildNode();
				finalResult.addAll(this.eval_visitor.eval_(r.getNode(), node.jjtGetChild(1), listState).nodeList());
			}
			return new OXPathType(finalResult);
		}
	}

	/**
	 * Evaluates <tt>ASTOXPathExtractionMarker</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTOXPathExtractionMarker node, PAATStateEvalSet data) throws OXPathException {
		OXPathNodeList contextSet = data.getContextSet();
		OXPathExtractionMarker marker = node.getExtractionMarker();
		//we avoid all of this if in AFP
		if (data.isActionFreeNavigation()) {
			if (node.hasList()) return this.accept(node.jjtGetChild((marker.isAttribute())?1:0), data);
			else return new OXPathType(contextSet);
		}
		//apply the extraction marker for each node in the set
		OXPathNodeList newContext = new OXPathNodeList();
		Iterator<OXPathContextNode> iteratorContext = contextSet.iterator();
		while (iteratorContext.hasNext()) {
			OXPathContextNode context = iteratorContext.next();
			int numChild = 0;
			int newLastSibling;
			if (marker.isAttribute()) {
				PAATStateEvalSet newState = new PAATState.Builder(data).setContextSet(new OXPathNodeList(context))//i+1 for position due to OXPath counting beginning at 1
				.setDocumentProtect((node.hasList() || iteratorContext.hasNext())?true:data.isDocumentProtected()).buildSet();
				newLastSibling = this.extractor.extractNode(context.getNode(), marker.getLabel(),context.getParent(),
						this.accept(node.jjtGetChild(numChild++), newState).toPrettyHtml());
			}
			else {
				newLastSibling = this.extractor.extractNode(context.getNode(), marker.getLabel(),context.getParent());
			}
			//new last has to be accounted for
			newContext.add(new OXPathContextNode(context.getNode(),context.getParent(),newLastSibling));	
		}
		if (node.hasList()) {//if there are following simple parts of the expression
			if (!node.getSetBasedEval().equals(PositionFuncEnum.NEITHER)) return this.accept(node.jjtGetChild((marker.isAttribute())?1:0), new PAATState.Builder(data).setContextSet(newContext).buildSet());
			else {//the positional predicate was in the attribute and we switch back to iterative evaluation
				OXPathNodeList finalResult = new OXPathNodeList();
				Iterator<OXPathContextNode> iteratorResult = newContext.iterator();
				while (iteratorResult.hasNext()) {
					OXPathContextNode newNode = iteratorResult.next();
					PAATStateEvalIterative newState = new PAATState.Builder(data).setContextNode(newNode).setDocumentProtect((iteratorResult.hasNext())?true:data.isDocumentProtected()).buildNode();
					finalResult.addAll(this.eval_visitor.eval_(newNode.getNode(), node.jjtGetChild((marker.isAttribute())?1:0), newState).nodeList());
				}
				return new OXPathType(finalResult);
			}
		}
		else return new OXPathType(newContext);
	}

	/**
	 * Evaluates <tt>ASTXPathUnaryExpr</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathUnaryExpr node, PAATStateEvalSet data) throws OXPathException {
		OXPathType result = this.accept(node.jjtGetChild(0), data);
		for (int i = 0; i < node.getNumberOperators(); i++) {
			result = node.getUnaryOperator().evaluate(result);
		}
		return result;
	}

	/**
	 * Evaluates <tt>ASTXPathPrimaryExpr</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathPrimaryExpr node, PAATStateEvalSet data) throws OXPathException {
		return this.accept(node.jjtGetChild(0), data);
	}

	/**
	 * Evaluates <tt>ASTXPathNumber</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathNumber node, PAATStateEvalSet data) throws OXPathException {
		return new OXPathType(node.getValue());
	}

	/**
	 * Evaluates <tt>ASTXPathFunctionCall</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathFunctionCall node, PAATStateEvalSet data) throws OXPathException {
		if (!node.getFunction().checkParameterCount(node.getNumParameters())) 
			throw new OXPathException("Unexpected number of parameters: " + node.getNumParameters() 
					+ " for function: " + node.getFunction().getName());
		ArrayList<OXPathType> args = new ArrayList<OXPathType>();
		for (int i=0; i < node.getNumParameters(); i++) {
			args.add(this.accept(node.jjtGetChild(i), data));
		}
		return node.getFunction().evaluate(args, data);
	}

	/**
	 * Evaluates <tt>ASTXPathUnionExpr</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTBinaryOpExpr node, PAATStateEvalSet data) throws OXPathException {
		return node.getBinaryOperator().evaluate(this.accept(node.jjtGetChild(0), data), this.accept(node.jjtGetChild(1), data));
	}

	/**
	 * Evaluates <tt>ASTXPathPathExpr</tt> types in the AST
	 * @param node query node
	 * @param data the PAAT-specific state information at {@code node}
	 * @return the result of the evaluation at {@code node}
	 */
	@Override
	public OXPathType visitNode(ASTXPathPathExpr node, PAATStateEvalSet data) throws OXPathException {
		//This one is basically the set-based version of RelativeOXPath expression; due to JavaCC node creation, it was unclear how to replace the second part with a RelativeOXPath node
		OXPathNodeList context = this.accept(node.jjtGetChild(0), data).nodeList();
		int numChild = 1;//we've already evaluated the first child
		//next, handle any simple expression via eval_
		OXPathNodeList simpleResult;
		if (node.hasSimpleList()) {
			simpleResult = new OXPathNodeList();
			int astSimple = numChild++;
			Iterator<OXPathContextNode> iterator = context.iterator();
			while (iterator.hasNext()) {
				OXPathContextNode c = iterator.next();
				boolean newProtect = (iterator.hasNext())?true:data.isDocumentProtected();
				PAATStateEvalIterative newState = new PAATState.Builder(data).setContextNode(c).setDocumentProtect(newProtect).buildNode();
				simpleResult.addAll(this.eval_visitor.eval_(c.getNode(), node.jjtGetChild(astSimple), newState).nodeList());
			}
		}
		else simpleResult = context;
		//finally, handle any complex expression
		OXPathType complexResult;
		if (node.hasComplexList()) {
			complexResult = this.accept(node.jjtGetChild(numChild++), new PAATState.Builder(data).setContextSet(simpleResult).buildSet());
		}
		else complexResult = new OXPathType(simpleResult);
		return complexResult;
	}

	/**
	 * In order to trigger the proxy override via reflection (which doesn't happen with internal calls),
	 * this method is used whenever eval_ needs to be called (for either PAAT visitor).   
	 * @param context the context node (this is redundant info - already encoded in state - but allows 
	 * the proxy to cache the values in such a way that they can be cleared by page.
	 * @param astNode the node in the AST where we are at for evaluation
	 * @param state the EvalState at this point
	 * @return the output of eval_ at this point
	 * @throws OXPathException in case of exception
	 */
	public OXPathType eval_(DOMNode context, Node astNode, PAATStateEvalIterative state) throws OXPathException {
		return this.eval_visitor.eval_(context, astNode, state);
	}

	/**
	 * Facilitates action evaluation
	 * @param contextNode node for action
	 * @param action action to perform
	 * @param protect {@code true} if page is protected, {@code false} otherwise
	 * @param actionID identifier for the action to close
	 * @return the notional root element of the resulting document
	 * @throws OXPathException in case of other exception
	 */
	private OXPathContextNode takeAction(OXPathContextNode contextNode, Action action, boolean protect, int actionID) throws OXPathException{
		try {
			//first, handle URL actions
			if (action.getActionType().equals(ActionType.URL)) {
				this.webclient.navigate((String)action.getValue(),true);
				DOMNode newRoot = this.webclient.getContentDOMWindow().getDocument().getDocumentElement();
				return new OXPathContextNode(newRoot,contextNode.getParent(),contextNode.getLast());
			}
			DOMElement context = (DOMElement)contextNode.getNode();
			int parentExtract = contextNode.getParent();
			int lastExtract = contextNode.getLast();
			DOMElement page = context.getOwnerDocument().getDocumentElement();
			FieldTypes ft = ActionEngine.getFieldType(context);
			DOMElement newPage;
			switch (action.getActionType()) {
			case POSITION :
				newPage = ActionEngine.takeAction(context, ft, (Integer)action.getValue());
				break;
			case EXPLICIT :
				newPage = ActionEngine.takeAction(context, ft, (String)action.getValue());
				break;
			case KEYWORD :
				newPage = ActionEngine.takeAction(context, ft, (ActionKeywords)action.getValue());
				break;
			default://in case we have an ungrounded variable action
				newPage = page;
			}
			this.openActions.add(++this.currentAction);
			if (action.hasWait()) {
				long current = System.nanoTime();
				while (System.nanoTime() - current < action.getWait()*1E9) {}//waits are in seconds
			}
			DOMDocument newDocument = newPage.getOwnerDocument();
			DOMWindow newWindow = newDocument.getEnclosingWindow();
			WebBrowser newBrowser = newWindow.getBrowser();
			if (newWindow.isJustOpened()) {
				if (!protect) freeMem(page.getOwnerDocument(), actionID);
			}
			else {
				if (protect) {
					if (!this.backController.containsKey(newBrowser)) this.backController.put(newBrowser,new Stack<Integer>());
					this.backController.get(newBrowser).push(1);//we clear nothing
				}
				else {
					this.clearObjectMem(page.getOwnerDocument(), actionID);
					if (this.backController.containsKey(newBrowser) && !this.backController.get(newBrowser).isEmpty()) {
						this.backController.get(newBrowser).push(this.backController.get(newBrowser).pop()+1);
					}
				}
			}
			return new OXPathContextNode(newPage,parentExtract,lastExtract);
		}
		catch (IOException e) {
			throw new OXPathException("Error executing action: " + e );
		}
	}

	/**
	 * Clears local object memory associated with the page
	 * @param page the page to clear memory from local data structures
	 * @param actionID identifier for the action to close
	 */
	private void clearObjectMem(DOMDocument page, int actionID) {
		this.eval_visitor.clear(page);
		this.extractor.clear(page);
		this.openActions.remove(actionID);
	}

	/**
	 * This method clears buffered information associated with a specific web page (closes page and any memoized info)
	 * @param page the page to close and remove buffered info from
	 * @param actionID identifier for the action to close
	 */
	private void freeMem(DOMDocument page, int actionID) {
		try {
			this.clearObjectMem(page,actionID);
			WebBrowser currentBrowser =  page.getEnclosingWindow().getBrowser();
			if (this.backController.containsKey(currentBrowser) && !this.backController.get(currentBrowser).isEmpty()) {
					int backs = this.backController.get(currentBrowser).pop();
					for (int i = 0; i < backs; i++) {
						currentBrowser.back(true);
					}
			}
			else {
				this.backController.remove(currentBrowser);
				page.getEnclosingWindow().close();
			}
		} catch (NullPointerException e) {
			logger.debug("Trying to close a window that was already closed!");
		}
	}

	/**
	 * free Memory from a page given an arbitrary {@code OXPathContextNode} from the page.
	 * @param context an {@code OXPathContextNode} from which to clear associated page memory
	 * @param actionID identifier for the action to close
	 */
	private void freeMem(OXPathContextNode context, int actionID) {
		try {
			if (context.getNode().getOwnerDocument()!=null) {//the page hasn't been closed yet
				DOMDocument oldPage = context.getNode().getOwnerDocument();
				this.freeMem(oldPage, actionID);
			}
		}
		catch (NullPointerException e) {}//we don't care if something was null; we've already cleared it
	}

	/**
	 * Closes open windows and finishes query evaluation
	 * @return same <tt>TreeWalker</tt> object with WebClient closed properly
	 * @throws OXPathException in case of problems closing objects/stream
	 */
	private void endWalk() throws OXPathException {
		this.extractor.endExtraction();
	}

	/**
	 * {@code PAATEval_Visitor} for calls of eval_ as dictated by PAAT
	 */
	private final PAATEval_ eval_visitor;

	/**
	 * initial WebClient object for TreeWalker instance, before a page is fetched
	 */
	private WebBrowser webclient;

	/**
	 * stores the number of the current last node number assigned
	 */
	int nodeNum = 0;
	/**
	 * handles the extraction
	 */
	Extractor extractor;

	/**
	 * slf4j logger
	 */
	private Logger logger;

	/**
	 * Holds state for controlling page memory management; synchronizes 
	 */
	private Map<WebBrowser,Stack<Integer>> backController = new HashMap<WebBrowser,Stack<Integer>>();

	/**
	 * (Currently) global object facilitating DOM node refreshes (after back instantiations)
	 */
	private DOMLookup domlookup = new DOMLookupDocumentPosition();
	
	/**
	 * Holds currently "open" actions, so we know if the freeMem() call at the end of an action sequence is necessary
	 */
	private Set<Integer> openActions = new HashSet<Integer>();
	
	/**
	 * Counter for actions (serves as a unique id for open actions in the {@code openActions} set
	 */
	private int currentAction = 0;
}
