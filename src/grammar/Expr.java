package grammar;

import main.Token;
import main.TokenType;
import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Expr {
    Expr expression;

    /**
     * Constructor TODO
     * @param expression TODO
     */
    public Expr(Expr expression) {
        this.expression = expression;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Expr parseExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING expr-----------------------------");

        // ---------------------------looking for numExpr (int or dbl)----------------------------------------

        if (tokens.get(TOKEN_IDX.index + 1).getTokenType() != TokenType.REL_OP) {
            NumExpr numExp = NumExpr.parseNumExpr(tokens, nestLevel);

            if (numExp != null) {
                return new Expr(numExp);
            }

            // ---------------------------looking for s_expr (string expr)----------------------------------------

            // if string lit id or funcion call
            Expr s_expr = SExpr.parseSExpr(tokens, nestLevel);
            if (s_expr != null) {
                return new Expr(s_expr);
            }
        }
        // ---------------------------looking for b_expr (bool expr)----------------------------------------

        Expr b_expr = BExpr.parseBExpr(tokens, nestLevel);
        if (b_expr != null) {
            return new Expr(b_expr);
        }


        // ---------------------------error :( no valid expr found----------------------------------------

        Token t = tokens.get(TOKEN_IDX.index);
        StringBuilder sb = new StringBuilder();
        sb.append("Syntax error\nInvalid token. Expected Expr. Got: ");
        sb.append(t.getTokenType().toString()).append("\n");
        sb.append(t.getFilename() + ":" + t.getLineNum());
        throw new ParsingException(sb.toString());

    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        return expression.convertToJott();
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }

}
