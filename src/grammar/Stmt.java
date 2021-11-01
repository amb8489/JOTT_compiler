package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


// stmt -> asmt|var_dec|func_call ;                                                                          <-- DONE

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Stmt {
    AsmtStmt asmtStmt;
    VarDec varDec;
    FuncCall funcCall;
    int nestLevel;

    /**
     * Constructor TODO
     *
     * @param nestLevel TODO
     * @param asmtStmt  TODO
     * @param varDec    TODO
     * @param funcCall  TODO
     */
    public Stmt(int nestLevel, AsmtStmt asmtStmt, VarDec varDec, FuncCall funcCall) {
        this.nestLevel = nestLevel;
        this.asmtStmt = asmtStmt;
        this.varDec = varDec;
        this.funcCall = funcCall;
    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("-------------------PARSING STMT------------------");

        //----------------------------trying asmtStmt TODO HERE ISSUE
        TOKEN_IDX.saveTokenIndex();

        //System.out.println("Statment first again::::" + tokens.get(TOKEN_IDX.index).getToken());

        AsmtStmt asmtStmt = AsmtStmt.parseAsmtStmt(tokens, nestLevel);

        if (asmtStmt != null) {
            TOKEN_IDX.popRestore();
            return new Stmt(nestLevel, asmtStmt, null, null);
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
        if (funcCall != null) {
            // ---------------------- check for end statement ------------------------------------

            //check for ;
            Token endStmt = tokens.get(TOKEN_IDX.index);

            if (endStmt.getTokenType() != TokenType.SEMICOLON) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Syntax error\nInvalid token. Expected ;. Got: ");
                stringBuilder.append(endStmt.getTokenType().toString()).append("\n");
                stringBuilder.append(endStmt.getFilename() + ":" + endStmt.getLineNum());
                throw new ParsingException(stringBuilder.toString());
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
     *
     * @return TODO
     */
    public String convertToJott() {
        if (asmtStmt != null) {
            return asmtStmt.convertToJott();
        }
        if (varDec != null) {
            return varDec.convertToJott();
        }
        if (funcCall != null) {
            return funcCall.convertToJott() + ";";
        }
        return null;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public boolean validateTree() throws ParsingException {
        if (asmtStmt != null) {
            return asmtStmt.validateTree();
        }
        if (varDec != null) {
            return varDec.validateTree();
        }
        if (funcCall != null) {
            return funcCall.validateTree();
        }
        return true;
    }


}
