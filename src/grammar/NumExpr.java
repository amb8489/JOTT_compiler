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
    public ArrayList<NumExpr> finalExpr;
    public String exprType;
    // ---------------------- constructors for different cases --------------------------------

    public NumExpr(NumType numType, Token mathOp) {
        super(null, null);
        this.numType = numType;
        this.mathOp = mathOp;
        this.functionCall = null;
    }

    public NumExpr(NumType numType) {
        super(null, null);
        this.numType = numType;
        this.mathOp = null;
        this.functionCall = null;

    }

    public NumExpr(FuncCall call, Token mathOp) {
        super(null, null);
        this.numType = null;
        this.functionCall = call;
        this.mathOp = mathOp;
    }

    public NumExpr(ArrayList<NumExpr> finalExpr, String ExpType) {
        super(null, null);
        this.finalExpr = finalExpr;
        this.exprType = ExpType;
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
            if (exp.numType != null && exp.numType.getNumType() != null) {
                if (!exp.numType.numType.equals("Integer")) {
                    isINTexp = false;
                    break;
                }
            }
        }

        if (!isINTexp) {
            int i = 0;
            for (NumExpr exp : expLst) {
                if (exp.numType != null && exp.numType.getNumType() != null) {
                    if (!exp.numType.numType.equals("Double")) {
                        isINTexp = true;
                        break;
                    }
                }
                i++;
            }
            if (isINTexp) {
                throw new ParsingException("expression with int op double not allowed, line: " + expLst.get(i).numType.number.getLineNum());
            }
        }

        String ExpType = isINTexp ? "Integer" : "Double";


        return new NumExpr(expLst, ExpType);
    }

    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (NumExpr n : finalExpr) {
            if (n.numType != null && n.mathOp != null) {
                jstr.append(n.numType.convertToJott() + "" + n.mathOp.getToken() + "");
            } else if (n.functionCall != null && n.mathOp != null) {
                jstr.append(n.functionCall.convertToJott() + "" + n.mathOp.getToken() + "");
            } else if (n.functionCall == null && n.mathOp == null) {
                jstr.append(n.numType.convertToJott() + "");
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
        for (NumExpr n : this.finalExpr) {

            // if we have a number or a var then we know its a number expr and can be varified
            // for int expr or dbl exp below this for loop
            if (n.functionCall == null) {
                isSingleFunctionCall = 0;
                break;
            }

            // how many function calls weve seen
            isSingleFunctionCall++;
            // gettting that functions real return type from table

            // makes sure function exits
            if (!ValidateTable.functions.containsKey(n.functionCall.name.getToken())) {
                throw new ParsingException("use of undefined Function : " + n.functionCall.name.getToken() + " line:" + n.functionCall.name.getLineNum());
            }
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

        /////



        ////



        PrevFunctionType = null;

        // this will varify that function return types match the expr type
        for (NumExpr n : this.finalExpr) {
            // this is checked above but im just keeping it for now
            if (n.functionCall != null) {

                if (!ValidateTable.functions.get(n.functionCall.name.getToken()).get(0).equals(this.exprType)) {
                    throw new ParsingException("func Wrong type in exp: " + ValidateTable.functions.get(n.functionCall.name.getToken()).get(0));
                }
            }
            // this checks for a var in an expr like : 1 + x that 1) x exists 2) its been init and 3) its type is okay

            // is a var and not a number ex: is x and NOT like 1 or .2
            if (n.numType != null && n.numType.isVar) {

                ///1) var exits

                if (ValidateTable.variables.containsKey(n.numType.varNumber)) {
                    ArrayList<String> varProperties = ValidateTable.variables.get(n.numType.varNumber);

                    ///2) var type matches expr type
                    if (PrevFunctionType == null){
                        PrevFunctionType = varProperties.get(0);
                    }else {

                        if (varProperties.get(0).equals(PrevFunctionType)) {

                            ///3) var has been init

                            if (varProperties.get(1) == null) {
                                throw new ParsingException("use of un-init var: " + n.numType.varNumber + " line:" + n.numType.number.getLineNum());
                            }
                        } else {
                            throw new ParsingException("bad var type in exp: " + ValidateTable.variables.get(n.numType.varNumber).get(0) + " " + n.numType.varNumber);
                        }
                    }
                } else {
                    throw new ParsingException("use of undefined var: " + n.numType.varNumber + " line:" + n.numType.number.getLineNum());
                }
            }
        }
        System.out.println("---|||"+PrevFunctionType);
        this.type= PrevFunctionType;
        return true;
    }

}
