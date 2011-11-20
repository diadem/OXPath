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

import java.util.Iterator;

import diadem.common.web.dom.DOMDocument;
import diadem.common.web.dom.DOMNode;
import uk.ac.ox.comlab.diadem.oxpath.core.extraction.Extractor;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATState;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalIterative;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalSet;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathNodeList;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.model.language.OXPathExtractionMarker;
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

/**
 * 
 * Visitor encoding the eval_ function of the PAAT algorithm.  Called by the 
 * PAATEvalVisitor, but wrapped in a dynamic proxy of a {PAATEval_Wrapper} object.
 * @author AndrewJSel
 *
 */
public class PAATEval_Visitor extends OXPathVisitorGenericAdaptor<PAATStateEvalIterative, OXPathType> implements PAATEval_ {

	/**
	 * basic constructor, with references to the calling visitor, the extractor, and the wrapper object
	 * @param paateval the calling {@code PAATEvalVisitor}
	 * @param ext the extractor that handles output
	 */
	public PAATEval_Visitor(PAATEvalVisitor paateval, Extractor ext) {
		this.paatSet=paateval;
		this.extractor=ext;
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(SimpleNode node, PAATStateEvalIterative data)
	throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTExpression node, PAATStateEvalIterative data)
	throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTRelativeOXPathLocationPath node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTSimpleOXPathStepPath node,
			PAATStateEvalIterative data) throws OXPathException {
		//get the results by OXPath step first
		OXPathType newContext = data.getContextNode().getByOXPath(node.getStep());
		//immediately return if no results or no further path
		if (newContext.nodeList().isEmpty() || !node.hasList()) return newContext;
		OXPathNodeList result = new OXPathNodeList();
		//we apply PAAT eval_ as normal
		if (node.getSetBasedEval().equals(PositionFuncEnum.NEITHER)) {
			Iterator<OXPathContextNode> iterator = newContext.nodeList().iterator();
			while (iterator.hasNext()) {
				OXPathContextNode c = iterator.next();
				boolean newProtect = (iterator.hasNext())?true:data.isDocumentProtected();
				PAATStateEvalIterative newState = new PAATState.Builder(data).setContextNode(c)
				.setDocumentProtect(newProtect).buildNode();
				result.addAll(this.paatSet.eval_(c.getNode(), node.jjtGetChild(0), 
						newState).nodeList());
			}
		}
		//otherwise, we take a set based approach
		else {
//			//JavaScript returns unsorted lists - We move the sorting here (this is the only time we need to do this because of position() and last())
//			if (node.getStep().getAxis().getType().equals(AxisType.BACKWARD)) newContext.nodeList().sortReverseOrder();
			PAATStateEvalSet newState = new PAATState.Builder(data).setContextSet(newContext.nodeList()).buildSet();
			result.addAll(this.paatSet.accept(node.jjtGetChild(0), newState).nodeList());
		}
		return new OXPathType(result);
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTOXPathKleeneStarPath node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTOXPathActionPath node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTOXPathNodeTestOp node,
			PAATStateEvalIterative data) throws OXPathException {/* we don't have to worry about set-based eval here, as this is only called when not a set and a later predicate in same step is not set-based eval*/
		OXPathType result = node.getSelectorPredicate().evaluateIterative(data.getContextNode());
		if (result.nodeList().isEmpty()) return OXPathType.EMPTYRESULT;
		if (node.hasList()) {
			return this.paatSet.eval_(data.getContextNode().getNode(), node.jjtGetChild(0), data);
		}
		else return result;
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathLiteral node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathPredicate node,
			PAATStateEvalIterative data) throws OXPathException {
		OXPathContextNode context = data.getContextNode();
		
		PAATStateEvalSet predState = new PAATState.Builder(data).setPosition(0).setLast(0).setDocumentProtect((node.hasList())?true:data.isDocumentProtected())
											.setContextSet(new OXPathNodeList(new OXPathContextNode(context.getNode(),context.getLast(),context.getLast())))
											.setDocumentProtect((node.hasList())?true:data.isDocumentProtected()).buildSet();
		OXPathType predResult = this.paatSet.accept(node.jjtGetChild(0), predState);
		if(!predResult.booleanValue() && !node.isOptional()) return OXPathType.EMPTYRESULT;
		if (node.hasList()) return this.paatSet.eval_(context.getNode(), node.jjtGetChild(1), data);
		else return new OXPathType(context);
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTOXPathExtractionMarker node,
			PAATStateEvalIterative data) throws OXPathException {/* we don't have to worry about set-based eval here, as this is only called when not a set and a later predicate in same step is not set-based eval*/
		OXPathContextNode context = data.getContextNode();
		OXPathExtractionMarker marker = node.getExtractionMarker();
		if (data.isActionFreeNavigation()) {//only process the extraction markers if this isn't action free prefix navigation
			if (node.hasList()) return this.paatSet.eval_(context.getNode(), node.jjtGetChild((marker.isAttribute())?1:0), data);
			else return new OXPathType(context);
		}
		int numChild = 0;
		int newLastSibling;
		if (marker.isAttribute()) {
			PAATStateEvalSet newState = new PAATState.Builder(data).setContextSet(new OXPathNodeList(context))
					.setDocumentProtect((node.hasList())?true:data.isDocumentProtected()).buildSet();
			newLastSibling = this.extractor.extractNode(context.getNode(), marker.getLabel(),context.getParent(),
					this.paatSet.accept(node.jjtGetChild(numChild++), newState).toPrettyHtml());
		}
		else {
			newLastSibling = this.extractor.extractNode(context.getNode(), marker.getLabel(),context.getParent());
		}
		//new last has to be accounted for
		OXPathContextNode newContext = new OXPathContextNode(context.getNode(),context.getParent(),newLastSibling);
		if (node.hasList()) {
			PAATStateEvalIterative newState = new PAATState.Builder(data).setContextNode(newContext).buildNode();
			return this.paatSet.eval_(newContext.getNode(), node.jjtGetChild(numChild++), newState);
		}
		else return new OXPathType(newContext);
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTBinaryOpExpr node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathUnaryExpr node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathPrimaryExpr node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathNumber node, PAATStateEvalIterative data)
	throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathFunctionCall node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}

	/**
	 * Computes the values for the AST at this given state as dictated by eval_ in PAAT
	 * @param node the AST node upon which evaluation occurs
	 * @param data state information at this point in the evaluation
	 * @return the result of evaluation at this node
	 * @throws OXPathException in case of OXPath processing error
	 */
	@Override
	public OXPathType visitNode(ASTXPathPathExpr node,
			PAATStateEvalIterative data) throws OXPathException {
		throw new OXPathException("Unexpected call made to eval_ over node type " + node.getClass());
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ox.comlab.diadem.oxpath.utils.OXPathCache#clear(diadem.common.web.dom.DOMDocument)
	 */
	/**
	 * Clears all memoized data for the input {@code DOMDocument}.  Will be overridden as long as a proxy object is used (should 
	 * always be used for OXPath classes overridding this method).
	 * @param page {@code DOMDocument} we are removing all memoized results for, presumably because the page is being closed in PAAT
	 */
	@Override
	public Boolean clear(DOMDocument page) {
		// This should never be called outside the proxy
		throw new RuntimeException("Don't use the clear(page) method of the " + this.getClass().toString() + " outside of a proxy class");
	}
	
	/**
	 * Need to call this method to check children so that memoization is applied
	 * @param context the context node
	 * @param astNode the node in the AST to evaluate over
	 * @param state the state of the evaluation
	 * @return evaluation result
	 * @throws OXPathException in case of exception in evaluation
	 */
	@Override
	public OXPathType eval_(DOMNode context, Node astNode,
			PAATStateEvalIterative state) throws OXPathException {
		return this.accept(astNode, state);
	}

	/**
	 * The eval visitor that calls this method (the complementary object in the mutually recursive pair)
	 */
	private final PAATEvalVisitor paatSet;
	/**
	 * The object responsible for piping out OXPath extraction nodes
	 */
	private final Extractor extractor;
}
