package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * BExpr is a boolean expression.
 * <p>
 * BExpr can be
 * 1) bool
 * 2) numExpr rel_op numExpr
 * 3) sExpr rel_op sExpr
 * 4) bExpr rel_op bExpr
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class BExpr extends Expr {
    private ArrayList<BExpr> finalExpr = null;
    private Expr expr;
    private Token bool;
    private Token relOp;
    public String scope;

    /**
     * This is the constructor for a boolean expression class.
     *
     * @param finalExp TODO blah
     */
    public BExpr(ArrayList<BExpr> finalExp,String scope) {
        super(null, null,null);
        this.finalExpr = finalExp;
        this.scope = scope;

    }

    /**
     * Constructor TODO
     *
     * @param bool TODO blah
     */
    public BExpr(Token bool,String scope) {
        super(null, null,null);
        this.bool = bool;
        this.scope = scope;

    }

    /**
     * Constructor TODO
     *
     * @param bool  TODO blah
     * @param relOp TODO blah
     */
    public BExpr(Token bool, Token relOp,String scope) {
        super(null, null,null);
        this.bool = bool;
        this.relOp = relOp;
        this.scope = scope;

    }

    /**
     * Constructor
     *
     * @param expr  TODO blah
     * @param relOP TODO blah
     */
    public BExpr(Expr expr, Token relOP,String scope) {
        super(null, null,null);
        this.relOp = relOP;
        this.expr = expr;
        this.scope = scope;

    }

    /**
     * Constructor
     *
     * @param expr TODO blah
     */
    public BExpr(Expr expr,String scope) {
        super(null, null,null);
        this.expr = expr;
        this.scope = scope;
    }

    /**
     * TODO
     *
     * @param tokens      TODO
     * @param booleanList TODO
     * @param nestLevel   TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static ArrayList<BExpr> parseBExpr_r(int nestLevel, ArrayList<Token> tokens, ArrayList<BExpr> booleanList,String scope)
            throws ParsingException {
        Token possibleBool = tokens.get(TokenIndex.currentTokenIndex);
        boolean isBool = false;
        Expr possibleExpr = null;

        if ("true false".contains(possibleBool.getToken())) {
            TokenIndex.currentTokenIndex++;
            isBool = true;
        } else {
            possibleExpr = NumExpr.parseNumExpr(tokens, nestLevel,scope);

            if (possibleExpr == null) {
                possibleExpr = SExpr.parseSExpr(tokens, nestLevel,scope);
                if (possibleExpr == null) {
                    String message = String.format("Syntax error\nInvalid token. Expected ;. Got: %s\n%s:%s",
                            possibleBool.getTokenType().toString(),
                            possibleBool.getFilename(),
                            possibleBool.getLineNum());
                    throw new ParsingException(message);
                }
            }
        }
        Token possibleRelOp = tokens.get(TokenIndex.currentTokenIndex);

        if (isBool) {
            if (possibleRelOp.getTokenType() == TokenType.REL_OP) {
                TokenIndex.currentTokenIndex++;

                //System.out.printf("bool op, going again: %s%n", tokens.get(TOKEN_IDX.index).getToken());
                booleanList.add(new BExpr(possibleBool, possibleRelOp, scope));
                return parseBExpr_r(nestLevel, tokens, booleanList, scope);
            }

            // lone bool
            //System.out.println("lone bool");
            booleanList.add(new BExpr(possibleBool, scope));
            return booleanList;

        } else {
            if (possibleRelOp.getTokenType() == TokenType.REL_OP) {
                TokenIndex.currentTokenIndex++;

                //System.out.printf("expr op, going again: %s%n", tokens.get(TOKEN_IDX.index).getToken());
                booleanList.add(new BExpr(possibleExpr, possibleRelOp, scope));
                return parseBExpr_r(nestLevel, tokens, booleanList, scope);
            }
            // lone expr
            //System.out.println("lone expr");

            booleanList.add(new BExpr(possibleExpr, scope));
            return booleanList;
        }
    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Expr parseBExpr(ArrayList<Token> tokens, int nestLevel,String scope) throws ParsingException {

        //System.out.println("-------------------- parsing bool expr --------------------");

        ArrayList<BExpr> f = parseBExpr_r(nestLevel, tokens, new ArrayList<>(), scope);

        return new BExpr(f, scope);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        for (BExpr n : finalExpr) {
            // bool relOp
            if (n.bool != null && n.relOp != null) {
                jottString.append(String.format("%s %s", n.bool.getToken(), n.relOp.getToken()));
            }

            // bool
            if (n.bool != null && n.relOp == null) {
                jottString.append(String.format("%s ", n.bool.getToken()));
            }

            // exp relOp
            if (n.expr != null && n.relOp != null) {
                jottString.append(String.format("%s%s ", n.expr.convertToJott(), n.relOp.getToken()));
            }

            // exp
            if (n.expr != null && n.relOp == null) {
                jottString.append(String.format("%s ", n.expr.convertToJott()));
            }
        }

        return jottString.toString();
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
     * Ensure the boolean expression code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        return false;
    }
}
