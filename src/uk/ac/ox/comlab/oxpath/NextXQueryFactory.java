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
package uk.ac.ox.comlab.oxpath;

/**
 * Class that builds objects for Next Link type XQuery expressions
 * @author AndrewJSel
 *
 */
public class NextXQueryFactory extends XQueryFactory {
	
	/**
	 * Constructor that builds NextXQueryFactory objects
	 * @param nextLinkName value of text for next link or "image" keyword if non-textual
	 * @param nextLinkPath navigation-based XPath expression for next link
	 * @throws BadDataException in case of malformed XPath expression
	 */
	public NextXQueryFactory(String nextLinkName, String nextLinkPath) throws BadDataException {
		super();
		String formattedNextLinkPath = XQueryHelper.addXHTMLNamespacePrefixes(nextLinkPath);
		//create existsNextPage and nextPage queries
		this.existsNextPage = this.getBoilerPlate();
		this.nextPage = this.getBoilerPlate();
		if (nextLinkName.equals(NONTEXTLINKKEYWORD)) {
			//TODO: Fix this when better method is discovered
			this.existsNextPage += ("string(" + formattedNextLinkPath + ")");
		}
		else {//XQuery that tests if the textual value of the node resulting from the given XPath expression contains the key value 
			this.existsNextPage += ("contains(string(" + formattedNextLinkPath + "),\"" + nextLinkName + "\")");
		}
		//creates XQuery that will return string of node at path (with normalized whitespace per XPath function)
		this.nextPage += ("normalize-space(string(" + formattedNextLinkPath + "/@href))");
	}
	
	/**
	 * Method for getting the query that determines if the next page exists
	 */
	public String getExistsNextPageQuery() {
		return this.existsNextPage;
	}
	
	/**
	 * Method for getting the query that finds the link that retrieves the next page
	 */
	public String getNextPageQuery() {
		return this.nextPage;
	}

	/**
	 * private instance field for boolean XQuery determining if next field exists
	 */
	private String existsNextPage;
	
	/**
	 * private instance field for XQuery returning next page 
	 */
	private String nextPage;
	
	/**
	 * String representing keyword for non-text-based next link
	 */
	public final static String NONTEXTLINKKEYWORD = "image";
}
