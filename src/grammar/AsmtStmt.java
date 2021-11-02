package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * AsmtStmt is an assignment statement.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class AsmtStmt {
    public Type type;                       // what variable type is this?
    private final Identifier identifier;    // what variable name (id) is this?
    public Expr expr;                       //
    public String scope;

    /**
     * This is the constructor for AsmtStmt.
     *
     * @param type       what variable type is this?
     * @param identifier what variable name is this?
     * @param expr       the expression object that holds
     */
    public AsmtStmt(Type type, Identifier identifier, Expr expr, String scope) {
        this.type = type;
        this.identifier = identifier;
        this.expr = expr;
        this.scope = scope;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
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
     *
     * @param tokens TODO: blah
     * @return TODO: blah
     * @throws ParsingException TODO: blah
     */
    public static AsmtStmt parseAsmtStmt(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {
        //System.out.println("------------------------PARSING ASMT-STMT------------------------");

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.get(TokenIndex.currentTokenIndex);
        //System.out.println("    FIRST:" + typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum(), scope);
        TokenIndex.currentTokenIndex++;

        Token idToken = tokens.get(TokenIndex.currentTokenIndex);

        if (Type.isType(typeToken)) {
            TokenIndex.currentTokenIndex++;

            // getting next token
            //System.out.println("    SECOND:" + idToken.getToken());
            if (idToken.getTokenType() != TokenType.ID_KEYWORD) {
                return null;
            }
        } else {
            idToken = typeToken;
        }

        Identifier id = new Identifier(idToken, scope); // making id
        Token equalsToken = tokens.get(TokenIndex.currentTokenIndex); // looking for = token

        // checking for =
        //System.out.println("    THIRD:" + equalsToken.getToken());
        if (equalsToken.getTokenType() != TokenType.ASSIGN) {
            return null;

        }
        TokenIndex.currentTokenIndex++;

        // checking for expression
        //System.out.println("\tLOOKING FOR EXPR");
        Expr expr = NumExpr.parseExpr(tokens, nestLevel, scope);

        //check for ;
        Token endStmt = tokens.get(TokenIndex.currentTokenIndex);
        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            String message = String.format("Syntax error\nInvalid token. Expected ;. Got: %s\n%s:%s",
                    endStmt.getTokenType().toString(),
                    endStmt.getFilename(),
                    endStmt.getLineNum());
            throw new ParsingException(message);
        }
        TokenIndex.currentTokenIndex++;

        // if successful assignment statement
        if (Type.isType(typeToken)) {
            return new AsmtStmt(type, id, expr, scope);
        }
        return new AsmtStmt(null, id, expr, scope);
    }

    /**
     * Ensure the assignment statement code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {


        // 1) check that id for var is good
        Identifier.check(identifier.id);

        // 2) check if we are looking at a : type id = expr  __OR__   id = expr

        // 2) if we have : type id = val;
        if (type != null) {

            // check that we have already defined this var ??????????????? scope??????
            if (!ValidateTable.getScope(scope).variables.containsKey(identifier.convertToJott())) {


                this.expr.validateTree();

                // we had a change in type
                // we change the type
                if (expr.expr.type != null) {
                    expr.type = expr.expr.type;
                }
                // see that type of left = type of right for function

                System.out.println(this.type.type + " " + expr.type);

                if (type.type.equals(expr.type)) {
                    ValidateTable.getScope(scope).variables.put(identifier.convertToJott(), new ArrayList<>() {{
                        add(type.type);
                        add(expr.convertToJott());
                    }});
                    return true;
                }
                // Failure
                String msg = "variable "+identifier.convertToJott()+" being assigned wrong type";
                String fileName = identifier.id.getFilename();
                int lineNum = identifier.id.getLineNum();
                throw new ParsingException(String.format(String.format("SemanticError:\n %s\n%s:%d",msg,fileName,lineNum)));
            } else {
                // Failure
                String msg = "variable "+identifier.convertToJott()+" is already defined in scope";
                String fileName = identifier.id.getFilename();
                int lineNum = identifier.id.getLineNum();
                throw new ParsingException(String.format(String.format("SemanticError:\n %s\n%s:%d",msg,fileName,lineNum)));            }
        } else {
            // ELSE if we have:    id = val;
            // updating value already in table
            //1) check that the var in question exists
            String varId = identifier.convertToJott();

            if (!ValidateTable.getScope(scope).variables.containsKey(varId)) {
                // Failure
                String msg = "variable "+varId+" does not exist";
                String fileName = identifier.id.getFilename();
                int lineNum = identifier.id.getLineNum();
                String SemanticErrorMsg = String.format("SemanticError:\n %s\n%s:%d",msg,fileName,lineNum);
                throw new ParsingException(String.format(SemanticErrorMsg));
            }

            //2) get type var tpye and check its being assigned the same type
            // get type var tpye and check its being assigned the same type
            String varType = ValidateTable.getScope(scope).variables.get(identifier.convertToJott()).get(0);

            expr.validateTree();
            if (expr.expr.type != null) {
                expr.type = expr.expr.type;
            }

            if (expr.type.equals(varType)) {
                ValidateTable.getScope(scope).variables.get(identifier.convertToJott()).set(1, expr.convertToJott());
                return true;
            }

            // Failure
            String msg = "variable "+varId+" being assigned wrong type";
            String fileName = identifier.id.getFilename();
            int lineNum = identifier.id.getLineNum();
            String SemanticErrorMsg = String.format("SemanticError:\n %s\n%s:%d",msg,fileName,lineNum);
            throw new ParsingException(String.format(SemanticErrorMsg));

        }
    }
}

