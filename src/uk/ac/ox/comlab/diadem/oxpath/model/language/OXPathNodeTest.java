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

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathNodeList;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Enum encoding OXPath node tests
 * @author AndrewJSel
 *
 */
public enum OXPathNodeTest implements NodeTest {
	
	/**
	 * field nodetest
	 */
	FIELD("field()"),
	
	/**
	 * any-field nodetest
	 */
	ANYFIELD("any-field()");

	/**
	 * basic constructor
	 * @param iValue {@code String} representation of the nodetest
	 */
	private OXPathNodeTest(String iValue) {
		this.value = iValue;
	}
	
	/**
	 * evaluates the context node with the step using this axis and nodetest
	 * @param node context node
	 * @param axis axis to apply
	 * @return the evaluated step
	 * @throws OXPathException in case an inappropriate nodetest is input
	 */
	public OXPathType evaluate(OXPathContextNode node, Axis axis) throws OXPathException{
		if (!(axis.getType().equals(AxisType.FORWARD) || axis.getType().equals(AxisType.BACKWARD) ))
			throw new OXPathException("Incompatible axis with OXPath node test");
		OXPathNodeList<OXPathContextNode> result = new OXPathNodeList<OXPathContextNode>();
		for (NodeTestFields field : NodeTestFields.values()) {
			OXPathNodeList<OXPathContextNode> tempResult = node.getByXPath(axis.getValue()+field.getValue()).nodeList();
			for (OXPathContextNode tempNode : tempResult) {
				if ((tempNode.getNode().isVisible()) || this.equals(ANYFIELD)) result.add(tempNode);
			}
			result.addAll(node.getByXPath(axis.getValue()+field.getValue()).nodeList());
		}
		return new OXPathType(result);
	}
	
	/**
	 * Enum used only to determine fields considered by OXPath <tt>field()</tt> and <tt>any-field()</tt> nodetests
	 * @author AndrewJSel
	 *
	 */
	private enum NodeTestFields {
		INPUT("input"),
		SELECT("select"),
		TEXTAREA("textarea"),
		BUTTON("button");
		
		/**
		 * basic constructor
		 * @param iValue textual representation of nodetest
		 */
		private NodeTestFields(String iValue) {
			this.value = iValue;
		}
		
		/**
		 * returns textual representation of nodetest
		 * @return textual representation of nodetest
		 */
		public String getValue() {
			return this.value;
		}
		
		/**
		 * textual representation of the node test
		 */
		private final String value;
	}

	/**
	 * returns the value of the node test encoded as a {@code String} object
	 * @return the value of the node test encoded as a {@code String} object
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * returns the type associated with this node test
	 * @return the type associated with this node test
	 */
	@Override
	public NodeTestType getType() {
		return NodeTestType.OXPATH;
	}

	/**
	 * instance field storing {@code String} representation of the node test
	 */
	private final String value;
}