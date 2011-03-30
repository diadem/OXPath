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
 * 
 */
package uk.ac.ox.comlab.oxpath.oxpathTreeWalker;

import static uk.ac.ox.comlab.oxpath.oxpathTreeWalker.OXPathType.OXPathTypes.BOOLEAN;
import static uk.ac.ox.comlab.oxpath.oxpathTreeWalker.OXPathType.OXPathTypes.NODESET;
import static uk.ac.ox.comlab.oxpath.oxpathTreeWalker.OXPathType.OXPathTypes.STRING;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.getChildByName;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.getJJTree;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.getJJTreeFromString;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.getNodeID;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.getValue;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.hasChildByName;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.hasDescendantOrSelfByName;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.toSimpleNode;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTABBREVIATEDABSOLUTEOXPATHLOCATIONPATH;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTGROUNDEDOXPATHACTIONEXPLICIT;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHACTIONKEYWORD;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHACTIONUNTILPRED;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHACTIONWAITPRED;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHEXTRACTIONMARKER;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHNODETESTOP;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHOPTIONALPREDICATE;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHSTEP;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHURLACTION;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTOXPATHUSERACTION;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTPATHDECLARATION;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTRELATIVEOXPATHLOCATIONPATH;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTSCRIPT;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTUNGROUNDEDOXPATHACTIONPOSITION;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHADDITIVEEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHANDEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHEQUALITYEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHFILTEREXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHFUNCTIONCALL;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHLITERAL;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHMULTIPLICATIVEEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHNUMBER;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHOREXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHPATHEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHPREDICATE;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHPRIMARYEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHRELATIONALEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHUNARYEXPR;
import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripterTreeConstants.JJTXPATHUNIONEXPR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.ox.comlab.oxpath.BadDataException;
import uk.ac.ox.comlab.oxpath.benchmark.BenchFactory;
import uk.ac.ox.comlab.oxpath.benchmark.BenchMarker;
import uk.ac.ox.comlab.oxpath.oxpathTreeWalker.OXPathType.OXPathTypes;
import uk.ac.ox.comlab.oxpath.scriptParser.Node;
import uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter;
import uk.ac.ox.comlab.oxpath.scriptParser.SimpleNode;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * Class for evaluating OXPath queries over Abstract Syntax Trees.  TreeWalker objects are mutable by private methods.  API consists of static
 * methods that evaluate OXPath queries.
 * @author AndrewJSel
 *
 */
public class TreeWalker {
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4jverbose.properties");
//		System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
		try {
			SimpleNode root = getJJTree(args[0]);			
//			System.out.println(stringDump(root,""));
			Document d = evaluateOXPathQuery(root);
//			System.out.println(getStringFromDocument(d));
//			PrintWriter out = new PrintWriter("largeQuery.xml");
			System.out.println(getStringFromDocument(d));
//			evaluateOXPathQuery("testNew1.oxp");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	} 
	
	/**
	 *  Main API method for client use.  Evaluates an OXPath expression encoded in an input file.
	 * @param in filename of the OXPath input file
	 * @return XML Document with extraction results
	 * @throws ParserConfigurationException in case parser configuration error
	 * @throws DOMException in case of XML Document exception 
	 * @throws BadDataException in case of AST structure exception
	 */
	public static Document evaluateOXPathQuery(String in) throws DOMException, ParserConfigurationException, BadDataException, Exception {
		return evaluateOXPathQuery(OXPathScripter.getJJTree(in));
	}
	
	/**
	 * Main API method for client use.  Evaluates an OXPath expression encoded as a <tt>String</tt> object.
	 * @param in OXPath expression as a <tt>String</tt> object
	 * @return XML Document with extraction results
	 * @throws ParserConfigurationException in case parser configuration error
	 * @throws DOMException in case of XML Document exception 
	 * @throws BadDataException in case of AST structure exception
	 */
	public static Document evaluateOXPathQueryFromString(String in) throws DOMException, ParserConfigurationException, BadDataException, Exception {
		return evaluateOXPathQuery(getJJTreeFromString(in));
	}
	
	/**
	 * Main API method for client use.  Evaluates an OXPath expression over an Abstract Syntax Tree.
	 * @param n root of the AST
	 * @return XML Document with extraction results
	 * @throws ParserConfigurationException in case parser configuration error
	 * @throws DOMException in case of XML Document exception 
	 * @throws BadDataException in case of AST structure exception
	 * @throws IOException in case of malformed xml
	 * @throws SAXException 
	 */
	public static Document evaluateOXPathQuery(Node n) throws ParserConfigurationException, DOMException, BadDataException, SAXException, IOException {
		TreeWalker tw = new TreeWalker();
		OXPathType result = tw.evaluate(tw.newState(new OXPathType(), n));
		switch (result.isType()) {
			case NODESET :
				if (result.nodeList().isEmpty()) System.out.println("Empty Node List returned");
				for (OXPathDomNode i : result.nodeList()) {
//					i.getNode().removeAllChildren();
					System.out.println(i.getNode().asXml());
//					if (i.getNode() instanceof HtmlTextInput) {((HtmlTextInput) i.getNode()).select();
//					System.out.println(((HtmlTextInput) i.getNode()).getSelectedText());
//					System.out.println(((HtmlPage) i.getNode().getPage()).executeJavaScript("findaandb(document.forms['inp']['pointa'].value,document.forms['inp']['pointb'].value);").getJavaScriptResult());
//					System.out.println(((HtmlPage) i.getNode().getPage()).executeJavaScript("findaandb(document.forms['inp']['pointa'].value,document.forms['inp']['pointb'].value);").getNewPage().getWebResponse().getContentAsString());}
				}
				break;
			case STRING :
			case NUMBER :
			case BOOLEAN :
				System.out.println(result.getValue());
				break;
			case NULL :
				System.out.println("NULL Context");
				break;
		}
		tw.endWalk();
		return tw.returnOutput();
//		//		return new TreeWalker().evaluate(new TreeWalkerState(new ArrayList<Node>(),))
//		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}
	
	/**
	 * Used merely to get an XML string from a document.
	 * Method posted by Ravi Srirangam at http://www.theserverside.com/discussions/thread.tss?thread_id=26060 
	 * @param doc input document
	 * @return String representation of document
	 */
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	}
	
	/**
	 * Main evaluation function that selects appropriate evaluation function based on AST node type
	 * @param state TreeWalker context for current evaluation call
	 * @return same TreeWalker object with new state.
	 * @throws BadDataException in case of malformed AST structure
	 */
	private OXPathType evaluate(TreeWalkerState state) throws BadDataException {
		int nodeID = getNodeID(toSimpleNode(state.getQueryPosition()));
//		if (state.getContext().nodeList().isEmpty() && !(nodeID==JJTOXPATHURLACTION)) return new OXPathType(); 
		switch(nodeID)
		{
			case JJTSCRIPT :
				return this.script(state);
			case JJTPATHDECLARATION :
				return this.pathDeclaration(state);
//			case JJTOXPATHPATHSEGMENT :
//				return this.oxpathPathSegment(state);
			case JJTRELATIVEOXPATHLOCATIONPATH :
				return this.relativeOXPathLocationPath(state);
//			case JJTOXPATHKLEENESTAR :
//				return this.oxpathKleeneStar(state);
			case JJTOXPATHSTEP :
				return this.oxpathStep(state);
			case JJTXPATHPREDICATE :
				return this.xpathPredicate(state);
			case JJTOXPATHOPTIONALPREDICATE :
				return this.oxpathOptionalPredicate(state);
//			case JJTOXPATHPREDICATEEXPR :
//				return this.oxpathPredicateExpr(state);
			case JJTOXPATHURLACTION :
				return this.oxpathURLAction(state);
			case JJTOXPATHUSERACTION :
				return this.oxpathUserAction(state);
			case JJTOXPATHACTIONWAITPRED :
				return this.oxpathActionWaitPred(state);
			case JJTOXPATHACTIONUNTILPRED :
				return this.oxpathActionUntilPred(state);
			case JJTOXPATHEXTRACTIONMARKER :
				return this.oxpathExtractionMarker(state);
			case JJTXPATHEXPR :
				return this.xpathExpr(state);
			case JJTXPATHOREXPR :
				return this.xpathOrExpr(state);
			case JJTXPATHANDEXPR :
				return this.xpathAndExpr(state);
			case JJTXPATHEQUALITYEXPR :
				return this.xpathEqualityExpr(state);
			case JJTXPATHRELATIONALEXPR :
				return this.xpathRelationalExpr(state);
			case JJTXPATHADDITIVEEXPR :
				return this.xpathAdditiveExpr(state);
			case JJTXPATHMULTIPLICATIVEEXPR :
				return this.xpathMultiplicativeExpr(state);
			case JJTXPATHUNARYEXPR :
				return this.xpathUnaryExpr(state);
			case JJTXPATHPRIMARYEXPR :
				return this.xpathPrimaryExpr(state);
			case JJTXPATHFUNCTIONCALL :
				return this.xpathFunctionCall(state);
			case JJTXPATHUNIONEXPR :
				return this.xpathUnionExpr(state);
			case JJTXPATHPATHEXPR :
				return this.xpathPathExpr(state);
			case JJTXPATHFILTEREXPR :
				return this.xpathFilterExpr(state);
			case JJTABBREVIATEDABSOLUTEOXPATHLOCATIONPATH :
				return this.abbreviatedAbsoluteOXPathLocationPath(state);
			case JJTXPATHNUMBER :
				return this.xpathNumber(state);
			case JJTXPATHLITERAL :
				return this.xpathLiteral(state);
		    default :
		    	throw new BadDataException("Unknown AST node (" + nodeID + ") evaluated");
		}
	}

	/**
	 * Evaluates <tt>XPathLiteral</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathLiteral(TreeWalkerState state) throws BadDataException {
		String resultRaw  = getValue(toSimpleNode(state.getQueryPosition()));
		//string off containing quotes (" or ') before continuing
		return new OXPathType(resultRaw.substring(1, resultRaw.length()-1));
	}

	/**
	 * Evaluates <tt>XPathNumber</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathNumber(TreeWalkerState state) throws BadDataException{
		String numberAsString = getValue(toSimpleNode(state.getQueryPosition()));
		Double number = Double.parseDouble(numberAsString);
		return new OXPathType(number);
	}

	/**
	 * Evaluates <tt>AbbreviatedAbsoluteOXPathLocationPath</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType abbreviatedAbsoluteOXPathLocationPath(TreeWalkerState state) throws BadDataException {
		SimpleNode next = getChildByName(toSimpleNode(state.getQueryPosition()),"RelativeOXPathLocationPath");
		OXPathType newContext = new OXPathType(state.getContext().nodeList().getByXPath("descendant-or-self::*"));
		return evaluate(newState(state).setContext(newContext).setQueryPosition(next));//.setIsDescendant(true)
	}

	/**
	 * Evaluates <tt>XPathFilterExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathFilterExpr(TreeWalkerState state) throws BadDataException {
		OXPathType newContext = evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(0)));
		Collections.sort(newContext.nodeList(), newContext.nodeList());
		//filter by each predicate
		for (int i = 1; i < state.getQueryPosition().jjtGetNumChildren(); i++) {
			SimpleNode pred = toSimpleNode(state.getQueryPosition().jjtGetChild(i));
//			if (hasChildByName(pred,"XPathPredicate")) {
				OXPathNodeList<OXPathDomNode> filterList = new OXPathNodeList<OXPathDomNode>();
				for (int j = 0; j < newContext.nodeList().size(); j++) {
					OXPathDomNode original = newContext.nodeList().get(j);
					OXPathDomNode filterNode = new OXPathDomNode(original.getNode(),original.getLast(),original.getLast());
					int p = j+1;
					int l = newContext.nodeList().size();
//					if (state.isDescendant()) {//in case of descendant or self binding less tightly than axis, must do the following
//						DomNode filterParent= (DomNode) filterNode.getNode().getFirstByXPath("..");
//						String newStepString = stepString.replace("//", "");
//						List<DomNode> siblings = filterParent.getFirstByXPath(newStepString);
//						l = siblings.size();
//						for (int s = 0; s < siblings.size(); s++) {
//							if (siblings.get(s).equals(filterNode)) p=s;
//						}
//					}
//					else {
//						p=j+1;
//						l=newContext.nodeList().size();
//					}
					//position offset by 1 because of conversion to XPath numbering from Java numbering
					TreeWalkerState newState = newState(new OXPathType(filterNode), pred, state.getActionFreePrefix(), state.getActionFreePrefixEnd(),
							state.isActionFreeNavigation(), p, l);
					windowSet.add((TopLevelWindow) filterNode.getNode().getPage().getEnclosingWindow().getTopWindow());
					if (evaluate(newState).booleanValue()) {
						//TODO: check if page is dirty; if so, find new = AFP(original), then filterList.add(new); else do below
						filterList.add(original);
					}
					windowSet.remove((TopLevelWindow) filterNode.getNode().getPage().getEnclosingWindow().getTopWindow());
					if (original.getNode().getPage().getEnclosingWindow().getTopWindow().equals(webclient.getCurrentWindow().getTopWindow())) ((TopLevelWindow) webclient.getCurrentWindow().getTopWindow()).close();
				}
				newContext.set(filterList);
			}		
		return newContext;
	}

	/**
	 * Evaluates <tt>XPathPathExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathPathExpr(TreeWalkerState state) throws BadDataException {//only have to handle the second case
		OXPathType filter = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		if (hasChildByName(toSimpleNode(state.getQueryPosition()),"DescendantOrSelfShort")) {
			filter = new OXPathType(filter.nodeList().getByXPath("descendant-or-self::*"));
		}
		SimpleNode next = getChildByName(toSimpleNode(state.getQueryPosition()),"RelativeOXPathLocationPath");
//		boolean descend = hasChildByName(toSimpleNode(state.getQueryPosition()),"DescendantOrSelfShort");
		return evaluate(newState(state).setContext(filter).setQueryPosition(next));//.setIsDescendant(descend)
	}

	/**
	 * Evaluates <tt>XPathUnionExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathUnionExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(1))));
		OXPathNodeList<OXPathDomNode> newList = new OXPathNodeList<OXPathDomNode>();
		newList.addAll(lhs.nodeList());
		newList.addAll(rhs.nodeList());
		return new OXPathType(newList);
	}

	/**
	 * Evaluates <tt>XPathFunctionCall</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathFunctionCall(TreeWalkerState state) throws BadDataException {
		String name = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0)));
		ArrayList<OXPathType> args = new ArrayList<OXPathType>();
		for (int i = 1; i < state.getQueryPosition().jjtGetNumChildren(); i++) {
			args.add(evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(i))));
		}
		OXPathNodeList<OXPathDomNode> context = state.getContext().nodeList();
		if (name.equals("position(")) {
			if (!(args.size()==0)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(state.getPosition());
		}
		else if (name.equals("last(")) {
			if (!(args.size()==0)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(state.getLast());
		}
		else if (name.equals("count(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(args.get(0).nodeList().size());
		}
		else if (name.equals("id(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(args.get(0).nodeList().getByXPath("id(.)"));/*a bit of hack; our getByXPath function builds the set
																			   *by constructing results from a single node at a time;
																			   *meant normally for navigation*/
		}
		else if (name.equals("namespace-uri(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType((String) context.get(0).getNode().getFirstByXPath("namespace-uri(.)"));
			else return new OXPathType((String) args.get(0).nodeList().get(0).getNode().getFirstByXPath("namespace-uri(.)"));
		}
		else if (name.equals("local-name(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType((String) context.get(0).getNode().getFirstByXPath("local-name(.)"));
			else return new OXPathType((String) args.get(0).nodeList().get(0).getNode().getFirstByXPath("local-name(.)"));
		}
		else if (name.equals("name(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType((String) context.get(0).getNode().getFirstByXPath("name(.)"));
			else return new OXPathType((String) args.get(0).nodeList().get(0).getNode().getFirstByXPath("name(.)"));
		}
		else if (name.equals("string(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType (state.getContext().string());
			else return new OXPathType(args.get(0).string());
		}
		else if (name.equals("concat(")) {
			if (args.size()<2) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			StringBuilder sb = new StringBuilder();
			for (OXPathType arg : args) {
				sb.append(arg.string());
			}
			return new OXPathType(sb.toString());
		}
		else if (name.equals("starts-with(")) {
			if (!(args.size()==2)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(args.get(0).string().startsWith(args.get(1).string()));
		}
		else if (name.equals("contains(")) {
			if (!(args.size()==2)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(args.get(0).string().contains(args.get(1).string()));
		}
		else if (name.equals("substring-before(")) {
			if (!(args.size()==2)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (!(args.get(0).string().contains(args.get(1).string()))) return new OXPathType(false);
			return new OXPathType(args.get(0).string().substring(0, args.get(0).string().indexOf(args.get(1).string())));
		}
		else if (name.equals("substring-after(")) {
			if (!(args.size()==2)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (!(args.get(0).string().contains(args.get(1).string()))) return new OXPathType(false);
			//have to adjust for the length of the second argument for substring after
			return new OXPathType(args.get(0).string().substring(args.get(0).string().indexOf(args.get(1).string())+args.get(1).string().length()));
		}
		else if (name.equals("substring(")) {//-1 because XPath begins string index at 1, java at 0
			if (!(args.size()==2) && !(args.size()==3)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.size()==2) return new OXPathType(args.get(0).string().substring(args.get(1).number().intValue()+1));
			else return new OXPathType(args.get(0).string().substring(args.get(1).number().intValue()-1,
					                                                  args.get(1).number().intValue()-1+args.get(2).number().intValue()));
		}
		else if (name.equals("string-length(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType(state.getContext().string().length());
			else return new OXPathType(args.get(0).string().length());
		}
		else if (name.equals("normalize-space(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType(state.getContext().string().trim());//NOTE: java trim() strips C0 control chars, xpath normalize-space() doesn't
			else return new OXPathType(args.get(0).string().trim());	
		}
		else if (name.equals("translate(")) {
			if (!(args.size()==3)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			String target = args.get(1).string();
			String replacement = args.get(2).string();
			if (replacement.length() > target.length()) replacement = replacement.substring(0, target.length());
			return new OXPathType(args.get(0).string().replace(target, replacement));
		}
		else if (name.equals("Boolean(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(args.get(1).booleanValue());
		}
		else if (name.equals("not(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(!args.get(0).booleanValue());
		}
		else if (name.equals("true(")) {
			if (!(args.size()==0)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(true);
		}
		else if (name.equals("false(")) {
			if (!(args.size()==0)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(false);
		}
		else if (name.equals("lang(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType((Boolean) context.get(0).getNode().getFirstByXPath("lang(" + args.get(0).string() + ")"));
		}
		else if (name.equals("number(")) {
			if (args.size()>1) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			if (args.isEmpty()) return new OXPathType(state.getContext().number());
		    return new OXPathType(args.get(0).number());
		}
		else if (name.equals("sum(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			double sum = 0;
			for (OXPathDomNode o : args.get(0).nodeList()) {
				double d = (Double) o.getNode().getFirstByXPath("number(.)");
				sum += d;
			}
			return new OXPathType(sum);
		}
		else if (name.equals("floor(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(Math.floor(args.get(0).number()));
		}
		else if (name.equals("ceiling(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(Math.ceil(args.get(0).number()));
		}
		else if (name.equals("round(")) {
			if (!(args.size()==1)) throw new BadDataException("Unspecified number of parameters for function " + name + ")");
			return new OXPathType(Math.round(args.get(0).number()));
		}


		throw new BadDataException("Function " + name.toString() + ") is unimplemented!");
	}

	/**
	 * Evaluates <tt>XPathPrimaryExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathPrimaryExpr(TreeWalkerState state) throws BadDataException {
		SimpleNode next = toSimpleNode(state.getQueryPosition().jjtGetChild(0));
		switch(getNodeID(next)) {
			case JJTXPATHLITERAL :
				return new OXPathType(getValue(next));
			case JJTXPATHNUMBER :
				return new OXPathType(Double.parseDouble(getValue(next)));
			default:
				return evaluate(newState(state).setQueryPosition(next));
		}
	}

	/**
	 * Evaluates <tt>XPathUnaryExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathUnaryExpr(TreeWalkerState state) throws BadDataException {
		Double num = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(state.getQueryPosition().jjtGetNumChildren()-1)))).number();
		int numOps = state.getQueryPosition().jjtGetNumChildren()-1;
		return new OXPathType(((numOps % 2)==1) ? -num : num );
	}

	/**
	 * Evaluates <tt>XPathMultiplicativeExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathMultiplicativeExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		String oper = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(1)));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(2))));
		//add or subtract based on the operator
		return new OXPathType((oper.equals("*")) ? (lhs.number() * rhs.number()) : 
			((oper.equals("div")) ? (lhs.number() / rhs.number()) : 
				(lhs.number() % rhs.number())));
	}

	/**
	 * Evaluates <tt>XPathAdditiveExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathAdditiveExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		String oper = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(1)));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(2))));
		//add or subtract based on the operator
		return new OXPathType((oper.equals("+")) ? (lhs.number() + rhs.number()) : (lhs.number() - rhs.number()));
	}

	/**
	 * Evaluates <tt>XPathRelationalExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathRelationalExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		OXPathTypes lhsType = lhs.isType();
		String oper = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(1)));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(2))));
		OXPathTypes rhsType = lhs.isType();

		//convert any nodesets to lists of numbers
		ArrayList<Double> lhsList = new ArrayList<Double>();
		ArrayList<Double> rhsList = new ArrayList<Double>();
		if (lhsType.equals(NODESET)) {
			for (OXPathDomNode o : lhs.nodeList()) {
				lhsList.add(Double.valueOf((String) o.getNode().getFirstByXPath("string(.)")));
			}
		}
		else lhsList.add(lhs.number());
		if (rhsType.equals(NODESET)) {
			for (OXPathDomNode o : rhs.nodeList()) {
				rhsList.add(Double.valueOf((String) o.getNode().getFirstByXPath("string(.)")));
				}
		}
		else rhsList.add(rhs.number());
		
		//compare the lists
		for (Double i : lhsList) {
			double l = i.doubleValue();
			for (Double j : rhsList) {
				double r = j.doubleValue();
				boolean gt = l>r;
				boolean lt = l<r;
				boolean eq = l==r;
				if (oper.equals(">") && gt) return new OXPathType(true);
				else if (oper.equals(">=") && (gt || eq)) return new OXPathType(true);
				else if (oper.equals("<") && lt) return new OXPathType(true);
				else if (oper.equals("<=") && (lt || eq)) return new OXPathType(true);
			}
		}
		return new OXPathType(false);
	}

	/**
	 * Evaluates <tt>XPathEqualityExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathEqualityExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		OXPathTypes lhsType = lhs.isType();
		String oper = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(1)));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(2))));
		OXPathTypes rhsType = rhs.isType();
		boolean isTrue = false;
		
		if (oper.equals("=") || oper.equals("!=")) {
			boolean hasNodeSet = (lhsType.equals(NODESET) || rhsType.equals(NODESET));
//			boolean hasString = (lhsType.equals(STRING) || rhsType.equals(STRING));
			boolean hasNumber = (lhsType.equals(OXPathTypes.NUMBER) || rhsType.equals(OXPathTypes.NUMBER));
			boolean hasBoolean = (lhsType.equals(BOOLEAN) || rhsType.equals(BOOLEAN));
			
			if (hasBoolean) {
				return new OXPathType((oper.equals("=")) ? lhs.booleanValue()==rhs.booleanValue() : !(lhs.booleanValue()==rhs.booleanValue()));
			}
			
			//otherwise, if we have a nodeset, and we need to at least convert the nodeset to string(.) equivalents
			ArrayList<String> lhsList = new ArrayList<String>();
			ArrayList<String> rhsList = new ArrayList<String>();
			if (hasNodeSet) {//handle the nodeset comparisons
				if (lhsType.equals(NODESET)) {
					for (OXPathDomNode o : lhs.nodeList()) {
						DomNode i = o.getNode();
						lhsList.add((String) i.getFirstByXPath("string(.)"));
					}
				}
				if (rhsType.equals(NODESET)) {
					for (OXPathDomNode o : rhs.nodeList()) {
						DomNode i = o.getNode();
						lhsList.add((String) i.getFirstByXPath("string(.)"));
					}
				}
			}
			
			if (lhsType.equals(STRING)) lhsList.add(lhs.string());
			if (rhsType.equals(STRING)) rhsList.add(rhs.string());
			
			if (!hasNumber) {//comparisons can be made directly
				for (String i : lhsList) {
					for (String j : rhsList) {
						isTrue = (oper.equals("=")) ? i.equals(j) : !i.equals(j);
						if (isTrue) return new OXPathType(true);
					}
				}
				return new OXPathType(false);
			}
			else {//we have to deal with number conversions
				if (lhsType.equals(rhsType)) {//both doubles
					return new OXPathType((oper.equals("=")) ? lhs.number().doubleValue()==rhs.number().doubleValue() : lhs.number().doubleValue()!=rhs.number().doubleValue());
				}//we proceed knowing exactly one side is a number and the other already has String values loaded into a list
				double num = (lhsType.equals(OXPathTypes.NUMBER)) ? lhs.number() : rhs.number();
				ArrayList<String> list = (lhsType.equals(OXPathTypes.NUMBER)) ? rhsList : lhsList;
				for (String i : list) {
					isTrue = (oper.equals("=")) ? (Double.parseDouble(i)==num) : (Double.parseDouble(i)!=num);
					if (isTrue) return new OXPathType(true);
				}
				return new OXPathType (false);
			}			
		}
		
		else {//equals "~=" or "#="; we get to use strings only, much easier
			//we can assume everything is strings
			if (oper.equals("~=")) {
				Scanner scan = new Scanner(lhs.string());
				while (scan.hasNext()) {
					if (scan.next().equals(rhs)) isTrue = true;
				}
				scan.close();
				return new OXPathType(true);
			}
			else {
				return new OXPathType(lhs.string().contains(rhs.string()));
			}			
		}		
	}

	/**
	 * Evaluates <tt>XPathAndExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathAndExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(1))));
		return new OXPathType(lhs.booleanValue() && rhs.booleanValue());
	}

	/**
	 * Evaluates <tt>XPathOrExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathOrExpr(TreeWalkerState state) throws BadDataException {
		OXPathType lhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
		OXPathType rhs = evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(1))));
		return new OXPathType(lhs.booleanValue() || rhs.booleanValue());
	}

	/**
	 * Evaluates <tt>XPathExpr</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathExpr(TreeWalkerState state) throws BadDataException {
		return evaluate(newState(state).setQueryPosition(toSimpleNode(state.getQueryPosition().jjtGetChild(0))));
	}

	/**
	 * Evaluates <tt>OXPathExtractionMarker</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */

	private OXPathType oxpathExtractionMarker(TreeWalkerState state) throws BadDataException {
		OXPathNodeList<OXPathDomNode> nodes = state.getContext().nodeList();
		OXPathNodeList<OXPathDomNode> newNodes = new OXPathNodeList<OXPathDomNode>();
		boolean attribute = ( state.getQueryPosition().jjtGetNumChildren() > 1 );
		for (OXPathDomNode n : nodes) {
			int nodeID = ++this.nodeNum;
			if (attribute) {
				OXPathType attributeResultRaw = evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(1)));
				String attributeResultFinal;
				if (attributeResultRaw.isType().equals(NODESET)) attributeResultFinal=attributeResultRaw.nodeList().asXML();
				else attributeResultFinal = attributeResultRaw.string();
				output.add(
//					new OutputNode(nodeID,n.getParent(),
//							getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0))),
//							evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(1))).string()).toString());
//				++numExtracted;
				new OutputNode(nodeID,n.getParent(),
						getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0))),
//						evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(1))).string()));//TODO: Fix this so that extraction markers work better
//						evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(1))).string()));
						attributeResultFinal));
				
			}
			else {
//				output.add(
//						new OutputNode(nodeID,n.getParent(),
//						getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0)))).toString());
//				++numExtracted;
				output.add(
						new OutputNode(nodeID,n.getParent(),
						getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0)))));
				newNodes.add(new OXPathDomNode(n.getNode(),n.getParent(),nodeID));	
			}
			this.docMap.put(nodeID, n.getNode().getPage().hashCode());
			this.nodeMap.put(nodeID, n.getNode().hashCode());
//			System.out.println(nodeID);
		}
//		Runtime.getRuntime().gc();
//		long runtime = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//		System.out.println(runtime + "," + this.pagesVisited);
		return (attribute) ? state.getContext() : new OXPathType(newNodes);
	}
	
	/**
	 * Evaluates <tt>OXPathActionPred</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType oxpathActionUntilPred(TreeWalkerState state) throws BadDataException{
//		System.out.println("oxpathActionUntilPred");
		String test = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0)));
		for (OXPathDomNode oContextNode : state.getContext().nodeList()) {
		    DomNode contextNode = oContextNode.getNode();		
			while (contextNode.getByXPath(test).isEmpty()) {
				try {
					synchronized (contextNode.getPage()) {
						contextNode.getPage().wait(1000);
					}
				} catch (Exception e) {
//					System.out.println("until exception");
//					throw new BadDataException("InterruptedException for Until Predicate on Action!");
				}
			}
		}
		return state.getContext();
//		String url = webclient.getCurrentWindow().getTopWindow().getEnclosedPage().getUrl().toString();
//		while (!url.contains("2011")) {
//			try {
//				((HtmlElement) state.getContext().nodeList().get(0).getNode()).click();
//				long t0,t1;
//		        t0=System.currentTimeMillis();
//		        do{
//		            t1=System.currentTimeMillis();
//		        }
//		        while (t1-t0<1000);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			url = webclient.getCurrentWindow().getTopWindow().getEnclosedPage().getUrl().toString();
//			System.out.println("Needed additional click");
//		}
//		return state.getContext();
	}
	
	/**
	 * Evaluates <tt>OXPathActionPred</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType oxpathActionWaitPred(TreeWalkerState state) throws BadDataException {
		for (OXPathDomNode oContextNode : state.getContext().nodeList()) {
			try {//TODO: verify this does the right thing with thread management
//				System.out.println("oxpathActionWaitPred");
				synchronized (oContextNode.getNode().getPage()) {
					oContextNode.getNode().getPage().wait(Long.parseLong(getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0)))));
				}
				} catch (NumberFormatException e) {
				throw new BadDataException("NumberFormatException from wait in action");
			} catch (InterruptedException e) {
				throw new BadDataException("InterruptedException from wait in action");
			} catch (BadDataException e) {
				throw new BadDataException("BadDataException from wait in action");
			} catch (RuntimeException e) {
				throw new BadDataException("RuntimeException from wait in action");
			}
		}	
		return state.getContext();
	}

	/**
	 * Evaluates <tt>XPathUserAction</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType oxpathUserAction(TreeWalkerState state) throws BadDataException {
		HtmlHelper helper = new HtmlHelper();
		OXPathNodeList<OXPathDomNode> newContext = new OXPathNodeList<OXPathDomNode>();
		SimpleNode actionInstruction = toSimpleNode(state.getQueryPosition().jjtGetChild(0));
		int actionType = getNodeID(actionInstruction);
		String actionValue = getValue(actionInstruction);
		//need to record and update a list of pages so we can drop the ones that aren't being referenced
		ArrayList<Page> contextPages = new ArrayList<Page>();
		for (int i = 0; i < state.getContext().nodeList().size(); i++) {
			contextPages.add(state.getContext().nodeList().get(i).getNode().getPage());
		}
		for (int i = 0; i < state.getContext().nodeList().size(); i++) {
			OXPathDomNode oContext = state.getContext().nodeList().get(i);
			DomNode iContext = oContext.getNode();
			String canonXPath = iContext.getCanonicalXPath();
			Page originalPage = iContext.getPage();
			BenchMarker actionOverhead = BenchFactory.newBenchMarker("actionOverhead");
			actionOverhead.start();
			try {//add the roots of the pages
				switch (actionType) {
					case JJTUNGROUNDEDOXPATHACTIONPOSITION :
						Scanner scan = new Scanner(actionValue).useDelimiter("\\s*,\\s*");
						ArrayList<Integer> input = new ArrayList<Integer>();
						while (scan.hasNext()) {
							input.add(Integer.parseInt(scan.next()));
						}
						scan.close();
//						if (hasChildByName(toSimpleNode(state.getQueryPosition()),"OXPathAbsoluteActionSignal")) {
							newContext.add(new OXPathDomNode((DomNode) helper.takeAction(
									((HtmlPage) iContext.getPage()),
									iContext,
									helper.getFieldType(iContext),
									input).getEnclosingWindow().getTopWindow().getEnclosedPage(), 
											oContext.getParent(), oContext.getLast()));
//						}
//						else {
//							newContext.add(new OXPathDomNode((DomNode) ((DomNode) helper.takeAction(
//									((HtmlPage) iContext.getPage()),
//									iContext,
//									helper.getFieldType(iContext),
//									input).getEnclosingWindow().getTopWindow().getEnclosedPage()).getFirstByXPath(canonXPath), 
//											oContext.getParent(), oContext.getLast()));
//						}
						break;
					case JJTGROUNDEDOXPATHACTIONEXPLICIT :
//						if (hasChildByName(toSimpleNode(state.getQueryPosition()),"OXPathAbsoluteActionSignal")) {
							newContext.add(new OXPathDomNode((DomNode) helper.takeAction(
									((HtmlPage) iContext.getPage()),
									iContext,
									helper.getFieldType(iContext),
									//strip off leading, trailing " or ' characters
									actionValue.substring(1, actionValue.length()-1)).getEnclosingWindow().getTopWindow().getEnclosedPage(), 
											oContext.getParent(), oContext.getLast()));
//						}
//						else {
//							newContext.add(new OXPathDomNode((DomNode) ((DomNode) helper.takeAction(
//									((HtmlPage) iContext.getPage()),
//									iContext,
//									helper.getFieldType(iContext),
//									//strip off leading, trailing " or ' characters
//									actionValue.substring(1, actionValue.length()-1)).getEnclosingWindow().getTopWindow().getEnclosedPage()).getFirstByXPath(canonXPath), 
//											oContext.getParent(), oContext.getLast()));
//						}
						break;
					case JJTOXPATHACTIONKEYWORD :
//						if (hasChildByName(toSimpleNode(state.getQueryPosition()),"OXPathAbsoluteActionSignal")) {
////						if ((this.numPagesAccessed % 100)==0) {
//								Runtime.getRuntime().gc();
//								long runtime = Runtime.getRuntime().totalMemory()
//										- Runtime.getRuntime().freeMemory();
//								long currenttime = System.nanoTime();
//								long durationtime = currenttime - starttime;
//								System.out.println(durationtime + "," + runtime + "," + this.numPagesAccessed + ",0");
//						
//							}
						this.numPagesAccessed++;
//							newContext.add(new OXPathDomNode((DomNode) helper.takeAction(
//									((HtmlPage) iContext.getPage()),
//									iContext,
//									helper.getFieldType(iContext),
//									ActionKeywords.getActionKeyword(actionValue)).getEnclosingWindow().getTopWindow().getEnclosedPage(), 
//											oContext.getParent(), oContext.getLast()));
						//TODO: DELETE
//						System.out.println(iContext + "," + iContext.getPage().getWebClient().getWebWindows().size());
						OXPathDomNode odn = (new OXPathDomNode((DomNode) helper.takeAction(
								((HtmlPage) iContext.getPage()),
								iContext,
								helper.getFieldType(iContext),
								ActionKeywords.getActionKeyword(actionValue)).getEnclosingWindow().getTopWindow().getEnclosedPage(), 
										oContext.getParent(), oContext.getLast()));
						logger.info("Accessing page at URL: " + webclient.getCurrentWindow().getTopWindow().getEnclosedPage().getUrl());
						//do this to assure the page returned is the new focus
						if (!webclient.getCurrentWindow().getTopWindow().equals(odn.getNode().getPage().getEnclosingWindow().getTopWindow())) {
							newContext.add(new OXPathDomNode((DomNode) webclient.getCurrentWindow().getEnclosedPage(), 
									oContext.getParent(), oContext.getLast()));
						} else newContext.add(odn);
//						long currenttime = System.nanoTime();
//						long durationtime = currenttime - starttime;
//						System.out.println(newContext.get(newContext.size()-1).getNode().getPage().getUrl() + "," + webclient.getWebWindows().size() + "," + durationtime + ","+ numPagesAccessed); numPagesAccessed++;
//						}
//						else {
//							newContext.add(new OXPathDomNode((DomNode) ((DomNode) helper.takeAction(
//									((HtmlPage) iContext.getPage()),
//									iContext,
//									helper.getFieldType(iContext),
//									ActionKeywords.getActionKeyword(actionValue)).getPage().getEnclosingWindow().getTopWindow().getEnclosedPage()).getFirstByXPath(canonXPath), 
//											oContext.getParent(), oContext.getLast()));
//						}
//							if (counter > 30 || (counter==21 || counter==11)) {
//								counter++;
//								long losetime = System.nanoTime();
//								Runtime.getRuntime().gc();
//								long memory = Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory();
//								gctime = gctime + System.nanoTime() - losetime;
//								long elapsed = System.nanoTime() - gctime - starttime;
//								System.out.println(memory + "," + elapsed + "," + this.numExtracted + "," + ++this.pagesVisited);
//							}
//							else counter++;
						break;
				}	
			} catch (IOException e) {
				throw new BadDataException("IOException from user action");
			} catch (RuntimeException e) {
				throw new BadDataException("RuntimeException from user action");
			}
			
			contextPages.set(i, newContext.get(newContext.size()-1).getNode().getPage().getEnclosingWindow().getTopWindow().getEnclosedPage());
			//close previous window if a new window is returned and the old window isn't referenced by a predicate
			//check if the current (focus) page is different than the original; if the old page is on the predicate "stack"
			//or if the page is used by any other context node; if any are true
			if (!originalPage.getEnclosingWindow().getTopWindow().equals(webclient.getCurrentWindow().getTopWindow())
					&& !windowSet.contains(originalPage.getEnclosingWindow().getTopWindow())
					&& !contextPages.contains(newContext.get(newContext.size()-1).getNode().getPage())) {
				((TopLevelWindow) originalPage.getEnclosingWindow().getTopWindow()).close();
			}
			actionOverhead.finish();
		}
		
		OXPathType result = new OXPathType(newContext);
		if (hasChildByName(toSimpleNode(state.getQueryPosition()),"OXPathActionWaitPred")) {//if has another child, then there is an action predicate
			evaluate(newState(state).setContext(result).setQueryPosition(getChildByName(toSimpleNode(state.getQueryPosition()),"OXPathActionWaitPred")));
		}
		else if (hasChildByName(toSimpleNode(state.getQueryPosition()),"OXPathActionUntilPred")) {//if has another child, then there is an action predicate
			evaluate(newState(state).setContext(result).setQueryPosition(getChildByName(toSimpleNode(state.getQueryPosition()),"OXPathActionUntilPred")));
		}
//		System.out.println(webclient.getCurrentWindow().getTopWindow().getEnclosedPage().getUrl());
//		for (WebWindow w : webclient.getWebWindows()) {
//			System.out.println(w.getTopWindow().getEnclosedPage().getUrl());
//		}
		if (hasChildByName(toSimpleNode(state.getQueryPosition()),"OXPathAbsoluteActionSignal")) {
			return result;
		}//find and return the result of navigating the Action Free Prefix
		else {
			result =  evaluate(newState(result, state.getActionFreePrefix(), state.getActionFreePrefix(), state.getQueryPosition(), true, state.getPosition(), state.getLast()));
//			System.out.println("Escape");
			return result;
		}
		//		else return evaluate(newState(result, state.getActionFreePrefix(), state.getActionFreePrefix(), state.getQueryPosition(), true, state.getPosition(), state.getLast()));
	}

	/**
	 * Evaluates <tt>XPathURLAction</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType oxpathURLAction(TreeWalkerState state) throws BadDataException {
		HtmlPage newPage;
		webclient.setAjaxController(new NicelyResynchronizingAjaxController());
		webclient.getCache().setMaxSize(250);
		webclient.getCookieManager().setCookiesEnabled(false);
//		webclient.setPopupBlockerEnabled(true);
//		webclient.setThrowExceptionOnFailingStatusCode(false);
//		webclient.setJavaScriptEnabled(false);
		webclient.setThrowExceptionOnScriptError(false);
		try {
			BenchMarker pageRetrievalOverhead = BenchFactory.newBenchMarker("pageRetrievalOverhead");
			pageRetrievalOverhead.start();
			String urlaction = getValue(toSimpleNode(state.getQueryPosition().jjtGetChild(0)));
			if (hasChildByName(toSimpleNode(state.getQueryPosition()),"XPathLiteral")) //strip off quotes or double quotes delimiting literal
				urlaction = urlaction.substring(1, urlaction.length()-1);
			newPage = this.webclient.getPage(urlaction);
			pageRetrievalOverhead.finish();
		} catch (FailingHttpStatusCodeException e) {
			throw new BadDataException("FailingHttpStatusCodeException from Page Fetch");
		} catch (MalformedURLException e) {
			throw new BadDataException("MalformedURLException from Page Fetch");
		} catch (IOException e) {
			throw new BadDataException("IOException from Page Fetch");
		} catch (RuntimeException e) {
			throw new BadDataException("RuntimeException from Page Fetch");
		}
//		if (counter) {
//			long losetime = System.nanoTime();
//			Runtime.getRuntime().gc();
//			long memory = Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory();
//			gctime = gctime + (System.nanoTime() - losetime);
//			long elapsed = System.nanoTime() - gctime - starttime;
//			System.out.println(memory + "," + elapsed + "," + this.numExtracted + "," + 0);
//			this.urlCurrent = newPage.getUrl().toString();
//		}
//		else counter++
		//		this.pagesVisited++;
		DomNode newRoot = newPage;
		OXPathNodeList<OXPathDomNode> newContext = new OXPathNodeList<OXPathDomNode>();
//		for (OXPathDomNode i : state.getContext().nodeList()) {
		newContext.add(new OXPathDomNode(newRoot, 0, 0));
//		}
		OXPathType result = new OXPathType(newContext);
		if (state.getQueryPosition().jjtGetNumChildren() > 1) {//if has another child, then there is an action predicate
			evaluate(newState(state).setContext(result).setQueryPosition(state.getQueryPosition().jjtGetChild(1)));
		}
		return result;
	}

//	/**
//	 * Evaluates <tt>XPathPredicateExpr</tt> nodes in AST
//	 * @param state context at current point in query evaluation
//	 * @return same TreeWalker object with updated state
//	 * @throws BadDataException in case of poorly constructed abstract syntax tree
//	 */
//	private OXPathType oxpathPredicateExpr(TreeWalkerState state) throws BadDataException {
//		
//		return null;
//	}

	/**
	 * Evaluates <tt>OXPathOptionalPredicate</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType oxpathOptionalPredicate(TreeWalkerState state) throws BadDataException {
		OXPathType result = evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(0)));
		if (result.isType().equals(OXPathTypes.NODESET)) {
			for (OXPathDomNode o : result.nodeList()) {
				DomNode i = o.getNode();
				//close previous window if a new window is returned and the old window isn't referenced by a predicate
				if (!windowSet.contains(i.getPage().getEnclosingWindow().getTopWindow()))
					((TopLevelWindow) i.getPage().getEnclosingWindow().getTopWindow()).close();
			}
		}
		return new OXPathType(true);
	}

	/**
	 * Evaluates <tt>XPathPredicate</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType xpathPredicate(TreeWalkerState state) throws BadDataException {
		OXPathType resultRaw = evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(0)));
		if (resultRaw.isType().equals(OXPathTypes.NUMBER)) {
			return new OXPathType(new Double(resultRaw.number()).intValue()==state.getPosition());
		}
		boolean returnValue = resultRaw.booleanValue();
		if (resultRaw.isType().equals(OXPathTypes.NODESET)) {
			for (OXPathDomNode o : resultRaw.nodeList()) {
				DomNode i = o.getNode();
				//close previous window if a new window is returned and the old window isn't referenced by a predicate
				if (!windowSet.contains(i.getPage().getEnclosingWindow().getTopWindow()))
					((TopLevelWindow) i.getPage().getEnclosingWindow().getTopWindow()).close();
			}
		}
		return new OXPathType(returnValue);
	}

	/**
	 * Evaluates <tt>OXPathStep</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType oxpathStep(TreeWalkerState state) throws BadDataException {
		SimpleNode step = toSimpleNode(state.getQueryPosition());
		StringBuilder sb = new StringBuilder();
		if (hasChildByName(step, "OXPathURLAction") || hasChildByName(step, "OXPathUserAction")) {
			return evaluate(newState(state).setQueryPosition(step.jjtGetChild(0)));
		}
		//take care of step first, absent any predicates or extraction markers
		boolean forward;
		if (hasChildByName(step,"AbbreviatedStep")) {
			sb.append(getValue(toSimpleNode(step.jjtGetChild(0))));
			forward = true;
		}
		else {
			String[] nodeTests;
			String[] axisSpecifiers;
			if(hasChildByName(step,"OXPathNodeTest")) {
				nodeTests = FIELDS;
			}
			else {
				nodeTests = new String[1];
//				if (hasDescendantOrSelfByName(step,"OXPathNodeTestOp")) {
//					SimpleNode nodeTest = toSimpleNode(getChildByName(step,"XPathNodeTest"));
//					nodeTests[0] = getValue(toSimpleNode(nodeTest.jjtGetChild(0)));
//					nodeTestOp = toSimpleNode(nodeTest.jjtGetChild(1));
//				}
//				else
				nodeTests[0] = getValue(getChildByName(toSimpleNode(step),"XPathNodeTest"));
			}
			if (hasChildByName(step,"OXPathAxisName")) {
				String axis = getValue(getChildByName(toSimpleNode(step),"OXPathAxisName"));
				if (axis.equals("descendant-or-following::")) {
					axisSpecifiers = FORWARDAXES;
					forward = true;
				}
				else if (axis.equals("ancestor-or-preceding::")) {
					axisSpecifiers = BACKWARDAXES;
					forward = false;
				}
				else if (axis.equals("style::")) {
					axisSpecifiers = new String[1];
					axisSpecifiers[0] = "style";
					forward = false;
					//TODO: only supports first node style
					ComputedCSSStyleDeclaration style = ((HTMLElement) state.getContext().nodeList().get(0).getNode().getScriptObject()).jsxGet_currentStyle();
					try {
//						System.out.println(cssproperties.getMethod(nodeTests[0]));
//						System.out.println(cssproperties.getMethod(nodeTests[0]).invoke(style).toString());
						return new OXPathType(cssproperties.getMethod(nodeTests[0]).invoke(style).toString());
					} catch (Exception e) {
						throw new BadDataException("Unrecognized CSS property!");
					} 
//					return new OXPathType(style.jsxFunction_getPropertyValue(nodeTests[0]));
				}
				else {
					forward=true;
					throw new BadDataException("Unsupported additional OXPath Axis");
				}
			}
			else {
				axisSpecifiers = new String[1];
				if (hasChildByName(toSimpleNode(step),"XPathAxisSpecifier")) {
					SimpleNode axis = getChildByName(toSimpleNode(step),"XPathAxisSpecifier");
					String axisName = getValue(axis);
					axisSpecifiers[0] = axisName;
					forward = !(hasChildByName(axis,"XPathBackwardAxisName"));
				} else {
					axisSpecifiers[0] = "";
					forward=true;
				}
				
			}
			sb.append("(");
			for (String i : axisSpecifiers) {
				for (String j : nodeTests) {
//					if (state.isDescendant()) sb.append("//");
					sb.append(i); sb.append(j); sb.append("|");
				}
			}
			sb.append("false)");//just makes for a faster loop
		}
		if (hasChildByName(step,"OXPathNodeTest")) if (getValue(getChildByName(step,"OXPathNodeTest")).startsWith("field")) sb.append(VISIBILITYTEST);
		String stepString = sb.toString();
//		OXPathType tempNewContext = 
//		new OXPathType(state.getContext().nodeList().getByXPath(stepString,forward));
		ArrayList<OXPathType> tempNewContext = new ArrayList<OXPathType>();
		for (OXPathDomNode o : state.getContext().nodeList()) {
			tempNewContext.add(new OXPathType(o.getByXPath(stepString,forward)));
		}
//		OXPathType newContext;
//		if (!(nodeTestOp==null)) {
//			newContext = new OXPathType();
//			for (OXPathDomNode t : tempNewContext.nodeList()) {
//				String classValue = (String) t.getNode().getFirstByXPath("./@class");
//				Scanner scan = new Scanner(classValue);
//				boolean hasClass = false;
//				while (scan.hasNext()) {
//					if (scan.next().equals(nodeTestOp.jjtGetChild(1))) hasClass = true;
//				}
//				scan.close();
//				if (hasClass) newContext.nodeList().add(t);
//			}
//		} else 
//	    newContext = tempNewContext;
		for (int i = 1; i < step.jjtGetNumChildren(); i++) {
			SimpleNode pred = toSimpleNode(step.jjtGetChild(i));
			if  (getNodeID(pred) == JJTXPATHPREDICATE) {
				for (OXPathType newContext : tempNewContext) {
					OXPathNodeList<OXPathDomNode> filterList = new OXPathNodeList<OXPathDomNode>();
					for (int j = 0; j < newContext.nodeList().size(); j++) {
						OXPathDomNode original = newContext.nodeList().get(j);
						OXPathDomNode filterNode = new OXPathDomNode(
								original.getNode(), original.getLast(),
								original.getLast());
						//					System.out.println(filterNode);
						int p = 1;
						int l;
						//					if (state.isDescendant()) {//in case of descendant or self binding less tightly than axis, must do the following
						//						DomNode filterParent= (DomNode) filterNode.getNode().getFirstByXPath("..");
						//						String newStepString = stepString.replace("//", "");
						//						List<DomNode> siblings = filterParent.getFirstByXPath(newStepString);
						//						l = siblings.size();
						//						for (int s = 0; s < siblings.size(); s++) {
						//							if (siblings.get(s).equals(filterNode)) p=s;
						//						}
						//					}
						//					else {
						p = j + 1;
						l = newContext.nodeList().size();
						//					}
						//position offset by 1 because of conversion to XPath numbering from Java numbering
						TreeWalkerState newState = newState(new OXPathType(
								filterNode), pred, state.getActionFreePrefix(),
								state.getActionFreePrefixEnd(),
								state.isActionFreeNavigation(), p, l);
						windowSet.add((TopLevelWindow) filterNode.getNode()
								.getPage().getEnclosingWindow().getTopWindow());
						if (evaluate(newState).booleanValue()) {
							//TODO: check if page is dirty; if so, find new = AFP(original), then filterList.add(new); else do below
							filterList.add(original);
						}
//						windowSet.remove((TopLevelWindow) filterNode.getNode()
//								.getPage().getEnclosingWindow().getTopWindow());
						//TODO: Figure out why this is wrong!!
						if (!original
								.getNode()
								.getPage()
								.getEnclosingWindow()
								.getTopWindow()
								.equals(webclient.getCurrentWindow()
										.getTopWindow()))
							((TopLevelWindow) webclient.getCurrentWindow()
									.getTopWindow()).close();
					}
					newContext.set(filterList);
				}
			}
			else if ((getNodeID(pred) == JJTOXPATHOPTIONALPREDICATE) ) {
				OXPathNodeList<OXPathDomNode> newContextNodes = new OXPathNodeList<OXPathDomNode>();
				for (OXPathType t :tempNewContext) {
					newContextNodes.addAll(t.nodeList());
				}
				OXPathType newContext = new OXPathType(newContextNodes);
				if (forward) Collections.sort(newContext.nodeList(), newContext.nodeList());
				else Collections.sort(newContext.nodeList(), Collections.reverseOrder(newContext.nodeList()));
				for (int j = 0; j < newContext.nodeList().size(); j++) {
					OXPathDomNode original = newContext.nodeList().get(j);
					OXPathDomNode filterNode = new OXPathDomNode(original.getNode(),original.getLast(),original.getLast());
					//position offset by 1 because of conversion to XPath numbering from Java numbering
					TreeWalkerState newState = newState(new OXPathType(filterNode), pred, state.getActionFreePrefix(), state.getActionFreePrefixEnd(),
							state.isActionFreeNavigation(), j+1, newContext.nodeList().size());
					windowSet.add((TopLevelWindow) filterNode.getNode().getPage().getEnclosingWindow().getTopWindow());
					evaluate(newState);
					//TODO: check if page is dirty; if so, find new = AFP(original), then filterList.add(new); else do below
					windowSet.remove((TopLevelWindow) filterNode.getNode().getPage().getEnclosingWindow().getTopWindow());
					if (original.getNode().getPage().getEnclosingWindow().getTopWindow().equals(webclient.getCurrentWindow().getTopWindow())) ((TopLevelWindow) webclient.getCurrentWindow().getTopWindow()).close();
				}
			}
			else if ((getNodeID(pred) == JJTOXPATHEXTRACTIONMARKER) && !state.isActionFreeNavigation()) {
//				if ((counter%10)==0) {
//					Runtime.getRuntime().gc();
//					long runtime = Runtime.getRuntime().totalMemory()
//							- Runtime.getRuntime().freeMemory();
//					System.out.println(runtime + "," + this.pagesVisited);
////					counter++;
//					this.pagesVisited++;
//				}
//				else counter++;
				OXPathNodeList<OXPathDomNode> newContextNodes = new OXPathNodeList<OXPathDomNode>();
				for (OXPathType t :tempNewContext) {
					newContextNodes.addAll(t.nodeList());
				}
				OXPathType newContext = new OXPathType(newContextNodes);
				if (forward) Collections.sort(newContext.nodeList(), newContext.nodeList());
				else Collections.sort(newContext.nodeList(), Collections.reverseOrder(newContext.nodeList()));
				for (int j = 0; j < newContext.nodeList().size(); j++) {
					OXPathDomNode n = newContext.nodeList().get(j);
					newContext.nodeList().set(j, evaluate(newState(state).setContext(new OXPathType(n)).setQueryPosition(pred)).nodeList().get(0));
				}
				//expensive, but must be done in case positional predicate follows marker
				//first, preserve order of DomNodes and make list
				ArrayList<DomNode> index = new ArrayList<DomNode>();
				for (int k = 0; k < newContext.nodeList().size(); k++) {
					index.add(newContext.nodeList().get(k).getNode());
				}
				for (OXPathType t : tempNewContext) {
					for (int k =0; k < t.nodeList().size(); k++) {
						int n = index.indexOf(t.nodeList().get(k).getNode());
						OXPathDomNode m = newContext.nodeList().get(n);
						t.nodeList().set(k, m);
					}
				}
//				newContext = evaluate(newState(state).setContext(newContext).setQueryPosition(pred));
			}
			else if (getNodeID(pred) == JJTOXPATHNODETESTOP) {//handle the . and # operators
				for (OXPathType newContext: tempNewContext) {
					OXPathNodeList<OXPathDomNode> filterList = new OXPathNodeList<OXPathDomNode>();
					String test = (getValue(toSimpleNode(pred.jjtGetChild(0)))
							.equals(".")) ? "string(./@class)"
							: "string(./@id)";
					for (OXPathDomNode t : newContext.nodeList()) {
						String classValue = (t.getNode().getFirstByXPath(test));
						Scanner scan = new Scanner(classValue);
						boolean hasClass = false;
						while (scan.hasNext()) {
							if (scan.next()
									.equals(getValue(toSimpleNode(pred
											.jjtGetChild(1)))))
								hasClass = true;
						}
						scan.close();
						if (hasClass)
							filterList.add(t);
					}
					newContext.set(filterList);
				}
			}
		}
		//now we compact the result
		OXPathNodeList<OXPathDomNode> result = new OXPathNodeList<OXPathDomNode>();
		for (OXPathType i :tempNewContext) {
			result.addAll(i.nodeList());
		}
//		if (forward) Collections.sort(result, result);
//		else Collections.sort(result, Collections.reverseOrder(result));
//		Runtime.getRuntime().gc();
//		System.out.println(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
		return new OXPathType(result);
	}

//	/**
//	 * Evaluates <tt>OXPathKleeneStar</tt> nodes in AST
//	 * @param state context at current point in query evaluation
//	 * @return same TreeWalker object with updated state
//	 * @throws BadDataException in case of poorly constructed abstract syntax tree
//	 */
//	private OXPathType oxpathKleeneStar(TreeWalkerState state) throws BadDataException {
//		
//		return null;
//	}

	/**
	 * Evaluates <tt>RelativeOXPathLocationPath</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType relativeOXPathLocationPath(TreeWalkerState state) throws BadDataException {
//		OXPathNodeList<OXPathDomNode> resultRaw = new OXPathNodeList<OXPathDomNode>();
		SimpleNode step = toSimpleNode(state.getQueryPosition().jjtGetChild(0));
		boolean stepIsAbsoluteAction = (hasDescendantOrSelfByName(step,"OXPathURLAction") || hasDescendantOrSelfByName(step,"OXPathAbsoluteActionSignal"));
		//if an absolute action, this node becomes the new root for the action free prefix on the AST
		if (stepIsAbsoluteAction && !state.isActionFreeNavigation()) state.setActionFreePrefix(state.getQueryPosition());
		boolean stepIsAction = stepIsAbsoluteAction || hasDescendantOrSelfByName(step,"OXPathUserAction");
		ArrayList<TreeWalkerState> lists = new ArrayList<TreeWalkerState>(); 
		if (state.isActionFreeNavigation() && stepIsAction) {//do nothing if arriving at an action during action-free navigation, unless this is the final action
			if (step.jjtGetChild(0).equals(state.getActionFreePrefixEnd())) return state.getContext();
			else lists.add(state);
		}
		else if (hasDescendantOrSelfByName(step,"OXPathUserAction")) {
			if (!state.getContext().nodeList().isEmpty()) {
				TopLevelWindow actionWindow = (TopLevelWindow) state
						.getContext().nodeList().get(0).getNode().getPage()
						.getEnclosingWindow().getTopWindow();
				boolean inSet = this.windowSet.contains(actionWindow);
				if (!inSet)
					this.windowSet.add(actionWindow);
				for (OXPathDomNode i : state.getContext().nodeList()) {
//					lists.add(evaluate(
//							newState(state).setQueryPosition(step).setContext(
//									new OXPathType(i))).nodeList());
					lists.add(newState(state).setQueryPosition(step).setContext(
									new OXPathType(i)));
				}
				if (!inSet)
					this.windowSet.remove(actionWindow);
			}
		}
		else {
			lists.add(newState(state).setQueryPosition(step));
//			lists.add(resultRaw);
		}
		OXPathType result = new OXPathType();
		OXPathType resultFinal = new OXPathType();
		for (TreeWalkerState list : lists) {
			if (state.isActionFreeNavigation() && stepIsAction) result = new OXPathType(list.getContext().nodeList());
			else {
				if (hasDescendantOrSelfByName(step,"OXPathUserAction")) {
				TopLevelWindow actionWindow = (TopLevelWindow) state.getContext().nodeList().get(0).getNode().getPage()
				.getEnclosingWindow().getTopWindow();
				boolean inSet = this.windowSet.contains(actionWindow);
		        if (!inSet) this.windowSet.add(actionWindow);
		        //TODO: why this instead of result = evaluate(list);
				result = new OXPathType(evaluate(list).nodeList());
				if (!inSet)
					this.windowSet.remove(actionWindow);
				} else {
					result = evaluate(list);
					if (!result.isType().equals(NODESET)) return result;
				}
			}
			//TODO: make copy of page page.clone(), add to hashMap, save page.hashCode()
			//		state.setIsDescendant(hasChildByName(toSimpleNode(state.getQueryPosition()),"DescendantOrSelfShort"));	
//			result = new OXPathType(list);
			if (!hasChildByName(toSimpleNode(state.getQueryPosition()),
					"OXPathKleeneStar")) {
				if (hasChildByName(toSimpleNode(state.getQueryPosition()),
						"DescendantOrSelfShort")) {
					result = new OXPathType(result.nodeList().getByXPath("descendant-or-self::*"));
				}
				//			OXPathType result = new OXPathType(resultRaw);
				//			//set if descendant step
				//			state.setIsDescendant(hasChildByName(toSimpleNode(state.getQueryPosition()),"DescendantOrSelfShort"));
				if (hasChildByName(toSimpleNode(state.getQueryPosition()),
						"RelativeOXPathLocationPath")) {
					result = evaluate(newState(state).setContext(result)
							.setQueryPosition(
									getChildByName(
											toSimpleNode(state.getQueryPosition()),
											"RelativeOXPathLocationPath")));
				}
				//			else return result;
			} else {
				SimpleNode kleeneStar = toSimpleNode(state.getQueryPosition()
						.jjtGetChild(1));
				//				state.getContext().nodeList();
				//			OXPathType result = new OXPathType(resultRaw);
				if (kleeneStar.jjtGetNumChildren() == 1) {
					while (!result.nodeList().isEmpty()) {
						if (hasChildByName(toSimpleNode(state.getQueryPosition()),
								"RelativeOXPathLocationPath")) {//has path after Kleene star
							if (hasChildByName(toSimpleNode(state.getQueryPosition()),
									"DescendantOrSelfShort")) {
								result = new OXPathType(result.nodeList().getByXPath(
										"descendant-or-self::*"));
							}
							evaluate(newState(state).setContext(result)
									.setQueryPosition(
											getChildByName(toSimpleNode(state
													.getQueryPosition()),
													"RelativeOXPathLocationPath")));
//							result = evaluate(newState(state).setContext(result)
//									.setQueryPosition(
//											getChildByName(toSimpleNode(state
//													.getQueryPosition()),
//													"RelativeOXPathLocationPath")));
						}
						//TODO: check if page is dirty; if so, result = AFP(), then proceed with below
						result = evaluate(newState(state).setContext(result)
								.setQueryPosition(kleeneStar.jjtGetChild(0)));
					}
				} else {//has two children, and is a bounded Kleene-star expression
					SimpleNode kleeneStarPred = toSimpleNode(kleeneStar.jjtGetChild(1));
					int min = Integer.parseInt(getValue(toSimpleNode(kleeneStarPred
							.jjtGetChild(0))));
					if (min < 0)
						throw new BadDataException(
								"Can't iterate over Kleene-Star less than 0 times.");
					int max = min;
					if (kleeneStarPred.jjtGetNumChildren() > 1) {//has max
						max = Integer.parseInt(getValue(toSimpleNode(kleeneStarPred
								.jjtGetChild(1))));
						if (max < min)
							throw new BadDataException(
									"Can't iterate over Kleene-Star less times than the minimum.");
					}
					for (int i = 0; i < min; i++) {//complete the minimum advances of the Kleene star
						result = evaluate(newState(state).setContext(result)
								.setQueryPosition(kleeneStar.jjtGetChild(0)));
					}
					if (hasChildByName(toSimpleNode(state.getQueryPosition()),
							"RelativeOXPathLocationPath")) {//has path after Kleene star
						OXPathType tempResult;
						if (hasChildByName(toSimpleNode(state.getQueryPosition()),
								"DescendantOrSelfShort")) {
							tempResult = new OXPathType(result.nodeList().getByXPath(
									"descendant-or-self::*"));
						}
						else tempResult = new OXPathType(result.nodeList());
						//result=
						evaluate(newState(state).setContext(tempResult)
								.setQueryPosition(
										getChildByName(
												toSimpleNode(state.getQueryPosition()),
												"RelativeOXPathLocationPath")));
					}
					int iter = max - min;
					while (!result.nodeList().isEmpty() && (iter > 0)) {//iterate once when max=min by semantic definition
						//TODO: check if page is dirty; if so, result = AFP(), then proceed with below
						result = evaluate(newState(state).setContext(result)
								.setQueryPosition(kleeneStar.jjtGetChild(0)));
						OXPathType tempResult;
						if (hasChildByName(toSimpleNode(state.getQueryPosition()),
								"RelativeOXPathLocationPath")) {//has path after Kleene star
							if (hasChildByName(toSimpleNode(state.getQueryPosition()),
									"DescendantOrSelfShort")) {
								tempResult = new OXPathType(result.nodeList().getByXPath(
										"descendant-or-self::*"));
							}
							else tempResult = new OXPathType(result.nodeList());
							//result=
							evaluate(newState(state).setContext(tempResult)
									.setQueryPosition(
											getChildByName(toSimpleNode(state
													.getQueryPosition()),
													"RelativeOXPathLocationPath")));
						}
						iter--;
					}
				}
				//				return result;
				
			}
			//TODO: remove original page.clone() from hashMap by saved page.hashCode
			if (resultFinal.nodeList().isEmpty()) resultFinal = new OXPathType(result.nodeList());
		}
		return resultFinal;	
	}

//	/**
//	 * Evaluates <tt>OXPathPathSegment</tt> nodes in AST
//	 * @param state context at current point in query evaluation
//	 * @return same TreeWalker object with updated state
//	 * @throws BadDataException in case of poorly constructed abstract syntax tree
//	 */
//	private TreeWalkerState oxpathPathSegment(TreeWalkerState state) throws BadDataException {
//		return newState(state.getContext(),state.getOutputParent(),state.getQueryPosition().jjtGetChild(0),state.getActionFreePrefix(),state.isActionFreeNavigation());
//	}

	/**
	 * Evaluates <tt>PathDeclaration</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType pathDeclaration(TreeWalkerState state) throws BadDataException {
		return evaluate(this.newState(state.getContext(), state.getQueryPosition().jjtGetChild(0)));
	}

	/**
	 * Evaluates <tt>Script</tt> nodes in AST
	 * @param state context at current point in query evaluation
	 * @return same TreeWalker object with updated state
	 * @throws BadDataException in case of poorly constructed abstract syntax tree
	 */
	private OXPathType script(TreeWalkerState state) throws BadDataException {
		OXPathType result = new OXPathType();
		for (int i = 0; i<state.getQueryPosition().jjtGetNumChildren(); i++) {
			
			//			Element temp = output.createElement("path"+i);
//			output.add(new OutputNode(++this.nodeNum,0,"path"+i));
//			result = evaluate(newState(new OXPathType(),state.getQueryPosition().jjtGetChild(i)));
			result = evaluate(newState(state).setQueryPosition(state.getQueryPosition().jjtGetChild(i)));
		}
//		call after DOM, AST walks
		return result;
	}

	/**
	 * Signature for creating new TreeWalkerState object based on minimal constructor
	 * @param context context
	 * @param queryPosition context position in AST
	 * @return new state set with specified parameters
	 */
	private TreeWalkerState newState(OXPathType context, Node queryPosition) {
		return this.new TreeWalkerState(context, queryPosition);
	}

	/**
	 * 
	 * Signature for creating verbose new TreeWalkerState object
	 * @param context context
	 * @param queryPosition context position in AST
	 * @param aFP action free prefix position in AST 
	 * @param aFPE action free prefix end position in AST
	 * @param isAFP determines if current navigation is by Action Free Prefix 
	 * @param position position in parent context
	 * @param last last position in parent context
	 * @param descendant determines if next step is prepended with abbreviated descendant or short axis
	 * @return new state set with specified parameters
	 */
	private TreeWalkerState newState(OXPathType context, Node queryPosition, Node aFP, Node aFPE, boolean isAFP, int position, int last) {
		return this.new TreeWalkerState(context, queryPosition, aFP, aFPE, isAFP, position, last);
	}
	
	/**
	 * 
	 * Signature for creating new TreeWalkerState object
	 * @param state state whose content is copied
	 * @return new state set with specified parameters
	 */
	private TreeWalkerState newState(TreeWalkerState state) {
		return this.new TreeWalkerState(state);
	}

	/**
	 * Closes open windows and finishes query evaluation
	 * @return same <tt>TreeWalker</tt> object with WebClient closed properly
	 */
	private TreeWalker endWalk() {
//		for (WebWindow i :this.webclient.getWebWindows()) {
//	    	System.out.println(i.getEnclosedPage().getClass());
//	    	System.out.println((i.getEnclosedPage().getUrl()));
//	    }
		webclient.closeAllWindows();
//		System.out.println(this.returnOutput());
		return this;
	}

	/**
	 * Returns output of query.  Builds the document from an input list
	 * @return XML Document containing
	 * @throws ParserConfigurationException in case of XML parse error
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private Document returnOutput() throws ParserConfigurationException, SAXException, IOException {
		//TODO: Sort siblings based on document order
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
		Document tempDoc = db.newDocument();
		ArrayList<Element> elements = new ArrayList<Element>();
		//elements are identified by position in elements rather than by name 
		elements.add(tempDoc.createElement("results"));
		tempDoc.appendChild(elements.get(0));
		for (OutputNode o : output) {
			elements.add(o.getId(), tempDoc.createElement(o.getLabel()));
			elements.get(o.getParent()).appendChild(elements.get(o.getId()));
			if (!o.getValue().equals("")) {
				while (o.getValue().contains(LESSTHANSUB)||o.getValue().contains(GREATERTHANSUB)) {
					logger.info("Reserved html sequence encountered");
					LESSTHANSUB += "asdfasdf";
					GREATERTHANSUB += "werqeew";
				}
				Text textNode = tempDoc.createTextNode(o.getValue().replace("<",LESSTHANSUB).replace(">",GREATERTHANSUB));//.replace("&", AMPSUB));
				elements.get(o.getId()).appendChild(textNode);
			}
//			System.out.println(elements.get(o.getId()));
		}
		String tempDocAsString = getStringFromDocument(tempDoc).replace(LESSTHANSUB,"<").replace(GREATERTHANSUB, ">");//.replace(AMPSUB,"&");
//		System.out.println(tempDocAsString);
		Document FinalDoc = db.parse(new InputSource(new ByteArrayInputStream(tempDocAsString.getBytes("utf-8"))));
		
		
		return FinalDoc;
		
//		for (OutputNode o : output) {
//			System.out.println(o.id+" "+o.parent+" "+ o.label+ " "+ o.value);
//		}
//		return null;
	}
	
	/**
	 * Filler constructor for initiating new TreeWalker object
	 * @throws ParserConfigurationException if XML document isn't created properly
	 */
	private TreeWalker() throws ParserConfigurationException {
		webclient = new WebClient(BrowserVersion.FIREFOX_3_6);
//		webclient = new WebClient();
	}
	
	
	
	/**
	 * WebClient object for TreeWalker instance
	 */
	private final WebClient webclient;
	
	private final CSSProperties cssproperties = new CSSProperties();
	
	private final Set<TopLevelWindow> windowSet = new HashSet<TopLevelWindow>();
	
	//TODO: private final Set<Page> clonesPages = new HashSet<Pages>();
	//      handle memory for these cases
	
	/**
	 * stores the number of the current last node number assigned
	 */
	int nodeNum = 0;
	/**
	 * holds output nodes
	 */
	ArrayList<OutputNode> output = new ArrayList<OutputNode>();
//	NullWriter output = new NullWriter();
	
	Map<Integer,Integer> docMap = new HashMap<Integer,Integer>();
	
	Map<Integer,Integer> nodeMap = new HashMap<Integer,Integer>();
	
//	int pagesVisited = 0;//we are four deep when we start the query
//	String urlCurrent = "";
//	int numExtracted = 0;
	long starttime = System.nanoTime();
//	long gctime = 0;
	
//	int counter = 1;
	private int numPagesAccessed = 0;
	
	/**
	 * log4jlogger
	 */
	private static Logger logger = Logger.getLogger("oxpathlog");
	
	/**
	 * Class for representing output nodes
	 * @author AndrewJSel
	 *
	 */
	private class OutputNode {
		
		/**
		 * Constructor for nested record nodes
		 * @param iId id of node
		 * @param iParent parent of node
		 * @param iLabel label of node
		 */
		public OutputNode(int iId, int iParent, String iLabel) {
			this.id = iId;
			this.parent = iParent;
			this.label = iLabel;
			this.value = "";
		}
		
		/**
		 * Constructor for attribute nodes
		 * @param iId id of node
		 * @param iParent parent of node
		 * @param iLabel label of node
		 * @param iValue value of node
		 */
		public OutputNode(int iId, int iParent, String iLabel, String iValue) {
			this(iId,iParent,iLabel);
			value = iValue;
		}
		
		/**
		 * Returns id
		 * @return id
		 */
		public int getId() {
			return this.id;
		}
		
		/**
		 * Returns parent
		 * @return parent
		 */
		public int getParent() {
			return this.parent;
		}
		
		/**
		 * Returns label
		 * @return label
		 */
		public String getLabel() {
			return this.label;
		}
		
		/**
		 * returns value
		 * @return value
		 */
		public String getValue() {
			return this.value;
		}
		
		/**
		 * returns <tt>String</tt> representation of object
		 * @return <tt>String</tt> representation of object
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass());
			sb.append("[");
			sb.append(this.getId());
			sb.append(",");
			sb.append(this.getParent());
			sb.append(",");
			sb.append(this.getLabel());
			if (!this.getValue().equals("")) {
				sb.append(",");
				sb.append(this.value);
			}
			sb.append("]");
			return (sb.toString());
		}
		
		/**
		 * instance field for storing id
		 */
		private int id;
		/**
		 * instance field for storing parent
		 */
		private int parent;
		/**
		 * instance field for storing label
		 */
		private String label;
		/**
		 * instance field for storing value
		 */
		private String value;
	}
	
	//TODO: Split this a factory and final class
	/**
	 * Private class; used only by TreeWalker, with instantiations for each call on evaluateOXPathExpression.
	 * Not safe for general use; mutable state, doesn't make defensive copies; if part of a public API, factory and final classes would be separate
	 * @author AndrewJSel
	 *
	 */
	private class TreeWalkerState{
		
//		/**
//		 * Constructor for TreeWalkerState to be used to instantiate before each call; this is appropriate after an action and the action free prefix is the same as the
//		 * current position in the query.
//		 * @param c the context over the DOM tree
//		 * @param o the current parent of the output info
//		 * @param q the current position in the query AST
//		 */
//		public TreeWalkerState(OXPathNodeList<OXPathDomNode> c, Element o, Node q) {
//			this.context=new OXPathType(c);
//			this.outputParent=o;
//			this.queryPosition=q;
//			this.actionFreePrefix = q;
//			this.actionFreeNavigation = false;
//			this.position = 1;
//			this.last = 1;
//		}
//		
//		/**
//		 * Constructor for TreeWalkerState to be used to instantiate before each call; this is appropriate when not computing context based on action-free prefix 
//		 * (i.e. action-free navigation is set to false).
//		 * @param c the context over the DOM tree
//		 * @param o the current parent of the output info
//		 * @param q the current position in the query AST
//		 * @param a the root of the action-free prefix
//		 */
//		public TreeWalkerState(OXPathNodeList<OXPathDomNode> c, Element o, Node q, Node a) {
//			this.context=new OXPathType(c);
//			this.outputParent=o;
//			this.queryPosition=q;
//			this.actionFreePrefix = a;
//			this.actionFreeNavigation = false;
//			this.position = 1;
//			this.last = 1;
//		}
//		
//		/**
//		 * Constructor for TreeWalkerState to be used to instantiate before each call; this constructor is for general use, as all state info is explicitly defined by 
//		 * input parameters.
//		 * @param c the context over the DOM tree
//		 * @param o the current parent of the output info
//		 * @param q the current position in the query AST
//		 * @param a the root of the action-free prefix
//		 * @param afn whether current navigation is action-free
//		 */
//		public TreeWalkerState(OXPathNodeList<OXPathDomNode> c, Element o, Node q, Node a, boolean afn) {
//			this.context=new OXPathType(c);
//			this.outputParent=o;
//			this.queryPosition=q;
//			this.actionFreePrefix = a;
//			this.actionFreeNavigation = afn;
//			this.position = 1;
//			this.last = 1;
//		}
//		
//		/**
//		 * Constructor for TreeWalkerState to be used to instantiate before each call; this is appropriate after an action and the action free prefix is the same as the
//		 * current position in the query.
//		 * @param c the context over the DOM tree
//		 * @param o the current parent of the output info
//		 * @param q the current position in the query AST
//		 */
//		public TreeWalkerState(OXPathType c, Element o, Node q) {
//			this.context=c;
//			this.outputParent=o;
//			this.queryPosition=q;
//			this.actionFreePrefix = q;
//			this.actionFreeNavigation = false;
//			this.position = 1;
//			this.last = 1;
//		}
//		
//		/**
//		 * Constructor for TreeWalkerState to be used to instantiate before each call; this is appropriate when not computing context based on action-free prefix 
//		 * (i.e. action-free navigation is set to false).
//		 * @param c the context over the DOM tree
//		 * @param o the current parent of the output info
//		 * @param q the current position in the query AST
//		 * @param a the root of the action-free prefix
//		 */
//		public TreeWalkerState(OXPathType c, Element o, Node q, Node a) {
//			this.context=c;
//			this.outputParent=o;
//			this.queryPosition=q;
//			this.actionFreePrefix = a;
//			this.actionFreeNavigation = false;
//			this.position = 1;
//			this.last = 1;
//		}
//		
		/**
		 * Basic starter constructor.  Populates with safe values with minimal input.  Assumes no action free prefix, no "parent" context, and no descendant step
		 * @param c input context
		 * @param q position in AST
		 */
		public TreeWalkerState(OXPathType c, Node q) {
			this.context = c;
			this.queryPosition = q;
			this.actionFreePrefix = q;
			this.actionFreePrefixEnd = q;
			this.actionFreeNavigation = false;
			this.position = 1;
			this.last = 1;
//			this.descendant = false;
		}
		
		/**
		 * (Verbose) Constructor for TreeWalkerState to be used to instantiate before each call; this constructor is for general use, as all state info 
		 * is explicitly defined by input parameters.  Safe for use immediately after construction
		 * @param c the context over the DOM tree
		 * @param q the current position in the query AST
		 * @param a the root of the action-free prefix
		 * @param ae the end of the action-free prefix
		 * @param afn whether current navigation is action-free
		 * @param p position in current context
		 * @param l last in current context
		 * @param d <tt>true</tt> if next step is a descendant; <tt>false</tt> otherwise
		 */
		public TreeWalkerState(OXPathType c, Node q, Node a, Node ae, boolean afn, int p, int l) {
			this.context=c;
			this.queryPosition=q;
			this.actionFreePrefix = a;
			this.actionFreePrefixEnd = ae;
			this.actionFreeNavigation = afn;
			this.position = p;
			this.last = l;
//			this.descendant = d;
		}
		
		/**
		 * Creates a new object with the same state as the input parameter; meant to be created than muted when only a few changes are necessary
		 * @param that state whose content is copied
		 */
		public TreeWalkerState(TreeWalkerState that) {
			this.context = that.getContext();
			this.queryPosition = that.getQueryPosition();
			this.actionFreePrefix = that.getActionFreePrefix();
			this.actionFreePrefixEnd = that.getActionFreePrefixEnd();
			this.actionFreeNavigation = that.isActionFreeNavigation();
			this.position = that.getPosition();
			this.last = that.getLast();
//			this.descendant = that.isDescendant();
		}
		
		/**
		 * Sets context at state
		 * @param o context
		 * @return same object with update applied
		 */
		public TreeWalkerState setContext(OXPathType o) {
			this.context = o;
			return this;
		}
		
		/**
		 * Returns context at c
		 * @return context node list
		 */
		public OXPathType getContext() {
			return this.context;
		}
		
//		/**
//		 * Returns the parent of the output	
//		 * @return node that is parent of output in current expression
//		 */
//		public Element getOutputParent() {
//			return this.outputParent;
//		}
		
		/**
		 * Sets the current position of the OXPath query evaluation within the AST
		 * @param current position of the OXPath query evaluation within the AST
		 * @return same object with update applied
		 */
		public TreeWalkerState setQueryPosition(Node n) {
			this.queryPosition = n;
			return this;
		}
		
		/**
		 * Returns the current position of the OXPath query evaluation within the AST
		 * @return the node currently being evaluated in the OXPath query
		 */
		public Node getQueryPosition() {
			return this.queryPosition;
		}
		
		/**
		 * Sets the action free prefix of current context
		 * @param n the action free prefix of current context
		 * @return same object with update applied
		 */
		public TreeWalkerState setActionFreePrefix(Node n) {
			this.actionFreePrefix = n;
			return this;
		}
		
		/**
		 * Returns the action free prefix of current context
		 * @return action free prefix as a String object
		 */
		public Node getActionFreePrefix() {
			return this.actionFreePrefix;
		}
		
		/**
		 * Returns the point in the AST where Action Free Navigation should end
		 * @return the point in the AST where Action Free Navigation should end
		 */
		public Node getActionFreePrefixEnd() {
			return this.actionFreePrefixEnd;
		}
		
		/**
		 * Sets if current navigation is action-free (i.e. retracing)
		 * @param b if current navigation is action-free (i.e. retracing)
		 * @return same object with update applied
		 */
		public TreeWalkerState setIsActionFreeNavigation(boolean b) {
			this.actionFreeNavigation = b;
			return this;
		}
		
		/**
		 * Returns if current navigation is action-free (i.e. retracing)
		 * @return boolean <tt>true</tt> if retracing action free prefix; <tt>false</tt> otherwise
		 */
		public boolean isActionFreeNavigation() {
			return this.actionFreeNavigation;
		}
		
		/**
		 * Sets current position of evaluated "parent" context within for filter for position function
		 * @param iPosition current position of evaluated "parent" context within for filter for position function
		 * @return same object with update applied
		 */
		public TreeWalkerState setPosition(int iPosition) {
			this.position = iPosition;
			return this;
		}
		
		/**
		 * Returns current position of evaluated "parent" context within for filter for position function
		 * @return position of the node within the "parent" context
		 */
		public int getPosition() {
			return this.position;
		}
		
		/**
		 * Sets the last position of evaluated "parent" context within filter expression
		 * @param iLast last position of evaluated "parent" context within filter expression
		 * @return same object with update applied
		 */
		public TreeWalkerState setLast(int iLast) {
			this.last = iLast;
			return this;
		}
		
		/**
		 * Returns last position of evaluated "parent" context within filter expression
		 * @return last position of the node within the "parent" context
		 */
		public int getLast() {
			return this.last;
		}
		
//		/**
//		 * Sets if the next step is a descendant step
//		 * @param isd <tt>true</tt> if next step is a descendant; <tt>false</tt> otherwise
//		 * @return same object with update applied
//		 */
//		public TreeWalkerState setIsDescendant(boolean isd) {
//			this.descendant = isd;
//			return this;
//		}
		
//		/**
//		 * Returns if the first step to be evaluated is a descendant step
//		 * @return <tt>true</tt> if next step is a descendant; <tt>false</tt> otherwise
//		 */
//		public boolean isDescendant() {
//			return this.descendant;
//		}
		
		/**
		 * context at current "step" in query
		 */
		private OXPathType context;
		
//		/**
//		 * parent node for current output
//		 */
//		private Element outputParent;
		
		/**
		 * the current position of the OXPath query evaluation within the AST
		 */
		private Node queryPosition;
		
		/**
		 * records the previous node for action free navigation root
		 */
		private Node actionFreePrefix;
		
		/**
		 * records the end node for action free navigation
		 */
		private Node actionFreePrefixEnd;
		
		/**
		 * Records if current navigation is action-free (i.e. retracing)
		 */
		private Boolean actionFreeNavigation;
		
		/**
		 * Records current node's position in the nodeset
		 */
		private int position;
		
		/**
		 * Records current nodeset's last position
		 */
		private int last;
		
//		/**
//		 * Determines whether the next step is a descendent or regular step
//		 */
//		private boolean descendant;
	}
	
	/**
	 * String enclosing visibility test for fields by examining inline CSS data
	 */
	public static String VISIBILITYTEST = "[not(./@type='hidden')][not(ancestor-or-self::*/@style[starts-with(normalize-space(substring-after(substring-after(.,'visibility'),':')),'hidden')])][not(ancestor-or-self::*/@style[starts-with(normalize-space(substring-after(substring-after(.,'display'),':')),'none')])]";
	
	/**
	 * Field names to check
	 */
	public static final String[] FIELDS = {"input", "select", "textarea", "button"};
	
	/**
	 * stores forward oxpath axes
	 */
	public static final String[] FORWARDAXES = {"descendant", "following"};
	
	/**
	 * stores backwards oxpath axes
	 */
	public static final String[] BACKWARDAXES = {"ancestor", "preceding"};
	//TODO: FIX these
	/**
	 * Some unique value (not occurring in XML) to substitute for < for dealing with the Java Document interface 
	 */
	public static String LESSTHANSUB = "etrwetwert";
	/**
	 * Some unique value (not occurring in XML) to substitute for > for dealing with the Java Document interface 
	 */
	public static String GREATERTHANSUB = "iouopiupiouopiu";
	/**
	 * Some unique value (not occurring in XML) to substitute for & for dealing with the Java Document interface 
	 */
	public static String AMPSUB = "xzvczcvzczxvczcxz";
	
}