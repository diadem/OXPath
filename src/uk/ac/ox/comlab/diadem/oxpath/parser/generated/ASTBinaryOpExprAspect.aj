/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;


import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTBinaryOpExpr;
import uk.ac.ox.comlab.diadem.oxpath.model.language.operators.BinaryOperator;

public aspect ASTBinaryOpExprAspect {

	public void ASTBinaryOpExpr.setBinaryOperator(BinaryOperator oper) {
		this.op = oper;
	}

	public BinaryOperator ASTBinaryOpExpr.getBinaryOperator() {
		return this.op;
	}

	private BinaryOperator ASTBinaryOpExpr.op;	

   public String ASTBinaryOpExpr.toString() {
	   return this.getClass().getSimpleName()+ "[" + this.getBinaryOperator().getOperator() + "]";
   }
}
