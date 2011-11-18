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
 * This subpackage includes classes and interface relating to the OXPath language. 
 */
package uk.ac.ox.comlab.diadem.oxpath.model.language;

//import diadem.common.web.dom.DOMCSSStyleDeclaration;
import diadem.common.web.dom.DOMElement;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Enum type encoding OXPath axes in OXPath
 * @author AndrewJSel
 *
 */
public enum OXPathAxis implements Axis {

	/**
	 * style axis
	 */
	STYLE("style") {
		
		/**
		 * evaluates the context node with the step using this axis and nodetest
		 * @param node context node
		 * @param nodetest nodetest to apply
		 * @return the evaluated step
		 * @throws OXPathException in case an inappropriate nodetest is input
		 */
		@Override
		public OXPathType evaluate(OXPathContextNode node, NodeTest nodetest) throws OXPathException {
			if (!nodetest.getType().equals(NodeTestType.NAMETEST)) throw new OXPathException("Illegal node test with style axis");
			try {//necessary because computed style can return a null value
				if (((XPathNameTest)nodetest).isSuffixWildcard()) {
//					DOMCSSStyleDeclaration styleElements = ((DOMElement) node.getNode()).getComputedStyle();
					
//					return new OXPathType();
					throw new OXPathException("style::* not yet supported.");
				}
				return new OXPathType(((DOMElement) node.getNode()).getComputedStyle().getPropertyValue(nodetest.getValue()));
			}
			catch (Exception e) {
				return new OXPathType();
			}
		}
	};

	/**
	 * basic constructor
	 * @param iValue name of axis
	 */
	private OXPathAxis(String iValue) {
		this.value = iValue + AXIS_DELIMITER;
	}

	/**
	 * returns value of axis encoded as {@code String}
	 * @return value of axis encoded as {@code String}
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * returns the {@code AxisType} of the object
	 * @return the {@code AxisType} of the object
	 */
	@Override
	public AxisType getType() {
		return AxisType.OXPATH;
	}
	
	/**
	 * evaluates the context node with the step using this axis and nodetest
	 * @param node context node
	 * @param nodetest nodetest to apply
	 * @return the evaluated step
	 * @throws OXPathException in case an inappropriate nodetest is input
	 */
	public abstract OXPathType evaluate(OXPathContextNode node, NodeTest nodetest) throws OXPathException;

	/**
	 * instance field storing {@code String} value of axis
	 */
	private final String value;

	/**
	 * axis delimiter
	 */
	public static final String AXIS_DELIMITER = "::";
}