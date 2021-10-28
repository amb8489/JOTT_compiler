package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class Type {
    String type = null;
    String filename;
    int lineNumber;

    /**
     * TODO
     * @param token TODO
     * @param filename TODO
     * @param lineNumber TODO
     */
    public Type(String token, String filename, int lineNumber) {
        this.type = token;
        this.filename = filename;
        this.lineNumber = lineNumber;
    }

    /**
     * TODO
     * @param token TODO
     * @return TODO
     */
    public static boolean isType(Token token) {
        String t = token.getToken();
        return t.equals("Integer") ||
                t.equals("Double") ||
                t.equals("Boolean") ||
                t.equals("String");
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     */
    public static Type parseFReturnStmt(ArrayList<Token> tokens, int nestLevel) {
        Token funcReturnType = tokens.get(TOKEN_IDX.index);
        if ( isType(funcReturnType) || funcReturnType.getToken().equals("Void")) {
            TOKEN_IDX.index++;
            return new Type(funcReturnType.getToken(),funcReturnType.getFilename(),funcReturnType.getLineNum());
        }
            return null;

    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public Type ParseType(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("-------------------PARSING TYPE-----------------");


        // ---------------------- check for type ------------------------------------

        Token typeToken = tokens.remove(0);
        if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {
            if ("Integer".equals(typeToken.getToken()) ||"Double".equals(typeToken.getToken()) ||
                "String".equals(typeToken.getToken())  || "Boolean".equals(typeToken.getToken())) {
                return new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
            }
        }
        // ---------------------- error what was passed in not a valid type ---------

        String sb = "Syntax error\nInvalid token. Expected Integer||Double||String||Boolean. Got: " +
                typeToken.getTokenType().toString() + "\n" +
                typeToken.getFilename() + ":" + typeToken.getLineNum();
        throw new ParsingException(sb);
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() { return type; }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() { return false;}

}
