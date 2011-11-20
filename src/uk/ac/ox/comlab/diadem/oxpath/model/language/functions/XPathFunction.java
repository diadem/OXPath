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
 * Package containing supporting classes, derived from the OXPath model (which itself extends the XPath model).
 * This subpackage includes classes and interface relating to the functions of the OXPath language. 
 */
package uk.ac.ox.comlab.diadem.oxpath.model.language.functions;

import java.util.ArrayList;

import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalSet;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Interface for implementing functions in PAAT in the OXPath core engine.
 * @author AndrewJSel
 *
 */
public interface XPathFunction {
	
	/**
	 * Returns the function name (no parentheses)
	 * @return the function name (no parentheses)
	 */
	public String getName();
	
	/**
	 * Returns the minimum number of parameters this function accepts
	 * @return the minimum number of parameters this function accepts
	 */
	public int getMinParameters();
	
	/**
	 * Returns the maximum number of parameters this function accepts
	 * @return the maximum number of parameters this function accepts
	 */
	public int getMaxParameters();
	
	/**
	 * Given the number of parameters, can statically check if this function accepts this number of parameters
	 * @param numParam num of parameters to check in a function call
	 * @return {@code true} if the number of parameters are legal; {@code false} otherwise
	 */
	public boolean checkParameterCount(int numParam);
	
	/**
	 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
	 * This should be checked beforehand with {@code checkParameterCount(int)}
	 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
	 * @param state the state of the evaluation
	 * @return the return value of the function as prescribed in the XPath 1.0 standard <tt>http://www.w3.org/TR/xpath/</tt>
	 * @throws OXPathException in case of function error
	 */
	public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException;

}
