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
 * Package supporting html functionality of core OXPath engine.
 */
package uk.ac.ox.comlab.diadem.oxpath.dom;

/**
 * Enum encoding action keywords
 * @author AndrewJSel
 *
 */
public enum ActionKeywords {
	/**
	 * <tt>click</tt> event
	 */
	CLICK("click"),
	
	/**
	 * <tt>dblclick</tt> event
	 */
	DBLCLICK("dblclick"),
	
	/**
	 * <tt>keydown</tt> event
	 */
	KEYDOWN("keydown"),
	
	/**
	 * <tt>keypress</tt> event
	 */
	KEYPRESS("keypress"),
	
	/**
	 * <tt>keyup</tt> event
	 */
	KEYUP("keyup"),
	
	/**
	 * <tt>mousedown</tt> event
	 */
	MOUSEDOWN("mousedown"),
	
	/**
	 * <tt>mouseenter</tt> event
	 */
	MOUSEENTER("mouseenter"),
	
	/**
	 * <tt>mousemove</tt> event
	 */
	MOUSEMOVE("mousemove"), 
	
	/**
	 * <tt>mouseover</tt> event
	 */
	MOUSEOVER("mouseover"), 
	
	/**
	 * <tt>mouseout</tt> event
	 */
	MOUSEOUT("mouseout"), 
	
	/**
	 * <tt>mouseup</tt> event
	 */
	MOUSEUP("mouseup"), 
	
	/**
	 * <tt>rightclick</tt> event
	 */
	RIGHTCLICK("rightclick"),
	
	/**
	 * <tt>check</tt> event
	 */
	CHECK("check"), 
	
	/**
	 * <tt>uncheck</tt> event
	 */
	UNCHECK("uncheck"),
	
	/**
	 * <tt>focus</tt>
	 */
	FOCUS("focus"),
	
	/**
	 * <tt>submit</tt> event
	 */
	SUBMIT("submit"),
	
	/**
	 * <tt>wheel</tt> event
	 */
	WHEEL("wheel");
	
	/**
	 * only constructor for ActionKeywords
	 * @param iValue value attribute of enum instance
	 */
	private ActionKeywords(String iValue) {this.value=iValue;}
	
	/**
	 * returns value for implicit parameter
	 * @return value encoded as String object
	 */
	public String getValue() {return this.value;}
	
	/**
	 * returns value for implicit parameter
	 * @return value encoded as String object
	 */
	@Override
	public String toString() {return this.value;}
	
	/**
	 * static method for selecting one of the ActionKeywords objects based on its value
	 * @param key String value for the ActionKeywords instance
	 * @return object based on key
	 */
	public static ActionKeywords getActionKeyword(String key) {
		for (ActionKeywords i : ActionKeywords.values()) {
			if (key.equals(i.getValue())) return i;
		}
		return null;//don't need to check this more carefully, as the parser does this for us
	}
	
	/**
	 * instance field for storing value of the enum object
	 */
	private String value;
}