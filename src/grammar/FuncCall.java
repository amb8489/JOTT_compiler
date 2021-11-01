package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 */
public class FuncCall {
    Token name; // function name
    Params parameters; // function parameters
    public String insideOfFunction;

    /**
     * This is the constructor for a function call.
     *
     * @param token      TODO
     * @param parameters TODO
     */
    public FuncCall(Token token, Params parameters) {
        this.name = token;
        this.parameters = parameters;
    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static FuncCall ParseFuncCall(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

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
            String string = "Syntax error\nInvalid token. Expected [. Got: " +
                    L_BRACKET.getTokenType().toString() + "\n" +
                    L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum();
            throw new ParsingException(string);
        }
        TokenIndex.currentTokenIndex++;

        // looking for function parameters
        Params params = Params.parseParams(tokens, nestLevel);

        // checking for ]
        Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            String stringBuilder = "Syntax error\nInvalid token. Expected [. Got: " +
                    R_BRACKET.getTokenType().toString() + "\n" +
                    R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum();
            throw new ParsingException(stringBuilder);
        }
        TokenIndex.currentTokenIndex++;

        // we are all done
        return new FuncCall(id, params);
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
     * Return this object as a Jott code.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        ValidateTable.checkFunctionCall(name, parameters);
        return true;


    }
}
