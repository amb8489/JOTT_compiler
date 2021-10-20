package grammar;

import main.Token;

import java.util.ArrayList;

public class Expr {
    public Expr() {



    }

    //     * expr ->
    //     i_expr|d_expr| start with type
    //     s_expr|
    //     b_expr|
    //     id
    //     |func_call


    public static Expr parseExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

            // int or double
        Expr numExp = NumExpr.parseNumExpr(tokens, nestLevel);

        if (numExp!=null){
            return new Expr();
        }

        Expr b_expr = BExpr.parseBExpr(tokens, nestLevel);
        if (b_expr!=null){
            return new Expr();
        }

        // if string lit id or funcion call
        Expr s_expr = SExpr.parseSExpr(tokens, nestLevel);
        if (b_expr!=null){
            return new Expr();
        }









        return new Expr();
    }

        public String convertToJott() {
        return "NOT FILLED IN YET";
    }
}
