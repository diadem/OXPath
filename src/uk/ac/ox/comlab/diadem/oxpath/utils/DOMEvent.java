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
 * Package with utilities for supporting OXPath expression evaluation. 
 */
package uk.ac.ox.comlab.diadem.oxpath.utils;

/**
 * DOM Events for controlling input into the {@code OXPathWebAccessNode.fireEvent(event)} method
 * by enumeration of each of the DOM 3 Events to fire.
 * @author AndrewJSel
 *
 */
public enum DOMEvent {
	/**
	* Enum value representing DOMAttributeNameChanged DOM Event
	*/
	DOMATTRIBUTENAMECHANGED("DOMAttributeNameChanged"),
	/**
	* Enum value representing DOMAttrModified DOM Event
	*/
	DOMATTRMODIFIED("DOMAttrModified"),
	/**
	* Enum value representing DOMCharacterDataModified DOM Event
	*/
	DOMCHARACTERDATAMODIFIED("DOMCharacterDataModified"),
	/**
	* Enum value representing DOMElementNameChanged DOM Event
	*/
	DOMELEMENTNAMECHANGED("DOMElementNameChanged"),
	/**
	* Enum value representing DOMFocusIn DOM Event
	*/
	DOMFOCUSIN("DOMFocusIn"),
	/**
	* Enum value representing DOMFocusOut DOM Event
	*/
	DOMFOCUSOUT("DOMFocusOut"),
	/**
	* Enum value representing DOMNodeInserted DOM Event
	*/
	DOMNODEINSERTED("DOMNodeInserted"),
	/**
	* Enum value representing DOMNodeInsertedIntoDocument DOM Event
	*/
	DOMNODEINSERTEDINTODOCUMENT("DOMNodeInsertedIntoDocument"),
	/**
	* Enum value representing DOMNodeRemoved DOM Event
	*/
	DOMNODEREMOVED("DOMNodeRemoved"),
	/**
	* Enum value representing DOMNodeRemovedFromDocument DOM Event
	*/
	DOMNODEREMOVEDFROMDOCUMENT("DOMNodeRemovedFromDocument"),
	/**
	* Enum value representing DOMSubtreeModified DOM Event
	*/
	DOMSUBTREEMODIFIED("DOMSubtreeModified"),
	/**
	* Enum value representing error DOM Event
	*/
	ERROR("error"),
	/**
	* Enum value representing focus DOM Event
	*/
	FOCUS("focus"),
	/**
	* Enum value representing focusin DOM Event
	*/
	FOCUSIN("focusin"),
	/**
	* Enum value representing focusout DOM Event
	*/
	FOCUSOUT("focusout"),
	/**
	* Enum value representing keydown DOM Event
	*/
	KEYDOWN("keydown"),
	/**
	* Enum value representing keypress DOM Event
	*/
	KEYPRESS("keypress"),
	/**
	* Enum value representing keyup DOM Event
	*/
	KEYUP("keyup"),
	/**
	* Enum value representing load DOM Event
	*/
	LOAD("load"),
	/**
	* Enum value representing mousedown DOM Event
	*/
	MOUSEDOWN("mousedown"),
	/**
	* Enum value representing mouseenter DOM Event
	*/
	MOUSEENTER("mouseenter"),
	/**
	* Enum value representing mouseleave DOM Event
	*/
	MOUSELEAVE("mouseleave"),
	/**
	* Enum value representing mousemove DOM Event
	*/
	MOUSEMOVE("mousemove"),
	/**
	* Enum value representing mouseout DOM Event
	*/
	MOUSEOUT("mouseout"),
	/**
	* Enum value representing mouseover DOM Event
	*/
	MOUSEOVER("mouseover"),
	/**
	* Enum value representing mouseup DOM Event
	*/
	MOUSEUP("mouseup"),
	/**
	* Enum value representing resize DOM Event
	*/
	RESIZE("resize"),
	/**
	* Enum value representing scroll DOM Event
	*/
	SCROLL("scroll"),
	/**
	* Enum value representing select DOM Event
	*/
	SELECT("select"),
	/**
	* Enum value representing textInput DOM Event
	*/
	TEXTINPUT("textInput"),
	/**
	* Enum value representing unload DOM Event
	*/
	UNLOAD("unload"),
	/**
	* Enum value representing wheel DOM Event
	*/
	WHEEL("wheel");
	
	/**
	 * basic constructor
	 * @param iName name of event, per DOM 3 standard
	 */
	private DOMEvent(String iName) {name=iName;}
	
	/**
	 * Returns DOM 3 standard name of enum value
	 * @return DOM 3 standard name of enum value
	 */
	@Override
	public String toString() {return this.name;}
	
	/**
	 * private instance field storing the enum value's name
	 */
	private String name;
}
