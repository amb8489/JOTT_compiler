package grammar;

import main.Token;
import main.TokenType;

import java.text.ParseException;
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
    public NumType num;
    public Token mathOp;
    public ArrayList<NumExpr> finalexp;
    public String ExpType;
    // ---------------------- constructors for different cases --------------------------------

    public NumExpr(NumType num, Token mathOp) {
        super(null, null);
        this.num = num;
        this.mathOp = mathOp;
        this.functionCall = null;
    }

    public NumExpr(NumType num) {
        super(null, null);

        this.num = num;
        this.mathOp = null;
        this.functionCall = null;

    }

    public NumExpr(FuncCall call, Token mathOp) {
        super(null, null);
        this.num = null;
        this.functionCall = call;
        this.mathOp = mathOp;
    }

    public NumExpr(ArrayList<NumExpr> finalExp, String ExpType) {
        super(null, null);
        this.finalexp = finalExp;
        this.ExpType = ExpType;
        this.num = null;
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
//            System.out.println("    Function call found: " + call.convertToJott());
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
        if (expLst == null) {
            return null;
        }

        boolean isINTexp = true;


        for (NumExpr exp : expLst) {
            if (exp.num != null && exp.num.getNumType() != null) {
                if (!exp.num.numType.equals("Integer")) {
                    isINTexp = false;
                    break;
                }
            }
        }

        if (!isINTexp) {
            int i = 0;
            for (NumExpr exp : expLst) {
                if (exp.num != null && exp.num.getNumType() != null) {
                    if (!exp.num.numType.equals("Double")) {
                        isINTexp = true;
                        break;
                    }
                }
                i++;

            }
            if (isINTexp) {

                throw new ParsingException("expression with int op double not allowed, line: " + expLst.get(i).num.number.getLineNum());
            }
        }

        String ExpType = isINTexp ? "Integer" : "Double";


        return new NumExpr(expLst, ExpType);
    }

    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (NumExpr n : finalexp) {
            if (n.num != null && n.mathOp != null) {
                jstr.append(n.num.convertToJott() + "" + n.mathOp.getToken() + "");
            } else if (n.functionCall != null && n.mathOp != null) {
                jstr.append(n.functionCall.convertToJott() + "" + n.mathOp.getToken() + "");
            } else if (n.functionCall == null && n.mathOp == null) {
                jstr.append(n.num.convertToJott() + "");
            } else if (n.functionCall != null) {
                jstr.append(n.functionCall.convertToJott() + "");
            }
        }

        return jstr.toString();
    }

    @Override
    public boolean validateTree() throws ParsingException {
        // a single function or a change of only functions call can be any type, we dont know yet their so we gotta varify the function type
        // because int epr will take all single function calls made even if that fuction returns a string itll think it returns an int

        // see how many function call are in the expr
        int isSingleFunctionCall = 0;

        // keep track of the function call type (its real type looked up in the table)
        String PrevFunctionType = null;

        // going through all the exprs looking for a lone function call
        for (NumExpr n : this.finalexp) {

            // if we have a number or a var then we know its a number expr and can be varified
            // for int expr or dbl exp below this for loop
            if (n.functionCall == null) {
                isSingleFunctionCall = 0;
                break;
            }

            // how many function calls weve seen
            isSingleFunctionCall++;
            // gettting that functions real return type from table
            String funcType = ValidateTable.functions.get(n.functionCall.name.getToken()).get(0);

            // if its the first func call we've seen set the prev type to this type
            if (PrevFunctionType == null) {
                PrevFunctionType = funcType;
            } else
                // at this point our expr is only made of function falls like foo[] + boo[] +too[]... only functions
                // all of these function return types should match to be a valid expr
                if (!funcType.equals(PrevFunctionType)) {
                    throw new ParsingException("func mis match type: " + ValidateTable.functions.get(n.functionCall.name.getToken()).get(0));
                }
        }
        // if we had an  expr of  function calls that all return types matched then we know that this expr type is really
        // what we were seeing in the table and not just the assumed catch all int expr for a function
        if (isSingleFunctionCall > 0) {
            this.setType(PrevFunctionType);
            return true;
        }

        // this will varify that function return types match the expr type
        for (NumExpr n : this.finalexp) {
            // this is checked above but im just keeping it for now
            if (n.functionCall != null) {

                if (!ValidateTable.functions.get(n.functionCall.name.getToken()).get(0).equals(this.ExpType)) {
                    throw new ParsingException("func Wrong type in exp: " + ValidateTable.functions.get(n.functionCall.name.getToken()).get(0));
                }
            }
            // this checks for a var in an expr like : 1 + x that 1) x exists 2) its been init and 3) its type is okay

            // is a var and not a number ex: is x and NOT like 1 or .2
            if (n.num != null && n.num.isVar) {

                ///1) var exits

                if (ValidateTable.variables.containsKey(n.num.Vnum)) {
                    ArrayList<String> varProperties = ValidateTable.variables.get(n.num.Vnum);

                    ///2) var type matches expr type
                    if (varProperties.get(0).equals(this.ExpType)) {

                        ///3) var has been init

                        if (varProperties.get(1)==null) {
                            throw new ParsingException("use of un-init var: " + n.num.Vnum + " line:" + n.num.number.getLineNum());
                        }
                    }else {
                        throw new ParsingException("bad var type in exp: " + ValidateTable.variables.get(n.num.Vnum).get(0) + " " + n.num.Vnum);
                    }
                } else {
                    throw new ParsingException("use of undefined var: " + n.num.Vnum + " line:" + n.num.number.getLineNum());
                }
            }

        }


        return false;
    }

}
