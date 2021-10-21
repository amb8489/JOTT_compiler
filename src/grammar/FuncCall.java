package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class FuncCall {
    Token id;
    Params parms;

    public FuncCall(Token id, Params parms) {
        this.id = id;
        this.parms = parms;
    }

    // func_call -> id [ params ]                                                                                       <-- DONE

    public static FuncCall ParseFuncCall(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("---------------------------- PARSING FUNCTION CALL ----------------------------");


        Token id = tokens.get(0);
        Token lb = tokens.get(1);


        if (id.getTokenType() != TokenType.ID_KEYWORD || lb.getTokenType() != TokenType.L_BRACKET) {
            return null;
        }
        tokens.remove(0);
        System.out.println("    1st:" + id.getToken());


        Token L_BRACKET = tokens.remove(0);
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(L_BRACKET.getTokenType().toString()).append("\n");
            sb.append(L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        System.out.println("    2nd:" + L_BRACKET.getToken());


        Params parms = Params.parseParams(tokens, nestLevel);

        Token R_BRACKET = tokens.remove(0);
        System.out.println("    4th:" + R_BRACKET.getToken());
        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(R_BRACKET.getTokenType().toString()).append("\n");
            sb.append(R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        System.out.println("function call done");

        return new FuncCall(id, parms);
    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();


        jstr.append(id.getToken() + "[ ");
        jstr.append(parms.convertToJott() + " ]");

        return jstr.toString();
    }
}
