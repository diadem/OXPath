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
/**Package used for OXPath, an automated framework for retrieving Deep Web Content using a commodity computing environment
 * 
 */
package uk.ac.ox.comlab.oxpath;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.PropertyConfigurator;

import Examples.intf.OutputProvider;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Class used for testing framework
 * 
 * @author AndrewJSel
 * 
 */
public class OXPathTester {

	/**
	 * main procedure to be executed on program start
	 * 
	 * @param args
	 *            command line parameters of [0] OXPath specification file and [1] output XML file
	 * @throws Exception
	 *             in case of no web page found
	 */
	public static void main(String[] args) throws Exception {
		// Test to assure all works as it should
		try {

			// System.out.println(Pattern.matches("\\w+::(\\w+|\\*)",
			// "(aaa::*)[21]"));
			// AdditionalAxisNavigator aan = new
			// AdditionalAxisNavigator("next-field::*");
			// System.out.println(aan.getAxisType());
			// System.out.println(aan.getNodeType());
			// System.out.println(aan.getOffset());
			// System.out.println("");
			// AdditionalAxisNavigator aan = new
			// AdditionalAxisNavigator("(aaa::*)[21]");
			// Configure log4j properties to output warnings to console
//			URL url = new URL("http://www.google.co.uk.");
//			url.openConnection();
//			System.out.println(url);
			PropertyConfigurator.configure("log4j.properties");
			UnGroundedOXPathExpression test = new UnGroundedOXPathExpression("test6.oxp");
			// System.out.println(test);
			ArrayList<String> gTest = new ArrayList<String>();
			gTest = test.GroundedXPathBuilder();
			// System.out.println(gTest);
			ArrayList<GroundedOXPathExpression> oxps = new ArrayList<GroundedOXPathExpression>();
			for (String iPath : gTest) {
				GroundedOXPathExpression goxp = new GroundedOXPathExpression(test, iPath);
				oxps.add(goxp);
				// System.out.println(goxp.toString());
			}
			// object for output
			// PrintWriter out = new PrintWriter("TestResultOutput.xml");
			// iterates through each of the grounded XPressions
			for (GroundedOXPathExpression oxp : oxps) {
				HtmlPage page = oxp.getResultFromWebQuery();
				OutputProvider outer = oxp.scrapeByXQuery(page);
				System.out.println(outer.toString());
			}
			// out.close();
			System.out.println("Finished!");
			// System.out.print(page.getInputdataAsString());
			// QueryToken qt = new
			// QueryToken("//form[@id=\"aspnetForm\"]/next-field::*/{\"Buying\",\"Renting\"}/next-field::*/{\"GBP\"}/next-field::*/{\"Any\"}/next-field::*/{\"Any\"}/next-field::*/{\"Oxford\"}/next-field::*/{\"submit\"}");
			// while (qt.getFirstToken()!="") {
			// System.out.println(qt.getFirstToken());
			// System.out.println(qt.getRestTokens());
			// System.out.println(qt.getFirstTokenType());
			// System.out.println("");
			// qt = new QueryToken(qt.getRestTokens());
			// }
			// GroundedXPression test1 = new GroundedXPression(test,
			// test.getPathName());
			// System.out.println(test.toString());
			// DatabaseHelper dbh = new DatabaseHelper();
			// Connection con = dbh.getNewConnection(test.getDatabaseName());
			// Statement stat = con.createStatement();
			// ResultSet result = stat.executeQuery("SELECT * FROM R1");
			// result.next();
			// System.out.println(result.getString("MaxPrice"));
		}
		// finally {}
		catch (FileNotFoundException exception) {
			System.out.println("Input file not found!");
		} catch (BadDataException exception) {
			System.out.println(exception.getMessage());
		} catch (IOException exception) {
			System.out.println("IO exception!");
			System.out.println(exception.getStackTrace());
		} catch (ClassNotFoundException exception) {
			System.out.println("Class Not Found Exception!");
		} catch (SQLException exception) {
			System.out.println("SQL exception!");
		} catch (XPathExpressionException exception) {
			System.out.println("XPath exception - check query form page!");
			exception.printStackTrace();
		}
	}

}
