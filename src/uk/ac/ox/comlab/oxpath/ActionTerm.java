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

import uk.ac.ox.comlab.oxpath.TermHelper.TermTypes;

/**
 * Class used to create objects for Action Terms, used to take actions inside XHTML/HTML4 web pages
 * @author AndrewJSel
 *
 */
public class ActionTerm {

	/**
	 * Constructor for action terms
	 * @param actionToken input token, formatted as an action axis from a navigation path in an OXPathExpression
	 * @throws BadDataException if malformed action token
	 */
	public ActionTerm(String actionToken) throws BadDataException {
		if (!actionToken.startsWith(QueryToken.ACTIONSTART)||!actionToken.endsWith(QueryToken.ACTIONEND)) {
			throw new BadDataException("Malformed Action Token!");
		}
		else {
			String pureToken = actionToken.substring((actionToken.indexOf(QueryToken.ACTIONSTART) + QueryToken.ACTIONSTART.length()), actionToken.lastIndexOf(QueryToken.ACTIONEND));
			//first if/else handles attribute identification/processing
			if (pureToken.startsWith(ATTRIBUTESTART)) {
				this.hasAttribute= true;
				this.attributeName = pureToken.substring(0 + ATTRIBUTESTART.length(), pureToken.indexOf(ATTRIBUTEDELIMITER));
				pureToken = pureToken.substring(pureToken.indexOf(ATTRIBUTEDELIMITER) + ATTRIBUTEDELIMITER.length());
			}
			else {
				this.hasAttribute = false;
			}
			if (pureToken.startsWith(MULTISELECTSTART)&&pureToken.endsWith(MULTISELECTEND)) {//if the term is a multiselect
				String purestToken = pureToken.substring((actionToken.indexOf(QueryToken.ACTIONSTART) + QueryToken.ACTIONSTART.length()), actionToken.lastIndexOf(QueryToken.ACTIONEND));
				ArrayList<String> tempList = TermHelper.commaTokenizer(purestToken);
				this.termType = TermHelper.getTermType(tempList.get(0));//requires all multiselect terms be referenced the same way
				if (this.termType==TermHelper.TermTypes.EXPLICIT) {//No Keywords to utilize here
					for (String temp : tempList) {
						if (temp.startsWith("\"")&&temp.endsWith("\"")) {
							this.tokens.add(temp.substring(1, pureToken.lastIndexOf('"')));
						}
						else throw new BadDataException("Malformed Action Token!");
					}
				}
				else {//must be POSITION
					for (String temp : tempList) {
						this.intTokens.add(Integer.getInteger(temp));
					}
					//check token integrity
					for (Integer i : this.intTokens) {
						if (i==null) throw new BadDataException("Malformed MULTISELECT POSITION Action Token!");
					}
				}
			}			
			else if (pureToken.contains(MULTISELECTSTART)||pureToken.contains(MULTISELECTEND)) {//should not have further {} characters
				throw new BadDataException("Illegal { or } character encountered in action ");
			}
			else {//single action to take
				this.termType = TermHelper.getTermType(pureToken);
				if (this.termType==TermHelper.TermTypes.KEYWORD) {
					this.tokens.add(pureToken);
				}
				else if (this.termType==TermHelper.TermTypes.EXPLICIT) {
					if (pureToken.startsWith("\"")&&pureToken.endsWith("\"")) {
						this.tokens.add(pureToken.substring(1, pureToken.lastIndexOf('"')));
					}
					else throw new BadDataException("Malformed Action Token!");
				}
				else {//must be POSITION
					this.intTokens.add(Integer.parseInt(pureToken));
					//check token integrity
					if (this.intTokens.isEmpty()) throw new BadDataException("Malformed POSITION Action Token!");
				}
			}
		}
	}
	
	/**
	 * Standard getter returning token type of the implicit parameter
	 * @return term type of the object
	 */
	public TermHelper.TermTypes getActionType() {
		return this.termType;
	}
	
	/**
	 * Returns the number of actions
	 * @return number of actions as int
	 */
	public int getSizeActions() {
		int rv;
		if (this.termType==TermTypes.KEYWORD) rv=1;
		else if (this.termType==TermTypes.EXPLICIT) rv=tokens.size();
		else rv=intTokens.size();//must be POSITION
		return rv;
	}
	
	/**
	 * Returns the Explicit Action (CHECK first that object has explicit actions - <tt>null</tt> value is returned if not)
	 * @param n position to return (counter begins at O)
	 * @return the explicit action at position <tt>n</tt>
	 */
	public String getExplicitAction(int n) {
		return this.tokens.get(n);
	}
	
	/**
	 * Returns the Position Action (CHECK first that object has position actions - <tt>null</tt> value is returned if not)
	 * @param n index to return (counter begins at O)
	 * @return the position action at index <tt>n</tt>
	 */
	public Integer getPositionAction(int n) {
		return this.intTokens.get(n);
	}
	
	/**
	 * Returns the Keyword Action (CHECK first that object has keyword actions - <tt>null</tt> value is returned if not)
	 * @param n position to return (counter begins at O)
	 * @return the keyword action at position <tt>n</tt>
	 */
	public String getKeywordAction(int n) {
		return this.tokens.get(n);
	}
	
	/**
	 * method for determining if this action token has an attribute
	 * @return <tt>true</tt> if action token has attribute; <tt>false</tt> otherwise
	 */
	public boolean hasAttribute() {
		return this.hasAttribute;
	}
	
	/**
	 * method for returning this action token's attribute
	 * @return name of attribute for this action (<tt>null</tt> if none)
	 */
	public String getAttributeName() {
		return this.attributeName;
	}

	//private strings storing state
	private ArrayList<String> tokens = new ArrayList<String>();
	private ArrayList<Integer> intTokens = new ArrayList<Integer>();//generic must use a class, so wrapper class of int is used
	private TermHelper.TermTypes termType;
	private boolean hasAttribute = false;
	private String attributeName;
	
	/**
	 * Constant String for Multiselection Start
	 */
	public final static String MULTISELECTSTART = "{";
	
	/**
	 * Constant String for Multiselection End
	 */
	public final static String MULTISELECTEND = "}";
	
	/**
	 * Constant Strings signaling use of an attribute
	 */
	public final static String ATTRIBUTESTART = "@";
	public final static String ATTRIBUTEDELIMITER = "=";
}
