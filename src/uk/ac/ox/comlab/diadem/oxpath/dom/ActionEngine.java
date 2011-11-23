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
package uk.ac.ox.comlab.diadem.oxpath.dom;

import java.io.IOException;

import diadem.common.web.dom.DOMElement;
import diadem.common.web.dom.DOMHTMLOptionsCollection;
import diadem.common.web.dom.DOMHTMLSelect;
import diadem.common.web.dom.DOMNamedNodeMap;
import diadem.common.web.dom.DOMNode;
import diadem.common.web.dom.event.DOMFocusEvent;
import diadem.common.web.dom.event.DOMKeyboardEvent;
import diadem.common.web.dom.event.DOMMouseEvent;

import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;
import uk.ac.ox.comlab.diadem.oxpath.dom.FieldTypes;


/**
 * 
 * Class for enabling actions in web pages 
 * @author AndrewJSel
 *
 */
public class ActionEngine {

	/**
	 * Returns a FieldType based on the HTML data of the node.
	 * 
	 * @param n
	 *            Node object to return FieldType
	 * @return value from FieldType enum relating HTML form field type of <tt>n</tt>
	 */
	public static FieldTypes getFieldType(DOMElement n) {
		FieldTypes ft = null;
		String nodeName = n.getNodeName().toLowerCase();
		if (nodeName == null)
			throw new NullPointerException("Input parameter cannot be null!");
		// these are all mutually exclusive conditions, ft should be set exactly once
		if (nodeName.equals("input")) {
			DOMNamedNodeMap<?> attributes = n.getAttributes();
			if (attributes==null) return FieldTypes.TEXT;//standard defaults to text if not @type is present}
			DOMNode typeRaw = attributes.getNamedItem("type");
			if (typeRaw == null)
				return FieldTypes.TEXT;//standard defaults to text if not @type is present
			String type = typeRaw.getNodeValue().toLowerCase();
			if (type.equals("text")) {
				return FieldTypes.TEXT;
			} else if (type.equals("password")) {
				return FieldTypes.PASSWORD;
			} else if (type.equals("checkbox")) {
				return FieldTypes.CHECKBOX;
			} else if (type.equals("radio")) {
				return FieldTypes.RADIOBUTTON;
			} else if (type.equals("button")) {
				return FieldTypes.INPUTBUTTON;
			} else if (type.equals("file")) {
				return FieldTypes.INPUTFILE;
			} else if (type.equals("image")) {
				return FieldTypes.INPUTIMAGE;
			} else if (type.equals("submit")) {
				return FieldTypes.INPUTSUBMIT;
			} else if (type.equals("reset")) {
				return FieldTypes.INPUTRESET;
			} else {//unknown value defaults to text
				return FieldTypes.TEXT;
			}
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
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  action string of action
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws OXPathException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public static DOMElement takeAction(DOMElement context, FieldTypes ft, String action) throws OXPathException, IOException {
		DOMElement newPage;
		switch (ft) {
		case SELECT:
			//safe because we checked for <select> in the switch above
			DOMHTMLSelect select = ((DOMElement) context).htmlUtil().asHTMLSelect();

			DOMHTMLOptionsCollection options = select.getOptions();

			for (int i = 0; i < options.getLength(); i++) {
				if (options.item(i).getText().equals(action)) select.setSelectedIndex(i);
			}
			return context.getOwnerDocument().getDocumentElement();
		case TEXT://otherwise, we try and type on the element
		case TEXTAREA:
		case PASSWORD:
		default:
			if (action.endsWith(ENTERSIGNAL)) {
				newPage = context.typeAndEnter(action.substring(0, action.lastIndexOf(ENTERSIGNAL))).getDocument().getDocumentElement();//use the typeAndEnter method on the action String after stripping the trailing '\n'
			} else {
				newPage = context.type(action).getDocument().getDocumentElement(); 
			}
			return newPage;
//		default:
//			throw new OXPathException("Cannot use explicit references for HTML elements other than <select> or <input> tags that support text entry");
		}

	}


	/**
	 * This method takes an action on a page, including any relevant javascript events listening for said action
	 * Handles selection on an HTML <tt>select</tt> element
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  position of action
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws OXPathException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public static DOMElement takeAction(DOMElement context, FieldTypes ft, int action) throws OXPathException, IOException {
		switch (ft) {// because we checked the node properties in getFieldType(), each of the initial casts is a safe operation
		case SELECT:
			DOMHTMLSelect select = ((DOMElement) context).htmlUtil().asHTMLSelect();
//			for (Integer i : action) {
				select.setSelectedIndex(action);
				//				page = (HtmlPage) select.setSelectedAttribute(select.getOption(i-1), true);
//			}
			return context.getOwnerDocument().getDocumentElement();
		default :
			throw new OXPathException("Cannot use position references for HTML elements other than <select>");
		}
	}

	/**
	 * This method takes an action on a page, including any relevant javascript events listening for said action.  This method omits the optional {@code key} parameter.
	 * 
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  keyword of action
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws OXPathException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public static DOMElement takeAction(DOMElement context, FieldTypes ft, ActionKeywords action) throws OXPathException, IOException {
		return ActionEngine.takeAction(context, ft, action, '\n');
	}

	/**
	 * This method takes an action on a page, including any relevant javascript events listening for said action
	 * 
	 * @param context
	 *            the context node (upon which to take the action)
	 * @param ft
	 *            type of the context node
	 * @param action 
	 * 			  keyword of action
	 * @param key
	 * 			  character to press for keyboard events; input ignored for other events
	 * @return the <tt>page</tt> input parameter after the action has occurred
	 * @throws OXPathException
	 *             if malformed action token
	 * @throws IOException
	 *             if IO error occurs
	 */
	public static DOMElement takeAction(DOMElement context, FieldTypes ft, ActionKeywords action, char key) throws OXPathException, IOException {
		DOMElement newPage;//so we don't mute reference supplied as parameter
		switch (action) {
		case CLICK :
			newPage = context.click().getDocument().getDocumentElement();
			break;
		case DBLCLICK :
			newPage = context.fireMouseEvent(DOMMouseEvent.dblclick).getDocument().getDocumentElement();
			break;
		case FOCUS :
			newPage = context.fireFocusEvent(DOMFocusEvent.focus).getDocument().getDocumentElement();
			break;
		case MOUSEDOWN :
			newPage = context.fireMouseEvent(DOMMouseEvent.mousedown).getDocument().getDocumentElement();
			break;
		case MOUSEENTER :
			newPage = context.fireMouseEvent(DOMMouseEvent.mouseenter).getDocument().getDocumentElement();
			break;
		case MOUSEMOVE :
			newPage = context.fireMouseEvent(DOMMouseEvent.mousemove).getDocument().getDocumentElement();
			break;
		case MOUSEOVER :
			newPage = context.mouseover().getDocument().getDocumentElement();
			break;
		case MOUSEOUT :
			newPage = context.fireMouseEvent(DOMMouseEvent.mouseout).getDocument().getDocumentElement();
			break;
		case MOUSEUP :
			newPage = context.fireMouseEvent(DOMMouseEvent.mouseup).getDocument().getDocumentElement();
			break;
		case RIGHTCLICK :
			throw new RuntimeException("Right click not implemented!");
			//				break;
		case CHECK :
			throw new RuntimeException("Check not implemented!");
			//				switch (ft) {
			//					case CHECKBOX :
			//						returnPage = (HtmlPage) ((HtmlCheckBoxInput) element).setChecked(true);
			//						break;
			//					case RADIOBUTTON :
			//						returnPage = (HtmlPage) ((HtmlRadioButtonInput) element).setChecked(true);
			//						break;
			//					default :
			//						returnPage = element.click();
			//						break;
			//				}				
			//				break;
		case UNCHECK :
			throw new RuntimeException("Uncheck not implemented!");
			//				switch (ft) {
			//				case CHECKBOX :
			//					returnPage = (HtmlPage) ((HtmlCheckBoxInput) element).setChecked(false);
			//					break;
			//				case RADIOBUTTON :
			//					returnPage = (HtmlPage) ((HtmlRadioButtonInput) element).setChecked(false);
			//					break;
			//				default :
			//					returnPage = element.click();
			//					break;
			//				}
			//				break;
		case KEYDOWN :
			newPage = context.fireKeyboardEvent(DOMKeyboardEvent.keydown,key).getDocument().getDocumentElement();
			break;
		case KEYPRESS :
			newPage = context.fireKeyboardEvent(DOMKeyboardEvent.keypress,key).getDocument().getDocumentElement();
			break;
		case KEYUP ://implemented as part of doType(String) method (protected and incorporated into simulation)
			newPage = context.fireKeyboardEvent(DOMKeyboardEvent.keyup,key).getDocument().getDocumentElement();
			break;
		case SUBMIT :
			newPage = context.fireKeyboardEvent(DOMKeyboardEvent.keypress,'\n').getDocument().getDocumentElement();
			break;
		case WHEEL :
			newPage = context.fireMouseEvent(DOMMouseEvent.mousewheel).getDocument().getDocumentElement();
			break;
		default ://do nothing
			return context.getOwnerDocument().getDocumentElement();
		}
		return newPage;
	}

	/**
	 * Constant encoding carriage return character
	 */
	public static final String ENTERSIGNAL = "\n";
}