package grammar;

import main.Token;

import java.util.ArrayList;

public class Expr {
    Expr e;
    public Expr(Expr e) {
        this.e = e;
    }


    //     * expr ->
    //     i_expr|d_expr| start with type
    //     s_expr|
    //     b_expr|
    //     id
    //     |func_call


    public static Expr parseExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING expr------------------------");
            // int or double
        NumExpr numExp = NumExpr.parseNumExpr(tokens, nestLevel);

        if (numExp!=null){
            return new Expr(numExp);
        }

        Expr b_expr = BExpr.parseBExpr(tokens, nestLevel);
        if (b_expr!=null){
            return new Expr(b_expr);
        }

        // if string lit id or funcion call
        Expr s_expr = SExpr.parseSExpr(tokens, nestLevel);
        if (b_expr!=null){
            return new Expr(s_expr);
        }



        Token t = tokens.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append("Syntax error\nInvalid token. Expected Expr. Got: ");
        sb.append(t.getTokenType().toString()).append("\n");
        sb.append(t.getFilename() + ":" +t.getLineNum());
        throw new ParsingException(sb.toString());

    }

        public String convertToJott() {
            return e.convertToJott();
    }
}
