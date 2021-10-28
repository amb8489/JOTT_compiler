package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


// stmt -> asmt|var_dec|func_call ;                                                                          <-- DONE

public class Stmt {
    AsmtStmt asmt;
    VarDec varDec;
    FuncCall funcCall;
    int nestLevel;

    /**
     * Constructor TODO
     * @param nestLevel TODO
     * @param asmt TODO
     * @param varDec TODO
     * @param funcCall TODO
     */
    public Stmt(int nestLevel, AsmtStmt asmt, VarDec varDec, FuncCall funcCall) {
        this.nestLevel = nestLevel;
        this.asmt = asmt;
        this.varDec = varDec;
        this.funcCall = funcCall;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("-------------------PARSING STMT------------------");

        //----------------------------trying asmt TODO HERE ISSUE
        TOKEN_IDX.saveTokenIndex();

        //System.out.println("Statment first again::::" + tokens.get(TOKEN_IDX.index).getToken());

        AsmtStmt asmt = AsmtStmt.parseAsmtStmt(tokens, nestLevel);

        if (asmt != null) {
            TOKEN_IDX.popRestore();
            return new Stmt(nestLevel, asmt, null, null);
        } else {
            TOKEN_IDX.restoreTokenIndex();
        }
        //----------------------------trying var_dec
        //System.out.println("first again::::" + tokens.get(TOKEN_IDX.index).getToken());
        TOKEN_IDX.saveTokenIndex();

        VarDec varDec = VarDec.parseVarDec(tokens, nestLevel);

        if (varDec != null) {
            TOKEN_IDX.popRestore();

            return new Stmt(nestLevel, null, varDec, null);

        } else {
            TOKEN_IDX.restoreTokenIndex();
        }

        //----------------------------trying func_call
        //System.out.println("first again::::" + tokens.get(TOKEN_IDX.index).getToken());
        TOKEN_IDX.saveTokenIndex();


            FuncCall funcCall = FuncCall.ParseFuncCall(tokens, nestLevel);
            if(funcCall != null){
            // ---------------------- check for end statment ------------------------------------

            //check for ;
            Token endStmt = tokens.get(TOKEN_IDX.index);

            if (endStmt.getTokenType() != TokenType.SEMICOLON) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected ;. Got: ");
                sb.append(endStmt.getTokenType().toString()).append("\n");
                sb.append(endStmt.getFilename() + ":" + endStmt.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.index++;


            TOKEN_IDX.popRestore();
            //System.out.println("-------------->>>>>>"+funcCall.convertToJott());

            return new Stmt(nestLevel, null, null, funcCall);

        } else {
            TOKEN_IDX.restoreTokenIndex();
        }
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        if (asmt != null) { return asmt.convertToJott(); }
        if (varDec != null) { return varDec.convertToJott(); }
        if (funcCall != null) { return funcCall.convertToJott() + ";"; }
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() throws ParsingException {

        if (asmt != null) { return asmt.validateTree(); }
        if (varDec != null) { return varDec.validateTree(); }
        if (funcCall != null) { return funcCall.validateTree(); }
        return true;
    }


}
