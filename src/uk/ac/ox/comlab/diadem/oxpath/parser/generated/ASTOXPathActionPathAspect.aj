/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathActionPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.Node;
import uk.ac.ox.comlab.diadem.oxpath.model.language.Action;

public aspect ASTOXPathActionPathAspect {

	public void ASTOXPathActionPath.setAction(Action act) {
		this.action=act;
	}
	
	public Action ASTOXPathActionPath.getAction() {
		return this.action;
	}
	
	public void ASTOXPathActionPath.setHasTail(boolean ht) {
		this.hasTail=ht;
	}
	
	public boolean ASTOXPathActionPath.hasTail() {
		return this.hasTail;
	}
	
	public void ASTOXPathActionPath.setInsideKleeneStar(Node kleene) {
		this.insideKleene = kleene;
	}
	
	public boolean ASTOXPathActionPath.isInsideKleeneStar() {
		return this.insideKleene!=null;
	}
	
	/**
	 * Returns the outermost Kleene star node (for closing actions)
	 * Check for {@code null} values when using this or call {@code node.isInsideKleeneStar()}
	 * @return the outermost Kleene star node (for closing actions)
	 */
	public Node ASTOXPathActionPath.insideKleeneStar() {
		return this.insideKleene;
	}
	
	private Action ASTOXPathActionPath.action;
	private boolean ASTOXPathActionPath.hasTail = false;
	private Node ASTOXPathActionPath.insideKleene = null;
	
	public String ASTOXPathActionPath.toString() {
		return this.getClass().getSimpleName() + "[action:" + this.getAction().getValue() + ", type=" + this.getAction().getActionType() + "," + "hasTail=" + this.hasTail() + ", insideKleene=" + ((this.insideKleene!=null)?this.insideKleene.toString():"null") + "]";
	}
}
