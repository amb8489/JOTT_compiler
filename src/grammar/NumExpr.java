package grammar;

import main.Token;

import java.util.ArrayList;

public class NumExpr extends Expr{


    public NumExpr() {


    }
    //i_expr -> id|int|int op int|int op i_expr|i_expr op int|i_expr op i_expr|func_call                               <-- DONE

    public static Expr parseNumExpr(ArrayList<Token> tokens, int nestLevel) {
        System.out.println("-------------------- parsing Int expr --------------------");

        // check next two tokens assuming we have two more tokens
        tokens.get(0);
        tokens.get(1);
        // check for num op ELSE check for just id else check fo r





        return new Expr();
    }


    public String convertToJott() {
        return "";
    }




}
