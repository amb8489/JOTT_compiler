package grammar;

import java.util.ArrayList;

import main.Token;
import main.TokenType;

public class AsmtStmt {

    private final Type type;
    private final Identifier name;
    private final Expr exp;

    public AsmtStmt(int nestLevel, Type type, Identifier name, Expr exp) {
        this.type = type;
        this.name = name;
        this.exp = exp;
    }


    // the format of asmt is {INDENT}TYPE NAME = EXPR ;
    // where insendt is the number of tabs
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        jstr.append("     ".repeat(0));
        if (!type.convertToJott().equals(name.convertToJott())) {
            jstr.append(type.convertToJott() + " ");
        }
        jstr.append(name.convertToJott() + " = ");
        jstr.append(exp.convertToJott() + ";\n");
        return jstr.toString();
    }


    public static AsmtStmt parseAsmtStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING ASMT-STMT------------------------");

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    FIRST:" + typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
        TOKEN_IDX.IDX++;


        Token idToken = tokens.get(TOKEN_IDX.IDX);


        if (Type.isType(typeToken)) {
            TOKEN_IDX.IDX++;


            // getting next token
            System.out.println("    SECOND:" + idToken.getToken());
            if (idToken.getTokenType() != TokenType.ID_KEYWORD) {
                return null;
            }
        } else {
            idToken = typeToken;
        }

        // making id
        Identifier id = new Identifier(idToken);


        //looking for = token
        Token equalsToken = tokens.get(TOKEN_IDX.IDX);

        // checking for =
        System.out.println("    THIRD:" + equalsToken.getToken());
        if (equalsToken.getTokenType() != TokenType.ASSIGN) {
            return null;

        }
        TOKEN_IDX.IDX++;


        // checking for expression
        System.out.println("    LOOKING FOR EXPR");
        Expr expr = NumExpr.parseExpr(tokens, nestLevel);

        System.out.println("----------------22----"+expr);

        //check for ;
        Token endStmt = tokens.get(TOKEN_IDX.IDX);
        if (endStmt.getTokenType() != TokenType.SEMICOLON) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected ;. Got: ");
            sb.append(endStmt.getTokenType().toString()).append("\n");
            sb.append(endStmt.getFilename() + ":" + endStmt.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;

        // if successful assignment statement
        return new AsmtStmt(nestLevel, type, id, expr);
    }


}
