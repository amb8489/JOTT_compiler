package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This class represents a function call.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 */
public class FuncCall {
    Token name; // function name
    private final Params parameters; // function parameters
    private String scope;

    /**
     * This is the constructor for a function call.
     *
     * @param token      TODO
     * @param parameters TODO
     */
    public FuncCall(Token token, Params parameters, String scope) {
        this.name = token;
        this.parameters = parameters;
        this.scope = scope;

    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static FuncCall ParseFuncCall(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {

        // check if the function call starts with id
        Token id = tokens.get(TokenIndex.currentTokenIndex);
        Token leftBracket = tokens.get(TokenIndex.currentTokenIndex + 1);
        if (id.getTokenType() != TokenType.ID_KEYWORD || leftBracket.getTokenType() != TokenType.L_BRACKET) {
            return null;
        }
        TokenIndex.currentTokenIndex++;

        // checking for [ (redundant check)
        Token L_BRACKET = tokens.get(TokenIndex.currentTokenIndex);
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected [. Got: %s\n%s:%s",
                    L_BRACKET.getTokenType().toString(), L_BRACKET.getFilename(), L_BRACKET.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // looking for function parameters
        Params params = Params.parseParams(tokens, nestLevel, scope);

        // checking for ]
        Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected ]. Got: %s\n%s:%s",
                    R_BRACKET.getTokenType().toString(), R_BRACKET.getFilename(), R_BRACKET.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // we are all done
        return new FuncCall(id, params, scope);
    }

    /**
     * Ensure the code in the function call is valid.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        jottString.append(String.format("%s[", name.getToken()));

        if (parameters == null) {
            jottString.append("]");
            return jottString.toString();
        }
        jottString.append(String.format("%s]", parameters.convertToJott()));

        return jottString.toString();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        StringBuilder javaString = new StringBuilder();

        String func_name = name.getToken();
        if (func_name.equals("print")) {
        	func_name = "System.out.println";
        }
        else if (func_name.equals("input")) {
        	func_name = "Helper.input";
        }
        else if (func_name.equals("concat")) {
            return  "("+parameters.paramsList.get(0).expr.convertToJava()+" + "+parameters.paramsList.get(1).expr.convertToJava()+")";
        }
        else if  (func_name.equals("length")) {
        	return parameters.paramsList.get(0).expr.convertToJava() + ".length()";
        }
        
        javaString.append(String.format("%s(", func_name));

        if (parameters == null) {
        	javaString.append(")");
            return javaString.toString();
        }
        javaString.append(String.format("%s)", parameters.convertToJava()));

        return javaString.toString();
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        StringBuilder cString = new StringBuilder();

        String func_name = name.getToken();

        if (parameters == null) {
            cString.append(")");
            return cString.toString();
        }

        String parameters_string = parameters.convertToC();


        if (func_name.equals("print")) {
            func_name = "printf";


            String printType = "%d";
            if (parameters.paramsList.get(0).expr.type.equals("String") || parameters.paramsList.get(0).expr.type.equals("Boolean")) {
                printType = "%s";
            }
            if (parameters.paramsList.get(0).expr.type.equals("Double")) {
                printType = "%f";
            }

            cString.append(String.format("%s ( %s,", func_name, printType));
            cString.append(String.format("%s)", parameters_string));

        }else if(func_name.equals("concat")) {
            func_name = "strcat";

            cString.append(String.format("%s ( %s)", func_name, parameters_string));
        }else if(func_name.equals("length")) {
            func_name = "strlen";

            cString.append(String.format("%s( %s)", func_name, parameters_string));
        } else if (func_name.equals("input")){
            func_name = "fgets";
            cString.append("printf(\"%s\","+parameters.paramsList.get(0).expr.convertToC()+");\n");
            cString.append("    char __STR_IN__["+parameters.paramsList.get(1).expr.convertToC()+"];\n");
            cString.append("    fgets(__STR_IN__,"+ parameters.paramsList.get(1).expr.convertToC()+", stdin);\n");

        }else {

            cString.append(String.format("%s( ", func_name));


            //TODO do this later
            cString.append(String.format("%s)", parameters_string));

        }
        return cString.toString();

    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        StringBuilder PyString = new StringBuilder();

        String func_name = name.getToken();

        if (func_name.equals("length")) {
            func_name = "len";
        }
        if (func_name.equals("concat")) {
            return  "("+parameters.paramsList.get(0).expr.convertToPython()+" + "+parameters.paramsList.get(1).expr.convertToPython()+")";
        }
        if (func_name.equals("input")) {
            return  "input("+parameters.paramsList.get(0).expr.convertToPython()+")";
        }

        PyString.append(String.format("%s(", func_name));

        if (parameters == null) {
            PyString.append(")");
            return PyString.toString();
        }
        PyString.append(String.format("%s)", parameters.convertToPython()));

        return PyString.toString();
    }

    /**
     * Return this object as a Jott code.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        ValidateTable.checkFunctionCall(name, parameters);
        return true;


    }
}
