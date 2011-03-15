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

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter;
import uk.ac.ox.comlab.oxpath.scriptParser.SimpleNode;

/**
 * abstract class that contains common state and method info for the GroundedOXPathExpression and UnGroundedOXPathExpression classes
 * @author AndrewJSel
 */
public abstract class OXPathExpression {

	/**
	 * constructor (used only by child classes, as OXPathExpression is an abstract class)
	 */
	public OXPathExpression() {
		//initialization of scrapedInfo Map - ALL CHILD CLASSES MUST USE THIS CONSTRUCTOR for object initialization
//		scrapedInfo = new HashMap<String,String>();
	}
	
	/**
	 * method for setting if database is used
	 * @param aDatabaseUsed whether or not a database is used
	 */
	public void setDatabaseUsed(boolean aDatabaseUsed) {
		databaseUsed = aDatabaseUsed;
	}
	
	/**
	 * method for retrieving if database is used
	 * @return if database is used
	 */
	public boolean getDatabaseUsed() {
		return this.databaseUsed;
	}
	
	/**
	 * method for setting database id info
	 * @param aDatabaseName name of the database containing ontological info
	 */
	public void setDatabaseName(String aDatabaseName) {
		databaseName = aDatabaseName;
	}
	
	/**
	 * method for retrieving database id info
	 * @return database name
	 */
	public String getDatabaseName() {
		return this.databaseName;
	}
	
	/**
	 * method for setting if a global URL is used
	 * @param aUrlUsed boolean if the URL is used
	 */
	public void setURLUsed(boolean aUrlUsed) {
		this.urlUsed = aUrlUsed;
	}
	
	/**
	 * method for getting the value if the URL is used
	 * @return boolean <it>true</it> if a global URL is used, <it>false</it> otherwise
	 */
	public boolean getURLUsed() {
		return this.urlUsed;
	}
	
	/**
	 * method for setting URL
	 * @param aURLName name of URL for web form
	 */
	public void setURLName(String aURLName) {
		this.urlName = aURLName;
	}
	
	/**
	 * method for retrieving URL name
	 * @return name of URL for web form
	 */
	public String getURLName() {
		return this.urlName;
	}
	
	/**
	 * method for setting if next link is used in XQuery part of OXPathExpression
	 * @param aNextUsed whether or not next link is used
	 */
	public void setNextUsed(boolean aNextUsed) {
		nextUsed = aNextUsed;
	}
	
	/**
	 * method for retrieving if next link is used in XQuery part of OXPathExpression
	 * @return if next link is used in XQuery
	 */
	public boolean getNextUsed() {
		return this.nextUsed;
	}
	
	/**
	 * method for setting next links, if used; clears all current contents from the list
	 * @param aNextLink name (key) for next link
	 */
	public void addNextLinkNames(ArrayList<String> aNextLink) {
		nextNames.clear();
		nextNames.addAll(aNextLink);
	}
	
	/**
	 * **INTERNAL API - subject to change** method for getting next link, if used
	 * @return name of the next link as the key in the XQuery terms
	 */
	protected ArrayList<String> getNextLinkNames() {
		return this.nextNames;//protect method as it returns the mutable state object to the user
	}
	
	/**
	 * method for adding scraped info field in XML output
	 * @param key field name
	 * @param valuePath AST of standard XPath corresponding to key
	 */
	public void addScrapedInfo(String key, SimpleNode valuePath) {
		scrapedInfo.put(key, valuePath);
	}
	
	/**
	 * Used to add
	 * @param parent
	 * @throws BadDataException
	 */
	public void addScrapedInfo(SimpleNode parent) throws BadDataException {
		if (!parent.toString().equals("ScrapeDeclaration")) throw new BadDataException("addScrapedInfo called with a non-ScrapeDeclaration node!");
		String scraperName = OXPathScripter.getValue(OXPathScripter.getChildByName(parent, "ScraperName"));
		SimpleNode scraperPath = OXPathScripter.safeGetChild(parent, 1);
		scrapedInfo.put(scraperName, scraperPath);
	}
	
	/**
	 * method for removing scraped info field in XML output
	 * @param key key of map tuple to be removed
	 */
	public void removeScrapedInfo(String key) {
		scrapedInfo.remove(key);
	}
	
	/**
	 * returns the scraped info path corresponding to the input key
	 * @param key key of interest
	 * @return AST of path corresponding to key
	 */
	public SimpleNode getScrapedInfoPath(String key) {
		return this.scrapedInfo.get(key);
	}
	
	/**
	 * returns if the input key has already been mapped into the Scraped Information mapping
	 * @param key key of interest
	 * @return whether or not input key is present in the Scraped Information mapping
	 */
	public boolean hasScrapedInfoPath(String key) {
		return this.scrapedInfo.containsKey(key);
	}
	
	/**
	 * gets the set of keys for the Scraped Information mapping
	 * @return set of keys from the Scraped Information mapping
	 */
	public Set<String> getScrapedInfoKeys() {
		return scrapedInfo.keySet();
	}
	
	/**
	 * used for converting an OXPathExpression object to a String object with state info for testing purposes
	 * @param pathInfo details of path details so children can insert path info into this class
	 * @return String object with state info
	 */
	public String toString(String pathInfo) {
		StringBuilder out = new StringBuilder();
		//returns class name with all state info
		out.append(this.getClass().getName()+"\n");
		if (databaseUsed)
			out.append(" [database ==> " + databaseName + "]\n");
		else
			out.append(" [database not used]\n");
		if (urlUsed)
			out.append(" [url ==> " + urlName + "]\n");
		else
			out.append(" [global url not used]\n");
		//legacy code back before path types diverged between types of OXPath expressions
//		for (String pathName : pathNames) {
//			out += " [path ==> " + pathName + "]\n";
//		}
		out.append(pathInfo);
		if (nextUsed)
			out.append(" [nextField ==> " + nextNames + "]\n");
		else
			out.append(" [output retrieved over single page only]\n");
		//iterate through each of the scraped terms
		out.append(" XQ:\n");
		Set<String> keySet = scrapedInfo.keySet();
		for (String key : keySet)
		{
			SimpleNode value = scrapedInfo.get(key);
			out.append(" [" + key + " ==> " + OXPathScripter.stringDump(value, "") + "]\n");
		}
		return out.toString();
	}
	
	/**
	 * overridden method used for converting an OXPathExpression object to a String object with state info for testing purposes
	 * @return String object with state info
	 */
	public String toString() {
		return this.toString("");
	}
	
	/**
	 * instance field for storing if a database is used
	 */
	private boolean databaseUsed = false;
	/**
	 * instance field for storing database identification info
	 */
	private String databaseName;
	/**
	 * instance field for a storing if a global URL is used
	 */
	private boolean urlUsed = false;
	/**
	 * instance field for URL name
	 */
	private String urlName;
	/**
	 * instance field for determining if next link is used in OXPathExpression
	 * @deprecated
	 */
	private boolean nextUsed = false;
	/**
	 * instance field for storing key for next link, if used
	 * @deprecated
	 */
	private ArrayList<String> nextNames = new ArrayList<String>();
	/**
	 * instance field for storing XQuery information
	 */
	private Map<String, SimpleNode> scrapedInfo = new HashMap<String,SimpleNode>();
	
	//constant to determine database, url, path, and when file begins scraped field data
	/**
	 * Constant for XQuery signal
	 */
	public static final String XQUERYSIGNAL = "XQ:";
	/**
	 * Constant for Database Signal
	 */
	public static final String DATABASESIGNAL = "database";
	/**
	 * Constant for URL Signal
	 */
	public static final String URLSIGNAL = "url";
	/**
	 * Constant for Path Signal
	 */
	public static final String PATHSIGNAL = "path";
	/**
	 * Constant for Next Signal
	 */
	public static final String NEXTSIGNAL = "while";
	
	
	
}
