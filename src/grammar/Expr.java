package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Expr is a general expression that could potentially represent any of these:
 * 1) an integer expression (NumExpr <-- can handle integers)
 * 2) a double expression (NumExpr <-- can handle doubles)
 * 3) a string expression (SExpr)
 * 4) a boolean expression (BExpr)
 * 5) id
 * 6) a function call
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 */
public class Expr {
    public Expr expr;
    public String type;

    /**
     * This is a constructor for an expression.
     *
     * @param expr an expression that could be BExpr, DExpr
     * @param type TODO
     */
    public Expr(Expr expr, String type) {
        this.expr = expr;
        this.type = type;
    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Expr parseExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        // looking for a numExpr (either integer or double)
        if (tokens.get(TokenIndex.currentTokenIndex + 1).getTokenType() != TokenType.REL_OP) {
            NumExpr numExpr = NumExpr.parseNumExpr(tokens, nestLevel);

            if (numExpr != null) {
                return new Expr(numExpr, numExpr.exprType);
            }

            // looking for a string expression

            // determine whether this string is a literal id or a function call
            Expr sExpr = SExpr.parseSExpr(tokens, nestLevel);
            if (sExpr != null) {
                return new Expr(sExpr, "String");
            }

        }

        // looking  for a boolean expression
        Expr bExpr = BExpr.parseBExpr(tokens, nestLevel);
        return new Expr(bExpr, "Boolean");

        // throw an error, no valid expression found
    }

    /**
     * This is a setter for the field type
     *
     * @param type the value to set type field to
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        return expr.convertToJott();
    }


    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return null;
    }

    /**
     * Ensure the code in the expression is valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        return expr.validateTree();
    }
}
