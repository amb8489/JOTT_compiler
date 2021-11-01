package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//     return_stmt -> return expr end_stmt

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ReturnStmt {
    public Expr expr;

    /**
     * Constructor TODO
     *
     * @param expression TODO
     */
    public ReturnStmt(Expr expression) {
        this.expr = expression;
    }

    public String convertToJott() {
        String jottString = "\t".repeat(0) + "return " + expr.convertToJott() + ";\n";
        return jottString;
    }

    public static ReturnStmt parseReturnStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("------------------------PARSING Return Stmt------------------------");

        // ---------------------- check for return ------------------------------------
        Token returnToken = tokens.get(TOKEN_IDX.index);
        //System.out.println("\tFIRST:" + returnToken.getToken());
        if (!returnToken.getToken().equals("return")) {
            return null;
        }
        TOKEN_IDX.index++;

        // ---------------------- check for expr ------------------------------------
        // checking for expression
        //System.out.println("\tLOOKING FOR EXPR");

        Expr expr = Expr.parseExpr(tokens, nestLevel);
        // ---------------------- check for end statment ------------------------------------

        //check for ;
        Token endStmt = tokens.get(TOKEN_IDX.index);

        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            return null;
        }
        TOKEN_IDX.index++;

        // ---------------------- DONE ------------------------------------

        //System.out.println("return found: return " + expr.convertToJott());
        return new ReturnStmt(expr);

    }

    public boolean validateTree() {
        return false;
    }

}
