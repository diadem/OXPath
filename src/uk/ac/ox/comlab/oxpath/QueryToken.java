/**
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
/**  Main package for implementation of OXPath framework
 *  @author AndrewJSel
 */
package uk.ac.ox.comlab.oxpath;

/**
 * Class for retrieving the next token, rest, and token type of a navigational path string 
 * @author AndrewJSel
 *
 */
public class QueryToken {
	
	/**
	 * Constructor method for QueryToken objects, using a navigation path as input parameter
	 * Only considers actual tokens (check for empty string expression BEFORE call)
	 * @param s navigation path segment (from a GroundedOXPathExpression object)
	 * @throws BadDataException 
	 */
	public QueryToken(String s) throws BadDataException {
		//first token starts with / or // (shorthand for descendant axis), so the first token will be closed with the next / character
		//all first token will have form /X+/ or //X+/
		//currently implements only navigation XPath (doesn't support compound-axis navigation, predicates)
		if (s.indexOf('/', 2)>0) {//not last token
			this.firstToken = s.substring(0, s.indexOf('/',2));
			this.restTokens = s.substring(this.firstToken.length());//rest of tokens are formed from the remainder of the input String
		}
		else {//last token
			this.firstToken = s;
			this.restTokens = "";
		}
		if (this.firstToken.startsWith(ACTIONSTART)) {
			if (!(this.firstToken.endsWith(ACTIONEND))) {
				throw new BadDataException("Malformed path expression!  Check Action Literals!");
			}
			else this.firstTokenType = TokenType.ACTION;
		}
		else if (containsAdditionalAxes(this.firstToken)) {
			this.firstTokenType = TokenType.ADDITIONALAXIS;
		}
		else {
			this.firstTokenType = TokenType.XPATHSTANDARD;
		}
	}
	
	/**
	 * Method to determine if a token contains one of the OXPath additional axes
	 * @param token input token (String object)
	 * @return <tt>true</tt> if additional axes is present, <tt>false</tt> otherwise
	 */
	public boolean containsAdditionalAxes(String token) {
		boolean addAxisFound = false;
		for (AdditionalAxes a : AdditionalAxes.values()) {
			if (token.contains(a.getValue())) {
				addAxisFound = true;
			}
		}
		return addAxisFound;
	}

	/**
	 * Method returns first token
	 * @return first token as String object
	 */
	public String getFirstToken() {
		return this.firstToken;
	}
	
	/**
	 * Method returns rest of tokens as single String
	 * @return rest of tokens as String object
	 */
	public String getRestTokens() {
		return this.restTokens;
	}
	
	/**
	 * Method returns the token type of the first token
	 * @return token type of the first token as a member of the TokenType enum
	 */
	public TokenType getFirstTokenType() {
		return this.firstTokenType;
	}
	
	
	
	/**
	 * enum encoding token types
	 */
	public enum TokenType { XPATHSTANDARD, ADDITIONALAXIS, ACTION }
	
	//instance fields storing first token, rest, and first token type
	private String firstToken;
	private String restTokens;
	private TokenType firstTokenType;
	
	public static final String ACTIONSTART = "/{";
	public static final String ACTIONEND = "}";
}
