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
public class NumExpr extends Expr {

    private FuncCall functionCall;
    private NumType num;
    private Token mathOp;
    private ArrayList<NumExpr> finalexp = null;

    // ---------------------- constructors for different cases --------------------------------

    public NumExpr(NumType num, Token mathOp) {
        super(null);
        this.num = num;
        this.mathOp = mathOp;
        this.functionCall = null;
    }

    public NumExpr(NumType num) {
        super(null);
        this.num = num;
        this.mathOp = null;
        this.functionCall = null;

    }

    public NumExpr(FuncCall call, Token mathOp) {
        super(null);
        this.num = null;

        this.functionCall = call;
        this.mathOp = mathOp;
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
        Token possible_num = tokens.get(TOKEN_IDX.index);
        Token possible_op = tokens.get(TOKEN_IDX.index + 1);

        if ((possible_num.getTokenType() == TokenType.NUMBER || possible_num.getTokenType() == TokenType.ID_KEYWORD) && possible_op.getTokenType() == TokenType.MATH_OP) {
            TOKEN_IDX.index += 2;

            // ---------------------- numExpr can be id,id op, num,num op --------------------

            expLst.add(new NumExpr(new NumType(possible_num), possible_op));

            // ---------------------- trying to complete id/int op with valid follow ---------
            System.out.println("    going again int/id op");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }


        // ---------------------- check for function call op --------------------------------
        FuncCall call = FuncCall.ParseFuncCall(tokens, nestLevel);
        possible_op = tokens.get(TOKEN_IDX.index);

        if (call != null && possible_op.getTokenType() == TokenType.MATH_OP) {
            TOKEN_IDX.index++;
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
            TOKEN_IDX.index++;
            expLst.add(new NumExpr(new NumType(possible_num)));
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
            if (n.num != null && n.mathOp != null) {
                jstr.append(n.num.convertToJott() + " " + n.mathOp.getToken() + " ");
            } else if (n.functionCall != null && n.mathOp != null) {
                jstr.append(n.functionCall.convertToJott() + " " + n.mathOp.getToken() + " ");
            } else if (n.functionCall == null && n.mathOp == null) {
                jstr.append(n.num.convertToJott() + " ");
            } else if (n.functionCall != null) {
                jstr.append(n.functionCall.convertToJott() + " ");
            }
        }

        return jstr.toString();
    }

    public boolean validateTree() {



        return false;
    }

}
