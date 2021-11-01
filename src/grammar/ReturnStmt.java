package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//     return_stmt -> return expr end_stmt

/**
 * ReturnStmt holds a return statement.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ReturnStmt {
    public Expr expr;

    /**
     * This is a constructor for a return expression.
     *
     * @param expression an expression to be evaluated for return
     */
    public ReturnStmt(Expr expression) {
        this.expr = expression;
    }

    public String convertToJott() {
        return "\t".repeat(0) + "return " + expr.convertToJott() + ";\n";
    }

    public static ReturnStmt parseReturnStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        // check for return
        Token returnToken = tokens.get(TokenIndex.currentTokenIndex);
        if (!returnToken.getToken().equals("return")) {
            return null;
        }
        TokenIndex.currentTokenIndex++;

        // checking for expression
        Expr expr = Expr.parseExpr(tokens, nestLevel);

        //check for ;
        Token endStmt = tokens.get(TokenIndex.currentTokenIndex);

        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            return null;
        }
        TokenIndex.currentTokenIndex++;

        // done
        return new ReturnStmt(expr);

    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return null;
    }

    /**
     * Ensure the code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        return false;
    }

}
