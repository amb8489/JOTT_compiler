package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class NumExpr extends Expr {

    private FuncCall funcCall;
    private Token num;
    private Token mathop;

    private ArrayList<NumExpr> finalexp = null;

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

        // check next two tokens assuming we have two more tokens
        Token possible_num = tokens.get(0);
        Token possible_op = tokens.get(1);

        System.out.println(possible_num.getToken());
        System.out.println(possible_op.getToken());

        // check for num/id op ELSE check for just id else check for
        if ((possible_num.getTokenType() == TokenType.NUMBER || possible_num.getTokenType() == TokenType.ID_KEYWORD) && possible_op.getTokenType() == TokenType.MATH_OP) {
            tokens.remove(0);
            tokens.remove(0);

            expLst.add(new NumExpr(possible_num, possible_op));

            // go again
            System.out.println("    going again int/id op");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }


        // check for funccall op
        FuncCall call = FuncCall.ParseFuncCall(tokens, nestLevel);


        possible_op = tokens.get(0);

        if (call != null && possible_op.getTokenType() == TokenType.MATH_OP) {
            tokens.remove(0);
            expLst.add(new NumExpr(call, possible_op));
            System.out.println("    going again f(x)");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }


        // check for lone function call
        if (call != null) {
            System.out.println("    Function call found: " + call.convertToJott());

            expLst.add(new NumExpr(call, null));
            return expLst;
        }

        // lone number done or lone id

        if (possible_num.getTokenType() == TokenType.NUMBER || possible_num.getTokenType() == TokenType.ID_KEYWORD) {
            tokens.remove(0);
            expLst.add(new NumExpr(possible_num));
            return expLst;
        }

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
            } else if (n.funcCall != null && n.mathop == null) {
                jstr.append(n.funcCall.convertToJott() + " ");
            } else if (n.funcCall != null && n.mathop == null) {
                jstr.append(n.funcCall.convertToJott() + " ");
            }
        }

        return jstr.toString();
    }


}
