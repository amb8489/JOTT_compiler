package grammar;

import main.Token;

import java.util.ArrayList;

public class BExpr extends Expr {
    public BExpr(Expr b_expr) {
        super(b_expr);
    }

    public static Expr parseBExpr(ArrayList<Token> tokens, int nestLevel) {
        return null;
    }
}
