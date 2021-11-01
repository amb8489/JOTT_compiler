package grammar;

import main.Token;
import main.TokenType;
import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)

 */
public class Expr {
    public Expr expr;
    public String type;

    /**
     * Constructor TODO
     * @param expr TODO
     */
    public Expr(Expr expr, String type) {
        this.expr = expr;
        this.type = type;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Expr parseExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("------------------------PARSING expr-----------------------------");

        // ---------------------------looking for numExpr (int or dbl)----------------------------------------

        if (tokens.get(TOKEN_IDX.index + 1).getTokenType() != TokenType.REL_OP) {
            NumExpr numExpr = NumExpr.parseNumExpr(tokens, nestLevel);

            if (numExpr != null) {
                return new Expr(numExpr, numExpr.exprType);
            }

            // ---------------------------looking for sExpr (string expr)----------------------------------------

            // if string lit id or funcion call
            Expr sExpr = SExpr.parseSExpr(tokens, nestLevel);
            if (sExpr != null) {
                return new Expr(sExpr,"String");
            }

        }
        // ---------------------------looking for bExpr (bool expr)----------------------------------------

        Expr bExpr = BExpr.parseBExpr(tokens, nestLevel);
        if (bExpr != null) {
            return new Expr(bExpr,"Boolean");
        }


        // ---------------------------error :( no valid expr found----------------------------------------

        Token t = tokens.get(TOKEN_IDX.index);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Syntax error\nInvalid token. Expected Expr. Got: ");
        stringBuilder.append(t.getTokenType().toString()).append("\n");
        stringBuilder.append(t.getFilename() + ":" + t.getLineNum());
        throw new ParsingException(stringBuilder.toString());

    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        return expr.convertToJott();
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() throws ParsingException {
        return expr.validateTree();
    }

    public void setType(String type) {
        this.type = type;
    }
}
