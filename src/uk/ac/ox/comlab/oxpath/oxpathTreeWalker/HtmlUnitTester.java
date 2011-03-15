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

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;

public class HtmlUnitTester {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
	    final WebClient webClient = new WebClient();
	    webClient.setThrowExceptionOnScriptError(false);
	    webClient.setAjaxController(new NicelyResynchronizingAjaxController());

	    // Get the first page
	    final HtmlPage page1 = webClient.getPage("http://www.reed.co.uk");

	    final HtmlTextInput k = page1.getFirstByXPath("//input[@id='k']");
	    k.type("java");
	    
	    final HtmlTextInput l = page1.getFirstByXPath("//input[@id='l']");
	    l.type("oxford");
	    
	    final HtmlSelect lp = page1.getFirstByXPath("//select[@id='lp']");
	    lp.setSelectedAttribute("5", true);
	    
	    final HtmlSubmitInput s = page1.getFirstByXPath("//input[@title='Search Jobs']");
	    final HtmlPage page2 = s.click();
	    
	    
	    final HtmlAnchor a = page2.getFirstByXPath("//a[@id='ctl00_Main_rptJobSearchResults_ctl01_lnkJobtitle']");
	    final HtmlPage page3 = a.click();
	    System.out.println(page3.getUrl());
	    // Get the form that we are dealing with and within that form, 
	    // find the submit button and the field that we want to change.
//	    final HtmlForm form = page1.getFormByName("site-search");

//	    final HtmlElement button = (HtmlElement) page1.getFirstByXPath("//input[@alt='Go']");
//	    final HtmlTextInput textField = (HtmlTextInput) page1.getFirstByXPath("//input[@title='Search for']");

	    // Change the value of the text d
//	    textField.setValueAttribute("Hyderabad");
//	    HtmlPage page2 = button.click();

	    // Now submit the form by clicking the button and get back the second page.
//	    HTMLFormElement t = (HTMLFormElement) form.getScriptObject();
//	    System.out.println(form);
//	    t.jsxFunction_submit();
//	    synchronized (page1) {
//	    	page1.wait(10000);
//	    }
	    //	    String path = form.getCanonicalXPath();
//	    final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.getElementById('site-search').submit()").getNewPage();
//	    final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.evaluate('" + path + "',documentNode, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null ).iterateNext().submit()").getNewPage();
//	    synchronize ()
	    
//	    System.out.println(page2.asXml());
	    
//	    for (WebWindow i :webClient.getWebWindows()) {
//	    	System.out.println(i.getEnclosedPage().getClass());
//	    	System.out.println((i.getEnclosedPage().getUrl()));
//	    }

	    webClient.closeAllWindows();

	}
}
