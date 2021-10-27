package grammar;

import main.Token;
import main.TokenType;
import java.util.ArrayList;

public class WhileLoop {
    private final Expr expression;
    private final Body body;
    private final int nestLevel;

    /**
     * TODO
     * @param nestLevel TODO
     * @param expr TODO
     * @param body TODO
     */
    public WhileLoop(int nestLevel, Expr expr, Body body) {
        this.expression = expr;
        this.body = body;
        this.nestLevel = nestLevel;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static WhileLoop parseWhile(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("---------------------------- PARSING while loop ----------------------------");

        // ---------------- checking while call starts with while----------------------

        Token whileToken = tokens.get(TOKEN_IDX.index);
        if (!whileToken.getToken().equals("while")) {
            return null;
        }
        System.out.println("\t1st:" + whileToken.getToken());
        TOKEN_IDX.index++;

        // ---------------------- checking for [ ----------------------------------

        Token L_BRACKET = tokens.get(TOKEN_IDX.index);
        System.out.println("\t2nd:"+L_BRACKET.getToken());
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET){
            String stringBuilder = "Syntax error\nInvalid token. Expected [. Got: " +
                    L_BRACKET.getTokenType().toString() + "\n" +
                    L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum();
            throw new ParsingException(stringBuilder);

        }
        System.out.println("\t3rd:"+L_BRACKET.getToken());
        TOKEN_IDX.index++;

        // ---------------------- checking for bool expr ------------------------------
        Expr expr = BExpr.parseBExpr(tokens,nestLevel);


        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.index);
        System.out.println("\t4th:"+R_BRACKET.getToken());

        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET){
            String stringBuilder = "Syntax error\nInvalid token. Expected ]. Got: " +
                    R_BRACKET.getTokenType().toString() + "\n" +
                    R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum();
            throw new ParsingException(stringBuilder);

        }
        TOKEN_IDX.index++;
        // ---------------------- checking for { ----------------------------------

        Token L_BRACE = tokens.get(TOKEN_IDX.index);
        System.out.println("\t5th:"+L_BRACE.getToken());
        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE){
            String stringBuilder = "Syntax error\nInvalid token. Expected {. Got: " +
                    L_BRACE.getTokenType().toString() + "\n" +
                    L_BRACE.getFilename() + ":" + L_BRACE.getLineNum();
            throw new ParsingException(stringBuilder);
        }
        TOKEN_IDX.index++;

        System.out.println("\t6th:"+L_BRACE.getToken());
        // ---------------------- checking for body -------------------------------

        Body body1 = Body.ParseBody(tokens, nestLevel);

        // ---------------------- checking for } ----------------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.index);
        System.out.println("\t7th:"+R_BRACE.getToken());
        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE){
            String stringBuilder = "Syntax error\nInvalid token. Expected }. Got: " +
                    R_BRACE.getTokenType().toString() + "\n" +
                    R_BRACE.getFilename() + ":" + R_BRACE.getLineNum();
            throw new ParsingException(stringBuilder);
        }
        TOKEN_IDX.index++;

        return new WhileLoop(nestLevel, expr, body1);
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        String SPACE = "\t".repeat(this.nestLevel -1);

        String jottString = "\t".repeat(0) +
                "while [ " +
                this.expression.convertToJott() + " ] { \n" +
                body.convertToJott() + SPACE + "}\n";
        return jottString;
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }

}
