/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.core.PositionFuncEnum;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPathExpr;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTXPathPathExprAspect {

	public void ASTXPathPathExpr.setHasSimpleList(boolean list) {
		this.hasSimpleList = list;
	}

	public boolean ASTXPathPathExpr.hasSimpleList() {
		return this.hasSimpleList;
	}

	private boolean ASTXPathPathExpr.hasSimpleList = false;

	public void ASTXPathPathExpr.setHasComplexList(boolean list) {
		this.hasComplexList = list;
	}

	public boolean ASTXPathPathExpr.hasComplexList() {
		return this.hasComplexList;
	}

	private boolean ASTXPathPathExpr.hasComplexList = false;

	public void ASTXPathPathExpr.setSetBasedEval(PositionFuncEnum set) {
		this.setEval = set;
	}

	public PositionFuncEnum ASTXPathPathExpr.getSetBasedEval() {
		return this.setEval;
	}

	private PositionFuncEnum ASTXPathPathExpr.setEval = PositionFuncEnum.NEITHER;

	public String ASTXPathPathExpr.toString() {
		return this.getClass().getSimpleName() + "[hasSimpleList" + this.hasSimpleList + ",hasComplexList=" + this.hasComplexList + ",setEval=" + this.setEval + "]";
	}

}
