package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Else if statement is a continuation of the larger if statement.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ElseifStmt {
    public final Expr expr;
    public final Body body;
    private final int nestLevel;
    public String insideOfFunction;

    /**
     * Constructor
     *
     * @param expr      TODO
     * @param body      TODO
     * @param nestLevel TODO
     */
    public ElseifStmt(Expr expr, Body body, int nestLevel) {
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
    public static ArrayList<ElseifStmt> ParseElsif_lst(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        // list of all else if we will encounter if any
        ArrayList<ElseifStmt> elseIfList = new ArrayList<>();

        // looking if there is an "elseif"
        Token elseif = tokens.get(TokenIndex.currentTokenIndex);

        // if no elseif then : ε case and return, this is not an error
        if (!elseif.getToken().equals("elseif")) {
            return null;
        }

        // while we did encounter an elseif in the tokens we will keep serching for elseif
        while (elseif.getToken().equals("elseif")) {
            // removing elseif
            TokenIndex.currentTokenIndex++;

            // looking for [
            Token L_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

            // check for if
            if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
                String string = "Syntax error\nInvalid token. Expected [. Got: " +
                        L_BRACKET.getTokenType().toString() + "\n" +
                        L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum();
                throw new ParsingException(string);
            }

            TokenIndex.currentTokenIndex++;

            // looking for bool expr
            Expr expr = Expr.parseExpr(tokens, nestLevel);

            // looking for ]
            Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);
            //System.out.println("    4th:" + R_BRACKET.getToken());
            // check for if
            if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
                String string = "Syntax error\nInvalid token. Expected ]. Got: " +
                        R_BRACKET.getTokenType().toString() + "\n" +
                        R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum();
                throw new ParsingException(string);
            }
            TokenIndex.currentTokenIndex++;

            // looking for {
            Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

            // check for if
            if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
                String string = "Syntax error\nInvalid token. Expected {. Got: " +
                        L_BRACE.getTokenType().toString() + "\n" +
                        L_BRACE.getFilename() + ":" + L_BRACE.getLineNum();
                throw new ParsingException(string);
            }
            TokenIndex.currentTokenIndex++;

            // looking for a body
            Body body = Body.ParseBody(tokens, nestLevel);

            // looking for a }
            Token R_BRACE = tokens.get(TokenIndex.currentTokenIndex);

            // check for if
            if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
                String string = "Syntax error\nInvalid token. Expected }. Got: " +
                        R_BRACE.getTokenType().toString() + "\n" +
                        R_BRACE.getFilename() + ":" + R_BRACE.getLineNum();
                throw new ParsingException(string);
            }

            TokenIndex.currentTokenIndex++;

            // adding what was found to the list of seen else if statements
            elseIfList.add(new ElseifStmt(expr, body, nestLevel));

            // looking for an else if statement
            elseif = tokens.get(TokenIndex.currentTokenIndex);
        }

        // finished with finding else ifs
        return elseIfList;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        String SPACE = "    ".repeat(this.nestLevel - 1);

        return "elseif[ " +
                expr.convertToJott() + "]{ \n" +
                body.convertToJott() + SPACE + "}";
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToJava() {
        return null;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToC() {
        return null;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToPython() {
        return null;
    }

    /**
     * Ensure the code in the else if statement is valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        return false;
    }
}
