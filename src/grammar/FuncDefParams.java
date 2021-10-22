package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class FuncDefParams  {
    private final Identifier id;
    private final Token type;
    private final Params params;

    public FuncDefParams(Identifier id, Token type, Params params) {
        this.id = id;
        this.type = type;
        this.params = params;
    }


//   func_def_params -> id : type func_def_params_t|ε                                                                 <-- DONE


    public static FuncDefParams parseFunctionDefParams(ArrayList<Token> tokens, int nestlevel) throws ParsingException {


        // ---------------------------look for id -----------------------------
        Token idd = tokens.get(TOKEN_IDX.IDX);

        if (idd.getTokenType() != TokenType.ID_KEYWORD){
            System.out.println("TODO ERROR 0");
            return null;
        }
        Identifier id = new Identifier(idd);

        TOKEN_IDX.IDX++;

        // ---------------------------look for : -----------------------------
        Token col = tokens.get(TOKEN_IDX.IDX);

        if (col.getTokenType() != TokenType.COLON){
            System.out.println("TODO ERROR 1");
        }

        TOKEN_IDX.IDX++;


        // ---------------------------look for type -----------------------------
        Token type = tokens.get(TOKEN_IDX.IDX);

        if (type.getTokenType() != TokenType.ID_KEYWORD){
            System.out.println("TODO ERROR 2");
            return null;
        }
        TOKEN_IDX.IDX++;


        // ---------------------------look for func def parm t -----------------------------

        Params params = Params.parseParams(tokens,nestlevel);

        return new FuncDefParams(id, type, params);

    }





    public String convertToJava() {
        return null;
    }

    public String convertToC() {
        return null;
    }


    public String convertToPython() {
        return null;
    }

    public boolean validateTree() {
        return false;
    }
}
