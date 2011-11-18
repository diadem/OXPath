/**
 * Package containing the JJTree OXPath parser specification.
 * In addition, package contains aspects associated with AST nodes.
 * These utilize crosscuts so generated code is not modified. 
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.generated;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathKleeneStarPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.Node;

/**
 * @author AndrewJSel
 *
 */
public aspect ASTOXPathKleeneStarPathAspect {

	public void ASTOXPathKleeneStarPath.setHasLowerBound(boolean hasLower) {
		this.hasLowerBound = hasLower;
	}

	public void ASTOXPathKleeneStarPath.setHasUpperBound(boolean hasUpper) {
		this.hasUpperBound = hasUpper;
	}

	public void ASTOXPathKleeneStarPath.setLowerBound(int lower) {
		this.lowerBound = lower;
	}

	public void ASTOXPathKleeneStarPath.setUpperBound(int upper) {
		this.upperBound = upper;
	}

	public void ASTOXPathKleeneStarPath.setHasFollowingPath(boolean hasfollowing) {
		this.hasFollowingPath = hasfollowing;
	}

	public boolean ASTOXPathKleeneStarPath.hasLowerBound() {
		return this.hasLowerBound;
	}

	public boolean ASTOXPathKleeneStarPath.hasUpperBound() {
		return this.hasUpperBound;
	}

	public int ASTOXPathKleeneStarPath.getLowerBound() {
		return this.lowerBound;
	}

	public int ASTOXPathKleeneStarPath.getUpperBound() {
		return this.upperBound;
	}

	public boolean ASTOXPathKleeneStarPath.hasFollowingPath() {
		return this.hasFollowingPath;
	}
	
	public void ASTOXPathKleeneStarPath.setInsideKleeneStar(Node kleene) {
		this.insideKleene = kleene;
	}
	
	public boolean ASTOXPathKleeneStarPath.isInsideKleeneStar() {
		return this.insideKleene!=null;
	}
	
	/**
	 * Returns the outermost Kleene star node (for closing actions)
	 * Check for {@code null} values when using this or call {@code node.isInsideKleeneStar()}
	 * @return the outermost Kleene star node (for closing actions)
	 */
	public Node ASTOXPathKleeneStarPath.insideKleeneStar() {
		return this.insideKleene;
	}

	private boolean ASTOXPathKleeneStarPath.hasLowerBound = false;
	private boolean ASTOXPathKleeneStarPath.hasUpperBound = false;
	private int ASTOXPathKleeneStarPath.lowerBound = 0;
	private int ASTOXPathKleeneStarPath.upperBound = Integer.MAX_VALUE;
	private boolean ASTOXPathKleeneStarPath.hasFollowingPath = false;
	private Node ASTOXPathKleeneStarPath.insideKleene = null;


	public String ASTOXPathKleeneStarPath.toString() {
		return this.getClass().getSimpleName() + "[" + "Lower=" + this.lowerBound  + ",Upper=" + this.upperBound + ",hasFollowingPath=" + this.hasFollowingPath + ",outerKleene=" + ((this.isInsideKleeneStar())?this.insideKleene.toString():"null") + "]";
	}
	
}
