/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.core.PositionFuncEnum;
import uk.ac.ox.comlab.diadem.oxpath.model.language.SelectorPredicate;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathNodeTestOp;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTOXPathNodeTestOpAspect {

	public void ASTOXPathNodeTestOp.setSelectorPredicate(SelectorPredicate select) {
		this.selector = select;
	}

	public SelectorPredicate ASTOXPathNodeTestOp.getSelectorPredicate() {
		return this.selector;
	}

	public void ASTOXPathNodeTestOp.setHasList(boolean hl) {
		this.hasList = hl;
	}

	public boolean ASTOXPathNodeTestOp.hasList() {
		return this.hasList;
	}

	private SelectorPredicate ASTOXPathNodeTestOp.selector;
	private boolean ASTOXPathNodeTestOp.hasList;

	public void ASTOXPathNodeTestOp.setSetBasedEval(PositionFuncEnum set) {
		this.setEval = set;
	}

	public PositionFuncEnum ASTOXPathNodeTestOp.getSetBasedEval() {
		return this.setEval;
	}

	private PositionFuncEnum ASTOXPathNodeTestOp.setEval = PositionFuncEnum.NEITHER;
	
	public String ASTOXPathNodeTestOp.toString() {
		return this.getClass().getSimpleName() + "[selector=" + this.selector.getValue() + ",hasList=" + this.hasList + ",setEval=" + this.setEval +  "]";
	}

}
