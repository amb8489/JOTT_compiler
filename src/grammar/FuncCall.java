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
public class FuncCall {
    Token token; // function name
    Params parameters; // function parameters

    // ---------------------------constructor----------------------------------------

    /**
     * Constructor TODO
     * @param token TODO
     * @param parameters TODO
     */
    public FuncCall(Token token, Params parameters) {
        this.token = token;
        this.parameters = parameters;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static FuncCall ParseFuncCall(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("---------------------------- PARSING FUNCTION CALL ----------------------------");
        System.out.println(tokens.get(TOKEN_IDX.index).getToken());

        // ---------------- checking function call starts with id [----------------------

        Token id = tokens.get(TOKEN_IDX.index);
        Token lb = tokens.get(TOKEN_IDX.index + 1);
        if (id.getTokenType() != TokenType.ID_KEYWORD || lb.getTokenType() != TokenType.L_BRACKET) {
            return null;
        }
        System.out.println("    1st:" + id.getToken());
        TOKEN_IDX.index++;


        // ---------------- checking for [ (redundant check)----------------------

        Token L_BRACKET = tokens.get(TOKEN_IDX.index);
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(L_BRACKET.getTokenType().toString()).append("\n");
            sb.append(L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.index++;
        System.out.println("    2nd:" + L_BRACKET.getToken());

        // ---------------- looking for params for functions----------------------

        Params parms = Params.parseParams(tokens, nestLevel);

        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.index);

        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(R_BRACKET.getTokenType().toString()).append("\n");
            sb.append(R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.index++;
        System.out.println("    4th:" + R_BRACKET.getToken());

        // ---------------------- all done ----------------------------------

        System.out.println("function call done");

        return new FuncCall(id, parms);
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();


        jstr.append(token.getToken() + "[ ");
        jstr.append(parameters.convertToJott() + "]");

        return jstr.toString();
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }

}
