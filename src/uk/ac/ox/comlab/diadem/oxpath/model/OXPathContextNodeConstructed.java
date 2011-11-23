/**
 * 
 */
package uk.ac.ox.comlab.diadem.oxpath.model;

import diadem.common.web.dom.DOMDocument;
import diadem.common.web.dom.DOMNamedNodeMap;
import diadem.common.web.dom.DOMNode;
import diadem.common.web.dom.DOMNodeList;
import diadem.common.web.dom.event.DOMEventListener;
import diadem.common.web.dom.xpath.DOMXPathEvaluator;

/**
 * 
 * Wrapper for DOM nodes that do not come from the browser.  Currently, this includes notional root and the style nodes, as they have no analogue in DOM.  Provides a minimal construction of a DOM Node so {@code compareTo}
 * works correctly for the sorted node list
 * @author AndrewJSel
 *
 */
public class OXPathContextNodeConstructed extends OXPathContextNode {

	/**
	 * Constructer for wrapped node.
	 * @param iKey display property name
	 * @param iValue display value of the node
	 * @param iParent extracted parent id
	 * @param iLast last-sibling extracted id
	 */
	public OXPathContextNodeConstructed(String iKey, String iValue, int iParent, int iLast) {
		super(null,iParent,iLast);
		this.key = iKey;
		this.value = iValue;
	}
	
	/**
	 * Evaluates XPath expression.  Since minimal wrapper, every expression returns the value
	 * of the node.
	 * @param path input XPath expression
	 * @return always the string value
	 */
	@Override
	public OXPathType getByXPath(String path) {
		return new OXPathType(this.value);
	}
	
	/**
	 * {@code compareTo} is implemented so as to establish natural ordering compatible with equals.
	 * @param other the other node to compare
	 * @return standard {@code compareTo} contract
	 */
	@Override
	public int compareTo(OXPathContextNode other) {
		return Integer.MIN_VALUE;//we want all Browser-based nodes to be greater than our constructed nodes in the list
	}
	
	/**
	 * {@code compareTo} is implemented so as to establish natural ordering compatible with equals.
	 * @param other the other node to compare
	 * @return standard {@code compareTo} contract
	 */
	public int compareTo(OXPathContextNodeConstructed other) {
		if (this.equals(other)) return 0;
		int keyDiff = this.key.compareTo(other.key);
		if (keyDiff!=0) return keyDiff;
		else {
			int valDiff = this.value.compareTo(other.key);
			if (valDiff!=0) return valDiff;
			else {//we don't need to check last-sibling as we assume normalized OXPath
				return other.getParent() - this.getParent();
			}
		}
	}
	
	/**
	 * Returns our implementation of the wrapped node.  At present, this is very minimal (only {@code toPrettyHtml} is implemented).
	 */
	@Override
	public DOMNode getNode() {
		final String nodeKey = this.key;
		final String nodeValue = this.value;
		return new DOMNode(){

			@Override
			public void addEventListener(String arg0, DOMEventListener arg1,
					boolean arg2) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public void removeEventListener(String arg0, DOMEventListener arg1,
					boolean arg2) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode appendChild(DOMNode arg0) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public short compareDocumentPosition(DOMNode arg0) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNamedNodeMap<?> getAttributes() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNodeList getChildNodes() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getFirstChild() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getLastChild() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public String getLocalName() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getNextSibling() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public String getNodeName() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public Type getNodeType() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public String getNodeValue() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMDocument getOwnerDocument() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getParentNode() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getPreviousSibling() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public String getTextContent() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMXPathEvaluator getXPathEvaluator() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode insertBefore(DOMNode arg0, DOMNode arg1) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public boolean isDescendant(DOMNode arg0) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public boolean isSameNode(DOMNode arg0) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public boolean isVisible() {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode removeChild(DOMNode arg0) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public DOMNode replaceChild(DOMNode arg0, DOMNode arg1) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
			}

			@Override
			public void setTextContent(String arg0) {
				throw new RuntimeException("Method for Constructed node wrapper not yet implmented!");
				
			}

			@Override
			public String toPrettyHTML() {
				return "<"+nodeKey+">"+nodeValue+"</"+nodeKey+">";
			}
			
		};
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OXPathContextNodeConstructed other = (OXPathContextNodeConstructed) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}



	/**
	 * display property name
	 */
	private final String key;
	/**
	 * display property value
	 */
	private final String value;
}
