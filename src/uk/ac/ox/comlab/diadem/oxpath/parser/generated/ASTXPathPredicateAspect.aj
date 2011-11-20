/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.core.PositionFuncEnum;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPredicate;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTXPathPredicateAspect {

	public void ASTXPathPredicate.setHasList(boolean hl) {
		this.hasList = hl;
	}

	public boolean ASTXPathPredicate.hasList() {
		return this.hasList;
	}
	
	public void ASTXPathPredicate.setIsOptional(boolean opt) {
		this.isOptional = opt;
	}
	
	public boolean ASTXPathPredicate.isOptional() {
		return this.isOptional;
	}

	private boolean ASTXPathPredicate.hasList = false;
	private boolean ASTXPathPredicate.isOptional = false;

	public void ASTXPathPredicate.setSetBasedEval(PositionFuncEnum set) {
		this.setEval = set;
	}

	public PositionFuncEnum ASTXPathPredicate.getSetBasedEval() {
		return this.setEval;
	}

	private PositionFuncEnum ASTXPathPredicate.setEval = PositionFuncEnum.NEITHER;
	
	public String ASTXPathPredicate.toString() {
		return this.getClass().getSimpleName() + "[isOptional=" +this.isOptional + ",hasList=" + this.hasList + ",setEval=" + this.setEval + "]";
	}
}
