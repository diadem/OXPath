/**
 * 
 */
package uk.ac.ox.comlab.diadem.oxpath.utils;

import diadem.common.web.dom.DOMDocument;

/**
 * Interface for representing web windows (HTML pages).
 * Meant to be part of the DIADEM API in the {@code diadem.commons.web} interface within
 * {@code WebBrowser} objects.
 * @author AndrewJSel
 */
public interface DOMWindow {

	/**
	 * Returns the top web page contained inside this web window
	 * @return the top web page contained inside this web window
	 */
	public DOMDocument getPage();
	
}
