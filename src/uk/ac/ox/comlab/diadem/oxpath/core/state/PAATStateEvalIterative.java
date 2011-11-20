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
package uk.ac.ox.comlab.diadem.oxpath.core.state;

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;

/**
 * @author AndrewJSel
 *
 */
public class PAATStateEvalIterative extends PAATState{

	/**
	 * Creates a new object with the same state as the input parameter; meant to be called only with {@code Builder.build()} method
	 * @param builder implicit {@code Builder} object for this {@code PAATState} object
	 */
	protected PAATStateEvalIterative(Builder builder, OXPathContextNode c) {
		super(builder);
		this.context = c;
	}
	
	/**
	 * Returns context at c
	 * @return context current context (a set or a node depending on evaluation pattern)
	 */
	public OXPathContextNode getContextNode() {
		return this.context;
	}
	
	/**
	 * Returns the type of the object
	 * @return the type of the object
	 */
	public PAATStateType getType() {
		return PAATStateType.ITERATIVE;
	}
	
	/**
	 * context at current "step" in query
	 */
	private final OXPathContextNode context;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PAATStateEvalIterative other = (PAATStateEvalIterative) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		return true;
	}
	
	
}
