package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


//while_loop -> while [ b_expr ] { body }

public class WhileLoop {
    private Expr exp;
    private Body body1;
    private int nestlevel;


    public WhileLoop(int nestLevel, Expr expr, Body body1,int nestlevel) {
        this.exp = expr;
        this.body1 = body1;
        this.nestlevel = nestlevel;
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
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(L_BRACKET.getTokenType().toString()).append("\n");
            sb.append(L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());

        }
        System.out.println("    3rd:"+L_BRACKET.getToken());
        TOKEN_IDX.IDX++;

        // ---------------------- checking for bool expr ------------------------------
        Expr expr = BExpr.parseBExpr(tokens,nestLevel);


        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    4th:"+R_BRACKET.getToken());
        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected ]. Got: ");
            sb.append(R_BRACKET.getTokenType().toString()).append("\n");
            sb.append(R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());

        }
        TOKEN_IDX.IDX++;
        // ---------------------- checking for { ----------------------------------

        Token L_BRACE = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    5th:"+L_BRACE.getToken());
        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected {. Got: ");
            sb.append(L_BRACE.getTokenType().toString()).append("\n");
            sb.append(L_BRACE.getFilename() + ":" + L_BRACE.getLineNum());
            throw new ParsingException(sb.toString());
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
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected }. Got: ");
            sb.append(R_BRACE.getTokenType().toString()).append("\n");
            sb.append(R_BRACE.getFilename() + ":" + R_BRACE.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;

        return new WhileLoop(nestLevel, expr, body1,nestLevel);
    }

    public String convertToJott() {
        String SPACE = "    ".repeat(this.nestlevel-1);

        StringBuilder jstr = new StringBuilder();
        jstr.append("     ".repeat(0));
        jstr.append("while [ ");
        jstr.append(this.exp.convertToJott() + " ] { \n");
        jstr.append(body1.convertToJott() + SPACE+"}\n");
        return jstr.toString();
    }
    public boolean validateTree() {
        return false;
    }

}
