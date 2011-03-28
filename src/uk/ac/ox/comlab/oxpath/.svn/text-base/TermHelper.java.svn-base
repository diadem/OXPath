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

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class has methods and enums to support grounding of OXPathExpression paths
 * @author AndrewJSel
 *
 */
public class TermHelper {
	
	/**
	 * Constructor for TermHelper object
	 */
	public TermHelper() {}
	
	/**
	 * Once an action is tokenized, this method determines their type
	 * @param terms String containing the action terms
	 * @return type of terms, given as a TermTypes value
	 * @throws BadDataException if malformed token is input
	 */
	public static TermTypes getTermType(String terms) throws BadDataException{
		TermTypes rv = null;
		if (isKeyword(terms)) rv=TermTypes.KEYWORD;
		else if (Character.isLetter(terms.trim().charAt(0))) rv=TermTypes.DATABASE;
		else if (terms.trim().startsWith("\"")) return rv=TermTypes.EXPLICIT;
		else if (Character.isDigit(terms.trim().charAt(0))) rv=TermTypes.POSITION;
		//in case set is leading entry
		else if (terms.trim().startsWith("{")) {
			if (terms.trim().substring(1).startsWith("\"")) return rv=TermTypes.EXPLICIT;
			else if (Character.isDigit(terms.trim().charAt(1))) rv=TermTypes.POSITION;
		}		
		if (rv==null) throw new BadDataException("Malformed Query Term!\n");
		return rv;
	}


	/**
	 * Static Method for generating a token list from a string with tokens separated by commas
	 * @param currTerm String containing the tokens
	 * @return ArrayList<String> of returned tokens
	 */
	public static ArrayList<String> commaTokenizer(String currTerm) {
		ArrayList<String> tokens = new ArrayList<String>();
		Scanner sc = new Scanner(currTerm);
		sc.useDelimiter("\\s*" + UnGroundedOXPathExpression.QTERMSEPARATOR + "\\s*");
		while (sc.hasNext()) {
			tokens.add(sc.next().trim());
		}
		sc.close();
		return tokens;
	}
	
	/**
	 * method verifies the current token is in quotes, does nothing if so, else raises BadDataException
	 * @param token token to verify
	 */
	public static void quoteChecker(String token) throws BadDataException {
		if (!((token.charAt(0)==('"'))&&(token.charAt(token.length()-1)=='"')&&(token.length()>1))) {
			throw new BadDataException(" Bad token found in QTerm!\n");
		}
	}
	
	/**
	 * method verifies the current token is a number, does nothing if so, else raises BadDataException
	 * @param iTerm token to verify
	 */
	public static void numberChecker(String iTerm) throws BadDataException {
		if (!Pattern.matches("[0-9]*", iTerm)) {
			throw new BadDataException(" Bad token found in QTerm!\n");
		}		
	}
	
	/**
	 *  method to determine if a String is a keyword
	 *  @param test String to determine if is keyword
	 *  @return whether or not <tt>test<tt> is a keyword
	 */
	public static boolean isKeyword(String test) {
		boolean rv = false;
		for (String kw : keywords) {
			if (kw.trim().toLowerCase().equals(test)) rv=true;
		}
		return rv;
	}
	/**
	 * enum describing the different types of terms that can be referenced in the selection part of an OXPathExpression
	 */
	public enum TermTypes { DATABASE, EXPLICIT, POSITION, KEYWORD }
	
	/**
	 * Value of click keyword
	 */
	public final static String CLICK = "click";
	/**
	 * value of submit keyword
	 */
	public final static String SUBMIT = "submit";
	/**
	 * List of OXPath keywords
	 */
	public final static String[] keywords = {CLICK, SUBMIT};	
}
