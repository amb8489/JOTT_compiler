package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Type tells us what a given token is.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Type {
    String type;
    String filename;
    int lineNumber;
    public String scope;

    /**
     * This is the constructor for a Type that takes in a token, a filename, and a line number.
     *
     * @param token      this is the token this type object is attributed to
     * @param filename   this is the filename where the token comes from
     * @param lineNumber this is the line number in the filename where the token is from
     */
    public Type(String token, String filename, int lineNumber, String scope) {
        this.type = token;
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.scope = scope;

    }

    /**
     * This is the constructor for a Type that just takes in a type.
     *
     * @param type tells
     */
    public Type(String type, String scope) {
        this.scope = scope;
        this.type = type;
    }

    /**
     * isType returns whether token is a type (Integer, Double, Boolean, or String) or not
     *
     * @param token the token to observe
     * @return whether the token is a type or not
     */
    public static boolean isType(Token token) {
        String tokenString = token.getToken();
        return tokenString.equals("Integer") ||
                tokenString.equals("Double") ||
                tokenString.equals("Boolean") ||
                tokenString.equals("String");
    }

    /**
     * Parse return statement.
     *
     * @param tokens the list of tokens to be parsed
     * @return the type
     */
    public static Type parseFReturnStmt(ArrayList<Token> tokens, String scope) {
        Token funcReturnType = tokens.get(TokenIndex.currentTokenIndex);
        if (isType(funcReturnType) || funcReturnType.getToken().equals("Void")) {
            TokenIndex.currentTokenIndex++;
            return new Type(funcReturnType.getToken(), funcReturnType.getFilename(), funcReturnType.getLineNum(), scope);
        }
        return null;

    }

    /**
     * Parse the return type
     *
     * @param tokens the list of tokens to be parsed
     * @return parsed type
     * @throws ParsingException if something goes wrong, an exception is thrown
     */
    public Type parseType(ArrayList<Token> tokens, String scope) throws ParsingException {
        // check for type
        Token typeToken = tokens.remove(0);
        if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {
            if ("Integer".equals(typeToken.getToken()) || "Double".equals(typeToken.getToken()) ||
                    "String".equals(typeToken.getToken()) || "Boolean".equals(typeToken.getToken())) {
                return new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum(), scope);
            }
        }

        // error, what was passed in is not a valid type
        throw new ParsingException(String.format("Syntax error\nInvalid token. " +
                        "Expected Integer||Double||String||Boolean. Got: %s\n%s:%s",
                typeToken.getTokenType().toString(), typeToken.getFilename(), typeToken.getLineNum()));
    }

    /**
     * Return this object as a convert.Jott code.
     *
     * @return a stringified version of this object as convert.Jott code
     */
    public String convertToJott() {
        return type;
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
    	if (type.equals("Integer")) {
    		return "int";
    	}
    	if (type.equals("Double")) {
    		return "double";
    	}
    	if (type.equals("Boolean")) {
    		return "boolean";
    	}
    	if (type.equals("Void")) {
    		return "void";
    	}
        return type;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        if (type.equals("Integer")) {
            return "int";
        }
        if (type.equals("Double")) {
            return "double";
        }
        if (type.equals("Boolean")) {
            return "bool";
        }
        if (type.equals("Void")) {
            return "void";
        }else{
            return"char *";
        }
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return "";
    }

    /**
     * Ensure the code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        return false;
    }
}
