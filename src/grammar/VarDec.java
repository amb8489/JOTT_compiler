package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class VarDec {
    private final Type type;
    private final Identifier identifier;

    /**
     * TODO
     * @param nestLevel TODO
     * @param type TODO
     * @param identifier TODO
     */
    public VarDec(int nestLevel, Type type,Identifier identifier){
            this.type = type;
            this.identifier = identifier;


    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static VarDec parseVarDec(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("------------------------PARSING Var-Dec------------------------");

        // ---------------------- check for correct type ---------------------------

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.get(TOKEN_IDX.index);
        //System.out.println("    FIRST:"+typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(),typeToken.getLineNum());
        TOKEN_IDX.index++;

        // ----------------------------- var name-----------------------------------

        // getting next token
        Token idToken = tokens.get(TOKEN_IDX.index);
        //System.out.println("    SECOND:"+idToken.getToken());
        if (idToken.getTokenType() != TokenType.ID_KEYWORD){
            return null;
        }
        TOKEN_IDX.index++;

        Identifier identifier = new Identifier(idToken);

        // --------------------------------- check for ; ---------------------------
        Token endStmt = tokens.get(TOKEN_IDX.index);
        //System.out.println("    THIRD:"+endStmt.getToken());

        if (endStmt.getTokenType() != TokenType.SEMICOLON){
            return null;
        }

        TOKEN_IDX.index++;

        // --------------------------------- DONE------- ---------------------------
        return new VarDec(nestLevel,type,identifier);
    }

    // the format of asmt is {INDENT}TYPE NAME;
    // where insendt is the number of tabs

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        String jottString = "     ".repeat(0) +
                type.convertToJott() + " " +
                identifier.convertToJott() + ";";
        return jottString;
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() {
        // name ---> type null
        ValidateTable.variables.put(identifier.convertToJott(),new ArrayList<String>() {{add(type.type);add(null);}});
        return true;
    }

}
