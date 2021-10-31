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
    private final Expr expr;

    /**
     * This is the constructor for AsmtStmt.
     * @param type TODO: blah
     * @param identifier TODO: blah
     * @param expr TODO: blah
     */
    public AsmtStmt(Type type, Identifier identifier, Expr expr) {
        this.type = type;
        this.identifier = identifier;
        this.expr = expr;
    }

    /**
     * The format of asmt is {INDENT}TYPE NAME = EXPR ;
     * where indent is the number of tabs
     * @return TODO <--- blah
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        jottString.append("\t".repeat(0));
        if (type != null) {
            jottString.append(String.format("%s ", type.convertToJott()));
        }

        jottString.append(String.format("%s = ", identifier.convertToJott()));
        jottString.append(String.format("%s;\n", expr.convertToJott()));

        return jottString.toString();
    }

    /**
     * TODO: blah
     * @param tokens TODO: blah
     * @return TODO: blah
     * @throws ParsingException TODO: blah
     */
    public static AsmtStmt parseAsmtStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("------------------------PARSING ASMT-STMT------------------------");

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.get(TOKEN_IDX.index);
        //System.out.println("    FIRST:" + typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
        TOKEN_IDX.index++;

        Token idToken = tokens.get(TOKEN_IDX.index);

        if (Type.isType(typeToken)) {
            TOKEN_IDX.index++;

            // getting next token
            //System.out.println("    SECOND:" + idToken.getToken());
            if (idToken.getTokenType() != TokenType.ID_KEYWORD) {
                return null;
            }
        } else {
            idToken = typeToken;
        }

        Identifier id = new Identifier(idToken); // making id
        Token equalsToken = tokens.get(TOKEN_IDX.index); // looking for = token


        // checking for =
        //System.out.println("    THIRD:" + equalsToken.getToken());
        if (equalsToken.getTokenType() != TokenType.ASSIGN) {
            return null;

        }
        TOKEN_IDX.index++;

        // checking for expression
        //System.out.println("\tLOOKING FOR EXPR");
        Expr expr = NumExpr.parseExpr(tokens, nestLevel);

        //System.out.println("----------------22----"+expr);

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
        if (Type.isType(typeToken)) {
            return new AsmtStmt(type, id, expr);
        }
        return new AsmtStmt(null, id, expr);
    }

    /**
     * TODO: need to implement this in phase 3
     * @return something
     */
    public boolean validateTree() throws ParsingException {

        //TODO
        // check that expr is good aka that function return types are = to expr type
        // and that vars in the expr 1) exist and 2) are the type of the expr type
        // check that ids for vars are ok use Identifier.check(identifier.id);


        // 1) check that id for var is good
        Identifier.check(identifier.id);

        // 2) check if we are looking at a : type id = expr  __OR__   id = expr

        // 2) if we have : type id = val;
        if (type != null) {

            // check that we havet already defined this var ??????????????? scope??????
            if (!ValidateTable.variables.containsKey(identifier.convertToJott())) {

                // see that type of left = type of right

                if (type.type.equals(expr.type)) {
                    ValidateTable.variables.put(identifier.convertToJott(), new ArrayList<>() {{
                        add(type.type);
                        add(expr.convertToJott());
                    }});
                    return true;
                }


                // Failure
                throw new ParsingException(String.format("var %s assigned wrong type: line %d", identifier.convertToJott(), identifier.id.getLineNum()));
            } else {
                throw new ParsingException(String.format("var %s dose already exist: line %d",identifier.convertToJott(),identifier.id.getLineNum()));
            }

        } else {
            // ELSE if we have:    id = val;

            // updating value already in table

            //1) check that the var in question exists
            String variableId = identifier.convertToJott();
            if (!ValidateTable.variables.containsKey(variableId)){
                throw new ParsingException(String.format("var %s dose not exist: line %d", variableId, identifier.id.getLineNum()));
            }

            //2) get type var type and check its being assigned the same type
            // get type var type and check its being assigned the same type
            String varType = ValidateTable.variables.get(identifier.convertToJott()).get(0);

            if (expr.type.equals(varType)) {
                ValidateTable.variables.get(identifier.convertToJott()).set(1, expr.convertToJott());
                return true;
            }
            // Failure
            throw new ParsingException(String.format("var %s assigned wrong type: line %d", variableId,identifier.id.getLineNum()));

        }
    }

}
