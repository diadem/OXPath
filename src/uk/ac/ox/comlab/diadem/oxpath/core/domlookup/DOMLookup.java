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

/**
 * These interface specifies classes that compensate for an issue (observed in Mozilla), 
 * where DOM references perpetuate after new pages are loaded and the old page is revisited by 
 * invoking {@code browser.back()}. Prepare references with {@code getNodeReferences(nodes)}.  Retrieve new nodes via a call to {@code getRenderedNode(staleNode,newDocument)}.
 * @author AndrewJSel
 *
 */
public interface DOMLookup {
	
	/**
	 * Creates a list of references to nodes so that they can be found in a new document.  Remember to handle the notional root if passed it!
	 * @param nodes the list of OXPathNodes
	 * @return references to these nodes retrievable in a new document
	 * @throws OXPathException in case of browser error (will carry the throwable cause)
	 */
	public ArrayList<NodeReference> getNodeReferences(OXPathNodeList<OXPathContextNode> nodes) throws OXPathException;
	
}
