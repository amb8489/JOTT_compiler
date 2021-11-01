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
public class Type {
    String type;
    String filename;
    int lineNumber;

    /**
     * TODO
     *
     * @param token      TODO
     * @param filename   TODO
     * @param lineNumber TODO
     */
    public Type(String token, String filename, int lineNumber) {
        this.type = token;
        this.filename = filename;
        this.lineNumber = lineNumber;
    }

    /**
     * TODO
     *
     * @param token TODO
     * @return TODO
     */
    public static boolean isType(Token token) {
        String tokenString = token.getToken();
        return tokenString.equals("Integer") ||
                tokenString.equals("Double") ||
                tokenString.equals("Boolean") ||
                tokenString.equals("String");
    }

    /**
     * TODO
     *
     * @param tokens TODO
     * @return TODO
     */
    public static Type parseFReturnStmt(ArrayList<Token> tokens) {
        Token funcReturnType = tokens.get(TOKEN_IDX.index);
        if (isType(funcReturnType) || funcReturnType.getToken().equals("Void")) {
            TOKEN_IDX.index++;
            return new Type(funcReturnType.getToken(), funcReturnType.getFilename(), funcReturnType.getLineNum());
        }
        return null;

    }

    /**
     * TODO
     *
     * @param tokens TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public Type ParseType(ArrayList<Token> tokens) throws ParsingException {
        //System.out.println("-------------------PARSING TYPE-----------------");


        // ---------------------- check for type ------------------------------------

        Token typeToken = tokens.remove(0);
        if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {
            if ("Integer".equals(typeToken.getToken()) || "Double".equals(typeToken.getToken()) ||
                    "String".equals(typeToken.getToken()) || "Boolean".equals(typeToken.getToken())) {
                return new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
            }
        }
        // ---------------------- error what was passed in not a valid type ---------

        String string = "Syntax error\nInvalid token. Expected Integer||Double||String||Boolean. Got: " +
                typeToken.getTokenType().toString() + "\n" +
                typeToken.getFilename() + ":" + typeToken.getLineNum();
        throw new ParsingException(string);
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public String convertToJott() {
        return type;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }

}
