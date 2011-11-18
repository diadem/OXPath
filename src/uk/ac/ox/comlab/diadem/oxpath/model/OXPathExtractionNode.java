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

import java.io.Serializable;

/**
 * Class for representing output nodes
 * @author AndrewJSel
 *
 */
public class OXPathExtractionNode implements Serializable{
	
	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 3883543828883428496L;
	
	
	/**
	 * Constructor for nested record nodes
	 * @param iId id of node
	 * @param iParent parent of node
	 * @param iLabel label of node
	 */
	public OXPathExtractionNode(int iId, int iParent, String iLabel) {
		this.id = iId;
		this.parent = iParent;
		this.label = iLabel;
		this.value = "";
	}
	
	/**
	 * Constructor for attribute nodes
	 * @param iId id of node
	 * @param iParent parent of node
	 * @param iLabel label of node
	 * @param iValue value of node
	 */
	public OXPathExtractionNode(int iId, int iParent, String iLabel, String iValue) {
		this(iId,iParent,iLabel);
		value = iValue;
	}
	
	/**
	 * Returns the end node object, signaling to the {@code OXPathOutputHandler} that all output has been sent and the communications channel can be closed
	 * @return the end node object, signaling to the {@code OXPathOutputHandler} that all output has been sent and the communications channel can be closed
	 */
	public static OXPathExtractionNode returnEndNode() {
		return OXPathExtractionNode.endNode;
	}
	
	/**
	 * Determines if the implicit parameter is the end node
	 * @return {@code true} if the implicit parameter is the end node; {@code false} otherwise
	 */
	public boolean isEndNode() {
		return this.equals(OXPathExtractionNode.endNode);
	}
	
	/**
	 * Returns id
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns parent
	 * @return parent
	 */
	public int getParent() {
		return this.parent;
	}
	
	/**
	 * Returns label
	 * @return label
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * returns value
	 * @return value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * returns <tt>String</tt> representation of object
	 * @return <tt>String</tt> representation of object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass());
		sb.append("[");
		sb.append(this.getId());
		sb.append(",");
		sb.append(this.getParent());
		sb.append(",");
		sb.append(this.getLabel());
		if (!this.getValue().equals("")) {
			sb.append(",");
			sb.append(this.value);
		}
		sb.append("]");
		return (sb.toString());
	}
	
	/**
	 * instance field for storing id
	 */
	private int id;
	/**
	 * instance field for storing parent
	 */
	private int parent;
	/**
	 * instance field for storing label
	 */
	private String label;
	/**
	 * instance field for storing value
	 */
	private String value;
	
	/**
	 * static field encoding state information for end node, the node that lets the consumer process know that all input is received and the connection can be closed
	 */
	private static final int endNodeID = -1;
	
	/**
	 * static field encoding state information for end node, the node that lets the consumer process know that all input is received and the connection can be closed
	 */
	private static final int endNodeParent = -2;
	
	/**
	 * static field encoding state information for end node, the node that lets the consumer process know that all input is received and the connection can be closed
	 */
	private static final String endNodeLabel= "endNodeLabel12345";
	
	/**
	 * static field encoding state information for end node, the node that lets the consumer process know that all input is received and the connection can be closed
	 */
	private static final String endNodeValue = "endNodeValue67890";
	/**
	 * storage for the end node, signaling to the consumer that all input has been received
	 */
	private static final OXPathExtractionNode endNode = new OXPathExtractionNode(endNodeID,endNodeParent, endNodeLabel,endNodeValue);


	/**
	 * Generated {@code hashCode()} override for object hashcode
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + parent;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * Generated {@code equals(Object)} override for object equality
	 * @param obj object to compare to the implicit parameter
	 * @return {@code true} if the input parameter is an equal extraction node to the implicit parameter (same id, parent, name, value)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OXPathExtractionNode other = (OXPathExtractionNode) obj;
		if (id != other.id)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (parent != other.parent)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}