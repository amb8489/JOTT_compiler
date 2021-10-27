package grammar;

import java.util.ArrayList;
import main.Token;
import main.TokenType;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class AsmtStmt {

    private final Type type;
    private final Identifier identifier;
    private final Expr expression;

    /**
     * This is the constructor for AsmtStmt.
     * @param nestLevel TODO: is this necessary?
     * @param type TODO: blah
     * @param identifier TODO: blah
     * @param expression TODO: blah
     */
    public AsmtStmt(int nestLevel, Type type, Identifier identifier, Expr expression) { // TODO: is nestLevel necessary?
        this.type = type;
        this.identifier = identifier;
        this.expression = expression;
    }

    /**
     * The format of asmt is {INDENT}TYPE NAME = EXPR ;
     * where indent is the number of tabs
     * @return TODO <--- blah
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        jottString.append("\t".repeat(0));
        if (!type.convertToJott().equals(identifier.convertToJott())) {
            jottString.append(String.format("%s ", type.convertToJott()));
        }

        jottString.append(String.format("%s = ", identifier.convertToJott()));
        jottString.append(String.format("%s;\n", expression.convertToJott()));

        return jottString.toString();
    }

    /**
     * TODO: blah
     * @param tokens TODO: blah
     * @param nestLevel TODO: blah
     * @return TODO: blah
     * @throws ParsingException TODO: blah
     */
    public static AsmtStmt parseAsmtStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING ASMT-STMT------------------------");

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.get(TOKEN_IDX.index);
        System.out.println("    FIRST:" + typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
        TOKEN_IDX.index++;

        Token idToken = tokens.get(TOKEN_IDX.index);

        if (Type.isType(typeToken)) {
            TOKEN_IDX.index++;

            // getting next token
            System.out.println("    SECOND:" + idToken.getToken());
            if (idToken.getTokenType() != TokenType.ID_KEYWORD) {
                return null;
            }
        } else {
            idToken = typeToken;
        }

        Identifier id = new Identifier(idToken); // making id
        Token equalsToken = tokens.get(TOKEN_IDX.index); // looking for = token


        // checking for =
        System.out.println("    THIRD:" + equalsToken.getToken());
        if (equalsToken.getTokenType() != TokenType.ASSIGN) {
            return null;

        }
        TOKEN_IDX.index++;

        // checking for expression
        System.out.println("\tLOOKING FOR EXPR");
        Expr expr = NumExpr.parseExpr(tokens, nestLevel);

        System.out.println("----------------22----"+expr);

        //check for ;
        Token endStmt = tokens.get(TOKEN_IDX.index);
        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            String message = String.format("Syntax error\nInvalid token. Expected ;. Got: %s\n%s:%s",
                    endStmt.getTokenType().toString(),
                    endStmt.getFilename(),
                    endStmt.getLineNum());
            throw new ParsingException(message);
        }
        TOKEN_IDX.index++;

        // if successful assignment statement
        return new AsmtStmt(nestLevel, type, id, expr);
    }

    /**
     * TODO: need to implement this in phase 3
     * @return something
     */
    public boolean validateTree() {
        return false;
    }

}
