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

import java.util.Comparator;
import java.util.TreeSet;


/**
 * 
 * Extends <tt>DomNode</tt> instead of <tt>Node</tt> because we will use <tt>DomNode</tt> specific methods like <tt>getByXPath()</tt>.  Implemented as
 * contracted by Java library interfaces except where noted.  In particular, for efficiency, lists only contain each element at most one time (set-based
 * paradigm)
 * @author AndrewJSel
 *
 */
public class OXPathNodeList extends TreeSet<OXPathContextNode> {

	public OXPathNodeList() {
		super();
	}
	
	public OXPathNodeList(OXPathContextNode node) {
		super();
		this.add(node);
	}
	
	public OXPathNodeList(Comparator<Object> comparator) {
		super(comparator);
	}
	
	/**
	 * Default id to get rid of compiler warning.  Recommend not serializing - not tested!
	 */
	private static final long serialVersionUID = 1L;
	
	

	
}
