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
package uk.ac.ox.comlab.oxpath.externalPredicates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Interface for encapsulating the external predicates used to query data sources for form input outside an actual OXPath external predicate
 * @author AndrewJSel
 */
public interface ExternalPredicates {

	/**
	 * Method for running a query on an external database; must input a configuration file containing database driver, url, username, and password.
	 * The SQL query is a String object but will be transformed into a Prepared Statement as part of the method.  Returns a SQL ResultSet with query
	 * answered by database.
	 * @param configFileName configuration file containing database driver, url, username, and password
	 * @param sqlQuery SQL query is a String object but will be transformed into a Prepared Statement as part of the method
	 * @return SQL ResultSet with query answered by database
	 */
	public ResultSet fromDB(String configFileName, String sqlQuery);
	
	/**
	 * Method for running a query on an external database; must input a parameters for database driver, url, username, and password.
	 * The SQL query is a String object but will be transformed into a Prepared Statement as part of the method.  Returns a SQL ResultSet with query
	 * answered by database.
	 * @param driver database driver
	 * @param url database url
	 * @param user database username
	 * @param password password for database user
	 * @param sqlQuery SQL query is a String object but will be transformed into a Prepared Statement as part of the method
	 * @return SQL ResultSet with query answered by database
	 */
	public ResultSet fromDB(String driver, String url, String user, String password, String sqlQuery);
	
	/**
	 *  Method for running a query on an external database; used when the database was previously declared, such as a database declaration in an OXPath script
	 * @param conn active SQL database connection
	 * @param sqlQuery SQL query is a String object but will be transformed into a Prepared Statement as part of the method
	 * @return SQL ResultSet with query answered by database
	 */
	public ResultSet fromDB(Connection conn, String sqlQuery);
	
	/**
	 * Method for retrieving input from a flat file, formatted as one input per line
	 * @param inputFileName input file name for a flat file, formatted as one input per line
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromFile(String inputFileName);
	
	/**
	 * Method for retrieving form field input from an XML document.
	 * @param docURL url of input document
	 * @param query XPath query that results in form field inputs
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromXML(String docURL, String query);
	
	/**
	 * Method for retrieving form field input from an XML document.  Use this method when an XML document is previously declared.
	 * @param query XPath query that results in form field inputs
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromXML(String query);
	
	/**
	 * Method for retrieving form field input from an RDF file.
	 * @param docURL url of input file
	 * @param query RDF query yielding desired data
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromRDF(String docURL, String query);
	
	/**
	 * Method for retrieving form field input from an RDF file.  Use this method when an RDF file is previously declared.
	 * @param query RDF query yielding desired data
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromRDF(String query);
	
	/**
	 * Method for retrieving form field input from an OXPath script.  Executes the script and run input query on resulting XML document.
	 * @param docURL url (file name if local) of input OXPath script
	 * @param query XPath query that results in form field inputs
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromOXPath(String docURL, String query);
	
	/**
	 * Method for retrieving form field input from an OXPath script.  Executes the script and run input query on resulting XML document.  Use this method when an
	 * OXPath script is previously declared; this will assure the script is executed only once.
	 * @param query XPath query that results in form field inputs
	 * @return ArrayList of String objects with form field inputs
	 */
	public ArrayList<String> fromOXPath(String query);
	
}
