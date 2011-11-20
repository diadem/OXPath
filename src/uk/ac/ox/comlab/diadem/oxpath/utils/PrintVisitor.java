/**
 * Supporting Java classes for OXPath
 */
package uk.ac.ox.comlab.diadem.oxpath.utils;

import uk.ac.ox.comlab.diadem.oxpath.model.language.ActionType;
import uk.ac.ox.comlab.diadem.oxpath.parser.OXPathParser;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTBinaryOpExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPrimaryExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.SimpleNode;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathActionPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathExtractionMarker;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathKleeneStarPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathNodeTestOp;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTRelativeOXPathLocationPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTExpression;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTSimpleOXPathStepPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathFunctionCall;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathLiteral;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathNumber;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPathExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPredicate;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathUnaryExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.visitor.OXPathVisitorGenericAdaptor;

/**
 * Simple visitor implementation to print out OXPath expressions into {@code String} objects that are encoded as
 * AST trees.
 * @author AndrewJSel
 *
 */
public class PrintVisitor extends OXPathVisitorGenericAdaptor<Object, String> {

	/**
	 * Empty constructor
	 */
	public PrintVisitor() {

	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(SimpleNode node, Object data) throws OXPathException {
		//This mehtod should never happen
		System.out.println("Unexpected Node type encountered.  Program exiting...");
		System.exit(-1);
		return "";//for compiler only
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTExpression node, Object data) throws OXPathException {
		return this.accept(node.jjtGetChild(0), null);
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTRelativeOXPathLocationPath node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		if (node.isAbsolutePath()) sb.append(SLASH);
		sb.append(this.accept(node.jjtGetChild(0),null));		
		if (node.hasSimplePath() && node.hasComplexPath()) sb.append(SLASH + this.accept(node.jjtGetChild(1),null));
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTSimpleOXPathStepPath node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		sb.append(node.getStep().getAxis().getValue() + node.getStep().getNodeTest().getValue());
		if (node.hasList()) {
			if (OXPathParser.hasChildByName(node, "SimpleOXPathStepPath")) sb.append(SLASH); //compositional break - just so we can make the slash if necessary
			sb.append(this.accept(node.jjtGetChild(0), null));
		}
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTOXPathKleeneStarPath node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();

		sb.append("(");
		sb.append(this.accept(node.jjtGetChild(0),null));
		sb.append(")*");


		if (node.hasLowerBound()) {
			sb.append("{");
			sb.append(node.getLowerBound());
			if (node.hasUpperBound()) {
				sb.append(",");
				sb.append(node.getUpperBound());
			}
			sb.append("}");
		}

		if (node.hasFollowingPath()) {
			sb.append(SLASH + this.accept(node.jjtGetChild(1), null));
		}

		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTOXPathActionPath node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		switch (node.getAction().getActionType()) {
		case URL:
			sb.append("doc(\"");
			sb.append(node.getAction().getValue());
			sb.append("\"");
			if (node.getAction().hasWait()) sb.append(",[" + node.getAction().getWait() + "]");
			sb.append(")");
			break;
		case EXPLICIT:
		case KEYWORD:
		case POSITION:
		case VARIABLE:
			sb.append("{");
			if (node.getAction().getActionType().equals(ActionType.EXPLICIT)) sb.append("\"");
			sb.append(node.getAction().getValue());
			if (node.getAction().getActionType().equals(ActionType.EXPLICIT)) sb.append("\"");
			if (node.getAction().hasWait()) sb.append("[" + node.getAction().getWait() + "]");
			if (node.getAction().isAbsoluteAction()) sb.append(SLASH);
			sb.append("}");
			break;
		}
		if (node.hasTail()) sb.append(SLASH + this.accept(node.jjtGetChild(0), null));
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTOXPathNodeTestOp node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		sb.append(node.getSelectorPredicate().getValue());
		if (node.hasList()) {
			if (OXPathParser.hasChildByName(node, "SimpleOXPathStepPath")) sb.append(SLASH); //compositional break - just so we can make the slash if necessary
			sb.append(this.accept(node.jjtGetChild(0), null));
		}
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathLiteral node, Object data) throws OXPathException {
		return "'" + node.getValue() + "'";
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathPredicate node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		sb.append(((node.isOptional())?"[?":"[") + this.accept(node.jjtGetChild(0), null) + "]");
		if (node.hasList()) {
			if (OXPathParser.hasChildByName(node, "SimpleOXPathStepPath")) sb.append(SLASH); //compositional break - just so we can make the slash if necessary
			sb.append(this.accept(node.jjtGetChild(1), null));
		}
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTOXPathExtractionMarker node, Object data) throws OXPathException {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(":<");
		sb.append(node.getExtractionMarker().getLabel());
		if (node.getExtractionMarker().isAttribute()) {
			sb.append("=");
			sb.append(this.accept(node.jjtGetChild(i++), null));
		}
		sb.append(">");
		if (node.hasList()) {
			if (OXPathParser.hasChildByName(node, "SimpleOXPathStepPath")) sb.append(SLASH); //compositional break - just so we can make the slash if necessary
			sb.append(this.accept(node.jjtGetChild(i), null));
		}
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTBinaryOpExpr node, Object data) throws OXPathException {
		return this.accept(node.jjtGetChild(0),null) + node.getBinaryOperator().getOperator() + this.accept(node.jjtGetChild(1),null);
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathUnaryExpr node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < node.getNumberOperators(); i++) {
			sb.append(node.getUnaryOperator().getOperator());
		}
		sb.append(this.accept(node.jjtGetChild(0),null));//because we count the number of operators, but the index starts at 0
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathPrimaryExpr node, Object data) throws OXPathException {
		String result = this.accept(node.jjtGetChild(0),null);
		return "(" + result + ")";
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathNumber node, Object data) throws OXPathException {
		return Double.toString(node.getValue());
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathFunctionCall node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		sb.append(node.getFunction().getName());
		sb.append("(");
		if (node.getNumParameters() > 0) {//only if we have parameters
			for (int i = 0; i < node.getNumParameters() - 1; i++) {
				sb.append(this.accept(node.jjtGetChild(i), null));
				sb.append(",");
			}
			sb.append(this.accept(node.jjtGetChild(node.getNumParameters()-1), null));
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Constructs the {@code String} representation of the subtree rooted at {@code node}
	 * @param node the subtree root the visitor prints
	 * @param data <i>not used in this visitor</i>
	 * @return the {@code String} representation of the expression subtree as determined by the visitor
	 */
	@Override
	public String visitNode(ASTXPathPathExpr node, Object data) throws OXPathException {
		StringBuilder sb = new StringBuilder();
		sb.append(this.accept(node.jjtGetChild(0),null));
		sb.append(this.accept(node.jjtGetChild(1),null));
		if (node.hasComplexList() && node.hasSimpleList()) sb.append(this.accept(node.jjtGetChild(2),null));		
		return sb.toString();
	}

	public static final String SLASH = "/";
}
