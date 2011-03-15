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
package uk.ac.ox.comlab.oxpath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class that provides objects, methods, and constants for XQuerys, which in turn support the info scraping function of the OXPath framework.
 * Specific objects will support either scrapping or next link query types in current implementation
 * @author AndrewJSel
 *
 */
public abstract class XQueryFactory {
	
	/**
	 * Constructor for Scraper objects; populates with state information for facilitating web scraping
	 */
	public XQueryFactory() {
		//create the boilerplate namespace by declaring the namespace; getDeclaresString() is populated based on the declares mapping
		declares.put(NAMESPACE,XHTMLNAMESPACEPREAMBLE);//add namespace
		boilerPlate = this.getDeclaresString();
		declares.remove(NAMESPACE);
	}
	
	/**
	 * **INTERNAL API - DO NOT USE** method for constructing the declares part of an XQuery
	 * @return segment of XQuery with declares information
	 */
	private String getDeclaresString() {
		String rv = "";
		Set<String> keys = this.declares.keySet();
		for (String key : keys) {
			rv += ("declare " + key + " " + this.declares.get(key) + ";\n");//declare field formatting 
		}
		return rv;
	}
	
	/**
	 * getter method that returns boilerplate for SAXON expressions (currently namespace declaration for XHTML)
	 * @return the boilerplate String to be used at the beginning of any SAXON expressions for OXPath (append queries after this return value)
	 */
	public String getBoilerPlate() {
		return this.boilerPlate;
	}

	/**
	 * private instance field for holding the declared values of the query
	 */
	private Map<String,String> declares = new HashMap<String,String>();
	
	/**
	 * constant holding the namespace declaration
	 */
	static final String NAMESPACE = "namespace";
	
	/**
	 * constant referencing path info for the XHTML namespace
	 */
	static final String XHTMLNAMESPACEPREAMBLE = "xhtml=\"http://www.w3.org/1999/xhtml\"";
	
	/**
	 * String holding the boilerplate portion of an XQuery
	 */
	private String boilerPlate;
}
