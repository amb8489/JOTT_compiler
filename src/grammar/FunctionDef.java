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
    public String scope;


    /**
     * This is the constructor for a function definition.
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
        this.scope = id.id.getToken();

    }

    public static FunctionDef parseFunctionDef(ArrayList<Token> tokens, int nestlevel) throws ParsingException {

        // look for an id
        Token id = tokens.get(TokenIndex.currentTokenIndex);

        if (id.getTokenType() != TokenType.ID_KEYWORD) {
            //System.out.println("TODO ERROR -1");
            String string = "Syntax error\nInvalid token. Expected <id>. Got: " +
                    id.getTokenType().toString() + "\n" +
                    id.getFilename() + ":" + id.getLineNum();
            throw new ParsingException(string);
        }
        TokenIndex.currentTokenIndex++;
        // look for [
        Token leftBracketToken = tokens.get(TokenIndex.currentTokenIndex);

        TokenIndex.currentTokenIndex++;

        // look for funcDefParams
        String funcName = id.getToken();
        FuncDefParams funcDefParams = FuncDefParams.parseFunctionDefParams(tokens, nestlevel, funcName);

        // look for ]
        Token rightBracketToken = tokens.get(TokenIndex.currentTokenIndex);

        TokenIndex.currentTokenIndex++;

        // look for :
        Token colonToken = tokens.get(TokenIndex.currentTokenIndex);

        TokenIndex.currentTokenIndex++;


        // look for function returns (aka type or void)
        Type return_ = Type.parseFReturnStmt(tokens, funcName);

        // look for {
        Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        TokenIndex.currentTokenIndex++;

        // look for body statement
        Body body = Body.ParseBody(tokens, nestlevel + 1, funcName);

        // look for }
        Token R_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        //System.out.println("TODO ERROR 7");
        //System.out.println("Found } --> " + R_BRACE.getToken());
        TokenIndex.currentTokenIndex++;

        return new FunctionDef(new Identifier(id, funcName), funcDefParams, body, return_, nestlevel);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {

        StringBuilder jottString = new StringBuilder();

        String funcP = (funcDefParams == null) ? "" : funcDefParams.convertToJott();

        String body = (this.body == null) ? "" : this.body.convertToJott();


        String space = "    ".repeat(this.nestLevel);
        jottString.append(String.format("%s%s [ %s ] ", space, id.convertToJott(), funcP));
        jottString.append(String.format(" : %s { \n%s%s}", returnType.convertToJott(), body, space));

        return jottString.toString();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
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
        return null;
    }

    /**
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        if (funcDefParams != null) {
            funcDefParams.validateTree();
        }

        if (body != null) {
            body.validateTree();
        }

        // if return type is INT DOUBLE STRING BOOL
        if (!this.returnType.type.equals("Void")) {
            if (this.body.hasReturn != null && !this.body.hasGuaranteedReturnFromIf) {
                this.body.hasReturn.expr.validateTree();

                this.body.hasReturn.expr.validateTree();

                if (body.hasReturn.expr.expr.type != null) {
                    this.body.hasReturn.expr.type = body.hasReturn.expr.expr.type;
                }

                if (ValidateTable.getScope(scope).functions.get(this.id.convertToJott()).get(0).equals(this.body.hasReturn.expr.type)) {
                    return true;
                }
                String msg = "function " + this.id.convertToJott() + " should return " + ValidateTable.functions.get(this.id.convertToJott()).get(0);
                String fileName = this.id.id.getFilename();
                int lineNum = this.id.id.getLineNum();
                throw new ParsingException(String.format(String.format("SemanticError:\n%s\n%s:%d", msg, fileName, lineNum)));
            }

            if (this.body.hasGuaranteedReturnFromIf) {
                return true;
            }

            String msg = "function " + this.id.convertToJott() + " missing return for Non void function";
            String fileName = this.id.id.getFilename();
            int lineNum = this.id.id.getLineNum();
            throw new ParsingException(String.format(String.format("SemanticError:\n %s\n%s:%d", msg, fileName, lineNum)));

        } else {
            // VOID HAS NO RETURN
            if (this.body != null && this.body.hasReturn == null && !this.body.hasGuaranteedReturnFromIf) {
                return true;
            } else {
                String msg = "function " + this.id.convertToJott() + " should return VOID ";
                String fileName = this.id.id.getFilename();
                int lineNum = this.id.id.getLineNum();
                throw new ParsingException(String.format(String.format("SemanticError:\n %s\n%s:%d", msg, fileName, lineNum)));

            }
        }
    }
}
