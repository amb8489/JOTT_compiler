package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

// s_expr -> str_literal|id|func_call

/**
 * Description
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
     * Constructor TODO
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
        //////System.out.println("-------------------parsing s_expr--------------------------");

        Token possibleString = tokens.get(TOKEN_IDX.index);

        // ----------------------check for string------------------
        if (possibleString.getTokenType() == TokenType.STRING) {

            TOKEN_IDX.index++;
            return new SExpr(possibleString, null, null, nestLevel);
        }

        // ----------------------check for id------------------

        if (possibleString.getTokenType() == TokenType.ID_KEYWORD) {
            TOKEN_IDX.index++;
            return new SExpr(null, possibleString, null, nestLevel);
        }
        // ----------------------check for func call------------------
        FuncCall funcCall = FuncCall.ParseFuncCall(tokens, nestLevel);
        if (funcCall != null) {
            TOKEN_IDX.index++;
            return new SExpr(null, null, funcCall, nestLevel);
        }

        return null;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    @Override
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
     * TODO
     *
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }

}
