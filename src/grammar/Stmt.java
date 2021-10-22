package grammar;

import main.Token;

import java.util.ArrayList;


// stmt -> asmt|var_dec|func_call ;                                                                          <-- DONE

public class Stmt {



    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("-------------------PARSING STMT------------------");



        System.out.println("first again::::"+tokens.get(0).getToken());

        //----------------------------trying asmt
        TOKEN_IDX.save_token_IDX();
        try {
            AsmtStmt asmt = AsmtStmt.parseAsmtStmt(tokens,nestLevel);
            TOKEN_IDX.popRestore();
            return new Stmt(nestLevel,asmt,null,null);
        }catch (ParsingException e){
            TOKEN_IDX.restore_token_IDX();
        }
        //----------------------------trying var_dec
        System.out.println("first again::::"+tokens.get(0).getToken());
        TOKEN_IDX.save_token_IDX();

        try {
            VarDec varDec = VarDec.parseVarDec(tokens,nestLevel);
            TOKEN_IDX.popRestore();

            return new Stmt(nestLevel,null,varDec,null);

        }catch (ParsingException e){
            TOKEN_IDX.restore_token_IDX();
        }

        //----------------------------trying func_call
        System.out.println("first again::::"+tokens.get(0).getToken());
        TOKEN_IDX.save_token_IDX();

        try {
            FuncCall funcCall = FuncCall.ParseFuncCall(tokens,nestLevel);
            TOKEN_IDX.popRestore();

            return new Stmt(nestLevel,null,null,funcCall);

        }catch (ParsingException ignored){
            TOKEN_IDX.restore_token_IDX();
        }
        return null;
    }


    public String convertToJott() {

        if (stringLit != null){
            return stringLit.getToken();
        }
        if (id != null){
            return id.getToken();
        }
        return funcCall.convertToJott();
    }




}
