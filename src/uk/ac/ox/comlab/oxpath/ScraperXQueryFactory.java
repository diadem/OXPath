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

import java.util.Map;
import java.util.Set;

/**
 * Class that builds objects to produce scrapper XQuery expressions
 * @author AndrewJSel
 *
 */
public class ScraperXQueryFactory extends XQueryFactory {

	/**
	 * constructor for ScraperXQueryFactory Objects
	 * @param attributeNames set of web form query terms specified in OXPath navigation path to be part of XML output  
	 * @param scrapedInfo mapping of data names (keys) and paths (values) to be captured in XML output
	 * @throws BadDataException thrown if supplied scrapped 
	 */
	public ScraperXQueryFactory(Set<String> attributeNames, Map<String,String> scrapedInfo) throws BadDataException {
		//produces standard FLWOR XQuery based on input parameters
		super();
		//add boilerplate
		this.scraperXQuery += this.getBoilerPlate();
		//add variables to later be bound (web form query terms specified in action segments of OXPath navigation script)
		for (String key : attributeNames) {
			this.scraperXQuery += ("declare variable $" + key + " as xs:string external;\n");//declare field formatting 
		}
		//add results formatting, XQ scraping "trunk" path
		if (scrapedInfo.containsKey(COMMONANCESTOR)) {
			this.scraperXQuery += ("<results> {for $" + COMMONANCESTOR +" in " + XQueryHelper.addXHTMLNamespacePrefixes(scrapedInfo.get(COMMONANCESTOR) + "\n "));
		}
		else {
			throw new BadDataException("No common ancestor defined in scraper sequence!");
		}		
		//assign variables for each path, adding the XHTML namespace so that they are accepted by the SAXON query engine
		Set<String> scrappedKeys = scrapedInfo.keySet();
		scrappedKeys.remove(COMMONANCESTOR);//take out the common ancestor
		for (String key : scrappedKeys) {
			this.scraperXQuery += ("let $" + key + " := " + XQueryHelper.addXHTMLNamespacePrefixes(scrapedInfo.get(key)) + " ");//create line for each of the variable declarations
		}
		//populate attributes
		this.scraperXQuery += "return <result ";//whitespace ok
		for (String name : attributeNames) {
			this.scraperXQuery += (name + "=\"{$" + name + "}\" ");
		}
		this.scraperXQuery += ">";//close <result> tag
		//assign result formatting
		for (String key : scrappedKeys) {
			this.scraperXQuery += "<" + key + ">{$" + key + "}</" + key + ">";
		}
		this.scraperXQuery += "</result> } </results>";
//		this.scraperXQuery = "declare namespace xhtml=\"http://www.w3.org/1999/xhtml\"; \n"+
//		"count(/xhtml:html/xhtml:body/xhtml:form/xhtml:div[2]/xhtml:div[4]/xhtml:div[3]/xhtml:div[1]/xhtml:div/xhtml:div[4]/xhtml:div/xhtml:table/xhtml:tbody/xhtml:tr)";
//		System.out.println(this.scraperXQuery);
	}
	
	/**
	 * Getter method that returns the scraper query held by this object
	 * @return ScraperQuery for the object in the form of a String
	 */
	public String getScraperQuery() {
		return this.scraperXQuery;
	}
	
	/**
	 * private instance field storing XQuery that scrapes single page for info based on OXPath expression, initialized as empty
	 */
	private String scraperXQuery = "";
	
	/**
	 * Constant representing the signal for the bound variable yielding the closest common ancestor nodeset for with each result will be created from
	 */
	public static final String COMMONANCESTOR = "CA";
}
