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

    public FuncCall functionCall;
    public NumType numType;
    public Token mathOp;
    public ArrayList<NumExpr> finalExpression;
    public String expType;
    // ---------------------- constructors for different cases --------------------------------

    public NumExpr(NumType numType, Token mathOp) {
        super(null,null);
        this.numType = numType;
        this.mathOp = mathOp;
        this.functionCall = null;
    }

    public NumExpr(NumType numType) {
        super(null,null);

        this.numType = numType;
        this.mathOp = null;
        this.functionCall = null;

    }

    public NumExpr(FuncCall call, Token mathOp) {
        super(null,null);
        this.numType = null;

        this.functionCall = call;
        this.mathOp = mathOp;
    }

    public NumExpr(ArrayList<NumExpr> finalExp,String ExpType) {
        super(null,null);
        this.finalExpression = finalExp;
        this.expType = ExpType;
        this.numType = null;
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
            ////System.out.println("    going again int/id op");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }


        // ---------------------- check for function call op --------------------------------
        FuncCall call = FuncCall.ParseFuncCall(tokens, nestLevel);
        possible_op = tokens.get(TOKEN_IDX.index);

        if (call != null && possible_op.getTokenType() == TokenType.MATH_OP) {
            TOKEN_IDX.index++;
            expLst.add(new NumExpr(call, possible_op));
            ////System.out.println("    going again f(x)");
            return parseNumExpr_r(tokens, nestLevel, expLst);
        }
        // ---------------------- check for lone function call ------------------------------

        // check for lone function call
        if (call != null) {
            ////System.out.println("    Function call found: " + call.convertToJott());
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
        ////System.out.println("INCORRECT NUM EXPR");
        return null;
    }


    public static NumExpr parseNumExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        ////System.out.println("-------------------- parsing num expr --------------------");

        ArrayList<NumExpr> expLst = parseNumExpr_r(tokens, nestLevel, new ArrayList<NumExpr>());
        if(expLst == null){
            return null;
        }

        boolean isINTexp = true;



        for(NumExpr exp : expLst){
            if (exp.numType != null && exp.numType.getNumType() != null) {
                if (!exp.numType.numType.equals("Integer")) {
                    isINTexp = false;
                    break;
                }
            }
        }

        if(!isINTexp){
            int i = 0;
            for(NumExpr exp : expLst){
                if (exp.numType != null && exp.numType.getNumType() != null) {
                    if (!exp.numType.numType.equals("Double")) {
                        isINTexp = true;
                        break;
                    }
                }
                i++;

            }
            if (isINTexp){

                throw new ParsingException("expression with int op double not allowed, line: "+expLst.get(i).numType.number.getLineNum());
            }
        }
        NumExpr b = new NumExpr(expLst,null);
        ////System.out.println("88999 "+b.convertToJott());

        String ExpType = isINTexp ? "Integer":"Double";


        return new NumExpr(expLst,ExpType);
    }

    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (NumExpr n : finalExpression) {
            if (n.numType != null && n.mathOp != null) {
                jstr.append(n.numType.convertToJott() + " " + n.mathOp.getToken() + " ");
            } else if (n.functionCall != null && n.mathOp != null) {
                jstr.append(n.functionCall.convertToJott() + " " + n.mathOp.getToken() + " ");
            } else if (n.functionCall == null && n.mathOp == null) {
                jstr.append(n.numType.convertToJott() + " ");
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
