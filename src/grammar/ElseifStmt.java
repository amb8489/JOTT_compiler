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
    public String scope;

    /**
     * This is a constructor for an else if statement.
     *
     * @param expr      an expression to be evaluated
     * @param body      the body for this else if statement
     * @param nestLevel how deep is this else if
     */
    public ElseifStmt(Expr expr, Body body, int nestLevel, String scope) {
        this.expr = expr;
        this.body = body;
        this.nestLevel = nestLevel;
        this.scope = scope;

    }

    /**
     * TODO
     *
     * @param tokens    a lisdhes=
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static ArrayList<ElseifStmt> ParseElsif_lst(ArrayList<Token> tokens, int nestLevel, String scope)
            throws ParsingException {
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
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected [. Got:%s\n%s:%s",
                        L_BRACKET.getTokenType().toString(), L_BRACKET.getFilename(), L_BRACKET.getLineNum()));
            }

            TokenIndex.currentTokenIndex++;

            // looking for bool expr
            Expr expr = BExpr.parseBExpr(tokens, nestLevel, scope);

            // looking for ]
            Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);
            //System.out.println("    4th:" + R_BRACKET.getToken());
            // check for if
            if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected ]. Got:%s\n%s:%s",
                        R_BRACKET.getTokenType().toString(), R_BRACKET.getFilename(), R_BRACKET.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;

            // looking for {
            Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

            // check for if
            if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected {. Got:%s\n%s:%s",
                        L_BRACE.getTokenType().toString(), L_BRACE.getFilename(), L_BRACE.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;

            // looking for a body
            Body body = Body.ParseBody(tokens, nestLevel, scope);

            // looking for a }
            Token R_BRACE = tokens.get(TokenIndex.currentTokenIndex);

            // check for if
            if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected }. Got:%s\n%s:%s",
                        R_BRACE.getTokenType().toString(), R_BRACE.getFilename(), R_BRACE.getLineNum()));
            }

            TokenIndex.currentTokenIndex++;

            // adding what was found to the list of seen else if statements
            elseIfList.add(new ElseifStmt(expr, body, nestLevel, scope));

            // looking for an else if statement
            elseif = tokens.get(TokenIndex.currentTokenIndex);
        }

        // finished with finding else ifs
        return elseIfList;
    }

    /**
     * Return this object as a convert.Jott code.
     *
     * @return a stringified version of this object as convert.Jott code
     */
    public String convertToJott() {
        String space = "    ".repeat(this.nestLevel - 1);
        return String.format("elseif[ %s]{ \n%s%s}", expr.convertToJott(), body.convertToJott(), space);
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToJava() {
        String space = "    ".repeat(this.nestLevel - 1);
        return String.format("else if( %s){ \n%s%s}", expr.convertToJava(), body.convertToJava(), space);
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToC() {
        String space = "    ".repeat(this.nestLevel - 1);
        return String.format("else if( %s){ \n%s%s}", expr.convertToC(), body.convertToC(), space);
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToPython() {
        String space = "    ".repeat(this.nestLevel - 1);
        return String.format("elif %s: \n%s%s", expr.convertToPython(), body.convertToPython(), space);
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
