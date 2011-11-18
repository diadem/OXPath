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
 */
package uk.ac.ox.comlab.diadem.oxpath.model;

import java.util.List;

import diadem.common.web.dom.DOMNode;

import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

import static uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes.*;

/**
 * Class for encoding OXPath return types, including nodesets, strings, numbers, and booleans
 * @author AndrewJSel
 *
 */
public class OXPathType {
	
	/**
	 * Null constructor
	 */
	public OXPathType() {
		this.type = NULL;
	}
	
	
	/**
	 * Returns a new (defensive) copy of the implicit parameter
	 * @param other {OXPathType to copy}
	 */
	public OXPathType(OXPathType other) {
		this();
		try {
			switch (other.isType()) {
				case NULL:
					return;
				case STRING:
					this.set(other.string());
					break;
				case NUMBER:
					this.set(other.number());
					break;
				case BOOLEAN:
					this.set(other.booleanValue());
					break;
				case NODESET:
					this.set(other.nodeList());
					break;
			}
		} catch (OXPathException e) {}//just to fool compiler; these exceptions won't happen because we check type beforehand
	}
	
	/**
	 * Constructor for nodelists
	 * @param in input nodelist
	 */
	public OXPathType(OXPathNodeList<OXPathContextNode> in) {
		this.set(in);
	}
	
	/**
	 * Constructor for strings
	 * @param in input String
	 */
	public OXPathType(String in) {
		this.set(in);
	}
	
	/**
	 * Constructor for number inputs
	 * @param in input int
	 */
	public OXPathType(double in) {
		this.set(in);
	}
	
	/**
	 * Constructor for booleans
	 * @param in input boolean
	 */
	public OXPathType(boolean in) {
		this.set(in);
	}

	/**
	 * Constructor for handling output from the getByXPath function from HtmlUnit
	 * @param byXPath input of List<?> from getByXPath
	 */
	public OXPathType(List<?> byXPath) {
		Object first = byXPath.get(0);
		if (first instanceof DOMNode) {
			this.nodes.addList(byXPath);
			this.type=NODESET;
		}
		else if (first instanceof String) {
			this.string = (String) first;
			this.type = STRING;
		}
		else if (first instanceof Double) {
			this.number = (Double) first;
			this.type = NUMBER;
		}
		else if (first instanceof Boolean) {
			this.bool = (Boolean) first;
			this.type = BOOLEAN;
		}
		else {
			this.type = NULL;
		}
	}

	/**
	  * Constructor for handling output from the getByXPath function from HtmlUnit
	 * @param byXPath input of List<?> from getByXPath
	 * @param parent reference to parent node of current context
	 * @param last reference to parent node of current context
	 */
	public OXPathType(List<?> byXPath, int parent, int last) {
		if (byXPath.isEmpty()) this.type = NULL;
		else {
			Object first = byXPath.get(0);
			if (first instanceof DOMNode) {
				this.nodes = new OXPathNodeList<OXPathContextNode>();
				for (Object n : byXPath) {
					this.nodes.add(new OXPathContextNode((DOMNode)n,parent,last));
				}
				this.type=NODESET;
			}
			else if (first instanceof String) {
				this.string = (String) first;
				this.type = STRING;
			}
			else if (first instanceof Double) {
				this.number = (Double) first;
				this.type = NUMBER;
			}
			else if (first instanceof Boolean) {
				this.bool = (Boolean) first;
				this.type = BOOLEAN;
			}
			else {
				this.type = NULL;
			}
		}
	}
	
	

	/**
	 * Expression for setting state
	 * @param j input NodeList
	 */
	public OXPathType(OXPathContextNode j) {
		this.nodes = new OXPathNodeList<OXPathContextNode>();
		this.nodes.add(j);
		this.type = NODESET;
	}

	/**
	 * Expression for setting state
	 * @param in input NodeList
	 */
	public void set(OXPathNodeList<OXPathContextNode> in) {
		if (this.nodes==null) this.nodes = new OXPathNodeList<OXPathContextNode>();
		this.nodes.addAll(in);
		this.type = NODESET;
	}
	
	/**
	 * Expression for setting state
	 * @param in input String
	 */
	public void set(String in) {
		this.string = in;
		this.type = STRING;
	}
	
	/**
	 * Expression for setting state
	 * @param in input int
	 */
	public void set(Double in) {
		this.number = in;
		this.type = NUMBER;
	}
	
	/**
	 * Expression for setting state
	 * @param in input boolean
	 */
	public void set(boolean in) {
		this.bool = in;
		this.type = BOOLEAN;
	}
	
	/**
	 * Expression for setting null state	
	 */
	public void set(Object in) {
		this.type = NULL;
	}
	
	/**
	 * Expression that returns type of Object
	 * @return type of implicit parameter
	 */
	public OXPathTypes isType() {
		return this.type;
	}
	
	/**
	 * Casts object as <tt>OXPathNodeList</tt>
	 * @return object as <tt>OXPathNodeList</tt>
	 * @throws OXPathException if the object is null
	 */
	public OXPathNodeList<OXPathContextNode> nodeList() throws OXPathException {
		if (this.type.equals(NODESET)) return this.nodes;
		else return new OXPathNodeList<OXPathContextNode>();
//		else throw new OXPathException("OXPathType exception - Can't cast " + this.type.toString() + " as " + NODESET.toString());
	}
	
	/**
	 * Casts object as <tt>String</tt>
	 * @return object as <tt>String</tt>
	 * @throws OXPathException if the object is null
	 */
	public String string() throws OXPathException {
		if (this.type.equals(STRING)) return this.string;
		else if (this.type.equals(NODESET)) {
			if (this.nodes.isEmpty()) return "";
			else return this.nodes.get(0).getByXPath("string(.)").string();
		}
		else if (this.type.equals(BOOLEAN)) return (this.bool) ? "true" : "false";
		else if (this.type.equals(NUMBER)) return String.valueOf(this.number);
		else throw new OXPathException("OXPathType exception - Can't cast " + this.type.toString() + " as " + STRING.toString());
	}
	
	/**
	 * Casts object as <tt>double</tt>
	 * @return object as <tt>double</tt>
	 * @throws OXPathException if the object is null
	 */
	public Double number() throws OXPathException {
		if (this.type.equals(NUMBER)) return this.number;
		else if (this.type.equals(STRING)) {
//			System.out.println(this.string);
			if (this.string().equals("false")) return 0.0;
			else if (this.string().equals("true")) return 1.0;
			else return Double.valueOf(this.string);
		}
		else if (this.type.equals(BOOLEAN)) return (this.bool) ? 1.0 : 0.0;
		else if (this.type.equals(NODESET)) return Double.valueOf(this.nodes.get(0).getByXPath("string(.)").string());
		else throw new OXPathException("OXPathType exception - Can't cast " + this.type.toString() + " as " + NUMBER.toString());
	}
	
	/**
	 * Casts object as <tt>boolean</tt>
	 * @return object as <tt>boolean</tt>
	 * @throws OXPathException if the object is null
	 */
	public boolean booleanValue() throws OXPathException {
		if (this.type.equals(BOOLEAN)) return this.bool;
		else if (this.type.equals(NODESET)) return (this.nodes.size()>0)?true:false;
		else if (this.type.equals(STRING)) return (this.string.length() > 0);
		else if (this.type.equals(NUMBER)) return !this.number.equals(0);
		else return false;
	}
	
	/**
	 * Not class-safe, but returns value based on instantiation of type in the object
	 * @return value of object
	 */
	public Object getValue() {
		switch (this.type) {
			case NODESET :
				return this.nodes;
			case STRING :
				return this.string;
			case NUMBER :
				return this.number;
			case BOOLEAN :
				return this.bool;
		}
		return null;
	}
	
	/**
	 * returns a {@code String} encoding of XPath primatives and concatenated pretty html versions of nodelists
	 * Useful for attribute extraction marker output.
	 * @return a {@code String} encoding of XPath primatives and concatenated pretty html versions of nodelists
	 * @throws OXPathException if the object is null
	 */
	public String toPrettyHtml() throws OXPathException{
		switch (this.type) {
		case STRING :
		case NUMBER :
		case BOOLEAN :
			return this.string();
		case NODESET :
			StringBuilder sb = new StringBuilder();
			for (OXPathContextNode c : this.nodes) {
				sb.append(c.getNode().toPrettyHTML());
			}
			return sb.toString();
		}
		return "";
	}
	
	@Override
	public String toString() {
		switch (this.isType()) {
		case NODESET :
			try {
				if (this.nodeList().isEmpty()) return "Empty Node List returned";
				else {
					StringBuilder sb = new StringBuilder();
					for (OXPathContextNode i : this.nodeList()) {
						sb.append(i.getNode().toString());
						sb.append("\n");
					}
					return sb.toString();
				}
			} catch (OXPathException e) {
				return "Error on Node reads";
			}
		case STRING :
		case NUMBER :
		case BOOLEAN :
			return this.getValue().toString();
		}	
		return "NULL Context";
	}
	
	/**
	 * enumerated types of different types in OXPath
	 * @author AndrewJSel
	 *
	 */
	public enum OXPathTypes {
		NODESET("node-set"), STRING("string"), NUMBER("number"), BOOLEAN("boolean"), NULL("null");
		OXPathTypes(String in) {this.name=in;}
		public String toString() {return this.name;}
		private String name;
	}
	
	/**
	 * Instance field for data type
	 */
	private OXPathTypes type;
	
	/**
	 * Instance field for storing field, if used
	 */
	private OXPathNodeList<OXPathContextNode> nodes;
	
	/**
	 * Instance field for storing field, if used
	 */
	private String string;
	
	/**
	 * Instance field for storing field, if used
	 */
	private Double number;
	
	/**
	 * Instance field for storing field, if used
	 */
	private boolean bool;
	
	/**
	 * premade object that returns empty output
	 */
	public static final OXPathType EMPTYRESULT = new OXPathType();

	
}
