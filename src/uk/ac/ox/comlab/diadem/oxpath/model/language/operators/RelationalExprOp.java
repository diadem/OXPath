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
 * This subpackage includes classes and interface relating to the operators of the OXPath language. 
 */
package uk.ac.ox.comlab.diadem.oxpath.model.language.operators;

import java.util.ArrayList;

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Enum encoding relational operators in OXPath
 * @author AndrewJSel
 *
 */
public enum RelationalExprOp implements BinaryOperator {

	/**
	 * greater than operator
	 */
	GREATER(">") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return this.compute(lhs,rhs);
		}
	}, 

	/**
	 * less than operator
	 */
	LESS("<") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return this.compute(lhs,rhs);
		}
	}, 

	/**
	 * greater than or equal to operator
	 */
	GREATERTE(">=") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return this.compute(lhs,rhs);
		}
	}, 

	/**
	 * less than or equal to operator
	 */
	LESSTE("<=") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return this.compute(lhs,rhs);
		}
	};

	/**
	 * Basic constructor.  Constructor precomputes lists needed by all operators.  Comparisons aren't done due to 
	 * XPath existential semantics, we can return immediately if a comparison succeeds.  
	 * @param iValue {@code String} encoding of the operator
	 */
	private RelationalExprOp(String op) {
		this.operator = op;
	}

	/**
	 * Precomputes lists needed by all operators.  Comparisons aren't done due to 
	 * XPath existential semantics, we can return immediately if a comparison succeeds.
	 * @param lhs left hand side
	 * @param rhs right hand side
	 * @param oper operator
	 * @return computed answer
	 */
	protected OXPathType compute(OXPathType lhs, OXPathType rhs) throws OXPathException{
		OXPathTypes lhsType = lhs.isType();
		OXPathTypes rhsType = rhs.isType();

		ArrayList<Double> lhsList = new ArrayList<Double>();
		ArrayList<Double> rhsList = new ArrayList<Double>();
		
		//convert any nodesets to lists of numbers
		if (lhsType.equals(OXPathTypes.NODESET)) {
			for (OXPathContextNode o : lhs.nodeList()) {
				lhsList.add(Double.valueOf(o.getByXPath("string(.)").string()));
			}
		}
		else lhsList.add(lhs.number());
		if (rhsType.equals(OXPathTypes.NODESET)) {
			for (OXPathContextNode o : rhs.nodeList()) {
				rhsList.add(Double.valueOf(o.getByXPath("string(.)").string()));
			}
		}
		else rhsList.add(rhs.number());

		//compare the lists
		for (Double i : lhsList) {
			double l = i.doubleValue();
			for (Double j : rhsList) {
				double r = j.doubleValue();
				boolean gt = l>r;
				boolean lt = l<r;
				boolean eq = l==r;
				if (this.equals(GREATER) && gt) return new OXPathType(true);
				else if (this.equals(GREATERTE) && (gt || eq)) return new OXPathType(true);
				else if (this.equals(LESS) && lt) return new OXPathType(true);
				else if (this.equals(LESSTE) && (lt || eq)) return new OXPathType(true);
			}
		}
		return new OXPathType(false);
	}

	/**
	 * returns the {@code String} representation of the operator
	 * @return the {@code String} representation of the operator
	 */
	public String getOperator() {
		return this.operator;
	}

	/**
	 * instance field encoding value of operator
	 */
	private final String operator;
}