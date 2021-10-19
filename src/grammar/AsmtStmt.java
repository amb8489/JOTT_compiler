package grammar;

import java.util.ArrayList;
import main.Token;
import main.TokenType;

public class AsmtStmt extends BodyStmt {

    private final Type type;
    private final Identifier name;
    private final Expr exp;

    public AsmtStmt(int nestLevel,Type type,Identifier name,Expr exp) {
        super(nestLevel);
        this.type = type;
        this.name = name;
        this.exp = exp;
    }



    // the format of asmt is {INDENT}TYPE NAME = EXPR ;
    // where insendt is the number of tabs
    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        jstr.append("     ".repeat(getNestLevel()));
        jstr.append(type.convertToJott() + " ");
        jstr.append(name.convertToJott() + " = ");
        jstr.append(exp.convertToJott()+ ";\n");
        return jstr.toString();
    }




    public static AsmtStmt parseAsmtStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING ASMT-STMT------------------------");

        // removing and checking the first token
        // should be an IDkeyword type

        Token typeToken = tokens.remove(0);
        System.out.println("FIRST:"+typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(),typeToken.getLineNum());

        Token varToken = tokens.remove(0);
        System.out.println("SECOND:"+typeToken.getToken());
        if (varToken.getTokenType() != TokenType.ID_KEYWORD){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected <id>. Got: ");
            sb.append(typeToken.getTokenType().toString()).append("\n");
            sb.append(varToken.getFilename() + ":" +varToken.getLineNum());
            throw new ParsingException(sb.toString());
        }

        Identifier id = new Identifier(varToken.getToken(), varToken.getFilename(),varToken.getLineNum());

        //looking for = token
        Token equalsToken = tokens.remove(0);
        // checking for =
        System.out.println("THIRD:"+typeToken.getToken());
        if (equalsToken.getTokenType() != TokenType.ASSIGN){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected =. Got: ");
            sb.append(equalsToken.getTokenType().toString()).append("\n");
            sb.append(equalsToken.getFilename() + ":" +equalsToken.getLineNum());
            throw new ParsingException(sb.toString());
        }

        // checking for expression
        Expr expr = Expr.parseExpr(tokens,nestLevel);

        // if successful assignment statement
        return new AsmtStmt(nestLevel,type,id,expr) ;
    }




    @Override
    public String convertToJava() {
        return null;
    }


    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
