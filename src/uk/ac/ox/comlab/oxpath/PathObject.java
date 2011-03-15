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

import uk.ac.ox.comlab.oxpath.scriptParser.SimpleNode;

/**
 * Simple class for associating path AST with their respective URLs
 * @author AndrewJSel
 */
public class PathObject {
	
	/**
	 * Basic Constructor for PathObject
	 * @param aPathUrl url as String
	 * @param aPathAST root node of Path Abstract Syntax Tree
	 */
	public PathObject(String aPathUrl, SimpleNode aPathAST) {
		this.setPathURL(aPathUrl);
		this.setPathAST(aPathAST);
	}
	
	/**
	 * Basic setter for associating url's with PathObjects
	 * @param aPathUrl path URL
	 */
	public void setPathURL(String aPathUrl) {
		this.pathUrl = aPathUrl;
	}
	
	/**
	 * Basic getter for url's with PathObjects
	 * @return path URL for implicit parameter
	 */
	public String getPathURL() {
		return pathUrl;
	}
	
	/**
	 * Basic setter for associating paths with PathObjects
	 * @param aPathAST root of the Abstract Syntax Tree for the path
	 */
	public void setPathAST(SimpleNode aPathAST) {
		this.pathAST=aPathAST;
	}
	
	/**
	 * Basic getter for paths with PathObjects
	 * @return path Abstract Syntax Tree for the PathObjects
	 */
	public SimpleNode getPathAST() {
		return pathAST;//returning mutable field, fix before eventual release
	}
	
	/**
	 * private field encoding path URL state info
	 */
	private String pathUrl;
	
	/**
	 * private field encoding path Abstract Syntax Tree
	 */
	private SimpleNode pathAST;
}
