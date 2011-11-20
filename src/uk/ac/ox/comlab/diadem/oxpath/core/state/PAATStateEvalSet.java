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

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathNodeList;

/**
 * 
 * @author AndrewJSel
 *
 */
public class PAATStateEvalSet extends PAATState{


	/**
	 * Creates a new object with the same state as the input parameter; meant to be called only with {@code Builder.build()} method
	 * @param builder implicit {@code Builder} object for this {@code PAATState} object
	 * @param c context set
	 * @param higher number of Kleene-star iterations to perform
	 * @param currAction the identifier for the current action being processed
	 */
	protected PAATStateEvalSet(Builder builder, OXPathNodeList c, int numHigher, int currAction) {
		super(builder);
		this.context = c;
		this.higher = numHigher;
		this.currentAction = currAction;
	}
	
	/**
	 * Returns context at c.  We don't make a defensive copy here, so be careful.
	 * @return context current context (a set or a node depending on evaluation pattern)
	 */
	public OXPathNodeList getContextSet() {
		return context;
	}
	
	/**
	 * Returns the type of the object
	 * @return the type of the object
	 */
	public PAATStateType getType() {
		return PAATStateType.SET;
	}

	/**
	 * Returns the number of Kleene operations to perform
	 * @return the number of Kleene operations to perform
	 */
	public int getNumKleeneStarIterations() {
		return this.higher;
	}
	
	/**
	 * Returns the identifier for the current action being processed
	 * @return the identifier for the current action being processed
	 */
	public int getCurrentAction() {
		return this.currentAction;
	}
	
	/**
	 * context at current "step" in query
	 */
	private final OXPathNodeList context;
	
	/**
	 * number of Kleene star iterations to perform (communicated by outer Kleene to inner action)
	 */
	private final int higher;
	
	/**
	 * identifier of the current action
	 */
	private final int currentAction;
}
