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
 * Package containing core OXPath functionality
 */
package uk.ac.ox.comlab.diadem.oxpath.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import diadem.common.web.WebBrowser;
import diadem.common.web.dom.impl.BrowserFactory;
import diadem.common.web.dom.impl.BrowserFactory.Engine;

import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATState;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathNodeList;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.output.OXPathOutputHandler;
import uk.ac.ox.comlab.diadem.oxpath.output.OXPathSimpleOutputHandler;
import uk.ac.ox.comlab.diadem.oxpath.output.OXPathXMLOutputHandler;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.Node;
import uk.ac.ox.comlab.diadem.oxpath.parser.OXPathParser;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Main API class for OXPath.  OXPathNavigator objects are instantiated with an output buffer defined.
 * These objects are then used to evaluate OXPath expressions.
 * @author AndrewJSel
 *
 */
public class OXPathNavigator {

	/**
	 * Main method; entry point to API when a .jar file
	 * @param args 1st argument defines <b>mode</b>: (1) <i>--cloud</i> for cloud mode; (2) <i>--xml</i> for xml output; or, (3) <i>--simple</i> for printing out tuples.
	 * 2nd argument specifies a file containing formatted OXPath expressions.  Without mode specified, output defaults to local XML and only one argument (file expected).
	 * In cloud mode, the second argument is a formatted OXPath expression.
	 */
	public static void main(String[] args) {
		try {
			if (args[0]=="--cloud") {
				OXPathNavigator.runOXPathCloud(args[1]);
			}
			else {
				Modes mode;
				String filename;
				if (args[0].equals("--xml")) {
					mode = Modes.XML;
					filename = args[1];
				}
				else if (args[0].equals("--simple")) {
					mode = Modes.SIMPLE;
					filename = args[1];
				}
				else {
					mode = Modes.XML;
					filename = args[0];
				}
				OXPathNavigator.runOXPathLocal(mode,filename);
			}
		} catch (Exception e) {
			System.out.println("Expect command line arguments as follows: [mode]? expression.  [mode] is either \"--cloud\", \"--xml\", or \"simple\".  Without mode specified, the default is xml mode.  The next command line argument is a file (containing formatted OXPath" +
					"expression) for the local modes, and an expression for the cloud mode.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private enum Modes { XML, SIMPLE }
	
	/**
	 * A "main" method for running OXPath on the local machine, printing out an XML document/tuples at the end to the console
	 * depending on the mode running
	 * @param mode run mode
	 * @param filename command line argument with the name of the file containing formatted OXPath expression(s)
	 */
	private static void runOXPathLocal(Modes mode, String filename) {
		try {
			Logger logger=LoggerFactory.getLogger(OXPathNavigator.class);
			CountDownLatch latch = new CountDownLatch(1);		
			ServerSocket dataServer = new ServerSocket(0);
			
			boolean isXML = (mode.equals(Modes.XML))?true:false;
			
			OXPathOutputHandler handler = (isXML)?
										  new OXPathXMLOutputHandler("localhost",dataServer.getLocalPort(),logger,latch):
				                          new OXPathSimpleOutputHandler("localhost",dataServer.getLocalPort(),logger);

			handler.start();
			
			Socket listen = dataServer.accept();
			ObjectOutputStream os = new ObjectOutputStream(listen.getOutputStream());
			
			FileReader reader = new FileReader(filename);
			OXPathParser parser = new OXPathParser(reader);
			Node root = parser.Expression();
			WebBrowser browser = BrowserFactory.newWebBrowser(Engine.SWT_MOZILLA, true);
			OXPathType result = evaluateOXPathQuery(root,browser,logger,os);
			
			System.out.println(result);
			os.close();
			

			if (isXML) {
				latch.await();
				System.out.println(((OXPathXMLOutputHandler) handler).returnDocumentAsString());
			}
			
//			browser.close();
			browser.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * A "main" method for the cloud; called when the first command line argument is <tt>"cloud"</tt>
	 * @param expression OXPath expression to evaluate
	 */
	private static void runOXPathCloud(String expression) {
		throw new RuntimeException("Cloud mode not supported in this release!");
	}
	
	/**
	 *  Main API method for client use.  Evaluates an OXPath expression encoded in an input file.
	 * @param in filename of the OXPath input file
	 * @param browser web browsing engine for expression evaluation
	 * @param logger the logger object for the process
	 * @return XML Document with extraction results
	 * @throws ParserConfigurationException in case parser configuration error
	 * @throws DOMException in case of XML Document exception 
	 * @throws OXPathException in case of AST structure exception
	 */
	public static OXPathType evaluateOXPathQueryFromFile(String in, WebBrowser browser, Logger logger, ObjectOutputStream os) throws DOMException, ParserConfigurationException, OXPathException, Exception {
		return evaluateOXPathQuery(OXPathParser.getJJTree(in),browser, logger, os);
	}
	
	/**
	 * Main API method for client use.  Evaluates an OXPath expression encoded as a <tt>String</tt> object.
	 * @param in OXPath expression as a <tt>String</tt> object
	 * @param browser web browsing engine for expression evaluation
	 * @param logger the logger object for the process
	 * @return XML Document with extraction results
	 * @throws ParserConfigurationException in case parser configuration error
	 * @throws DOMException in case of XML Document exception 
	 * @throws OXPathException in case of AST structure exception
	 */
	public static OXPathType evaluateOXPathQuery(String in, WebBrowser browser, Logger logger, ObjectOutputStream os) throws DOMException, ParserConfigurationException, OXPathException, Exception {
		return evaluateOXPathQuery(OXPathParser.getJJTreeFromString(in), browser, logger, os);
	}
	
	/**
	 * Main API method for client use.  Evaluates an OXPath expression over an Abstract Syntax Tree.
	 * @param n root of the AST
	 * @param browser web browsing engine for expression evaluation
	 * @param logger the logger object for the process
	 * @param os output stream to send the extraction nodes
	 * @return XML Document with extraction results
	 * @throws ParserConfigurationException in case parser configuration error
	 * @throws DOMException in case of XML Document exception 
	 * @throws OXPathException in case of AST structure exception
	 * @throws IOException in case of malformed xml
	 * @throws SAXException in case of malformed xml
	 */
	public static OXPathType evaluateOXPathQuery(Node n, WebBrowser browser, Logger logger, ObjectOutputStream os) throws ParserConfigurationException, DOMException, OXPathException, SAXException, IOException {
		if (logger==null) logger = LoggerFactory.getLogger(OXPathNavigator.class);
		PAATEvalVisitor pv = PAATEvalVisitor.newInstance(browser, logger, os);
		return pv.accept(n, new PAATState.Builder(new OXPathNodeList<OXPathContextNode>(OXPathContextNode.getNotionalContext())).buildSet());
	}
	
}
