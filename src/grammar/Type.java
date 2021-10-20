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
        return "Integer Double Boolean String".contains(T.getToken());
    }


    public Type ParseType(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("-------------------PARSING TYPE-----------------");

        Token typeToken = tokens.remove(0);


        if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {
            if ("Integer".equals(typeToken.getToken()) ||"Double".equals(typeToken.getToken()) ||
                "String".equals(typeToken.getToken())  || "Boolean".equals(typeToken.getToken())) {
                return new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
            }
        }
        // error
        StringBuilder sb = new StringBuilder();
        sb.append("Syntax error\nInvalid token. Expected Integer||Double||String||Boolean. Got: ");
        sb.append(typeToken.getTokenType().toString()).append("\n");
        sb.append(typeToken.getFilename() + ":" +typeToken.getLineNum());
        throw new ParsingException(sb.toString());


    }

    public String convertToJott(){
        return type;
    }

}
