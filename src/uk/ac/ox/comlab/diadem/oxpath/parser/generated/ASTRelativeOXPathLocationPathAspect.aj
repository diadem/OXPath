/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTRelativeOXPathLocationPath;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTRelativeOXPathLocationPathAspect {

	/**
	   * Sets if the expression has a simple path
	   * @param hasPath {@code true} for a simple path, {@code false} otherwise
	   */
	  public void ASTRelativeOXPathLocationPath.setHasSimplePath(boolean hasPath) {
		  this.hasSimplePath = hasPath;
	  }
	  
	  /**
	   * Returns if the expression has a simple path
	   * @return {@code true} for a simple path, {@code false} otherwise
	   */
	  public boolean ASTRelativeOXPathLocationPath.hasSimplePath() {
		  return this.hasSimplePath;
	  }
	  
	  /**
	   * Sets if the expression has a complex path
	   * @param hasPath {@code true} for a complex path, {@code false} otherwise
	   */
	  public void ASTRelativeOXPathLocationPath.setHasComplexPath(boolean hasPath) {
		  this.hasComplexPath = hasPath;
	  }
	  
	  /**
	   * Returns if the expression has a complex path
	   * @return {@code true} for a complex path, {@code false} otherwise
	   */
	  public boolean ASTRelativeOXPathLocationPath.hasComplexPath() {
		  return this.hasComplexPath;
	  }
	  
	  /**
	   * Sets if the expression is an absolute path
	   * @param absPath {@code true} is an absolute path, {@code false} otherwise
	   */
	  public void ASTRelativeOXPathLocationPath.setIsAbsolutePath(boolean absPath) {
		  this.isAbsolutePath = absPath;
	  }
	  
	  /**
	   * Returns if the expression is an absolute path
	   * @return {@code true} is an absolute path, {@code false} otherwise
	   */
	  public boolean ASTRelativeOXPathLocationPath.isAbsolutePath() {
		  return this.isAbsolutePath;
	  }
	  
	  /**
	   * instance field encoding if a simple path is part of this node
	   */
	  private boolean ASTRelativeOXPathLocationPath.hasSimplePath = false;
	  /**
	   * instance field encoding if a complex path is part of this node
	   */
	  private boolean ASTRelativeOXPathLocationPath.hasComplexPath = false;
	  /**
	   * instance field encoding if the path is absolute
	   */
	  private boolean ASTRelativeOXPathLocationPath.isAbsolutePath = false;
	  
	  public String ASTRelativeOXPathLocationPath.toString() {
		  return this.getClass().getSimpleName() + "[hasSimplePath=" +this.hasSimplePath + ",hasComplexPath=" + this.hasComplexPath + ",isAbsolutePath=" + this.isAbsolutePath + "]";
	  }
	  
	  
	
}
