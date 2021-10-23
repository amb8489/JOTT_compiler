package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//     return_stmt -> return expr end_stmt
public class ReturnStmt  {
    private final Expr exp;

    public ReturnStmt(int nestLevel, Expr exp) {
        this.exp = exp;
    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        jstr.append("     ".repeat(0));
        jstr.append("return ");
        jstr.append(exp.convertToJott() + ";\n");
        return jstr.toString();
    }


    public static ReturnStmt parseReturnStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Return Stmt------------------------");


        // ---------------------- check for return ------------------------------------
        Token returnToken = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    FIRST:" + returnToken.getToken());
        if (!returnToken.getToken().equals("return")) {
            return null;
        }
        TOKEN_IDX.IDX++;


        // ---------------------- check for expr ------------------------------------
        // checking for expression
        System.out.println("    LOOKING FOR EXPR");
        Expr expr = Expr.parseExpr(tokens, nestLevel);


        // ---------------------- check for end statment ------------------------------------

        //check for ;
        Token endStmt = tokens.get(TOKEN_IDX.IDX);

        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            return null;
        }
        TOKEN_IDX.IDX++;

        // ---------------------- DONE ------------------------------------

        System.out.println("return found: return " + expr.convertToJott());
        return new ReturnStmt(nestLevel, expr);

    }

}
