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
package uk.ac.ox.comlab.oxpath.oxpathTreeWalker;

import static uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter.getJJTree;

//import java.net.URI;
//import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;

import uk.ac.ox.comlab.oxpath.scriptParser.SimpleNode;

public class OXPathCommandLine {

	/**
	 * Method for command line OXPath tool
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		
		String inputfile = null;
		boolean verbose = false;
		for (String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.equals("-h")) {
					System.out.println(HELP);
					System.exit(0);
				}
				else if (arg.equals("-v")) verbose=true; 
				else System.out.println("Unknown option: " + arg);
//				System.exit(1);
			}
			else {
				if (inputfile!=null) {
					System.out.println("please only output one input file at a time");
					System.exit(1);
				}
				else inputfile = arg;
			}
		}
		URL uriConfigVerbose = null;
		URL uriConfigSilent = null;
		try {
			uriConfigVerbose = OXPathCommandLine.class.getResource("log4jverbose.properties").toURI().toURL();
			uriConfigSilent = OXPathCommandLine.class.getResource("log4jsilent.properties").toURI().toURL();
		} catch (Exception e1) {}
		if (verbose) PropertyConfigurator.configure(uriConfigVerbose);
		else PropertyConfigurator.configure(uriConfigSilent);
		
		if (inputfile==null) {
			System.out.println("OXPath expects exactly one input file");
			System.exit(1);
		}
		try {
			SimpleNode root = getJJTree(inputfile);			
			Document d = TreeWalker.evaluateOXPathQuery(root);
			System.out.println(TreeWalker.getStringFromDocument(d));
		} catch (Exception e) {
			System.out.println(e.toString());
//			e.printStackTrace();
		} 
	}
	
	public static final String HELP = "OXPath takes exactly one argument - a filename referring to an input file formatted as \"path=\"<OXPath>\";\"";

}
