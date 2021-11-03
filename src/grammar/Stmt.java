package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This is a statement which holds any of those:
 * 1) assign statement (asmt)
 * 2) var declaration (varDec)
 * 3) function call (funcCall)
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
    public String scope;

    /**
     * This is the constructor for a statement.
     *
     * @param nestLevel TODO
     * @param asmtStmt  TODO
     * @param varDec    TODO
     * @param funcCall  TODO
     */
    public Stmt(int nestLevel, AsmtStmt asmtStmt, VarDec varDec, FuncCall funcCall, String scope) {
        this.nestLevel = nestLevel;
        this.asmtStmt = asmtStmt;
        this.varDec = varDec;
        this.funcCall = funcCall;
        this.scope = scope;
    }

    /**
     * Parse a statement.
     *
     * @param tokens    an array of tokens to be parsed
     * @param nestLevel how deep
     * @return parse result in form of a statement object
     * @throws ParsingException throw an error if something is wrong
     */
    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {
        // trying asmtStmt
        TokenIndex.saveCurrentTokenIndex();
        AsmtStmt asmtStmt = AsmtStmt.parseAsmtStmt(tokens, nestLevel, scope);

        if (asmtStmt != null) {
            TokenIndex.popSavedTokenIndexStack();
            return new Stmt(nestLevel, asmtStmt, null, null, scope);
        } else {
            TokenIndex.restoreFromSavedTokenIndexStack();
        }

        // trying var dec
        TokenIndex.saveCurrentTokenIndex();
        VarDec varDec = VarDec.parseVarDec(tokens, scope);

        if (varDec != null) {
            TokenIndex.popSavedTokenIndexStack();
            return new Stmt(nestLevel, null, varDec, null, scope);
        } else {
            TokenIndex.restoreFromSavedTokenIndexStack();
        }

        // trying func_call
        TokenIndex.saveCurrentTokenIndex();

        FuncCall funcCall = FuncCall.ParseFuncCall(tokens, nestLevel, scope);
        if (funcCall != null) {
            // check for ;
            Token endStmt = tokens.get(TokenIndex.currentTokenIndex);

            if (endStmt.getTokenType() != TokenType.SEMICOLON) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected ;. Got: %s\n%s:%s",
                        endStmt.getTokenType().toString(), endStmt.getFilename(), endStmt.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;
            TokenIndex.popSavedTokenIndexStack();
            return new Stmt(nestLevel, null, null, funcCall, scope);
        } else {
            TokenIndex.restoreFromSavedTokenIndexStack();
        }
        return null;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
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
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        if (asmtStmt != null) {
            return asmtStmt.convertToJava();
        }
        if (varDec != null) {
            return varDec.convertToJava();
        }
        if (funcCall != null) {
            return funcCall.convertToJava() + ";";
        }
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {

        if (asmtStmt != null) {
            return asmtStmt.convertToPython();
        }
        if (varDec != null) {
            return varDec.convertToPython();
        }
        if (funcCall != null) {
            return funcCall.convertToPython();
        }
        return null;
    }

    /**
     * Ensure the code is valid
     *
     * @return whether code is valid or not
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
