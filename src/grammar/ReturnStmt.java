package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//     return_stmt -> return expr end_stmt
public class ReturnStmt extends Body {
    private final Expr exp;

    public ReturnStmt(int nestLevel,Expr exp) {
        super(nestLevel);
        this.exp = exp;
    }

    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        jstr.append("     ".repeat(getNestLevel()));
        jstr.append("return ");
        jstr.append(exp.convertToJott()+ ";\n");
        return jstr.toString();
    }




    public static ReturnStmt parseReturnStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Return Stmt------------------------");



        // ---------------------- check for return ------------------------------------
        Token returnToken = tokens.remove(0);
        System.out.println("    FIRST:"+returnToken.getToken());
        if (! returnToken.getToken().equals("return")){
            return null;
        }



        // ---------------------- check for expr ------------------------------------
        // checking for expression
        System.out.println("    LOOKING FOR EXPR");
        Expr expr = Expr.parseExpr(tokens,nestLevel);



        // ---------------------- check for end statment ------------------------------------

        //check for ;
        Token endStmt = tokens.remove(0);

        if (endStmt.getTokenType() != TokenType.SEMICOLON){
            return null;
        }


        // ---------------------- DONE ------------------------------------
        return new ReturnStmt(nestLevel,expr);

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
