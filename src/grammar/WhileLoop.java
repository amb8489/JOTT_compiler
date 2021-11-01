package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class WhileLoop {
    private final Expr expr;
    private final Body body;
    private final int nestLevel;

    /**
     * TODO
     *
     * @param nestLevel TODO
     * @param expr      TODO
     * @param body      TODO
     */
    public WhileLoop(int nestLevel, Expr expr, Body body) {
        this.expr = expr;
        this.body = body;
        this.nestLevel = nestLevel;
    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static WhileLoop parseWhile(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        // checking while call starts with while
        Token whileToken = tokens.get(TokenIndex.currentTokenIndex);
        if (!whileToken.getToken().equals("while")) {
            return null;
        }

        TokenIndex.currentTokenIndex++;

        // checking for [
        Token L_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            String stringBuilder = "Syntax error\nInvalid token. Expected [. Got: " +
                    L_BRACKET.getTokenType().toString() + "\n" +
                    L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum();
            throw new ParsingException(stringBuilder);

        }

        TokenIndex.currentTokenIndex++;

        // checking for bool expr
        Expr expr = BExpr.parseBExpr(tokens, nestLevel);

        // checking for ]
        Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            String stringBuilder = "Syntax error\nInvalid token. Expected ]. Got: " +
                    R_BRACKET.getTokenType().toString() + "\n" +
                    R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum();
            throw new ParsingException(stringBuilder);

        }
        TokenIndex.currentTokenIndex++;

        // checking for {
        Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            String stringBuilder = "Syntax error\nInvalid token. Expected {. Got: " +
                    L_BRACE.getTokenType().toString() + "\n" +
                    L_BRACE.getFilename() + ":" + L_BRACE.getLineNum();
            throw new ParsingException(stringBuilder);
        }
        TokenIndex.currentTokenIndex++;

        // checking for body
        Body body1 = Body.ParseBody(tokens, nestLevel);

        // checking for }
        Token R_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            String stringBuilder = "Syntax error\nInvalid token. Expected }. Got: " +
                    R_BRACE.getTokenType().toString() + "\n" +
                    R_BRACE.getFilename() + ":" + R_BRACE.getLineNum();
            throw new ParsingException(stringBuilder);
        }
        TokenIndex.currentTokenIndex++;

        return new WhileLoop(nestLevel, expr, body1);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        String space = "\t".repeat(this.nestLevel - 1);

        return "\t".repeat(0) +
                "while [ " +
                this.expr.convertToJott() + " ] { \n" +
                body.convertToJott() + space + "}\n";
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
