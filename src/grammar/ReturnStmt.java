package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//     return_stmt -> return expr end_stmt
public class ReturnStmt  {
    private final Expr expression;

    /**
     * Constructor TODO
     * @param nestLevel TODO
     * @param expression TODO
     */
    public ReturnStmt(int nestLevel, Expr expression) {
        this.expression = expression;
    }

    public String convertToJott() {
        String jottString = "\t".repeat(0) +
                "return " +
                expression.convertToJott() + ";\n";
        return jottString;
    }

    public static ReturnStmt parseReturnStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Return Stmt------------------------");

        // ---------------------- check for return ------------------------------------
        Token returnToken = tokens.get(TOKEN_IDX.index);
        System.out.println("\tFIRST:" + returnToken.getToken());
        if (!returnToken.getToken().equals("return")) {
            return null;
        }
        TOKEN_IDX.index++;

        // ---------------------- check for expr ------------------------------------
        // checking for expression
        System.out.println("\tLOOKING FOR EXPR");
        Expr expr = Expr.parseExpr(tokens, nestLevel);

        // ---------------------- check for end statment ------------------------------------

        //check for ;
        Token endStmt = tokens.get(TOKEN_IDX.index);

        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            return null;
        }
        TOKEN_IDX.index++;

        // ---------------------- DONE ------------------------------------

        System.out.println("return found: return " + expr.convertToJott());
        return new ReturnStmt(nestLevel, expr);

    }
    public boolean validateTree() {
        return false;
    }

}
