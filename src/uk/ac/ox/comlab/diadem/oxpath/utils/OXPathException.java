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
 * Package with utilities for supporting OXPath expression evaluation
 */
package uk.ac.ox.comlab.diadem.oxpath.utils;

/**
 * class describing exception thrown if user-supplied OXPath script contains errors/is missing data
 * @author AndrewJSel
 */
public class OXPathException extends Exception {
	/**
	 * basic constructor of exception
	 */
	public OXPathException() {
		super();
	}
	
	/**
	 * constructor allowing message, retrieved with Throwable.getMessage
	 * @param message message to pass along with OXPathException object
	 */
	public OXPathException(String message) {
		super(message);
		this.printStackTrace();
	}
	
	/**
	 * constructor that allows a {@code Throwable} to be associated with the exception
	 * @param message a message associated with the exception
	 * @param cause a {@code Throwable} resulting from some other method invocation
	 */
	public OXPathException(String message, Throwable cause) {
		super(message,cause);
	}
	
	//boilerplate for serialize interface
	private static final long serialVersionUID = 1L;
}
