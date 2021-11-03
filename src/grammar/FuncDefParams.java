package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This function represents function parameters.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class FuncDefParams {
    public Identifier identifier;
    public Token type;
    public final ArrayList<FuncDefParams> functionParameterList;
    public String scope;

    /**
     * This is the constructor for function definition parameters.
     *
     * @param identifier            TODO
     * @param type                  TODO
     * @param functionParameterList TODO
     */
    public FuncDefParams(Identifier identifier, Token type, ArrayList<FuncDefParams> functionParameterList,
                         String scope) {
        this.identifier = identifier;
        this.type = type;
        this.functionParameterList = functionParameterList;
    }

    /**
     * Constructor TODO
     *
     * @param functionParameterList TODO
     */
    public FuncDefParams(ArrayList<FuncDefParams> functionParameterList, String scope) {
        this.functionParameterList = functionParameterList;
        this.scope = scope;
    }

    /**
     * Constructor TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @param scope
     * @return TODO
     * @throws ParsingException TODO
     */
    public static FuncDefParams parseFunctionDefParams(ArrayList<Token> tokens, int nestLevel, String scope)
            throws ParsingException {

        ArrayList<FuncDefParams> functionParameterList = new ArrayList<>();

        // look for an id
        Token idd = tokens.get(TokenIndex.currentTokenIndex);

        if (idd.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }

        while (idd.getTokenType() != TokenType.R_BRACKET) {
            if (Type.isType(idd)) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. <id>. Got: TYPE \n%s:%s",
                        idd.getFilename(), idd.getLineNum()));
            }

            if (idd.getTokenType() != TokenType.ID_KEYWORD) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. <id>. Got:%s\n%s:%s",
                        idd.getTokenType().toString(), idd.getFilename(), idd.getLineNum()));
            }
            Identifier id = new Identifier(idd, scope);

            TokenIndex.currentTokenIndex++;

            // look for :
            Token column = tokens.get(TokenIndex.currentTokenIndex);

            TokenIndex.currentTokenIndex++;

            // look for a type
            Token type = tokens.get(TokenIndex.currentTokenIndex);

            if (!Type.isType(type)) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected <Type>. Got:%s\n%s:%s",
                        type.getTokenType().toString(), type.getFilename(), type.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;

            // look for extra parameters (parameters_t) for the function
            functionParameterList.add(new FuncDefParams(id, type, null, scope));


            idd = tokens.get(TokenIndex.currentTokenIndex);
            if (idd.getTokenType() != TokenType.COMMA && idd.getTokenType() == TokenType.R_BRACKET) {
                break;
            } else if (idd.getTokenType() != TokenType.COMMA) {
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected ,. Got:%s\n%s:%s",
                        idd.getTokenType().toString(), idd.getFilename(), idd.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;
            idd = tokens.get(TokenIndex.currentTokenIndex);

        }
        return new FuncDefParams(functionParameterList, scope);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        StringBuilder functionParameterList = new StringBuilder();

        if (this.functionParameterList != null) {
            for (FuncDefParams FDP : this.functionParameterList) {
                functionParameterList.append(FDP.convertToJott());
            }
            return functionParameterList.substring(0, functionParameterList.length() - 1);
        } else {
            jottString.append(String.format("%s:%s,", identifier.convertToJott(), type.getToken()));
            return jottString.toString();
        }
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        StringBuilder javaString = new StringBuilder();

        StringBuilder functionParameterList = new StringBuilder();

        if (this.functionParameterList != null) {
            for (FuncDefParams FDP : this.functionParameterList) {
                functionParameterList.append(FDP.convertToJava());
            }
            return functionParameterList.substring(0, functionParameterList.length() - 1);
        } else {
        	javaString.append(String.format("%s %s,", type.getToken(), identifier.convertToJava()));
            return javaString.toString();
        }
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
        StringBuilder PyString = new StringBuilder();

        StringBuilder functionParameterList = new StringBuilder();

        if (this.functionParameterList != null) {
            for (FuncDefParams FDP : this.functionParameterList) {
                functionParameterList.append(FDP.convertToPython());
            }
            return functionParameterList.substring(0, functionParameterList.length() - 1);
        } else {
            PyString.append(String.format("%s,", identifier.convertToPython()));
            return PyString.toString();
        }
    }

    /**
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {

        // check that var is not a keyword, if it is it wil throw an error
        for (FuncDefParams param : functionParameterList) {
            if (param.identifier != null) {
                Identifier.check(param.identifier.id);
            }
            if (param.type != null) {
                Type.isType(param.type);
            }
        }

        return true;
    }
}
