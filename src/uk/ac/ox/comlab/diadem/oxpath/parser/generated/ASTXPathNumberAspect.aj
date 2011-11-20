/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathNumber;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTXPathNumberAspect {

	public void ASTXPathNumber.setValue(double iValue) {
		  this.value = iValue;
	  }
	  
	  public double ASTXPathNumber.getValue() {
		  return this.value;
	  }
	  
	  private double ASTXPathNumber.value;
	  
	  public String ASTXPathNumber.toString() {
		  return this.getClass().getSimpleName() + "[" + this.value + "]";
	  }
	
}
