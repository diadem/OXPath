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

//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

/**
 * Class used to test the HTML client (currently HTMLUnit)
 * 
 * @author AndrewJSel
 * 
 */
public class HTMLClientTester {

	/**
	 * method for testing the HTML client
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		try {
			// final WebClient webClient = new WebClient();
			// final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
			// System.out.println(page.getTitleText());
			// System.out.println("HtmlUnit - Welcome to HtmlUnit");

			//
			// URL url = new URL("http://www.hamptons.co.uk");
			// URLConnection con = url.openConnection();
			// InputStream stream = con.getInputStream();
			// XMLReader reader = new Parser();
			// reader.setFeature(Parser.namespacesFeature, false);
			// reader.setFeature(Parser.namespacePrefixesFeature, false);
			//	
			// Transformer transformer = TransformerFactory.newInstance().newTransformer();
			//				
			// DOMResult result = new DOMResult();
			// transformer.transform(new SAXSource(reader, new InputSource(url.openStream())),
			// result);
			//		
			// Node root = result.getNode();
			//		
			// System.out.println(root.hasChildNodes());
			//		
			//
			// XPathFactory xpfactory = XPathFactory.newInstance();
			// XPath xpath = xpfactory.newXPath();
			// NodeList results = (NodeList) xpath.evaluate("//form[@id=\"aspnetForm\"]", root, XPathConstants.NODESET);
			//		
			// System.out.println(results.item(0).getNodeName());
			// System.out.println(root.compareDocumentPosition(root));
			// System.out.println(root.compareDocumentPosition(results.item(0)));
			// System.out.println(results.item(0).compareDocumentPosition(root));
			// XPathHelper helper = new XPathHelper();
			// System.out.println(helper.compare(results.item(0), results.item(0)));
			// System.out.println(helper.compareDocumentOrder(root,results.item(0)));
			// System.out.println(helper.compareDocumentOrder(results.item(0),root));

			// here we go - an DOM built from abitrary HTML
			// result.getNode();

			// System.out.println(result.getNode());

			//			
			// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// factory.setValidating(true);
			// factory.setNamespaceAware(true);//after A LOT of wasted time, I found you need this
			// DocumentBuilder builder = factory.newDocumentBuilder();
			// //needs to do local entity resolution, since W3C is default behaviour, and it now blocks all such traffic
			// OXPathEntityResolver xpEntityResolver = new OXPathEntityResolver();
			// builder.setEntityResolver(xpEntityResolver);

			// XMLCatalogResolver resolver = new XMLCatalogResolver();
			// resolver.setCatalogList(new String[] {"xhtml1-catalog.xml"});
			//
			// DOMParser parser = new DOMParser();
			// parser.setProperty("http://apache.org/xml/properties/internal/entity-resolver", resolver);

			// standard for opening a connection
			// URL url = new URL("http://www.amazon.com");
			// URL url = new URL("http://www.google.com");
			// URLConnection con = url.openConnection();
			// InputStream stream = con.getInputStream();

			// uses JTidy to clean html (pushes to out)
			// Tidy t = new Tidy();
			// t.setXHTML(true);
			// ByteArrayOutputStream out = new ByteArrayOutputStream();//uses ByteArrays instead of normal streams for converting as
			// // as outlined at http://ostermiller.org/convert_java_outputstream_inputstream.html
			// t.parse(stream, out);
			// System.out.println(out);

			// build document from out
			// Document docum = builder.parse(new ByteArrayInputStream(out.toByteArray()));
			long start = System.nanoTime();
			
			final WebClient webClient = new WebClient();
			webClient.setThrowExceptionOnFailingStatusCode(false);
			
			long current = System.nanoTime();
			while (true) {
				HtmlPage page1 = webClient.getPage("http://192.168.0.106/scholar.htm");
				List<?> nextlinks = page1.getByXPath("/descendant::a[contains(.,'Next')]");
				page1 = ((HtmlElement) nextlinks.get(0)).click();
				List<?> nextlinks2 = page1.getByXPath("/descendant::a[contains(.,'Next')]");
				current = System.nanoTime() - start;
				System.out.println(current);
			}
			
			// final HtmlImageInput imageInput = (HtmlImageInput) page1.getFirstByXPath("/following::input[@id='ctl00_SearchControl1_SearchButton']");
//			final HtmlSelect select = (HtmlSelect) page1.getFirstByXPath("//select[@name='ctl00$SearchControl1$DropDownListBranchArea']");
//			select.setSelectedAttribute("OC", true);
			// final HtmlTextInput areaField = page1.getFirstByXPath(xpathExpr);
//			final HtmlImageInput imageInput = (HtmlImageInput) select.getFirstByXPath("//input[@id='ctl00_SearchControl1_SearchButton']");
//			HtmlPage result = (HtmlPage) imageInput.click();
			// HtmlPage page2 = (HtmlPage) imageInput.click();
			// System.out.println(page2.getTitleText());
//			System.out.println(result.asXml());
//			System.out.println(result.getByXPath("string(.)").get(0).getClass());
//			webClient.closeAllWindows();

			// Get the first page
			// final HtmlPage page1 = webClient.getPage("http://www.hamptons.co.uk");
			// final HtmlPage page1 = webClient.getPage("http://www.google.com");
			// Get the form that we are dealing with and within that form,
			// find the submit button and the field that we want to change.
			// final HtmlForm form = page1.getFormByName("aspnetForm");
			// final List<HtmlForm> form1 = page1.getForms();
			// final HtmlForm form2 = page1.getFirstByXPath("//form[@id=\"aspnetForm\"]");
			// List<HtmlForm> forms = page1.getForms();
			// for (HtmlForm f : forms) {
			// System.out.println(f.getNameAttribute());
			// }
			// System.out.println(form.getNameAttribute());
			// final HtmlSubmitInput button = (HtmlSubmitInput) page1.getFirstByXPath("//input[@name='ctl00_ctl00_MainMasterPlaceHolder_PropertysearchControl1_tbxArea']");
			// final HtmlSubmitInput button = (HtmlSubmitInput) page1.getByXPath("//input[@name=btnG]")
			// final HtmlTextInput areaField = form.getInputByName("ctl00_ctl00_MainMasterPlaceHolder_PropertysearchControl1_tbxArea");
			// final DomNode node1 = page1.getFirstByXPath("//input[@id='ctl00_ctl00_MainMasterPlaceHolder_PropertysearchControl1_tbxArea']");
			// System.out.println(node1.getNodeName());
			// System.out.println(node1.getNodeValue());
			// System.out.println(node1.asText());
			// System.out.println(node1.getCanonicalXPath());
			// System.out.println(node1.getClass());
			// NamedNodeMap nns = node1.getAttributes();
			// System.out.println(nns.getNamedItem("type").getNodeValue());
			// System.out.println(nns.getNamedItem("type").getNodeName());
			// System.out.println(nns.getNamedItem("type").getNodeType());
			// System.out.println(nns.getNamedItem("type").getTextContent());

			// final HtmlTextInput areaField = (HtmlTextInput) page1.getFirstByXPath("//input[@id='ctl00_ContentPlaceHolder1_ctlQuickSearch_tbxArea']");
			// final HtmlRadioButtonInput interestField = form.getInputByName("ctl00_ContentPlaceHolder1_ctlQuickSearch_rdlIwantTo_0");
			// final HtmlSelect maxPriceCurrencyField = form.getInputByName("curr");

			// Change the value of the text field
			// areaField.setValueAttribute("Oxford");
			// interestField.setValueAttribute("Rent");

			// Now submit the form by clicking the button and get back the second page.
			// XPathHelper helper = new XPathHelper();
			// int compare1 = helper.compareDocumentOrder(areaField, button);
			// System.out.println(compare1);
			// int compare2 = helper.compareDocumentOrder(button, button);
			// System.out.println(compare2);
			// int compare3 = helper.compareDocumentOrder(button, areaField);
			// System.out.println(compare3);
			// final HtmlPage page2 = button.click();
			// System.out.println(page2.asXml());
//			webClient.closeAllWindows();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// catch (ParserConfigurationException e) {
		// e.printStackTrace();
		// }
		// catch (SAXException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (TransformerException e) {
		// e.printStackTrace();
		// } catch (XPathExpressionException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// final WebClient webClient = new WebClient();
		//
		// // Get the first page
		// final HtmlPage page1 = webClient.getPage("http://some_url");
		//
		// // Get the form that we are dealing with and within that form,
		// // find the submit button and the field that we want to change.
		// final HtmlForm form = page1.getFormByName("myform");
		//
		// final HtmlSubmitInput button = form.getInputByName("submitbutton");
		// final HtmlTextInput textField = form.getInputByName("userid");
		//
		// // Change the value of the text field
		// textField.setValueAttribute("root");
		//
		// // Now submit the form by clicking the button and get back the second page.
		// try {
		// final HtmlPage page2 = button.click();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
