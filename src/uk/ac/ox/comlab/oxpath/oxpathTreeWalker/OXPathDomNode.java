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
package uk.ac.ox.comlab.oxpath.oxpathTreeWalker;

import static uk.ac.ox.comlab.oxpath.oxpathTreeWalker.OXPathType.OXPathTypes.NODESET;
import uk.ac.ox.comlab.oxpath.BadDataException;
import uk.ac.ox.comlab.oxpath.benchmark.BenchFactory;
import uk.ac.ox.comlab.oxpath.benchmark.BenchMarker;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * Class for representing OXPathDomNode.  Acts as a wrapper for HtmlUnit's DomNode (and its child) decorating with parent marker and current marker references
 * @author AndrewJSel
 *
 */
public class OXPathDomNode {
	
	/**
	 * Constructor for the class.  Object "glues" together a DomNode in HtmlUnit's implementation, a reference to the parent marker,
	 *  and a reference to the last marker used
	 * @param iNode DomNode in HtmlUnit to use
	 * @param iParent reference to parent marker
	 * @param iLast reference to current marker
	 */
	public OXPathDomNode(DomNode iNode, int iParent, int iLast) {
		node = iNode;
		parent = iParent;
		last = iLast;
	}
	
	/**
	 * Gets the object node
	 * @return the DomNode
	 */
	public DomNode getNode() {
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
	
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof OXPathDomNode)) return false;
//		else {
//			OXPathDomNode that = (OXPathDomNode) obj; 
//			return ((this.getNode().equals(that.getNode()))&&(this.getParent()==that.getParent())&&(this.getLast()==that.getLast())) ? true : false;
//		}
//	}
	
	
	
	/**
	 * Returns the node by calling the getByXPath in HtmlUnit.  Only use when return value is an XPath nodeset data type
	 * @param stepString xpath query as a String
	 * @param forward true for forward navigation, false otherwise
	 * @return OXPathNodeList with all relevant nodes
	 * @throws BadDataException in case of error on adding nodes to the list (return value of xpath call is not a nodelist)
	 */
	public OXPathNodeList<OXPathDomNode> getByXPath(String stepString,
			boolean forward) throws BadDataException {
		OXPathNodeList<OXPathDomNode> result = new OXPathNodeList<OXPathDomNode>();
		BenchMarker xalanOverhead = BenchFactory.newBenchMarker("xalanOverhead");
		xalanOverhead.start();
		DomNode iC = this.getNode();
		//since we are passing in XPath, no extraction are encountered, so parent and current are the same
		OXPathType iResult = new OXPathType(iC.getByXPath(stepString), this.getParent(), this.getLast());
		if (iResult.isType().equals(NODESET)) {
			result.addList(iResult.nodeList());
		}
		xalanOverhead.finish();
		return result;
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
		OXPathDomNode other = (OXPathDomNode) obj;
		if (last != other.last)
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (parent != other.parent)
			return false;
		return true;
	}

	/**
	 * instance field storing the node
	 */
	private DomNode node;
	/**
	 * instance field storing the reference to the parent marker
	 */
	private int parent;
	/**
	 * instance field storing the reference to the current marker
	 */
	private int last;

}
