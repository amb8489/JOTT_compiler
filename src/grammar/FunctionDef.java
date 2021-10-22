package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class FunctionDef extends FunctionList {
        private Identifier id;
        private FuncDefParams func_def_params;
        private Body bdy;
        private ReturnStmt retrn;

    public FunctionDef(Identifier identifier, FuncDefParams func_def_params, Body body, ReturnStmt retrn) {
        super(null);
        this.id = identifier;
        this.func_def_params = func_def_params;
        this.bdy = body;
        this.retrn = retrn;
    }

    public FunctionDef(Identifier identifier) {
        super(null);
    }


    // function_def -> id [ func_def_params ] : function_return { body }                                                <-- DONE

    public static FunctionDef parseFunctionDef(ArrayList<Token> tokens, int nestlevel) throws ParsingException {


        // ---------------------------look for id -----------------------------
        Token id = tokens.get(TOKEN_IDX.IDX);

        if (id.getTokenType() != TokenType.ID_KEYWORD){
            System.out.println("TODO ERROR -1");
            return null;
        }
        TOKEN_IDX.IDX++;


        // ---------------------------look for [ -----------------------------
        Token lbrac = tokens.get(TOKEN_IDX.IDX);

        if (lbrac.getTokenType() != TokenType.L_BRACKET){
            System.out.println("TODO ERROR 0");
        }
        TOKEN_IDX.IDX++;



        // ---------------------------look for func_def_params ---------------

         FuncDefParams func_def_params = FuncDefParams.parseFunctionDefParams(tokens,nestlevel);

        if (func_def_params == null){
            System.out.println("TODO ERROR 1");
        }



        // ---------------------------look for ] -----------------------------
        Token rbrac = tokens.get(TOKEN_IDX.IDX);

        if (rbrac.getTokenType() != TokenType.R_BRACKET){
            System.out.println("TODO ERROR 2");
        }
        TOKEN_IDX.IDX++;

        // ---------------------------look for : -----------------------------
        Token col = tokens.get(TOKEN_IDX.IDX);

        if (col.getTokenType() != TokenType.COLON){
            System.out.println("TODO ERROR 3");
        }
        TOKEN_IDX.IDX++;

        // ---------------------------look for return stmt -----------------------------

        ReturnStmt retrn = ReturnStmt.parseReturnStmt(tokens,nestlevel);

        if(retrn == null){
            System.out.println("TODO ERROR 4");
        }

        // ---------------------------look for { -----------------------------


        Token L_BRACE = tokens.get(TOKEN_IDX.IDX);

        if (L_BRACE.getTokenType() != TokenType.L_BRACE){
            System.out.println("TODO ERROR 5");
        }
        TOKEN_IDX.IDX++;


        // ---------------------------look for body stmt -----------------------------

        Body bdy = Body.ParseBody(tokens,nestlevel);

        if(bdy == null){
            System.out.println("TODO ERROR 6");
        }

        // ---------------------------look for } -----------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.IDX);

        if (R_BRACE.getTokenType() != TokenType.R_BRACE){
            System.out.println("TODO ERROR 7");
        }

        return new FunctionDef(new Identifier(id),func_def_params,bdy,retrn);
    }

    @Override
    public String convertToJava() {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
