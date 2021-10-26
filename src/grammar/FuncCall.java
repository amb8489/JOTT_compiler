package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class FuncCall {

    // functions name
    Token id;

    // functions parms
    Params parms;

    // ---------------------------constructor----------------------------------------

    public FuncCall(Token id, Params parms) {
        this.id = id;
        this.parms = parms;
    }

    // func_call -> id [ params ]                                                                                       <-- DONE

    public static FuncCall ParseFuncCall(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("---------------------------- PARSING FUNCTION CALL ----------------------------");
        System.out.println(tokens.get(TOKEN_IDX.IDX).getToken());

        // ---------------- checking function call starts with id [----------------------

        Token id = tokens.get(TOKEN_IDX.IDX);
        Token lb = tokens.get(TOKEN_IDX.IDX + 1);
        if (id.getTokenType() != TokenType.ID_KEYWORD || lb.getTokenType() != TokenType.L_BRACKET) {
            return null;
        }
        System.out.println("    1st:" + id.getToken());
        TOKEN_IDX.IDX++;


        // ---------------- checking for [ (redundant check)----------------------

        Token L_BRACKET = tokens.get(TOKEN_IDX.IDX);
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(L_BRACKET.getTokenType().toString()).append("\n");
            sb.append(L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;
        System.out.println("    2nd:" + L_BRACKET.getToken());

        // ---------------- looking for params for functions----------------------

        Params parms = Params.parseParams(tokens, nestLevel);

        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.IDX);

        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(R_BRACKET.getTokenType().toString()).append("\n");
            sb.append(R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;
        System.out.println("    4th:" + R_BRACKET.getToken());

        // ---------------------- all done ----------------------------------

        System.out.println("function call done");

        return new FuncCall(id, parms);
    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();


        jstr.append(id.getToken() + "[ ");
        jstr.append(parms.convertToJott() + "]");

        return jstr.toString();
    }

    public boolean validateTree() {
        return false;
    }

}
