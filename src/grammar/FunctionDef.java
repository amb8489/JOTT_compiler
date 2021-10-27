package grammar;

import main.Token;
import main.TokenType;

import java.text.ParseException;
import java.util.ArrayList;

public class FunctionDef  {
    public Identifier id;
    private FuncDefParams func_def_params;
    private Body bdy;
    private Type retrn;
    private int nestlevel ;
    public FunctionDef(Identifier identifier, FuncDefParams func_def_params, Body body, Type retrn,int nestlevel) {
        this.id = identifier;
        this.func_def_params = func_def_params;
        this.bdy = body;
        this.retrn = retrn;
        this.nestlevel = nestlevel;
    }



    // function_def -> id [ func_def_params ] : function_return { body }                                                <-- DONE

    public static FunctionDef parseFunctionDef(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        System.out.println("------------------------PARSING Function DEF------------------------");

        // ---------------------------look for id -----------------------------
        Token id = tokens.get(TOKEN_IDX.IDX);

        if (id.getTokenType() != TokenType.ID_KEYWORD) {
            System.out.println("TODO ERROR -1");
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected <id>. Got: ");
            sb.append(id.getTokenType().toString()).append("\n");
            sb.append(id.getFilename() + ":" + id.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;

        System.out.println("    found id:" + id.getToken());
        // ---------------------------look for [ -----------------------------
        Token lbrac = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    found [???:" + lbrac.getToken());

        if (lbrac.getTokenType() != TokenType.L_BRACKET) {
            System.out.println("TODO ERROR 99");
        }
        TOKEN_IDX.IDX++;


        // ---------------------------look for func_def_params ---------------

        FuncDefParams func_def_params = FuncDefParams.parseFunctionDefParams(tokens, nestlevel);
        if (func_def_params == null) {
            System.out.println("empty params");
        }


        // ---------------------------look for ] -----------------------------
        Token rbrac = tokens.get(TOKEN_IDX.IDX);

        if (rbrac.getTokenType() != TokenType.R_BRACKET) {
            System.out.println("TODO ERROR 2");
        }
        System.out.println("found rbrac ? --> " + rbrac.getToken());

        TOKEN_IDX.IDX++;

        // ---------------------------look for : -----------------------------
        Token col = tokens.get(TOKEN_IDX.IDX);

        if (col.getTokenType() != TokenType.COLON) {
            System.out.println("TODO ERROR 3");
        }
        TOKEN_IDX.IDX++;
        System.out.println("found colon --> " + col.getToken());

        // ---------------------------look for function return AKA type or void -----------------------------

        Type retrn = Type.parseFReturnStmt(tokens, nestlevel);
        System.out.println("found return --> " + retrn.convertToJott());

        if (retrn == null) {
            System.out.println("TODO ERROR 4");
        }

        // ---------------------------look for { -----------------------------


        Token L_BRACE = tokens.get(TOKEN_IDX.IDX);

        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            System.out.println("TODO ERROR 5");
        }
        TOKEN_IDX.IDX++;
        System.out.println("found { --> " + L_BRACE.getToken());


        // ---------------------------look for body stmt -----------------------------

        Body bdy = Body.ParseBody(tokens, nestlevel+1);

        if (bdy == null) {
            System.out.println("found empty body");
        } else {
            System.out.println("Found body --> " + bdy.convertToJott());
        }

        // ---------------------------look for } -----------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.IDX);

        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            System.out.println("TODO ERROR 7");
        }
        System.out.println("Found } --> " + R_BRACE.getToken());
        TOKEN_IDX.IDX++;

        return new FunctionDef(new Identifier(id), func_def_params, bdy, retrn ,nestlevel);
    }
    public boolean validateJott() {
        return false;
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

    // function_def -> id [ func_def_params ] : function_return { body }                                                <-- DONE

    public String convertToJott() {

        StringBuilder jstr = new StringBuilder();

        String funcP = (func_def_params == null) ? "" : func_def_params.convertToJott();

        String bod = (bdy == null) ? "" : bdy.convertToJott();


        String SPACE = "    ".repeat(this.nestlevel);
        jstr.append(SPACE+ id.convertToJott()+ " [ " + funcP+" ] ");
        jstr.append(" : " + retrn.convertToJott()+" { \n" + bod + SPACE+"}");

        return jstr.toString();
    }

    public boolean validateTree() throws ParsingException {

        // if return type is INT DOUBLE STRING BOOL
        if (!this.retrn.type.equals("Void")){
            if (this.bdy.HasGuaranteedReturnFromIf || this.bdy.Hasreturn != null){
                return true;
            }
            throw new ParsingException("MISSING RETURN in function: "+this.id.convertToJott());

        }else{
            // VOID HAS NO RETURN
            if (this.bdy.Hasreturn == null && !this.bdy.HasGuaranteedReturnFromIf){
                return true;
            }else{
                throw new ParsingException("VOID function has return stmt");
            }

        }
    }


}
