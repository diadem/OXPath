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
/**
 * Package containing core functionality for the extraction features of the OXPath engine
 */
package uk.ac.ox.comlab.diadem.oxpath.core.extraction;

import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathCache;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;
import diadem.common.web.dom.DOMNode;

/**
 * Interface capturing the core methods for OXPath extraction
 * @author AndrewJSel
 *
 */
public interface Extractor extends OXPathCache { 

	/**
	 * Allows the extraction of the node specified by the pair <tt>(context,label)</tt> and returns a unique identifier (as an {@code int}) that
	 * uniquely identifies this extraction marker.  May be a previously occurring identifier if the <tt>(context,label)</tt> has already been 
	 * extracted (via a call to this method with this object). 
	 * @param context the context node in this label
	 * @param label the label of this node in the extraction marker
	 * @param parent the parent of the extraction node specified by <tt>(context,label)</tt>
	 * @return a unique identifier for this extraction (though by OXPath's merge semantics, this extraction node may have already been created -
	 * in this case, the previous identifier is reused)
	 * @throws OXPathException in case the parent of this node is inconsistent with a recurring node (one already created with a previous call to this method)
	 */
	public Integer extractNode(DOMNode context, String label, Integer parent) throws OXPathException;
	
	/**
	 * Allows the extraction of the node specified by the pair <tt>(context,label)</tt> and returns a unique identifier (as an {@code int}) that
	 * uniquely identifies this extraction marker.  May be a previously occurring identifier if the <tt>(context,label)</tt> has already been 
	 * extracted (via a call to this method with this object). 
	 * @param context the context node in this label
	 * @param label the label of this node in the extraction marker
	 * @param parent the parent of the extraction node specified by <tt>(context,label)</tt>
	 * @param value the value associated with this extraction node
	 * @return a unique identifier for this extraction (though by OXPath's merge semantics, this extraction node may have already been created -
	 * in this case, the previous identifier is reused)
	 * @throws OXPathException in case the parent of this node is inconsistent with a recurring node (one already created with a previous call to this method)
	 */
	public Integer extractNode(DOMNode context, String label, Integer parent, String value) throws OXPathException;
	
	/**
	 * Signals end of extraction
	 */
	public void endExtraction() throws OXPathException;
	
}
