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
import java.util.Scanner;

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes;
import static uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes.NODESET;
import static uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes.BOOLEAN;
import static uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes.STRING;
import static uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes.NUMBER;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * Enum encoding realtional operators in OXPath
 * @author AndrewJSel
 *
 */
public enum EqualityExprOp implements BinaryOperator{

	/**
	 * equals operator
	 */
	EQUALS("=") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return this.computeEquality(lhs, rhs);
		}
	},

	/**
	 * not equals operator
	 */
	NOTEQUAL("!=") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return this.computeEquality(lhs, rhs);
		}
	},

	/**
	 * wordtest operator
	 */
	WORDTEST("~=") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			boolean isTrue = false;
			Scanner scan = new Scanner(lhs.string());
			while (scan.hasNext()) {
				if (scan.next().equals(rhs)) isTrue = true;
			}
			scan.close();
			return new OXPathType(isTrue);
		}
	},

	/**
	 * contains operator
	 */
	CONTAINS("#=") {
		/**
		 * evaluates input by the operation and returns the result
		 * @param lhs left hand side
		 * @param rhs right hand side
		 * @return value of expression
		 */
		public OXPathType evaluate(OXPathType lhs, OXPathType rhs) throws OXPathException {
			return new OXPathType(lhs.string().contains(rhs.string()));
		}
	};

	/**
	 * basic constructor
	 * @param iValue {@code String} encoding of the operator
	 */
	private EqualityExprOp(String op) {
		this.operator = op;
	}

	/**
	 * Precomputes lists needed by all operators.  Comparisons aren't done due to 
	 * XPath existential semantics, we can return immediately if a comparison succeeds.
	 * <b>Use with {@code EQUALITY} and {@code NONEQUALITY} only.</b> 
	 * @param lhs left hand side
	 * @param rhs right hand side
	 * @param oper operator
	 * @return computed answer
	 */
	protected OXPathType computeEquality(OXPathType lhs, OXPathType rhs) throws OXPathException {
		//don't use with CONTAINS or WORDTEST operators
		if (this.equals(CONTAINS) || this.equals(WORDTEST)) 
			throw new OXPathException("Can't call computeEquality method with non-equality objects");
		
		OXPathTypes lhsType = lhs.isType();
		OXPathTypes rhsType = rhs.isType();
		boolean isTrue = false;

		boolean hasNodeSet = (lhsType.equals(NODESET) || rhsType.equals(NODESET));
		boolean hasNumber = (lhsType.equals(NUMBER) || rhsType.equals(NUMBER));
		boolean hasBoolean = (lhsType.equals(BOOLEAN) || rhsType.equals(BOOLEAN));

		if (hasBoolean) {
			return new OXPathType((this.equals(EQUALS)) ? lhs.booleanValue()==rhs.booleanValue() : !(lhs.booleanValue()==rhs.booleanValue()));
		}

		//otherwise, if we have a nodeset, and we need to at least convert the nodeset to string(.) equivalents
		ArrayList<String> lhsList = new ArrayList<String>();
		ArrayList<String> rhsList = new ArrayList<String>();
		if (hasNodeSet) {//handle the nodeset comparisons
			if (lhsType.equals(NODESET)) {
				for (OXPathContextNode o : lhs.nodeList()) {
					lhsList.add(o.getByXPath("string(.)").string());
				}
			}
			if (rhsType.equals(NODESET)) {
				for (OXPathContextNode o : rhs.nodeList()) {
					rhsList.add(o.getByXPath("string(.)").string());
				}
			}
		}

		if (lhsType.equals(STRING)) lhsList.add(lhs.string());
		if (rhsType.equals(STRING)) rhsList.add(rhs.string());

		if (!hasNumber) {//comparisons can be made directly
			for (String i : lhsList) {
				for (String j : rhsList) {
					isTrue = (this.equals(EQUALS)) ? i.equals(j) : !i.equals(j);
					if (isTrue) return new OXPathType(true);
				}
			}
			return new OXPathType(false);
		}
		else {//we have to deal with number conversions
			if (lhsType.equals(rhsType)) {//both doubles
				return new OXPathType((this.equals(EQUALS)) ? lhs.number().doubleValue()==rhs.number().doubleValue() : lhs.number().doubleValue()!=rhs.number().doubleValue());
			}//we proceed knowing exactly one side is a number and the other already has String values loaded into a list
			double num = (lhsType.equals(NUMBER)) ? lhs.number() : rhs.number();
			ArrayList<String> list = (lhsType.equals(NUMBER)) ? rhsList : lhsList;
			for (String i : list) {
				isTrue = (this.equals(EQUALS)) ? (Double.parseDouble(i)==num) : (Double.parseDouble(i)!=num);
				if (isTrue) return new OXPathType(true);
			}
			return new OXPathType (false);
		}
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