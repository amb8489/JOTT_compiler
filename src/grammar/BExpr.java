package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

////     * b_expr ->
//                 bool|
//               numexp rel_op numexp|
//               s_expr rel_op s_expr|
//               b_expr rel_op b_expr|
public class BExpr extends Expr {
    private ArrayList<BExpr> finalexp = null;

    private Expr exp = null;
    private Token bool = null;
    private Token relOp = null;
    // ---------------------- constructors for different cases --------------------------------


    public BExpr(ArrayList<BExpr> finalExp) {
        super(null);
        this.finalexp = finalExp;
    }

    public BExpr(Token bool) {
        super(null);
        this.bool = bool;
    }

    public BExpr(Token bool, Token relOP) {
        super(null);
        this.bool = bool;
        this.relOp = relOP;
    }

    public BExpr(Expr expr, Token relOP) {
        super(null);
        this.relOp = relOP;
        this.exp = expr;
    }

    public BExpr(Expr expr) {
        super(null);
        this.exp = expr;

    }


    public static ArrayList<BExpr> parseBExpr_r(ArrayList<Token> tokens, ArrayList<BExpr> blst, int nestLevel) throws ParsingException {


        Token possible_bool = tokens.get(TOKEN_IDX.IDX);
        boolean isBool = false;
        Expr possible_Expr = null;


        if ("true false".contains(possible_bool.getToken())) {
            TOKEN_IDX.IDX++;
            isBool = true;
        } else {
            possible_Expr = NumExpr.parseNumExpr(tokens, nestLevel);
            if (possible_Expr == null) {
                possible_Expr = SExpr.parseSExpr(tokens, nestLevel);
                if (possible_Expr == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Syntax error\nInvalid token. Expected expr. Got: ");
                    sb.append(possible_bool.getTokenType().toString()).append("\n");
                    sb.append(possible_bool.getFilename() + ":" + possible_bool.getLineNum());
                    throw new ParsingException(sb.toString());
                }
            }
        }
        Token possible_relOP = tokens.get(TOKEN_IDX.IDX);

        if (isBool) {
            if (possible_relOP.getTokenType() == TokenType.REL_OP) {
                TOKEN_IDX.IDX++;

                System.out.println("bool op, going again: " + tokens.get(TOKEN_IDX.IDX).getToken());
                blst.add(new BExpr(possible_bool, possible_relOP));
                return parseBExpr_r(tokens, blst, nestLevel);
            }
            // lone bool
            System.out.println("lone bool");
            blst.add(new BExpr(possible_bool));
            return blst;
        } else {
            if (possible_relOP.getTokenType() == TokenType.REL_OP) {
                TOKEN_IDX.IDX++;

                System.out.println("expr op, going again: " + tokens.get(TOKEN_IDX.IDX).getToken());
                blst.add(new BExpr(possible_Expr, possible_relOP));
                return parseBExpr_r(tokens, blst, nestLevel);
            }
            // lone expr
            System.out.println("lone expr");

            blst.add(new BExpr(possible_Expr));
            return blst;
        }
    }

    public static Expr parseBExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        System.out.println("-------------------- parsing BOOl expr --------------------");

        ArrayList<BExpr> f = parseBExpr_r(tokens, new ArrayList<BExpr>(), nestLevel);

        return new BExpr(f);
    }


    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (BExpr n : finalexp) {

            // bool relop
            if (n.bool != null && n.relOp != null) {
                jstr.append(n.bool.getToken() + " " + n.relOp.getToken() + " ");
            }

            // bool
            if (n.bool != null && n.relOp == null) {
                jstr.append(n.bool.getToken() + " ");
            }


            // exp relop
            if (n.exp != null && n.relOp != null) {
                jstr.append(n.exp.convertToJott() + n.relOp.getToken() + " ");
            }

            // exp
            if (n.exp != null && n.relOp == null) {
                jstr.append(n.exp.convertToJott() + " ");
            }
        }

        return jstr.toString();
    }

    public boolean validateTree() {
        return false;
    }
}
