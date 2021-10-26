package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class NumExpr extends Expr {

    private FuncCall funcCall;
    private Token num;
    private Token mathop;
    private ArrayList<NumExpr> finalexp = null;

    // ---------------------- constructors for different cases --------------------------------

    public NumExpr(Token num, Token mathop) {
        super(null);
        this.num = num;
        this.mathop = mathop;
        this.funcCall = null;

    }

    public NumExpr(Token num) {
        super(null);
        this.num = num;
        this.mathop = null;
        this.funcCall = null;

    }

    public NumExpr(FuncCall call, Token mathop) {
        super(null);
        this.num = null;

        this.funcCall = call;
        this.mathop = mathop;
    }

    public NumExpr(ArrayList<NumExpr> finalExp) {
        super(null);
        this.finalexp = finalExp;
    }


    //i_expr ->            id|int|
    //                    int op int|
    //                    int op i_expr|
    //                    i_expr op int|
    //                    i_expr op i_expr|
    //                    func_call

    private static ArrayList<NumExpr> parseNumExpr_r(ArrayList<Token> tokens, int nestLevel, ArrayList<NumExpr> expLst) throws ParsingException {

        // ---------------------- looking for id/int followed by math op --------------------------------
        Token possible_num = tokens.get(TOKEN_IDX.IDX);
        Token possible_op = tokens.get(TOKEN_IDX.IDX + 1);

        if ((possible_num.getTokenType() == TokenType.NUMBER || possible_num.getTokenType() == TokenType.ID_KEYWORD) && possible_op.getTokenType() == TokenType.MATH_OP) {
            TOKEN_IDX.IDX += 2;

            // ---------------------- numExpr can be id,id op, num,num op --------------------

            expLst.add(new NumExpr(possible_num, possible_op));

            // ---------------------- trying to complete id/int op with valid follow ---------
            System.out.println("    going again int/id op");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }


        // ---------------------- check for function call op --------------------------------
        FuncCall call = FuncCall.ParseFuncCall(tokens, nestLevel);
        possible_op = tokens.get(TOKEN_IDX.IDX);

        if (call != null && possible_op.getTokenType() == TokenType.MATH_OP) {
            TOKEN_IDX.IDX++;
            expLst.add(new NumExpr(call, possible_op));
            System.out.println("    going again f(x)");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }
        // ---------------------- check for lone function call ------------------------------

        // check for lone function call
        if (call != null) {
            System.out.println("    Function call found: " + call.convertToJott());
            expLst.add(new NumExpr(call, null));
            return expLst;
        }

        // ---------------------- check for lone id or num ---------------------------------

        if (possible_num.getTokenType() == TokenType.NUMBER || possible_num.getTokenType() == TokenType.ID_KEYWORD) {
            TOKEN_IDX.IDX++;
            expLst.add(new NumExpr(possible_num));
            return expLst;
        }
        // ---------------------error, goes on to try another expr in expr-----------------

        // error
        System.out.println("INCORRECT NUM EXPR");
        return null;
    }


    public static NumExpr parseNumExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        System.out.println("-------------------- parsing num expr --------------------");

        ArrayList<NumExpr> f = parseNumExpr_r(tokens, nestLevel, new ArrayList<NumExpr>());
        if (f == null) {
            return null;
        }
        return new NumExpr(f);
    }

    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (NumExpr n : finalexp) {
            if (n.num != null && n.mathop != null) {
                jstr.append(n.num.getToken() + " " + n.mathop.getToken() + " ");
            } else if (n.funcCall != null && n.mathop != null) {
                jstr.append(n.funcCall.convertToJott() + " " + n.mathop.getToken() + " ");
            } else if (n.funcCall == null && n.mathop == null) {
                jstr.append(n.num.getToken() + " ");
            } else if (n.funcCall != null) {
                jstr.append(n.funcCall.convertToJott() + " ");
            }
        }

        return jstr.toString();
    }

    public boolean validateTree() {
        return false;
    }

}
