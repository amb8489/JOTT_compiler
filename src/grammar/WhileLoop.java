package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This class represents a while loop.
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
    public String insideOfFunction;

    /**
     * This is the constructor for the while loop.
     *
     * @param nestLevel how deep is this while loop
     * @param expr      the while expression
     * @param body      the code to run while the expression is true
     */
    public WhileLoop(int nestLevel, Expr expr, Body body, String insideOfFunction) {
        this.expr = expr;
        this.body = body;
        this.nestLevel = nestLevel;
        this.insideOfFunction = insideOfFunction;

    }

    /**
     * Parse the while loop.
     *
     * @param tokens    an array of tokens to parse
     * @param nestLevel how deep is this?
     * @return the parse result in the form of a WhileLoop object
     * @throws ParsingException TODO
     */
    public static WhileLoop parseWhile(ArrayList<Token> tokens, int nestLevel, String insideOfFunction)
            throws ParsingException {
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
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected [. Got: %s\n%s:%s",
                    L_BRACKET.getTokenType().toString(), L_BRACKET.getFilename(), L_BRACKET.getLineNum()));
        }

        TokenIndex.currentTokenIndex++;

        // checking for bool expr
        Expr expr = BExpr.parseBExpr(tokens, nestLevel, insideOfFunction);

        // checking for ]
        Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected ]. Got: %s\n%s:%s",
                    R_BRACKET.getTokenType().toString(), R_BRACKET.getFilename(), R_BRACKET.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // checking for {
        Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected {. Got: %s\n%s:%s",
                    L_BRACE.getTokenType().toString(), L_BRACE.getFilename(), L_BRACE.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // checking for body
        Body body1 = Body.ParseBody(tokens, nestLevel, insideOfFunction);

        // checking for }
        Token R_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected }. Got: %s\n%s:%s",
                    R_BRACE.getTokenType().toString(), R_BRACE.getFilename(), R_BRACE.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        return new WhileLoop(nestLevel, expr, body1, insideOfFunction);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        String space = "\t".repeat(this.nestLevel - 1);
        return String.format("%swhile [ %s ] { \n%s%s}\n",
                "\t".repeat(0), this.expr.convertToJott(), body.convertToJott(), space);
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        String space = "\t".repeat(this.nestLevel - 1);
        return String.format("%swhile ( %s ) { \n%s%s}\n",
                "\t".repeat(0), this.expr.convertToJava(), body.convertToJava(), space);
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

        String space = "\t".repeat(this.nestLevel - 1);
        return String.format("%swhile %s: \n%s%s\n",
                "\t".repeat(0), this.expr.convertToPython(), body.convertToPython(), space);


    }

    /**
     * Ensure the code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {

        expr.validateTree();
        body.validateTree();
        return false;

    }
}
