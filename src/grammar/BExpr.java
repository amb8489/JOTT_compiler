package grammar;

import main.Token;
import main.TokenType;
import java.util.ArrayList;

////     * b_expr ->
//                 bool|
//               numexp rel_op numexp|
//               s_expr rel_op s_expr|
//               b_expr rel_op b_expr| TODO: what are those

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class BExpr extends Expr {
    private ArrayList<BExpr> finalexp = null;

    private Expr exp;
    private Token bool;
    private Token relOp;
    // ---------------------- constructors for different cases --------------------------------

    /**
     * Constructor TODO
     * @param finalExp TODO blah
     */
    public BExpr(ArrayList<BExpr> finalExp) {
        super(null);
        this.finalexp = finalExp;
    }

    /**
     * Constructor TODO
     * @param bool TODO blah
     */
    public BExpr(Token bool) {
        super(null);
        this.bool = bool;
    }

    /**
     * Constructor TODO
     * @param bool TODO blah
     * @param relOp TODO blah
     */
    public BExpr(Token bool, Token relOp) {
        super(null);
        this.bool = bool;
        this.relOp = relOp;
    }

    /**
     * Constructor
     * @param expr TODO blah
     * @param relOP TODO blah
     */
    public BExpr(Expr expr, Token relOP) {
        super(null);
        this.relOp = relOP;
        this.exp = expr;
    }

    /**
     * Constructor
     * @param expr TODO blah
     */
    public BExpr(Expr expr) {
        super(null);
        this.exp = expr;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param booleanList TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static ArrayList<BExpr> parseBExpr_r(ArrayList<Token> tokens, ArrayList<BExpr> booleanList, int nestLevel)
            throws ParsingException {
        Token possibleBool = tokens.get(TOKEN_IDX.index);
        boolean isBool = false;
        Expr possibleExpr = null;

        if ("true false".contains(possibleBool.getToken())) {
            TOKEN_IDX.index++;
            isBool = true;
        } else {
            possibleExpr = NumExpr.parseNumExpr(tokens, nestLevel);

            if (possibleExpr == null) {
                possibleExpr = SExpr.parseSExpr(tokens, nestLevel);
                if (possibleExpr == null) {
                    String message = String.format("Syntax error\nInvalid token. Expected ;. Got: %s\n%s:%s",
                            possibleBool.getTokenType().toString(),
                            possibleBool.getFilename(), 
                            possibleBool.getLineNum());
                    throw new ParsingException(message);
                }
            }
        }
        Token possibleRelOp = tokens.get(TOKEN_IDX.index);

        if (isBool) {
            if (possibleRelOp.getTokenType() == TokenType.REL_OP) {
                TOKEN_IDX.index++;
                
                System.out.printf("bool op, going again: %s%n", tokens.get(TOKEN_IDX.index).getToken());
                booleanList.add(new BExpr(possibleBool, possibleRelOp));
                return parseBExpr_r(tokens, booleanList, nestLevel);
            }
            
            // lone bool
            System.out.println("lone bool");
            booleanList.add(new BExpr(possibleBool));
            return booleanList;
            
        } else {
            if (possibleRelOp.getTokenType() == TokenType.REL_OP) {
                TOKEN_IDX.index++;

                System.out.printf("expr op, going again: %s%n", tokens.get(TOKEN_IDX.index).getToken());
                booleanList.add(new BExpr(possibleExpr, possibleRelOp));
                return parseBExpr_r(tokens, booleanList, nestLevel);
            }
            // lone expr
            System.out.println("lone expr");

            booleanList.add(new BExpr(possibleExpr));
            return booleanList;
        }
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Expr parseBExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        System.out.println("-------------------- parsing bool expr --------------------");

        ArrayList<BExpr> f = parseBExpr_r(tokens, new ArrayList<BExpr>(), nestLevel);

        return new BExpr(f);
    }

    /**
     * TODO
     * @return TODO
     */
    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        for (BExpr n : finalexp) {
            // bool relOp
            if (n.bool != null && n.relOp != null) {
                jottString.append(String.format("%s %s", n.bool.getToken(), n.relOp.getToken()));
            }

            // bool
            if (n.bool != null && n.relOp == null) {
                jottString.append(String.format("%s ", n.bool.getToken()));
            }

            // exp relOp
            if (n.exp != null && n.relOp != null) {
                jottString.append(String.format("%s%s ", n.exp.convertToJott(), n.relOp.getToken()));
            }

            // exp
            if (n.exp != null && n.relOp == null) {
                jottString.append(String.format("%s ", n.exp.convertToJott()));
            }
        }

        return jottString.toString();
    }

    /**
     * TODO
     * @return
     */
    public boolean validateTree() { return false; }
}
