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
package uk.ac.ox.comlab.oxpath;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

import uk.ac.ox.comlab.oxpath.oxpathTreeWalker.OXPathDomNode;
//import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
/**
 * Provides helper methods, in particular one that compares document order of two XML Document Nodes
 * Not a static class because of the requirements of Arrays.sort, so object is needed
 * @author AndrewJSel
 *
 */
public class XPathHelper implements Comparator<Node> {
	
	/**
	 * constructor for all XPathHelper objects
	 */
	public XPathHelper() {}
	
	/**
	 * Standard java comparator (operation) for total ordering of documents by document order (defined in W3C XPath Recommendation) 
	 * @param a First Node to compare
	 * @param b Second Node to compare
	 * @return 0 if a=b, &lt 0 iff a &lt b, >0 iff a &gt b, returns BadDataException
	 */
	public int compareDocumentOrder(Node a, Node b) {
		return this.compare(a, b);
	}
	
	/**
	 * implements compareDocumentOrder, needed to implement Comparator<Node> interface
	 * <b>INTERNAL API - USE <tt>compareDocumentOrder(a,b)</tt> INSTEAD</b>
	 * If used (for Arrays.sort(), for example), check first that <tt>a</tt> and <tt>b</tt> are in the Document first, as this method can't throw an exception and still implement the Comparator<T> interface
	 * @param a First Node to compare
	 * @param b Second Node to compare
	 * @return 0 if a=b or a,b are in different Document objects, &lt 0 iff a &lt b, >0 iff a &gt b
	 */
	public int compare(Node a, Node b) {
		if (a.isSameNode(b))return 0;
		else {
				/* Algorithm: build stacks tracing the lineage of both nodes to the root;
				 * next, iteratively pop top elements off the stack as long as they match;
				 * once elements at the top of the stack are found that are not the same, they are children;
				 * pick one, traverse nextSibling until the other is found (picked was first) or no more siblings (picked was second);
				 * return value corresponding to stack element encountered 
				 */
			Deque<Node> aStack = new ArrayDeque<Node>();
			Deque<Node> bStack = new ArrayDeque<Node>();
			//build complete ancestry stack, may be cheaper than checking for sameNode along the way
			Node aContext = a;
			while (aContext!=null) {
				aStack.addFirst(aContext);
				aContext = aContext.getParentNode();
			}
			Node bContext = b;
			while (bContext!=null) {
				bStack.addFirst(bContext);
				bContext = bContext.getParentNode();
			}
			//if statement checks to make sure nodes are in the same document
			if (aStack.peekFirst().isSameNode(bStack.peekFirst())) {
				//pop off while they are the same
				while ((aStack.peekFirst().isSameNode(bStack.peekFirst()))&&!(aStack.peekFirst().isSameNode(b))&&!(bStack.peekFirst().isSameNode(a))) {
					aStack.removeFirst();
					bStack.removeFirst();
				}
				//now that they aren't the same, these two are siblings, or one is an ancestor of the other
				if (aStack.peekFirst().isSameNode(b)) return 1;
				if (bStack.peekFirst().isSameNode(a)) return -1;
				Node context = aStack.removeFirst();
				while (!(context.isSameNode(bStack.peekFirst()))&&context.getNextSibling()!=null) {
					context = context.getNextSibling();
				}
				if (context.isSameNode(bStack.peekFirst())) return -1;
				else return 1;
			} else {
//				try {
//					throw new BadDataException("Can't compare nodes from different documents");
//				} catch (BadDataException e) {
//					System.out.println(e.getMessage());
//					e.printStackTrace();
					//returns 0, but shouldn't have ever been called without checking if a,b are in same Document
					return 0;
//				}
			}
		}
	}
	
	/**
	 * returns the next Node from the explicit parameter in XML Document Order
	 * @param n reference Node  
	 * @return the node next from <tt>n</tt> as defined by XML Document Order, returns <tt>null</tt> if this is the last node in the document
	 */
	public Node nextNodeDocumentOrder(Node n) {
		/* works by returning first child, if n has children
		 * else returns next sibling, if n has a next sibling
		 * else walks through ancestors until one has next sibling
		 */
		final Node next;
		if (n.hasChildNodes()) next = n.getFirstChild();
		else if (n.getNextSibling()!=null) next = n.getNextSibling();
		else {
			//go through parents until you find one with a next sibling, or pass the root
			Node context = n.getParentNode();
			while (context!=null&&context.getNextSibling()==null) {
				context = context.getParentNode();
			}
			//if context is null, you've passed the root Node, meaning n is the last node in the document
			if (context==null) next=null;
			else next=context.getNextSibling();
		}
		return next;
		
	}
	
	/**
	 * Method used to get the root Node of the DOM tree containing the input Node;
	 * can be used instead of getOwnerDocument() when Node hasn't been explicitly assigned to a Document object,
	 * as getOwnerDocument() won't work as expected in many implementations in this case.  
	 * @param n a Node from the DOM tree whose root is requested
	 * @return the root node of the DOM tree containing the input Node <tt>n</tt>
	 */
	public Node getRootNode(Node n) {
		Node context = n;
		while (context.getParentNode()!=null) {
			context = context.getParentNode();
		}
		return context;		
	}

	/**
	 * **USE THIS ONE!**
	 * External method; initializes ArrayList of Form Fields for use in future calls of other method
	 * @param page HtmlPage object encoding form page (or pages)
	 * @param pathName OXPath path from a GroundedOXPathExpression object
	 * @return resulting HtmlPage after query is executed
	 * @throws XPathExpressionException can be thrown if <tt>page</tt> is malformed document
	 * @throws BadDataException in case of malformed navigation path expression
	 * @throws IOException if IO error occurs
	 */
	public HtmlPage getQueryResult(HtmlPage page, String pathName) throws XPathExpressionException, BadDataException, IOException {
		//go to internal method with sorted ArrayList of HTML 4/XHTML Form Fields
		//right now obviously, page and context point to same object; implementation allows such detail
		return getQueryResult(page, page, pathName, getFields(page));
	}

	/**
	 * Internal method for recursively walking an OXPath path, modifying page(s) automatically, and returns first page of the query results 
	 * @param page form page to input query
	 * @param context current context Node (where the path has terminated thus far)
	 * @param pathName remainder of OXPath expression not parsed yet
	 * @param fields ArrayList\<DomNode\> object containing sorted (by document order) form field objects in <tt>page</tt> 
	 * @return HtmlPage encoding first page of query result
	 * @throws BadDataException in case of malformed navigation path expression
	 * @throws IOException if IO error occurs
	 * @throws XPathExpressionException can be thrown if <tt>page</tt> is malformed document
	 */
	private HtmlPage getQueryResult(HtmlPage page, DomNode context, String pathName, ArrayList<DomNode> fields) throws BadDataException, IOException, XPathExpressionException {
		//base case of recursive call, if pathName is consumed, then return page
		if (!(pathName.isEmpty())) {
			/* otherwise, there are 3 cases to consider:
			 * 	(1) one or more standard XPath axes are implemented next
			 *  (2) an OXPath additional axis is implemented
			 *  (3) an OXPath action is implemented on the selected node 	
			 */
			//get token type, next token, and rest
			QueryToken qt = new QueryToken(pathName);
//			System.out.println(qt.getFirstToken());
			switch (qt.getFirstTokenType()) {
				//standard XPath
				case XPATHSTANDARD:	
					return getQueryResult(page, (DomNode) context.getFirstByXPath(qt.getFirstToken()), qt.getRestTokens(), fields);
				//additional axis
				case ADDITIONALAXIS:
					AdditionalAxisNavigator aan = new AdditionalAxisNavigator(qt.getFirstToken());
					DomNode newContext = AdditionalAxisMove(context, aan, fields);
					System.out.println(qt.getRestTokens());
					return getQueryResult(page, newContext, qt.getRestTokens(), fields);
			//action	
				case ACTION:
					//in case of an action, identifies the type of the context node, takes appropriate action, and returns the resulting page after the action and any appropriate JavaScript has occurred
					HtmlHelper helper = new HtmlHelper();
					FieldTypes ft = helper.getFieldType(context);
					page = helper.takeAction(page, context, ft, qt.getFirstToken());
					//update attribute mapping if this action had an attribute
					if (helper.hasAttribute()) {
						this.attributes.put(helper.getLastAttributeName(),helper.getLastAttributeValue());
					}
					//check to see if we are now on a different page then the ones we tested for fields on and we still have more to navigate
					if (!(qt.getRestTokens().isEmpty())) {
						fields =this.getFields(page);
//						System.out.println("New fields sorted!");
					}
					return getQueryResult(page, context, qt.getRestTokens(), fields);
			}			
		}
		return page;
	}

	/**
	 * Method that facilitates an additional axis move
	 * @param context current context node
	 * @param aan Additional axis navigation encoded as an <tt>AdditionalAxisNavigator</tt> object
	 * @param fields the ordered list of fields in the current document
	 * @return
	 * @throws BadDataException
	 */
	private DomNode AdditionalAxisMove(DomNode context,	AdditionalAxisNavigator aan, ArrayList<DomNode> fields) throws BadDataException{
		//ultimately, the value of what we return
		DomNode newContext = null;
		//some useful integrity checks
		if (aan.getOffset()<1) throw new BadDataException("Additional Axis predicate must be positive integer!");
		if (((aan.getAxisType()==AdditionalAxes.NEXTFIELD)||(aan.getAxisType()==AdditionalAxes.PRECEDINGFIELD))&&aan.getOffset()!=1) {
			throw new BadDataException("Next-field and preceding-field tokens can't have a predicate");
		}
		//next determine orientation of the additional axes
		AxisOrientations axisOrient = getAxisOrientation(aan.getAxisType());
		//find the context node's position in respect to document order of the other field nodes
		//if return value >=0, then the context node is that position's field node;
		//else, return value is (-k-1) where k is the position BEFORE which the node belongs
		int contextPosition = Collections.binarySearch(fields, context, new XPathHelper());
		switch (axisOrient) {
			case FORWARD:
				if (contextPosition>=0) {
					if ((contextPosition+1)>fields.size()-1) throw new BadDataException("Can't use forward additional axis beyond last field in document order!");
					newContext = fields.get(contextPosition + 1);
					contextPosition = contextPosition + 1;
				}
				else {
					if ((-contextPosition-1)>fields.size()-1) throw new BadDataException("Can't use forward additional axis beyond last field in document order!");
					newContext = fields.get(-contextPosition-1);
					contextPosition = -contextPosition - 1;
				}//for clarity, we implement the first of the offset, as a context not a field tells us the position BEFORE insertion
				int count = aan.getOffset();//since we implemented the first above, we are assured that offset is always positive
				boolean done = false; //do a loop-and-a-half to determine 
				while (!done) {//continue to increment against offset
					if (HtmlHelper.isVisible(newContext)&&(aan.getNodeType().equals(AdditionalAxisNavigator.WILDCARD)||HtmlHelper.nodeSatisfiesAxis(newContext,aan))) {
						count = count - 1;//only increment the count if the node is visible and of the type needed by the additional axis node
					}
					if (count <= 0) {//we're finished
						done = true;
					}
					else {//if we still have more to go, advance the context node
						if ((contextPosition+1)>fields.size()-1) throw new BadDataException("Can't use forward additional axis beyond last field in document order!");
						newContext = fields.get(contextPosition+1);
						contextPosition = contextPosition + 1;
					}
				}
				break;
			case BACKWARD:
				if (contextPosition>=0) {
					if ((contextPosition-1)<0) throw new BadDataException("Can't use backward additional axis beyond first field in document order!");
					newContext = fields.get(contextPosition - 1);
					contextPosition = contextPosition - 1;
				}
				else {
					if ((-contextPosition-2)<0) throw new BadDataException("Can't use backward additional axis beyond first field in document order!");
					newContext = fields.get(-contextPosition-2);
					contextPosition = -contextPosition - 2;
				}//for clarity, we implement the first of the offset, as a context not a field tells us the position BEFORE insertion
				int counter = aan.getOffset();//since we implemented the first above, we are assured that offset is always positive
				boolean finished = false; //do a loop-and-a-half to determine 
				while (!finished) {//continue to increment against offset
					if (HtmlHelper.isVisible(newContext)&&(aan.getNodeType().equals(AdditionalAxisNavigator.WILDCARD)||HtmlHelper.nodeSatisfiesAxis(newContext,aan))) {
						counter = counter - 1;//only increment the count if the node is visible and of the type needed by the additional axis node
					}
					if (counter <= 0) {//we're finished
						finished = true;
					}
					else {//if we still have more to go, retreat the context node
						if ((contextPosition-1)<0) throw new BadDataException("Can't use backward additional axis beyond first field in document order!");
						newContext = fields.get(contextPosition - 1);
						contextPosition = contextPosition - 1;
					}
				}
				break;
		}
		return newContext;
	}
	
	/**
	 * private method for assigning orientation to additional axis 
	 * @param axisType enum value of AdditionalAxes that requires orientation
	 * @return orientation of explicit parameter 
	 * @throws BadDataException if unknown AdditionalAxis is used (unknown at the time of this method's creation)
	 */
	private AxisOrientations getAxisOrientation(AdditionalAxes axisType) throws BadDataException {
		AxisOrientations rv = null;
		if ((axisType==AdditionalAxes.NEXTFIELD)||(axisType==AdditionalAxes.FOLLOWINGFIELD)) {
			rv = AxisOrientations.FORWARD;
		}
		else if ((axisType==AdditionalAxes.PREVIOUSFIELD)||(axisType==AdditionalAxes.PRECEDINGFIELD)) {
			rv = AxisOrientations.BACKWARD;
		}
		if (rv==null) throw new BadDataException("Unhandled additional axis introduced!");
		return rv;
	}

	/**
	 * private enum for describing additional axis orientation
	 */
	private enum AxisOrientations { FORWARD, BACKWARD }
	

	/**
	 * Internal method for generating sorted ArrayList<DomNode> of all form fields in <tt>page</tt>
	 * @param page HtmlPage with form field elements for inputting query
	 * @return ArrayList<DomNode> object of containing all DOM tree nodes representing input fields, sorted in document order
	 * @throws XPathExpressionException for malformed path in FieldType enum 
	 */
	private ArrayList<DomNode> getFields(HtmlPage page) throws XPathExpressionException {
		//set page that has encoded fields
//		this.pageWithFields = page;
		//data type that will store web form fields
		ArrayList<DomNode> fields = new ArrayList<DomNode>();
		//iteratively add the HTML form inputs we need, using getByXPath seemed unreliable, so using XPathFactory methodology
		for (FieldTypes s : FieldTypes.values()) {
			if (s.hasPath()) {
				fields.addAll(getNodesbyFieldType(page, s.getPath()));
			}
		}
		Collections.sort(fields, new XPathHelper());
//		for (DomNode f : fields) {
//			System.out.println(f.getCanonicalXPath());
//		}
		return fields;
	}

	/**
	 * Internal method for returning an ArrayList of all nodes of a type (determined by FieldType parameter)
	 * @param page HtmlPage object that contains the DomNodes of interest
	 * @param path XPath expression for locating DomNodes of interest
	 * @return ArrayList of DomNodes of interest
	 * @throws XPathExpressionException for malformed path in FieldType enum
	 */
	@SuppressWarnings("unchecked")//needed for the (List<DomNode>) cast in the getByXPath(path) call, shouldn't ever be an issue
	private ArrayList<DomNode> getNodesbyFieldType(HtmlPage page, String path) throws XPathExpressionException {
		List<DomNode> results = new ArrayList<DomNode>();
		results = (List<DomNode>) page.getByXPath(path);
		return (ArrayList<DomNode>) results;
	}
	
	/**
	 * Gets the Query Attribute state information for this object.
	 * <b>IMPORTANT!</b>  Use only after getQueryResult() call of same object, as this method populates this data
	 * @return mapping of attribute names to their values for this specific XPathHelper (which corresponds to a grounded instance of an XPression)
	 */
	public Map<String,String> getXQueryAttributes() {
		return attributes;
	}
	/**
	 * instance field for encoding state data of the page which <fields> in a given corresponds to; this way, if this changes, fields are resorted
	 */
//	private HtmlPage pageWithFields = null;

	/**
	 * instance field for encoding attribute data of the path this XPathHelper has facilitated travel upon
	 */
	private Map<String,String> attributes = new HashMap<String,String>();	
	
}
