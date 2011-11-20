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
 * Package containing supporting classes, derived from the OXPath model (which itself extends the XPath model).
 */
package uk.ac.ox.comlab.diadem.oxpath.model;

import java.util.Collections;

import diadem.common.web.dom.DOMNode;
import diadem.common.web.dom.xpath.DOMXPathEvaluator;
import diadem.common.web.dom.xpath.DOMXPathException;
import diadem.common.web.dom.xpath.DOMXPathResult;
import uk.ac.ox.comlab.diadem.oxpath.model.language.Axis;
import uk.ac.ox.comlab.diadem.oxpath.model.language.AxisType;
import uk.ac.ox.comlab.diadem.oxpath.model.language.NodeTest;
import uk.ac.ox.comlab.diadem.oxpath.model.language.NodeTestType;
import uk.ac.ox.comlab.diadem.oxpath.model.language.OXPathAxis;
import uk.ac.ox.comlab.diadem.oxpath.model.language.OXPathNodeTest;
import uk.ac.ox.comlab.diadem.oxpath.model.language.Step;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Class for representing OXPathContextNode.  Acts as a wrapper for DOM nodes, decorated with parent marker and current marker references.
 * @author AndrewJSel
 *
 */
public class OXPathContextNode implements Comparable<OXPathContextNode>{

	/**
	 * Constructor for the class.  Object "glues" together a DomNode in HtmlUnit's implementation, a reference to the parent marker,
	 *  and a reference to the last marker used
	 * @param iNode DomNode in HtmlUnit to use
	 * @param iParent reference to parent marker
	 * @param iLast reference to current marker
	 */
	public OXPathContextNode(DOMNode iNode, int iParent, int iLast) {
		node = iNode;
		parent = iParent;
		last = iLast;
	}

	/**
	 * Gets the object node
	 * @return the DomNode
	 */
	public DOMNode getNode() {
		return node;
	}

	/**
	 * Gets the reference to the parent marker
	 * @return reference to the parent marker
	 */
	public int getParent() {
		return parent;
	}

	/**
	 * Gets the reference to the last marker
	 * @return reference to the last marker
	 */
	public int getLast() {
		return last;
	}

	@Override
	public String toString() {
		return (this.getClass()+ "[" + this.getNode()+", " +this.getParent()+", " +this.getLast() + "]");
	}
	
	/**
	 * Returns the node by calling the getByXPath in HtmlUnit.  Only use when return value is an XPath nodeset data type.
	 * @param stepString xpath query as a String
	 * @param forward {@code true} for forward navigation, {@code false} otherwise
	 * @return OXPathNodeList with all relevant nodes
	 * @throws OXPathException in case of error on adding nodes to the list (return value of xpath call is not a nodelist)
	 */
	public OXPathType getByXPath(String stepString, boolean forward) throws OXPathException {
		return this.getByXPath(stepString, forward, false);
	}

	/**
	 * Returns the node by calling the getByXPath in HtmlUnit.  Only use when return value is an XPath nodeset data type.
	 * @param stepString xpath query as a String
	 * @param forward {@code true} for forward navigation, {@code false} otherwise
	 * @param returnsNodes {@code true} if navigation returns a nodeset, {@code false} if this is unknown
	 * @return OXPathNodeList with all relevant nodes
	 * @throws OXPathException in case of error on adding nodes to the list (return value of xpath call is not a nodelist)
	 */
	public OXPathType getByXPath(String stepString, boolean forward, boolean returnsNodes) throws OXPathException {

		DOMNode context = this.getNode();
		DOMXPathEvaluator xpathEvaluator = context.getXPathEvaluator();
		short resultType = (returnsNodes)?DOMXPathResult.UNORDERED_NODE_SNAPSHOT_TYPE:DOMXPathResult.ANY_TYPE;
		
		//since we are passing in XPath, no extraction is encountered, so parent and current are the same
		DOMXPathResult iResult = xpathEvaluator.evaluate(stepString, context, xpathEvaluator.createNSResolver(context), resultType, null);

		//build the correct OXPathType from our result
		OXPathNodeList nodes;
		switch(iResult.getResultType()) {
		case DOMXPathResult.NUMBER_TYPE :
			return new OXPathType(iResult.getNumberValue());
		case DOMXPathResult.STRING_TYPE :
			return new OXPathType(iResult.getStringValue());
		case DOMXPathResult.BOOLEAN_TYPE :
			return new OXPathType(iResult.getBooleanValue());
		case DOMXPathResult.UNORDERED_NODE_SNAPSHOT_TYPE :
			nodes = (forward)?new OXPathNodeList():new OXPathNodeList(Collections.reverseOrder());
			for (long i=0; i<iResult.getSnapshotLength(); i++) {
				nodes.add(new OXPathContextNode(iResult.snapshotItem((int) i),this.getParent(),this.getLast()));
			}
			if (nodes.isEmpty()) return OXPathType.EMPTYRESULT;
			return new OXPathType(nodes);
		case DOMXPathResult.UNORDERED_NODE_ITERATOR_TYPE : //based on our evaluate method above, this should be the only kind of nodeset we see
			nodes = (forward)?new OXPathNodeList():new OXPathNodeList(Collections.reverseOrder());
			boolean done = false;
			boolean rerun = false;
			while (!done) {
				try {
					if (iResult.getInvalidIteratorState()) {
						iResult = xpathEvaluator.evaluate(stepString, context, xpathEvaluator.createNSResolver(context), DOMXPathResult.ANY_TYPE, null);
						rerun=true;
						done=true;	
					}
					else {
						DOMNode node = iResult.iterateNext();
						if (node==null) done=true;
						else nodes.add(new OXPathContextNode(node,this.getParent(),this.getLast()));
					}
				} catch (DOMXPathException e) {//it's possible for this to happen between the check and our invocation of iterateNext
//                    e.printStackTrace();
                    rerun =true;
					done = true;
				}
			}
			if (rerun) {
				iResult = xpathEvaluator.evaluate(stepString, context, xpathEvaluator.createNSResolver(context), DOMXPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
				nodes = new OXPathNodeList();
				for (long i=0; i<iResult.getSnapshotLength(); i++) {
					nodes.add(new OXPathContextNode(iResult.snapshotItem((int) i),this.getParent(),this.getLast()));
				}
			}
			if (nodes.isEmpty()) return OXPathType.EMPTYRESULT;
			return new OXPathType(nodes);//we only sort when necessary
			//				if (forward) return new OXPathType(nodes.sortForwardOrder());
			//				else return new OXPathType(nodes.sortReverseOrder());
		default :
			throw new OXPathException ("The browser broke the contract for the XPath evaluator interface!");
		}
	}

	/**
	 * Returns the node by calling the getByXPath in HtmlUnit.  Only use when return value is an XPath nodeset data type.  Shortcut for the more verbose signature of the other {@code getByXPath} method
	 * @param stepString xpath query as a String
	 * @return OXPathNodeList with all relevant nodes
	 * @throws OXPathException in case of error on adding nodes to the list (return value of xpath call is not a nodelist)
	 */
	public OXPathType getByXPath(String stepString) throws OXPathException {
		return this.getByXPath(stepString, true);
	}

	public OXPathType getByOXPath(Step step) throws OXPathException {
		return this.getByOXPath(step.getAxis(), step.getNodeTest());
	}

	private OXPathType getByOXPath(Axis axis, NodeTest nodetest) throws OXPathException {
		if ( (!axis.getType().equals(AxisType.OXPATH)) && (!nodetest.getType().equals(NodeTestType.OXPATH)) ) {
			//this step is just OXPath, so we can use the OXPath engine to get it
			boolean isForward = axis.getType().equals(AxisType.FORWARD);
			return this.getByXPath(axis.getValue()+nodetest.getValue(),isForward,true);
		}
		else if (axis.getType().equals(AxisType.OXPATH)) {
			return ((OXPathAxis) axis).evaluate(this, nodetest);
		}
		else {//we now consider OXPath nodetests
			return ((OXPathNodeTest) nodetest).evaluate(this,axis);
		}
	}

	/**
	 * Returns a unique {@code OXPathContextNode} object, as a placeholder for beginning OXPath expression evaluation
	 * before a root node is retrieved via the <tt>doc(uri)</tt> function.  Also used as a null context in iterative evaluation,
	 * so that null pointers are avoided.
	 * @return the unique notional Context
	 */
	public static OXPathContextNode getNotionalContext() {
		return OXPathContextNode.notionalContext;
	}

	/**
	 * Determines if the implicit parameter is the unique notional context
	 * @return {@code true} if this object is the notional context, {@code false} otherwise
	 */
	public boolean isNotionalContext() {
		return this.equals(OXPathContextNode.notionalContext);
	}
	
	/**
	 * Standard comparator for nodes.  Unfortunately, the interface does not allow an exception to be raised if the nodes are not comparable, so care should be taken before sorting 
	 * (perhaps by checking all nodes are in the same document).  Currently returns {@code Integer.MAX_VALUE} if not a number
	 * @param other first OXPathContextNode to compare
	 * @return standard Java Comparator convention
	 */
	@Override
	public int compareTo(OXPathContextNode other) {
		//compare position of DOMNodes
		DOMNode n1 = this.getNode();
		DOMNode n2 = other.getNode();
		
		short position = n1.compareDocumentPosition(n2);
		
		//This seems counterintuitive, but this is what we want based on Java's definition of "natural ordering" - for us, we assume document order to be the natural ordering, so preceding nodes have higher ordering values
		if (position==0) return 0;
		else if ((position & DOCUMENT_POSITION_PRECEDING)== DOCUMENT_POSITION_PRECEDING) return 1;
		else if ((position & DOCUMENT_POSITION_FOLLOWING)== DOCUMENT_POSITION_FOLLOWING) return -1;
		else return Integer.MAX_VALUE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + last;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + parent;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OXPathContextNode other = (OXPathContextNode) obj;
		if (last != other.last)
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (other.node ==null) {
			return false;
		} else if (!node.isSameNode(other.node))
			return false;
		if (parent != other.parent)
			return false;
		return true;
	}

	/**
	 * instance field storing the node
	 */
	private DOMNode node;
	/**
	 * instance field storing the reference to the parent marker
	 */
	private int parent;
	/**
	 * instance field storing the reference to the current marker
	 */
	private int last;
	/**
	 * encodes the notional context node for beginning navigation; the parent and last are both 0, the id for the "results" root in the 
	 * output
	 */
	private static final OXPathContextNode notionalContext = new OXPathContextNode(null,0,0);
	
	
	// DocumentPosition constants
	/**
	 * Document position constants
	 */
	  static final short      DOCUMENT_POSITION_DISCONNECTED = 0x01;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_PRECEDING    = 0x02;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_FOLLOWING    = 0x04;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_CONTAINS     = 0x08;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_CONTAINED_BY = 0x10;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 0x20;

}
