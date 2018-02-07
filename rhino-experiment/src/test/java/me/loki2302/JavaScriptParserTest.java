package me.loki2302;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.ReturnStatement;

public class JavaScriptParserTest {
    @Test
    public void test() {
        AstRoot astRoot = new Parser().parse("function add(x,y){return x+y;}", "1", 1);
        
        List<AstNode> statementNodes = astRoot.getStatements();
        assertEquals(1, statementNodes.size());
        
        AstNode firstStatementNode = statementNodes.get(0);
        assertEquals(FunctionNode.class, firstStatementNode.getClass());
        
        FunctionNode functionNode = (FunctionNode)firstStatementNode;
        assertEquals("add", functionNode.getName());
        
        List<AstNode> parameters = functionNode.getParams();
        assertEquals(2, parameters.size());
        
        AstNode parameter1Node = parameters.get(0);
        assertEquals(Name.class, parameter1Node.getClass());        
        Name name1Node = (Name)parameter1Node;
        assertEquals("x", name1Node.getIdentifier());
        
        AstNode parameter2Node = parameters.get(1);
        assertEquals(Name.class, parameter2Node.getClass());        
        Name name2Node = (Name)parameter2Node;
        assertEquals("y", name2Node.getIdentifier());
        
        AstNode bodyNode = functionNode.getBody();
        assertEquals(Block.class, bodyNode.getClass());
        Block blockNode = (Block)bodyNode;
        
        AstNode firstChild = (AstNode)blockNode.getFirstChild();
        assertEquals(ReturnStatement.class, firstChild.getClass());
        
        ReturnStatement returnStatement = (ReturnStatement)firstChild;
        AstNode returnValue = returnStatement.getReturnValue();
        assertEquals(InfixExpression.class, returnValue.getClass());
        
        InfixExpression infixExpression = (InfixExpression)returnValue;
        assertEquals(Token.ADD, infixExpression.getOperator());
        
        AstNode left = infixExpression.getLeft();
        assertEquals(Name.class, left.getClass());        
        Name leftName = (Name)left;
        assertEquals("x", leftName.getIdentifier());
        
        AstNode right = infixExpression.getRight();
        assertEquals(Name.class, right.getClass());        
        Name rightName = (Name)right;
        assertEquals("y", rightName.getIdentifier());
    }
}
