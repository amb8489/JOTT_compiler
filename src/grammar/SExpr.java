package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * SExpr is a string expression.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class SExpr extends Expr {
    private final Token stringLiteral;
    private final Token token;
    private final FuncCall funcCall;
    int nestLevel;

    /**
     * This is a constructor for a string expression.
     *
     * @param stringLiteral TODO
     * @param token         TODO
     * @param funcCall      TODO
     * @param nestLevel     TODO
     */
    public SExpr(Token stringLiteral, Token token, FuncCall funcCall, int nestLevel) {
        super(null, null);
        this.stringLiteral = stringLiteral;
        this.token = token;
        this.funcCall = funcCall;
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
    public static SExpr parseSExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        // parsing s_expr
        Token possibleString = tokens.get(TokenIndex.currentTokenIndex);

        // check for string
        if (possibleString.getTokenType() == TokenType.STRING) {

            TokenIndex.currentTokenIndex++;
            return new SExpr(possibleString, null, null, nestLevel);
        }

        // check for id
        if (possibleString.getTokenType() == TokenType.ID_KEYWORD) {
            TokenIndex.currentTokenIndex++;
            return new SExpr(null, possibleString, null, nestLevel);
        }

        // check for func call
        FuncCall funcCall = FuncCall.ParseFuncCall(tokens, nestLevel);
        if (funcCall != null) {
            TokenIndex.currentTokenIndex++;
            return new SExpr(null, null, funcCall, nestLevel);
        }

        return null;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        if (stringLiteral != null) {
            return stringLiteral.getToken();
        }
        if (token != null) {
            return token.getToken();
        }
        return funcCall.convertToJott();
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
