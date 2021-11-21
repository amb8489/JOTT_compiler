package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This class represents function definition.
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
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected <id>. Got: %s\n%s:%s",
                    id.getTokenType(), id.getFilename(), id.getLineNum()));
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
    	
        StringBuilder javaString = new StringBuilder();

        String funcP = (funcDefParams == null) ? "" : funcDefParams.convertToJava();

        String body = (this.body == null) ? "" : this.body.convertToJava();

        if (id.convertToJava().equals("main")) {
        	String[] arr = body.split("\n");
        	if (arr != null) {
        		if (arr[arr.length - 1].contains("return")) {
                	String return_insides = arr[arr.length - 1];
                	String before_return = return_insides.substring(0, return_insides.indexOf("return"));
                	return_insides = return_insides.substring(return_insides.indexOf("return") + 6);
                	return_insides = return_insides.substring(0, return_insides.indexOf(";"));
                	StringBuilder main_thing = new StringBuilder();
                	for (int i = 0; i < arr.length - 1; i++) {
                		main_thing.append(arr[i] + "\n");
                	}
            		main_thing.append(before_return + "System.exit(" + return_insides + ");");
                	body = main_thing.toString();
        		}
        	}
        }

        String space = "    ".repeat(this.nestLevel);
        if (id.convertToJava().equals("main")) {
            javaString.append(String.format(" public static void main(String[] args) "));
        }
        else {
            javaString.append(String.format("%spublic static %s %s ( %s ) ", space, returnType.convertToJava(), id.convertToJava(), funcP));
        }
        javaString.append(String.format("{ \n%s%s}", body, space));

        return javaString.toString();
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {

        StringBuilder cString = new StringBuilder();

        String funcP = (funcDefParams == null) ? "" : funcDefParams.convertToC();
        funcP = (funcP == "") ? (id.id.getToken().equals("main")) ? "void": funcP : funcP;

        String body = (this.body == null) ? "" : this.body.convertToC();


        String space = "    ".repeat(this.nestLevel);
        cString.append(String.format("%s%s %s ( %s ) ", space, returnType.convertToC(), id.convertToC(), funcP));
        cString.append(String.format("{ \n%s%s}", body, space));

        return cString.toString();
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {

        StringBuilder PyString = new StringBuilder();

        String funcP = (funcDefParams == null) ? "" : funcDefParams.convertToPython();

        String body = (this.body == null) ? "   pass" : this.body.convertToPython();
        String space = "    ".repeat(this.nestLevel);


        PyString.append(String.format("%sdef %s(%s):", space, id.convertToPython(), funcP));
        PyString.append(String.format("\n%s", body, space));

        return PyString.toString();










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

                if (ValidateTable.getScope(scope).functions.get(this.id.convertToJott()).get(0).equals(
                        this.body.hasReturn.expr.type
                )) {
                    return true;
                }

                String msg = String.format("function %s should return %s",
                        this.id.convertToJott(),
                        ValidateTable.functions.get(this.id.convertToJott()).get(0));
                String fileName = this.id.id.getFilename();
                int lineNum = this.id.id.getLineNum();
                throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", msg, fileName, lineNum));
            }

            if (this.body.hasGuaranteedReturnFromIf) {
                return true;
            }

            String msg = "function " + this.id.convertToJott() + " missing return for Non void function";
            String fileName = this.id.id.getFilename();
            int lineNum = this.id.id.getLineNum();
            throw new ParsingException(String.format("SemanticError:\n %s\n%s:%d", msg, fileName, lineNum));

        } else {
            // VOID HAS NO RETURN
            if (this.body != null && this.body.hasReturn == null && !this.body.hasGuaranteedReturnFromIf) {
                return true;
            } else {
                String msg = "function " + this.id.convertToJott() + ": void function with a return ";
                String fileName = this.id.id.getFilename();
                int lineNum = this.id.id.getLineNum();
                throw new ParsingException(String.format("SemanticError:\n %s\n%s:%d", msg, fileName, lineNum));

            }
        }
    }
}
