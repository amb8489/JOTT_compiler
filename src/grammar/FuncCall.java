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
