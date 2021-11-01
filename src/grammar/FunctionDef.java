package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class FunctionDef {
    public Identifier id;
    public final FuncDefParams funcDefParams;
    private final Body body;
    public final Type returnType;
    private final int nestLevel;

    /**
     * Constructor TODO
     *
     * @param identifier    TODO
     * @param funcDefParams TODO
     * @param body          TODO
     * @param returnType    TODO
     * @param nestLevel     TODO
     */
    public FunctionDef(Identifier identifier, FuncDefParams funcDefParams, Body body, Type returnType, int nestLevel) {
        this.id = identifier;
        this.funcDefParams = funcDefParams;
        this.body = body;
        this.returnType = returnType;
        this.nestLevel = nestLevel;
    }

    public static FunctionDef parseFunctionDef(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        //System.out.println("------------------------PARSING Function DEF------------------------");

        // ---------------------------look for id -----------------------------
        Token id = tokens.get(TOKEN_IDX.index);

        if (id.getTokenType() != TokenType.ID_KEYWORD) {
            //System.out.println("TODO ERROR -1");
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected <id>. Got: ");
            sb.append(id.getTokenType().toString()).append("\n");
            sb.append(id.getFilename() + ":" + id.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.index++;

        //System.out.println("    found id:" + id.getToken());
        // ---------------------------look for [ -----------------------------
        Token leftBracketToken = tokens.get(TOKEN_IDX.index);
        //System.out.println("    found [???:" + leftBracketToken.getToken());

        if (leftBracketToken.getTokenType() != TokenType.L_BRACKET) {
            //System.out.println("TODO ERROR 99");
        }
        TOKEN_IDX.index++;


        // ---------------------------look for funcDefParams ---------------

        FuncDefParams funcDefParams = FuncDefParams.parseFunctionDefParams(tokens, nestlevel);
        if (funcDefParams == null) {
            //System.out.println("empty params");
        }


        // ---------------------------look for ] -----------------------------
        Token rightBracketToken = tokens.get(TOKEN_IDX.index);

        if (rightBracketToken.getTokenType() != TokenType.R_BRACKET) {
            //System.out.println("TODO ERROR 2");
        }
        //System.out.println("found rightBracketToken ? --> " + rightBracketToken.getToken());

        TOKEN_IDX.index++;

        // ---------------------------look for : -----------------------------
        Token colonToken = tokens.get(TOKEN_IDX.index);

        if (colonToken.getTokenType() != TokenType.COLON) {
            //System.out.println("TODO ERROR 3");
        }
        TOKEN_IDX.index++;
        //System.out.println("found colon --> " + colonToken.getToken());

        // ---------------------------look for function return AKA type or void -----------------------------

        Type retrn = Type.parseFReturnStmt(tokens);
        //System.out.println("found return --> " + retrn.convertToJott());

        if (retrn == null) {
            //System.out.println("TODO ERROR 4");
        }

        // ---------------------------look for { -----------------------------


        Token L_BRACE = tokens.get(TOKEN_IDX.index);

        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            //System.out.println("TODO ERROR 5");
        }
        TOKEN_IDX.index++;
        //System.out.println("found { --> " + L_BRACE.getToken());


        // ---------------------------look for body stmt -----------------------------

        Body body = Body.ParseBody(tokens, nestlevel + 1);

        /**
         if (body == null) {
         //System.out.println("found empty body");
         } else {
         //System.out.println("Found body --> " + body.convertToJott());
         }
         **/

        // ---------------------------look for } -----------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.index);

        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            //System.out.println("TODO ERROR 7");
        }
        //System.out.println("Found } --> " + R_BRACE.getToken());
        TOKEN_IDX.index++;

        return new FunctionDef(new Identifier(id), funcDefParams, body, retrn, nestlevel);
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

        String funcP = (funcDefParams == null) ? "" : funcDefParams.convertToJott();

        String bod = (body == null) ? "" : body.convertToJott();


        String SPACE = "    ".repeat(this.nestLevel);
        jstr.append(SPACE + id.convertToJott() + " [ " + funcP + " ] ");
        jstr.append(" : " + returnType.convertToJott() + " { \n" + bod + SPACE + "}");

        return jstr.toString();
    }

    public boolean validateTree() throws ParsingException {
        if (funcDefParams != null) {
            funcDefParams.validateTree();
        }

        if (body != null) {
            body.validateTree();
        }

        // if return type is INT DOUBLE STRING BOOL
        if (!this.returnType.type.equals("Void")) {
            if (this.body.hasReturn != null) {
                if (ValidateTable.functions.get(this.id.convertToJott()).get(0).equals(this.body.hasReturn.expr.type)) {
                    this.body.hasReturn.expr.validateTree();
                    return true;
                }

                throw new ParsingException("RETURNING WRONG TYPE in function: " + this.id.convertToJott() + " " + this.body.hasReturn.expr.type + " " + ValidateTable.functions.get(this.id.convertToJott()).get(0));
            }
            throw new ParsingException("MISSING RETURN in function: " + this.id.convertToJott());
        } else {
            // VOID HAS NO RETURN
            if (this.body.hasReturn == null && !this.body.hasGuaranteedReturnFromIf) {
                return true;
            } else {
                throw new ParsingException("VOID function has return stmt");
            }

        }


    }


}
