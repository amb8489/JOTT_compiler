package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class VarDec{

    private final Type type;
    private final Identifier name;


    public VarDec(int nestLevel,Type type,Identifier name){
            this.type = type;
            this.name = name;
    }


    public static VarDec parseVarDec(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Var-Dec------------------------");



        // ---------------------- check for correct type ---------------------------

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    FIRST:"+typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(),typeToken.getLineNum());
        TOKEN_IDX.IDX++;

        // ----------------------------- var name-----------------------------------

        // getting next token
        Token idToken = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    SECOND:"+idToken.getToken());
        if (idToken.getTokenType() != TokenType.ID_KEYWORD){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected <id>. Got: ");
            sb.append(typeToken.getTokenType().toString()).append("\n");
            sb.append(idToken.getFilename() + ":" +idToken.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;

        Identifier id = new Identifier(idToken);




        //999999999cc


        // --------------------------------- check for ; ---------------------------
        Token endStmt = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    THIRD:"+endStmt.getToken());

        if (endStmt.getTokenType() != TokenType.SEMICOLON){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected ;. Got: ");
            sb.append(endStmt.getTokenType().toString()).append("\n");
            sb.append(endStmt.getFilename() + ":" +endStmt.getLineNum());
            throw new ParsingException(sb.toString());
        }

        TOKEN_IDX.IDX++;

        // --------------------------------- DONE------- ---------------------------
        return new VarDec(nestLevel,type,id);
    }



    // the format of asmt is {INDENT}TYPE NAME;
    // where insendt is the number of tabs
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        jstr.append("     ".repeat(0));
        jstr.append(type.convertToJott() + " ");
        jstr.append(name.convertToJott() + ";");
        return jstr.toString();
    }
}
