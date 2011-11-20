/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathLiteral;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTXPathLiteralAspect {

	public void ASTXPathLiteral.setValue(String iValue) {
		this.value = iValue.substring(1, iValue.length()-1);//strip off quotes
	}

	public String ASTXPathLiteral.getValue() {
		return this.value;
	}

	private String ASTXPathLiteral.value;

	public String ASTXPathLiteral.toString() {
		return this.getClass().getSimpleName() + "[" + this.value +"]";
	}
}
