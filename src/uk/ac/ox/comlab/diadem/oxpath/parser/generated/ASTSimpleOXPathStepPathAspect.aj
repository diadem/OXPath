/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTSimpleOXPathStepPath;
import uk.ac.ox.comlab.diadem.oxpath.core.PositionFuncEnum;
import uk.ac.ox.comlab.diadem.oxpath.model.language.Step;

public aspect ASTSimpleOXPathStepPathAspect {
	
	public void ASTSimpleOXPathStepPath.setStep(Step iStep) {
		this.step = iStep; 
	}

	public Step ASTSimpleOXPathStepPath.getStep() {
		return this.step;
	}
	
	public void ASTSimpleOXPathStepPath.setHasList(boolean iHasList) {
		this.hasList = iHasList;
	}
	
	public boolean ASTSimpleOXPathStepPath.hasList() {
		return this.hasList;
	}
	
//	public void ASTSimpleOXPathStepPath.setHasTail(boolean iHasTail) {
//		this.hasTail = iHasTail;
//	}
//	
//	public boolean ASTSimpleOXPathStepPath.hasTail() {
//		return this.hasTail;
//	}
	
	public void ASTSimpleOXPathStepPath.setSetBasedEval(PositionFuncEnum set) {
		this.setEval = set;
	}
	
	public PositionFuncEnum ASTSimpleOXPathStepPath.getSetBasedEval() {
		return this.setEval;
	}

	private Step ASTSimpleOXPathStepPath.step;
	
	private boolean ASTSimpleOXPathStepPath.hasList = false;
//	private boolean ASTSimpleOXPathStepPath.hasTail = false;
	private PositionFuncEnum ASTSimpleOXPathStepPath.setEval = PositionFuncEnum.NEITHER;
 
	public String ASTSimpleOXPathStepPath.toString() {
		return this.getClass().getSimpleName() + "[step=" + this.step.getAxis().getValue() + this.step.getNodeTest().getValue() + ",hasList=" + this.hasList + ",setEval=" + this.setEval + "]";
	}
	
}
