package grammar;

import main.Token;

import java.util.ArrayList;

public class SExpr extends Expr {

    public SExpr(Expr e) {
        super(e);
    }

    public static Expr parseSExpr(ArrayList<Token> tokens, int nestLevel) {
        return null;
    }
}
