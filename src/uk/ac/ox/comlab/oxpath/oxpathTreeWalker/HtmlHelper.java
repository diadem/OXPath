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
package uk.ac.ox.comlab.oxpath.oxpathTreeWalker;

import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import uk.ac.ox.comlab.oxpath.BadDataException;
import uk.ac.ox.comlab.oxpath.FieldTypes;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * 
 * Class for supporting actions in web pages 
 * @author AndrewJSel
 *
 */
public class HtmlHelper {

	/**
	 * Returns a FieldType based on the HTML data of the node.
	 * 
	 * @param n
	 *            Node object to return FieldType
	 * @return value from FieldType enum relating HTML form field type of <tt>n</tt>
	 */
	public FieldTypes getFieldType(Node n) {
		FieldTypes ft = null;
		String nodeName = n.getNodeName().toLowerCase();
		if (nodeName == null)
			throw new NullPointerException("Input parameter cannot be null!");
		// these are all mutually exclusive conditions, ft should be set exactly once
		if (nodeName.equals("input")) {
			NamedNodeMap nns = n.getAttributes();
			Node typeRaw = nns.getNamedItem("type");
			if (typeRaw == null)
				throw new NullPointerException("Input node must have a type attribute!");
			String type = typeRaw.getNodeValue().toLowerCase();
			if (type.equals("text")) {
				ft = FieldTypes.TEXT;
			} else if (type.equals("password")) {
				ft = FieldTypes.PASSWORD;
			} else if (type.equals("checkbox")) {
				ft = FieldTypes.CHECKBOX;
			} else if (type.equals("radio")) {
				ft = FieldTypes.RADIOBUTTON;
			} else if (type.equals("button")) {
				ft = FieldTypes.INPUTBUTTON;
			} else if (type.equals("file")) {
				ft = FieldTypes.INPUTFILE;
			} else if (type.equals("image")) {
				ft = FieldTypes.INPUTIMAGE;
			} else if (type.equals("submit")) {
				ft = FieldTypes.INPUTSUBMIT;
			} else if (type.equals("reset")) {
				ft = FieldTypes.INPUTRESET;
			}
			// no need for test here, as XHTML/HTML4 would raise error if tag didn't belong
		} else if (nodeName.equals("textarea")) {
			ft = FieldTypes.TEXTAREA;
		} else if (nodeName.equals("select")) {
			ft = FieldTypes.SELECT;
		} else if (nodeName.equals("button")) {
			ft = FieldTypes.BUTTON;
		} else if (nodeName.equals("a")) {
			ft = FieldTypes.HREF;
		} else {
			ft = FieldTypes.CLICKABLE;
		}
		return ft;
	}

	/**
	 * This method takes an action on a page, including any relevant javascript events listening for said action.
	 * Inputs text.  Simulates typing, rather than cutting and pasting.  Typing '\n' submits the corresponding form
	 * for many elements associated with fields
	 * 
	 * @param page
	 *            page to perform the action on
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  action string of action
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws BadDataException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public HtmlPage takeAction(HtmlPage page, DomNode context, FieldTypes ft, String action) throws BadDataException, IOException {
			HtmlElement textInput = (HtmlElement) context;
			textInput.focus();
			if (textInput instanceof HtmlTextInput) ((HtmlTextInput) textInput).setText("");
			textInput.type(action);
			return page;		
	}
	
	/**
	 * This method takes an action on a page, including any relevant javascript events listening for said action
	 * Handles selection on an HTML <tt>select</tt> element
	 * @param page
	 *            page to perform the action on
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  action string of action
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws BadDataException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public HtmlPage takeAction(HtmlPage page, DomNode context, FieldTypes ft, ArrayList<Integer> action) throws BadDataException, IOException {
		//handle selection by position within an HTML <select> element
		// decide which action is appropriate
		switch (ft) {// because we checked the node properties in getFieldType(), each of the initial casts is a safe operation
		case SELECT:
			HtmlSelect select = (HtmlSelect) context;
			select.focus();
			for (Integer i : action) {
				page = (HtmlPage) select.setSelectedAttribute(select.getOption(i-1), true);
			}
			return page;
		default :
			throw new BadDataException("Cannot use position references for HTML elements other than <select>");
		}
	}
	
	/**
	 * This method takes an action on a page, including any relevant javascript events listening for said action
	 * 
	 * @param page
	 *            page to perform the action on
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  action string of action
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws BadDataException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public HtmlPage takeAction(HtmlPage page, DomNode context, FieldTypes ft, ActionKeywords action) throws BadDataException, IOException {
		//we're only dealing with HtmlPage because of uri retrieval, so casts to (HtmlPage) are safe here
		HtmlPage returnPage = page;
		HtmlElement element = (HtmlElement) context;
//		element.focus();
		switch (action) {
			case CLICK :
				if (element instanceof HtmlAnchor) ((HtmlAnchor) element).click();
				returnPage = element.click();
				break;
			case DBLCLICK :
				returnPage = element.dblClick();
				break;
			case MOUSEDOWN :
				returnPage = (HtmlPage) element.mouseDown();
				break;
			case MOUSEMOVE :
				returnPage = (HtmlPage) element.mouseMove();
				break;
			case MOUSEOVER :
				returnPage = (HtmlPage) element.mouseOver();
				break;
			case MOUSEOUT :
				returnPage = (HtmlPage) element.mouseOut();
				break;
			case MOUSEUP :
				returnPage = (HtmlPage) element.mouseUp();
				break;
			case RIGHTCLICK :
				returnPage = (HtmlPage) element.rightClick();
				break;
			case CHECK :
				switch (ft) {
					case CHECKBOX :
						returnPage = (HtmlPage) ((HtmlCheckBoxInput) element).setChecked(true);
						break;
					case RADIOBUTTON :
						returnPage = (HtmlPage) ((HtmlRadioButtonInput) element).setChecked(true);
						break;
					default :
						returnPage = element.click();
						break;
				}				
				break;
			case UNCHECK :
				switch (ft) {
				case CHECKBOX :
					returnPage = (HtmlPage) ((HtmlCheckBoxInput) element).setChecked(false);
					break;
				case RADIOBUTTON :
					returnPage = (HtmlPage) ((HtmlRadioButtonInput) element).setChecked(false);
					break;
				default :
					returnPage = element.click();
					break;
			}
				break;
			case KEYDOWN : //implemented as part of doType(String) method (protected and incorporated into simulation)
			case KEYPRESS ://implemented as part of doType(String) method (protected and incorporated into simulation)
			case KEYUP ://implemented as part of doType(String) method (protected and incorporated into simulation)
				element.type("");
				break;
			case SUBMIT :
				element.type("\n");
				break;
			case WHEEL ://not currently implemented in HtmlUnit
			default ://do nothing
				break;
		}
		return returnPage;
	}
}
		
		
		
		
		
		
		
		
		
	
