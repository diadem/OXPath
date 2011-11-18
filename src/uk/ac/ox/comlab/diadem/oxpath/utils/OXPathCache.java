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
 *Package with utilities for supporting OXPath expression evaluation
 */
package uk.ac.ox.comlab.diadem.oxpath.utils;

import diadem.common.web.dom.DOMDocument;

/**
 * Interface that all OXPath memoized functions must support so that cached data relating to a page, <tt>P</tt>, can be cleared
 * once <tt>P</tt> is closed by PAAT.  In most implementations, this function will be only a filler, as the proxy's implementation
 * will override this method and not the method specified in this object.
 * @author AndrewJSel
 *
 */
public interface OXPathCache {

	/**
	 * Clears all memoized data for the input {@code DOMDocument}.  Will be overridden as long as a proxy object is used (should 
	 * always be used for OXPath classes overridding this method).
	 * @param page {@code DOMDocument} we are removing all memoized results for, presumably because the page is being closed in PAAT
	 * 
	 */
	public Boolean clear(DOMDocument page);
	
}
