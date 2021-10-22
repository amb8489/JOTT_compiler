package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


//while_loop -> while [ b_expr ] { body }

public class WhileLoop {
    private Expr exp;
    private Body body1;


    public WhileLoop(int nestLevel, Expr expr, Body body1) {
        this.exp = expr;
        this.body1 = body1;
    }


    public static WhileLoop parseWhile(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("---------------------------- PARSING while loop ----------------------------");

        // ---------------- checking while call starts with while----------------------

        Token whileToken = tokens.get(TOKEN_IDX.IDX);
        if (! whileToken.getToken().equals("while")) {
            return null;
        }
        System.out.println("    1st:" + whileToken.getToken());
        TOKEN_IDX.IDX++;

        // ---------------------- checking for [ ----------------------------------

        Token L_BRACKET = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    2nd:"+L_BRACKET.getToken());
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET){
            return null;

        }
        System.out.println("    3rd:"+L_BRACKET.getToken());
        TOKEN_IDX.IDX++;

        // ---------------------- checking for bool expr ------------------------------
        Expr expr = Expr.parseExpr(tokens,nestLevel);


        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    4th:"+R_BRACKET.getToken());
        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET){
            return null;

        }
        TOKEN_IDX.IDX++;
        // ---------------------- checking for { ----------------------------------

        Token L_BRACE = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    5th:"+L_BRACE.getToken());
        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE){
            return null;
        }
        TOKEN_IDX.IDX++;

        System.out.println("    6th:"+L_BRACE.getToken());
        // ---------------------- checking for body -------------------------------

        Body body1 = Body.ParseBody(tokens, nestLevel);

        // ---------------------- checking for } ----------------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    7th:"+R_BRACE.getToken());
        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE){
            return null;
        }
        TOKEN_IDX.IDX++;

        return new WhileLoop(nestLevel, expr, body1);
    }

    public String convertToJott() {

        return "while covert to jott not done";
    }

}
