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

import diadem.common.web.dom.DOMNode;
import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalIterative;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.Node;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathCache;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Interface used to wrap the eval_ visitor so that the {@code OXPathMemoizer} can be used with it.
 * The resulting proxy objects handle memoization and we can just solve and abstract the memoization
 * @author AndrewJSel
 *
 */
public interface PAATEval_ extends OXPathCache {

	/**
	 * Need to call this method to check children so that memoization is applied
	 * @param context the context node
	 * @param astNode the node in the AST to evaluate over
	 * @param state the state of the evaluation
	 * @return evaluation result
	 */
	public OXPathType eval_(DOMNode context, Node astNode, PAATStateEvalIterative state) throws OXPathException;
	
}
