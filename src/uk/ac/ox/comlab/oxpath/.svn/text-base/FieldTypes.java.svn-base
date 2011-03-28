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
package uk.ac.ox.comlab.oxpath;

/**
 * FieldTypes describe the type (and paths) for web form field queries
 * Only user viewable/mutable field elements are implemented (i.e. no <tt>&lt hidden &gt</tt> tags).
 * Also, includes other "clickable" elements as well, that are constructed differently so as not to be
 * navigated with the additional OXPath axes.  This would be better (ideally) as two different enums, 
 * one extending the other.  Unfortunately, Java doesn't allow multiple inheritance (and every enum 
 * implicitly extends java.lang.Enum).
 * @author AndrewJSel
 *
 */
public enum FieldTypes {
	
	/**
	 * For text box fields
	 */
	TEXT("//input[@type='text']"),
	
	/**
	 * For password fields
	 */
	PASSWORD("//input[@type='password']"),
	
	/**
	 * For text area fields
	 */
	TEXTAREA("//textarea"),
	
	/**
	 * For checkbox fields (each accessed separately)
	 */
	CHECKBOX("//input[@type='checkbox']"),
	
	/**
	 * For radiobox fields (each accessed separately)
	 */
	RADIOBUTTON("//input[@type='radio']"),
	
	/**
	 * for input button fields
	 */
	INPUTBUTTON("//input[@type='button']"),
	
	/**
	 * for input file fields
	 */
	INPUTFILE("//input[@type='file']"),
	
	/**
	 * for input image fields
	 */
	INPUTIMAGE("//input[@type='image']"),
	
	/**
	 * for input submit fields
	 */
	INPUTSUBMIT("//input[@type='submit']"),
	
	/**
	 * for input reset fields
	 */
	INPUTRESET("//input[@type='reset']"),
	
	/**
	 * For dropdown lists/multiple selects
	 */
	SELECT("//select"), 
	
	/**
	 * For buttons (though current HTML4/XHTML conventions dictate that inputs are used
	 */
	BUTTON("//button"),
	
	/**
	 * For anchors whose links we want to click (returning a further page)
	 */
	HREF(),
	
	/**
	 * Final enum for other elements that have <tt>onclick</tt> listening script events
	 */
	CLICKABLE();
	
	/**
	 * Constructor for initializing field types without paths (implicitly meaning these are clickable
	 * items that aren't navigated to with the OXPath additional axes)
	 */
	FieldTypes() {
		path = null;
		hasPath = false;
	}
	
	/**
	 * Constructor for initializing each enum value
	 * @param aPath XPath query for returning the nodes of the implicit enum type 
	 */
	FieldTypes(String aPath) { 
		this.path = aPath;
		this.hasPath = true;
	}
	
	/**
	 * 
	 * @return <tt>true</tt> if the enum value has a path (is reachable with an OXPath additional axis), is <tt>false</tt> otherwise
	 */
	public boolean hasPath() {
		return this.hasPath;
	}
	
	/**
	 * public method that returns the XPath query for retrieving the nodes of the implicit enum value 
	 * @return XPath query for the implicit enum value
	 */
	public String getPath() {
		return this.path;
	}
	
	//instance field storing the XPath query to retrieve the Nodes of each enum value from a document
	private String path;
	private boolean hasPath;
}
