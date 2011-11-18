/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.model.language.functions.XPathFunction;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathFunctionCall;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTXPathFunctionCallAspect {

	public void ASTXPathFunctionCall.setFunction(XPathFunction iFunction) {
		  this.function = iFunction;
	  }
	  
	  public XPathFunction ASTXPathFunctionCall.getFunction() {
		  return this.function;
	  }
	  
	  public void ASTXPathFunctionCall.addParameter() {
		  ++numParameters;
	  }
	  
	  public int ASTXPathFunctionCall.getNumParameters() {
		  return this.numParameters;
	  }
	  
	  private XPathFunction ASTXPathFunctionCall.function;
	  private int ASTXPathFunctionCall.numParameters = 0;
	
	  public String ASTXPathFunctionCall.toString() {
		  return this.getClass().getSimpleName() + "[" + this.function.getName() + ",numParam=" + this.getNumParameters() + "]";
	  }
	  
}
