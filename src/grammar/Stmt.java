package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


// stmt -> asmt|var_dec|func_call ;                                                                          <-- DONE

public class Stmt {
    AsmtStmt asmt = null;
    VarDec var_dec = null;
    FuncCall func_call = null;
    int nestLevel;


    public Stmt(int nestLevel, AsmtStmt asmt, VarDec vdec, FuncCall funcCall) {
        this.nestLevel = nestLevel;
        this.asmt = asmt;
        this.var_dec = vdec;
        this.func_call = funcCall;
    }

    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("-------------------PARSING STMT------------------");


        //----------------------------trying asmt TODO HERE ISSUE
        TOKEN_IDX.save_token_IDX();

        System.out.println("Statment first again::::" + tokens.get(TOKEN_IDX.IDX).getToken());


        try {
            AsmtStmt asmt = AsmtStmt.parseAsmtStmt(tokens, nestLevel);
            TOKEN_IDX.popRestore();
            return new Stmt(nestLevel, asmt, null, null);
        } catch (ParsingException e) {
            TOKEN_IDX.restore_token_IDX();
        }
        //----------------------------trying var_dec
        System.out.println("first again::::" + tokens.get(TOKEN_IDX.IDX).getToken());
        TOKEN_IDX.save_token_IDX();

        try {
            VarDec varDec = VarDec.parseVarDec(tokens, nestLevel);
            TOKEN_IDX.popRestore();

            return new Stmt(nestLevel, null, varDec, null);

        } catch (ParsingException e) {
            TOKEN_IDX.restore_token_IDX();
        }

        //----------------------------trying func_call
        System.out.println("first again::::" + tokens.get(TOKEN_IDX.IDX).getToken());
        TOKEN_IDX.save_token_IDX();

        try {
            FuncCall funcCall = FuncCall.ParseFuncCall(tokens, nestLevel);

            // ---------------------- check for end statment ------------------------------------

            //check for ;
            Token endStmt = tokens.get(TOKEN_IDX.IDX);

            if (endStmt.getTokenType() != TokenType.SEMICOLON) {
                return null;
            }
            TOKEN_IDX.IDX++;


            TOKEN_IDX.popRestore();
            System.out.println("-------------->>>>>>"+funcCall.convertToJott());

            return new Stmt(nestLevel, null, null, funcCall);

        } catch (ParsingException ignored) {
            TOKEN_IDX.restore_token_IDX();
        }
        return null;
    }


    public String convertToJott() {

        if (asmt != null) {
            return asmt.convertToJott();
        }
        if (var_dec != null) {
            return var_dec.convertToJott();
        }
        if (func_call != null) {
            return func_call.convertToJott()+";";
        }
        return null;
    }


}
