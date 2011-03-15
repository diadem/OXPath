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
package uk.ac.ox.comlab.oxpath.oxpathTreeWalker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;

/**
 * Class used for exposing Rhino methods for accessing JavaScript features
 * @author AndrewJSel
 *
 */
public class CSSProperties {

	/**
	 * Constructor that maps all CSS Property names with their Rhino functions
	 */
	public CSSProperties() {
		Method methods[] = ComputedCSSStyleDeclaration.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
        	String name = methods[i].getName();
        	if (name.startsWith("jsxGet_") && methods[i].getParameterTypes().length == 0) {
        		String cssName = name.substring(7);
        		Pattern p = Pattern.compile("(\\p{Upper})");
        		Matcher m = p.matcher(cssName);
        		StringBuffer sb = new StringBuffer();
        		while (m.find()) {
        			m.appendReplacement(sb, "-$1");
        		}	
        		cssName = m.appendTail(sb).toString().toLowerCase();
        		cssProperties.put(cssName,methods[i]);
        	}
        }
	}
	
	public Method getMethod(String key) {
		return cssProperties.get(key);
	}
	
	/**
	 * Instance field storing the cssProperties 
	 */
	Map<String,Method> cssProperties = new HashMap<String,Method>();
	
}
