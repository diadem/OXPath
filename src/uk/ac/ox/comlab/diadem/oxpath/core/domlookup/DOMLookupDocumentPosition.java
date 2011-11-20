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
 * Package supporting core OXPath functionality.  Contains the interface and implementation for 
 * retrieving current DOM references from references on old DOMs (obtained when the DOM was 
 * previously rendered before a {@code browser.back()} call.  
 */
package uk.ac.ox.comlab.diadem.oxpath.core.domlookup;

import java.util.ArrayList;

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathNodeList;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;
import diadem.common.web.dom.DOMDocument;
import diadem.common.web.dom.DOMNode;
import diadem.common.web.dom.xpath.DOMXPathEvaluator;
import diadem.common.web.dom.xpath.DOMXPathResult;

/**
 * An implementation for the DOM Lookup Position based on document order
 * @author AndrewJSel
 *
 */
public class DOMLookupDocumentPosition implements DOMLookup {

	/**
	 * empty constructor
	 */
	public DOMLookupDocumentPosition() {

	}

	/**
	 * Creates a list of references to nodes so that they can be found in a new document
	 * @param nodes the list of OXPathNodes
	 * @return references to these nodes retrievable in a new document
	 * @throws OXPathException in case of browser error (will carry the throwable cause)
	 */
	public ArrayList<NodeReference> getNodeReferences(OXPathNodeList nodes) throws OXPathException {
		ArrayList<NodeReference> result = new ArrayList<NodeReference>();
		for (OXPathContextNode node : nodes) {
			result.add(new NodeReferenceDocumentPosition(node));
		}
		return result;
	}

	/**
	 * This DOMLookup versions of references
	 * @author AndrewJSel
	 *
	 */
	private class NodeReferenceDocumentPosition implements NodeReference {

		/**
		 * Creates a node reference based on document position.  Remember to handle the notional root if passed it!
		 * @param node node to reference
		 */
		public NodeReferenceDocumentPosition(OXPathContextNode node) {

			if (node.equals(OXPathContextNode.getNotionalContext())) {
				this.order = NOTIONALCONTEXTORDER;
				this.parent = OXPathContextNode.getNotionalContext().getParent();
				this.last = OXPathContextNode.getNotionalContext().getLast();
			}
			else {
				DOMNode domnode = node.getNode();
				DOMXPathEvaluator xpathnode = domnode.getXPathEvaluator();
				DOMXPathResult resultvalue = xpathnode.evaluate(STALEQUERY, domnode, xpathnode.createNSResolver(domnode.getOwnerDocument()), DOMXPathResult.ANY_TYPE, null);
				this.order = resultvalue.getNumberValue();
				this.parent = node.getParent();
				this.last = node.getLast();
			}
		}

		/**
		 * Returns the rendered node from the current document based on the reference
		 * @param document the document to find the fresh node
		 * @return the rendered node from the current document based on the reference
		 */
		public OXPathContextNode getRenderedNode(DOMDocument document) {
			if (this.order==NOTIONALCONTEXTORDER) return OXPathContextNode.getNotionalContext();
			DOMXPathEvaluator xpathFresh = document.getXPathEvaluator();
			DOMXPathResult resultFresh = xpathFresh.evaluate(getFreshQuery(order), document, xpathFresh.createNSResolver(document), DOMXPathResult.ANY_TYPE, null);
			DOMNode fresh = resultFresh.iterateNext();
			return new OXPathContextNode(fresh,this.parent,this.last);
		}

		/**
		 * document order of node
		 */
		private final double order;
		/**
		 * parent of node
		 */
		private final int parent;
		/**
		 * last sibling of node
		 */
		private final int last;

	}

	/**
	 * The notional context reference for this class
	 */
	private static final double NOTIONALCONTEXTORDER = -1.0;

	/**
	 * Used to construct the fresh query prefix based on the result of the stale query
	 * @param order the document order of the fresh node
	 * @return the document order of the fresh node
	 */
	private String getFreshQuery(double order) {
		return FRESHQUERYPREFIX + order + FRESHQUERYSUFFIX;
	}

	/**
	 * Query to use from stale nodes to get a document count
	 */
	public static final String STALEQUERY = "count(ancestor::*) + count(preceding::*)";
	/**
	 * Prefix expression to get fresh nodes
	 */
	public static final String FRESHQUERYPREFIX = "descendant::*[";
	/**
	 * Suffix expression to get fresh nodes
	 */
	public static final String FRESHQUERYSUFFIX = "+1]";

}
