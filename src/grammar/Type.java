package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class Type {
    String type;
    String filename;
    int linenum;

    public Type(String token, String filename, int lineNum) {
        this.type = token;
        this.filename = filename;
        this.linenum = lineNum;
    }

    public static boolean isType(Token T) {
        String t = T.getToken();
        return t.equals("Integer") || t.equals("Double")||t.equals("Boolean")|| t.equals("String");
    }

    public static Type parseFReturnStmt(ArrayList<Token> tokens, int nestlevel) {
        Token funcReturnType = tokens.get(TOKEN_IDX.IDX);
        if ( isType(funcReturnType) || funcReturnType.getToken().equals("Void")) {
            TOKEN_IDX.IDX++;
            return new Type(funcReturnType.getToken(),funcReturnType.getFilename(),funcReturnType.getLineNum());
        }
            return null;

    }


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

        StringBuilder sb = new StringBuilder();
        sb.append("Syntax error\nInvalid token. Expected Integer||Double||String||Boolean. Got: ");
        sb.append(typeToken.getTokenType().toString()).append("\n");
        sb.append(typeToken.getFilename() + ":" +typeToken.getLineNum());
        throw new ParsingException(sb.toString());


    }

    public String convertToJott(){
        return type;
    }
    public boolean validateTree() {
        return false;
    }

}
