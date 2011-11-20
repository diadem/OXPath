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
 * Wrapper for the style nodes, as they have no analogue in DOM
 * @author AndrewJSel
 *
 */
public class OXPathContextNodeStyle extends OXPathContextNode {

	/**
	 * Constructer for wrapped node.
	 * @param iKey display property name
	 * @param iValue display value of the node
	 * @param iParent extracted 
	 * @param iLast
	 */
	public OXPathContextNodeStyle(String iKey, String iValue, int iParent, int iLast) {
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
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public void removeEventListener(String arg0, DOMEventListener arg1,
					boolean arg2) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode appendChild(DOMNode arg0) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public short compareDocumentPosition(DOMNode arg0) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNamedNodeMap<?> getAttributes() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNodeList getChildNodes() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getFirstChild() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getLastChild() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public String getLocalName() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getNextSibling() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public String getNodeName() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public Type getNodeType() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public String getNodeValue() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMDocument getOwnerDocument() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getParentNode() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode getPreviousSibling() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public String getTextContent() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMXPathEvaluator getXPathEvaluator() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode insertBefore(DOMNode arg0, DOMNode arg1) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public boolean isDescendant(DOMNode arg0) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public boolean isSameNode(DOMNode arg0) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public boolean isVisible() {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode removeChild(DOMNode arg0) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public DOMNode replaceChild(DOMNode arg0, DOMNode arg1) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
			}

			@Override
			public void setTextContent(String arg0) {
				throw new RuntimeException("Method for Style node wrapper not yet implmented!");
				
			}

			@Override
			public String toPrettyHTML() {
				return "<"+nodeKey+">"+nodeValue+"</"+nodeKey+">";
			}
			
		};
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
