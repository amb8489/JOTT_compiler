package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This class represents a variable declaration.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class VarDec {
    private final Type type;
    private final Identifier identifier;
    public String scope;

    /**
     * This is the constructor for a
     *
     * @param type       variable type
     * @param identifier variable name
     */
    public VarDec(Type type, Identifier identifier, String insideOfFunction) {
        this.type = type;
        this.identifier = identifier;
        this.scope = insideOfFunction;

    }

    /**
     * Parse variable declaration
     *
     * @param tokens tokens to parse
     * @return a parsed result in the form of a VarDec object
     * @throws ParsingException TODO
     */
    public static VarDec parseVarDec(ArrayList<Token> tokens, String insideOfFunction) throws ParsingException {
        // check for the correct type

        // removing and checking the first token
        // should be an id keyword type
        Token typeToken = tokens.get(TokenIndex.currentTokenIndex);
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum(), insideOfFunction);
        TokenIndex.currentTokenIndex++;

        // look for var name
        // getting next token
        Token idToken = tokens.get(TokenIndex.currentTokenIndex);
        if (idToken.getTokenType() != TokenType.ID_KEYWORD) {
            return null;
        }
        Identifier.check(idToken);
        TokenIndex.currentTokenIndex++;

        Identifier identifier = new Identifier(idToken, insideOfFunction);

        // look for ;
        Token endStmt = tokens.get(TokenIndex.currentTokenIndex);

        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            return null;
        }

        TokenIndex.currentTokenIndex++;

        return new VarDec(type, identifier, insideOfFunction);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        return String.format("%s%s %s;", "\t".repeat(0), type.convertToJott(), identifier.convertToJott());
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return String.format("%s%s %s;", "\t".repeat(0), type.convertToJava(), identifier.convertToJava());
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        if (type.convertToC().equals("char *")){
            return "char "+identifier.convertToC()+"[ 10 ];";

        }
        return String.format("%s%s %s;", "\t".repeat(0), type.convertToC(), identifier.convertToC());
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {

        StringBuilder javaString = new StringBuilder();

        javaString.append("\t".repeat(0));
        javaString.append(String.format("%s = None", identifier.convertToPython()));
        return javaString.toString();
    }

    /**
     * Ensure the code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        // [function scope to add too ,  var name  , [type , val ] ]
        ValidateTable.addVarToScope(scope, identifier.convertToJott(), type.type, null);
        return true;
    }
}
