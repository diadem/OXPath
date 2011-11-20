/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.model.language.operators.NegativeOperator;
import uk.ac.ox.comlab.diadem.oxpath.model.language.operators.UnaryOperator;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathUnaryExpr;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTXPathUnaryExprAspect {

	public void ASTXPathUnaryExpr.incrementOperatorNumber() {
		  ++this.numOps;
	  }
	  
	  public int ASTXPathUnaryExpr.getNumberOperators() {
		  return this.numOps;
	  }
	  
	  public UnaryOperator ASTXPathUnaryExpr.getUnaryOperator() {
		  return NegativeOperator.NEGATIVE;
	  }
	  
	  private int ASTXPathUnaryExpr.numOps = 0;
	  
	  public String ASTXPathUnaryExpr.toString() {
		  return this.getClass().getSimpleName() + "[numOps=" + this.numOps + "]";
	  }
	
}