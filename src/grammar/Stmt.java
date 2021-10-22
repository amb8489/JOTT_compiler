package grammar;

import main.Token;

import java.util.ArrayList;


// stmt -> asmt|var_dec|func_call ;                                                                          <-- DONE

public class Stmt extends BodyStmt {
    public Stmt(int nestLevel,AsmtStmt asmt,VarDec varDec,FuncCall funcCall) {
        super(null);
    }

    public Stmt(Object o) {
        super(o);
    }

    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("-------------------PARSING STMT------------------");

    // restore lost tokens? TODO ???


        System.out.println("first again::::"+tokens.get(0).getToken());

        //----------------------------trying asmt
        try {
            AsmtStmt asmt = AsmtStmt.parseAsmtStmt(tokens,nestLevel);
            return new Stmt(nestLevel,asmt,null,null);
        }catch (ParsingException e){
            tokens.clear();
            tokens.addAll(retore);
        }
        //----------------------------trying var_dec

        try {
            VarDec varDec = VarDec.parseVarDec(tokens,nestLevel);
            return new Stmt(nestLevel,null,varDec,null);

        }catch (ParsingException e){
            tokens.clear();
            tokens.addAll(retore);
        }

        //----------------------------trying func_call

        try {
            FuncCall funcCall = FuncCall.ParseFuncCall(tokens,nestLevel);

            return new Stmt(nestLevel,null,null,funcCall);

        }catch (ParsingException ignored){

        }
        return null;
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
