package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class VarDec extends Stmt{

    private final Type type;
    private final Identifier name;


    public VarDec(int nestLevel,Type type,Identifier name){

            super(nestLevel);
            this.type = type;
            this.name = name;
    }


    public static VarDec parseVarDec(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Var-Dec------------------------");

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.remove(0);
        System.out.println("    FIRST:"+typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(),typeToken.getLineNum());


        // getting next token
        Token idToken = tokens.remove(0);
        System.out.println("    SECOND:"+typeToken.getToken());
        if (idToken.getTokenType() != TokenType.ID_KEYWORD){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected <id>. Got: ");
            sb.append(typeToken.getTokenType().toString()).append("\n");
            sb.append(idToken.getFilename() + ":" +idToken.getLineNum());
            throw new ParsingException(sb.toString());
        }

        Identifier id = new Identifier(idToken.getToken(), idToken.getFilename(),idToken.getLineNum());

        //check for ;
        Token endStmt = tokens.remove(0);

        if (endStmt.getTokenType() != TokenType.SEMICOLON){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected ;. Got: ");
            sb.append(endStmt.getTokenType().toString()).append("\n");
            sb.append(endStmt.getFilename() + ":" +endStmt.getLineNum());
            throw new ParsingException(sb.toString());
        }


        // if successful var dec statement
        return new VarDec(nestLevel,type,id);
    }
    // the format of asmt is {INDENT}TYPE NAME;
    // where insendt is the number of tabs
    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        jstr.append("     ".repeat(getNestLevel()));
        jstr.append(type.convertToJott() + " ");
        jstr.append(name.convertToJott() + ";");
        return jstr.toString();
    }
}
