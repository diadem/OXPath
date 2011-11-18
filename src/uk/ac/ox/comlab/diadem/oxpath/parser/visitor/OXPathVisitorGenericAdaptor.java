/**
 * Package containing a generic adapter for OXPath computation
 */
package uk.ac.ox.comlab.diadem.oxpath.parser.visitor;

import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTExpression;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.Node;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.OXPathParserVisitor;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.SimpleNode;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathActionPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathExtractionMarker;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathKleeneStarPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTOXPathNodeTestOp;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTRelativeOXPathLocationPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTSimpleOXPathStepPath;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathFunctionCall;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathLiteral;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathNumber;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPathExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPredicate;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTBinaryOpExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathUnaryExpr;
import uk.ac.ox.comlab.diadem.oxpath.parser.ast.ASTXPathPrimaryExpr;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * 
 * Wrapper allowing generic specification of {@code Visitor} classes for the OXPath AST.  Used to 
 * General contract for this class is that all accept calls pass {@code data} of type {@code T} and 
 * return output of type {@code U}.  This is not safe; if the contract is broken, the class will break hard 
 * with a {@code CastClassException}.  Use only for convenience and test thoroughly.  Implement the {@code visitNode}
 * methods and call {@code this.accept(node,data)} instead of {@code node.jjtAccept(visitor,data)} in order to avoid 
 * casting in visitor implementations.  
 *  
 * @author AndrewJSel
 *
 */
public abstract class OXPathVisitorGenericAdaptor<T,U> implements OXPathParserVisitor {

	public abstract U visitNode(SimpleNode node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTExpression node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTRelativeOXPathLocationPath node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTSimpleOXPathStepPath node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTOXPathKleeneStarPath node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTOXPathActionPath node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTOXPathNodeTestOp node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathLiteral node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathPredicate node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTOXPathExtractionMarker node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTBinaryOpExpr node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathUnaryExpr node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathPrimaryExpr node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathNumber node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathFunctionCall node, T data) throws OXPathException;
	
	public abstract U visitNode(ASTXPathPathExpr node, T data) throws OXPathException;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(SimpleNode node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTExpression node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTRelativeOXPathLocationPath node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTSimpleOXPathStepPath node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTOXPathKleeneStarPath node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTOXPathActionPath node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTOXPathNodeTestOp node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathLiteral node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathPredicate node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTOXPathExtractionMarker node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTBinaryOpExpr node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathUnaryExpr node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathPrimaryExpr node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathNumber node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathFunctionCall node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTXPathPathExpr node, Object data) throws OXPathException {
		return (U) visitNode(node, (T) data);
	}
	
	@SuppressWarnings("unchecked")
	public U accept(Node node, T data) throws OXPathException {
		return (U) node.jjtAccept(this, data);
	}
}
