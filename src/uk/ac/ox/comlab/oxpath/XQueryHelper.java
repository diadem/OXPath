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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Examples.impl.HTTPClientUtil;
import Examples.impl.PageFactory;
import Examples.impl.WebPageImpl;
import Examples.impl.XQueryScraper;
import Examples.intf.WebPage;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Class contains methods for helping to facilitate the XQuery process, specific to web scraping activities for OXPath
 * 
 * @author AndrewJSel
 * 
 */
public class XQueryHelper {

	/**
	 * Method for taking a regular XPath expression and inserting XHTML namespace prefixes for all nodes that do not already contain a prefix. This is a useful method for preparing paths to be used with the SAXON engine, as it requires explicit namespaces for all nodes. This will not break a legal XPath expression, but may have unintended results with poorly-formed XPath; verify expression first
	 * 
	 * @param in
	 *            regular XPath expression for an XHTML page
	 * @return a String matching <tt>in</tt> except that any nodes that didn't have a namespace prefix now have one (for xhtml)
	 * @throws BadDataException
	 *             if malformed XPath Expression
	 */
	public static String addXHTMLNamespacePrefixes(String in) throws BadDataException {
		// recursive method; not necessarily efficient as straight parsing, these paths navigate through XHTML pages, so this application will be fast enough
		String rv = "";// initialize return value
		int openParanIndex = in.indexOf('(');// these values determine if we are using a function in later conditional branches
		int closeParanIndex = in.lastIndexOf(')');
		// first consider if the (sub)expression is XPath (leading /); otherwise, some primitive type (String, int, etc.)
		if (in.length() > 0 && (in.charAt(0) == '/' || in.charAt(0) == '$')) {// if in isn't empty, check if it is a navigational path (by starting with a step or variable
			Scanner scanAxis = new Scanner(in).useDelimiter("/");
			// handle if first token is bound variable
			if (in.charAt(0) == '$') {// if it in starts at a variable, the scanner will have a token we will copy straight over
				rv += scanAxis.next();
			}
			boolean done = false; // boolean for following loop and a half (because, at the least, we must replace the / consumed)
			while (!done) {
				if (!scanAxis.hasNext())
					done = true;
				else {
					rv += "/";// replaces consumed / in scanner, since we checked first for a leading /, all tokens are preceded with this character
					Scanner scanAxisName = new Scanner(scanAxis.next()).useDelimiter("::");
					while (scanAxisName.hasNext()) {// proper XPath, will have one or two tokens
						String temp = scanAxisName.next();// need temp, because we need to actually see and consume the token (no "peek" in scanner class)
						if (scanAxisName.hasNext()) {// if more than one token, then this first one is an axis name to just copy over
							rv += (temp + "::");// carry axis and axis identifier over
						} else {// we are on the last token (there might have only been one
							if (temp.equals("") || temp.startsWith("@") || temp.contains(":")) {// if the string is empty, is an attribute node, or already has a namespace prefix, then no namespace prefix is required
								rv += temp;
							} else {// otherwise, attach a namespace prefix
								rv += (XHTMLNamespacePrefix + XQueryHelper.addXHTMLNamespacePrefixes(temp));// recursive call because functions can be contained within node predicates
							}
						}
					}
					scanAxisName.close();
				}
			}
			scanAxis.close();
		}
		// next check if the current path is a function; if so recurse over parameters
		else if ((openParanIndex != -1 && closeParanIndex == -1) || (openParanIndex == -1 && closeParanIndex != -1) || (openParanIndex >= closeParanIndex && openParanIndex != -1)) {
			throw new BadDataException("Improper function in XPath expression for scrapper!");
		} else if (openParanIndex != -1 && closeParanIndex != -1) {// outside of expression is function; inside a comma-separated parameters to be recursed over
			ArrayList<String> tokens = new ArrayList<String>();
			int numOpenParan = 0;// counts the number of open parentheses encountered
			int lastCount = openParanIndex;
			for (int count = openParanIndex + 1; count < closeParanIndex; count++) {
				if (numOpenParan == 0 && (in.charAt(count) == ',')) {
					tokens.add(in.substring(lastCount + 1, count));// want to not include the last comma (or first paran, first token) or the comma separating this token from the next
					lastCount = count;
				} else if (in.charAt(count) == '(') {
					numOpenParan++;
				} else if (in.charAt(count) == ')') {
					numOpenParan--;
				}
			}
			tokens.add(in.substring(lastCount + 1, closeParanIndex));// once the end is reached, add the last token; assures, independent of the loop, that a token is added, as before leading, final char omitted
			rv += in.substring(0, openParanIndex + 1);// include the open parentheses
			for (String token : tokens) {
				rv += (XQueryHelper.addXHTMLNamespacePrefixes(token) + ",");
			}
			rv = rv.substring(0, rv.length() - 1) + in.substring(closeParanIndex);// -1 as length-1 is last character; this way, we remove trailing comma
		} else
			rv += in;// in case (sub)expression is a non-Path primitive type, simply passes it to higher call
		return rv; // we've done 1 of the conditional execution paths above
	}

	/**
	 * Method for scraping from a web page where scraping is limited to only a single page
	 * 
	 * @param page
	 *            the webpage to scrap from
	 * @param scraperQuery
	 *            object that holds the scraper query
	 * @param attributes
	 *            attributes to bind in result entries of XML output (defined in OXPath navigational expression)
	 * @return String containing XML output
	 */
	public static String scrapWebPage(WebPage page, ScraperXQueryFactory scraperQuery, Map<String, String> attributes) {
		XQueryScraper scraper = new XQueryScraper(scraperQuery.getScraperQuery());
		Set<String> keys = attributes.keySet();
		for (String key : keys) {// bind all attributes
			scraper.bindParameter(key, attributes.get(key));
		}
		return XQueryHelper.scrapWebPage(page, scraper);
	}

	/**
	 * Method for scraping from a web page where scraping is done across multiple pages
	 * 
	 * @param page
	 *            page the webpage to scrap from
	 * @param scraperQuery
	 *            object that holds the scraper query
	 * @param nextLink
	 *            object that holds querys related to finding the next page from the current one
	 * @param attributes
	 *            attributes to bind in result entries of XML output (defined in OXPath navigational expression)
	 * @param domainName
	 *            name of domain under investigation (necessary so host is known for navigation)
	 * @return String containing XML output
	 * @throws Exception
	 *             raised in creation of web page
	 * @throws IOException
	 *             in case of missing HTML page
	 */
	public static String scrapWebPage(WebPage page, ScraperXQueryFactory scraperQuery, NextXQueryFactory nextLink, Map<String, String> attributes, String domainName) throws IOException, Exception {
		XQueryScraper scraper = new XQueryScraper(scraperQuery.getScraperQuery());
		Set<String> keys = attributes.keySet();
		for (String key : keys) {// bind all attributes
			scraper.bindParameter((key), attributes.get(key));
		}
		return XQueryHelper.scrapWebPage(page, scraper, new XQueryScraper(nextLink.getExistsNextPageQuery()), new XQueryScraper(nextLink.getNextPageQuery()), domainName);
	}

	/**
	 * **Internal API - DO NOT USE!!** Method for scraping from a web page where scraping is limited to only a single page
	 * 
	 * @param page
	 *            the webpage to scrap from
	 * @param xQueryScraper
	 *            Scraper object holding the scraper query
	 * @return String containing XML output
	 */
	private static String scrapWebPage(WebPage page, XQueryScraper scraper) {
		return scraper.process(page).toString().replace("&amp;", "&");// cleans output &,<--&amp; introduced by OutputProvider.toString();
	}

	/**
	 * **Internal API - DO NOT USE!!** Method for scraping from a web page where scraping is done across multiple pages
	 * 
	 * @param page
	 *            page the webpage to scrap from
	 * @param scraperQuery
	 *            scraperQuery object that holds the scraper query
	 * @param isNextQuery
	 *            object that holds the query to determine if the next page exists as specified
	 * @param nextQuery
	 *            object that holds the query that finds the next page path
	 * @param domainName
	 *            name of domain under investigation (necessary so host is known for navigation)
	 * @return String containing XML output
	 * @throws Exception
	 *             raised in creation of web page
	 * @throws IOException
	 *             in case of missing HTML page
	 */
	private static String scrapWebPage(WebPage page, XQueryScraper scraper, XQueryScraper isNextQuery, XQueryScraper nextQuery, String domainName) throws IOException, Exception {
		String output = ""; // begin with null output
		output += XQueryHelper.scrapWebPage(page, scraper);
		String isNextResult = isNextQuery.process(page).toString();
		if (isNextResult.toLowerCase().contains("true")) {// necessary because SAXON spits out results in XML
			String nextPageGet = nextQuery.process(page).toString();
			// the pruning below is necessary because SAXON doesn't output just answers (even queries whose answers are primitives); all are wrapped in XML
			nextPageGet = nextPageGet.substring(nextPageGet.indexOf(SAXONSTRINGRESULTOPEN) + SAXONSTRINGRESULTOPEN.length(), nextPageGet.indexOf(SAXONSTRINGRESULTCLOSE));
			nextPageGet = nextPageGet.replace("&amp;", "&");// cleans output &,<--&amp; introduced by OutputProvider.toString()
			// System.out.println(nextPageGet);
			page = PageFactory.createWebPage(HTTPClientUtil.doHttpGet(domainName, nextPageGet));
			output += XQueryHelper.scrapWebPage(page, scraper, isNextQuery, nextQuery, domainName);
		}
		return output;// return the output
	}

	/**
	 * Method that scraps web pages according to the next links (encoded as arrayList of the list of canonical XPath paths for set of next Links)
	 * 
	 * @param page
	 *            the current page in focus (should already be scraped; next pages will be navigated to with this method)
	 * @param scraperQuery
	 *            the object encoded the query for scraping
	 * @param attributes
	 *            the attributes to populate within each atomic result
	 * @param nextNodePaths
	 *            a object encoding the nodeset for each nested level of next link navigation
	 * @return the scraped info (as specified by the query) of the pages as ordered by the <tt>nextNodes</tt> object
	 * @throws IOException
	 *             in case of malformed bytes in page input (won't occur if API is used for generation)
	 * @throws BadDataException
	 */
	public static String navigateNextAndScrapWebPage(HtmlPage page, ScraperXQueryFactory scraperQuery, Map<String, String> attributes, NextNavigator nextNodePaths) throws IOException, BadDataException {

		// recursive function that returns the results of iteratively clicking each next link
		// System.out.println(nextNodePaths);
		// System.out.println(nextNodePaths.hasNext());
		String output = XQueryHelper.scrapWebPage(XQueryHelper.convertHtmlPageToWebPage(page), scraperQuery, attributes);
		if (!nextNodePaths.isEmpty()) {// only do further next navigation if links are found
			boolean nextIsNew = false;
			HtmlElement nextLink = page.getFirstByXPath(nextNodePaths.getCurrentPath());// NextNavigator object construction assures that each query path returns only a single object
			HtmlPage nextPage;
			if (!(nextLink == null)) {
				nextPage = nextLink.click();
				String nextOutput = XQueryHelper.scrapWebPage(XQueryHelper.convertHtmlPageToWebPage(nextPage), scraperQuery, attributes);
				if (!output.equals(nextOutput)) {
					nextIsNew = true;
				}
			} else
				nextPage = page;
			// System.out.println("nextIsNew=" + nextIsNew);
			if (nextIsNew == true) {
				nextNodePaths.next(true);
				return output + XQueryHelper.navigateNextAndScrapWebPage(nextPage, scraperQuery, attributes, nextNodePaths);
			} else if (nextNodePaths.hasNext()) {
				nextNodePaths.next(false);
				return XQueryHelper.navigateNextAndScrapWebPage(nextPage, scraperQuery, attributes, nextNodePaths);
			} else
				return output;
		} else
			return output;

		/*
		 * //recursive function that returns the results of iteratively clicking each next link System.out.println(nextNodePaths); System.out.println(nextNodePaths.hasNext()); String rv = ""; String output = XQueryHelper.scrapWebPage(XQueryHelper.convertHtmlPageToWebPage(page), scraperQuery, attributes); rv += output; if (!nextNodePaths.isEmpty()) { System.out.println("getCurrentPath=" + nextNodePaths.getCurrentPath()); HtmlElement nextLink =
		 * page.getFirstByXPath(nextNodePaths.getCurrentPath());//this should only resolve to one link anyway boolean nextIsNew = false; if (!(nextLink==null)) { HtmlPage nextPage = nextLink.click(); String nextOutput = XQueryHelper.scrapWebPage(XQueryHelper.convertHtmlPageToWebPage(nextPage), scraperQuery, attributes); if (!output.equals(nextOutput)) {//currentPath leads to a next page nextIsNew = true; nextNodePaths.next(true); rv += XQueryHelper.navigateNextAndScrapWebPage(nextPage,
		 * scraperQuery, attributes, nextNodePaths); }
		 * 
		 * System.out.println("nextIsNew=" + nextIsNew); } if ((nextIsNew==false)&&(nextNodePaths.hasNext())) { either next node wasn't on page or doesn't result in a new page when clicked; if so, then we only keep checking if there are further next node paths to iterate through; if both are true, advance nextNodePaths state and keep scraping
		 * 
		 * nextNodePaths.next(false); rv += XQueryHelper.navigateNextAndScrapWebPage(page, scraperQuery, attributes, nextNodePaths); } } return rv;
		 */
	}

	/**
	 * method to convert a HtmlPage object (used by HTMLUnit) to a WebPage object (for use with the SAXON XQuery engine)
	 * 
	 * @param htmlPage
	 *            input page produced in the HTMLUnit framework
	 * @return a WebPage object that can utilized with SAXON
	 * @throws IOException
	 *             in case of malformed bytes occur in the HTMLPage (should not occur for a well-formed page)
	 */
	public static WebPage convertHtmlPageToWebPage(HtmlPage htmlPage) throws IOException {
		String htmlPageAsString = htmlPage.asXml();
		// create new webpage (in SAXON implementation) for use with XQueryScraper, powered by the SAXON engine
		return WebPageImpl.createWebPage(new ByteArrayInputStream(htmlPageAsString.getBytes())); // possible IOException
	}

	/**
	 * constant for namespace prefix for xhtml nodes
	 */
	public final static String XHTMLNamespacePrefix = "xhtml:";

	/**
	 * opening tag for atomic result when SAXON query returns String
	 */
	public final static String SAXONSTRINGRESULTOPEN = "<result:atomic-value xsi:type=\"xs:string\">";

	/**
	 * closing tag for atomic result when SAXON query returns String
	 */
	public final static String SAXONSTRINGRESULTCLOSE = "</result:atomic-value>";

}
