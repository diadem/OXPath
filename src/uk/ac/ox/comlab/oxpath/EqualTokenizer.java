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

import java.util.Scanner;

/**
 * Used to input a String, isolate the argument and values with a Scanner object; immutable once created
 * @author AndrewJSel
 */
public class EqualTokenizer {

	/**
	 * constructor that takes a string and tokenizes its argument and values
	 * @param in String to be parsed
	 */
	public EqualTokenizer(String in) {
		//uses Scanner object for parsing/tokenizing
		Scanner parser = new Scanner(in);
		//use first equals sign (=) as delimiter 
		parser.useDelimiter("\\s*=\\s*");
		argument = parser.next().trim();
		//reset delimiter and return all but = of the rest of the String
		parser.reset();
		if (parser.hasNext()) value = parser.nextLine().trim().substring(1).trim();//two trims needed to get rid //s*=//s*
		else value = "";
		parser.close();
	}
	
	/**
	 * retrieves argument of this EqualTokenizer object
	 * @return argument (trimmed of whitespace)
	 */
	public String getArgument() {
		return this.argument;
	}
	
	/**
	 * determines whether the argument is non-empty
	 * @return whether argument is non-empty
	 */
	public boolean hasNonEmptyArgument() {
		if (argument.equals("")) return false;
		else return true;
	}
	
	/**
	 * determines whether the value is non-empty
	 * @return whether value is non-empty
	 */
	public boolean hasNonEmptyValue() {
		if (value.equals("")) return false;
		else return true;
	}
	
	/**
	 * retrieves value of this EqualTokenizer object
	 * @return value value (trimmed of whitespace)
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * returns a String containing state information of object
	 */
	public String toString() {
		String out;
		//returns class name with all state info
		out = this.getClass().getName()+ "[attribute=" + this.argument + ", value=" + this.value + "]\n";
		return out;
	}
	
	
	
	//instance fields for argument (before =) and value (after =)
	private String argument;
	//value is initialized to "" to return empty String if no value token
	private String value;
}
