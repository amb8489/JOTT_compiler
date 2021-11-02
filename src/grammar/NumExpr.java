package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This is an expression that can automatically handle both integer and double expressions.
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
    public String scope;

    /**
     * A constructor to handle a numExpr with a number and a math operator.
     *
     * @param numType the type of number
     * @param mathOp  the type of math operation done with the number
     */
    public NumExpr(NumType numType, Token mathOp,String scope) {
        super(null, null,null);
        this.numType = numType;
        this.mathOp = mathOp;
        this.functionCall = null;
        this.scope = scope;

    }

    /**
     * A constructor to handle a numExpr with just a number.
     *
     * @param numType the type of number
     */
    public NumExpr(NumType numType,String scope) {
        super(null, null,null);
        this.numType = numType;
        this.mathOp = null;
        this.functionCall = null;
        this.scope = scope;


    }

    /**
     * A constructor to handle a numExpr with just a number.
     *
     * @param funcCall the function call object
     * @param mathOp   the mathematical operation done with this function call object
     */
    public NumExpr(FuncCall funcCall, Token mathOp,String scope) {
        super(null, null,null);
        this.numType = null;
        this.functionCall = funcCall;
        this.mathOp = mathOp;
        this.scope = scope;

    }

    /**
     * A constructor to handle a numExpr with a final expression and an expr type.
     *
     * @param finalExpr finalExpr object
     * @param exprType  what type is this finalExpr object
     */
    public NumExpr(ArrayList<NumExpr> finalExpr, String exprType,String scope) {
        super(null, null,null);
        this.finalExpr = finalExpr;
        this.exprType = exprType;
        this.numType = null;
        this.scope = scope;

    }

    private static ArrayList<NumExpr> parseNumExprR(ArrayList<Token> tokens, int nestLevel, ArrayList<NumExpr> exprList,String scope) throws ParsingException {

        //  looking for id/int followed by math op
        Token possibleNumber = tokens.get(TokenIndex.currentTokenIndex);
        Token possibleOp = tokens.get(TokenIndex.currentTokenIndex + 1);

        if ((possibleNumber.getTokenType() == TokenType.NUMBER || possibleNumber.getTokenType() == TokenType.ID_KEYWORD) && possibleOp.getTokenType() == TokenType.MATH_OP) {
            TokenIndex.currentTokenIndex += 2;

            // numExpr can be id, id op, num, or num op
            exprList.add(new NumExpr(new NumType(possibleNumber,scope), possibleOp,scope));

            // trying to complete id/int op with valid follow
            return parseNumExprR(tokens, nestLevel, exprList,scope);
        }


        //  check for function funcCall op
        FuncCall funcCall = FuncCall.ParseFuncCall(tokens, nestLevel,scope);
        possibleOp = tokens.get(TokenIndex.currentTokenIndex);

        if (funcCall != null && possibleOp.getTokenType() == TokenType.MATH_OP) {
            TokenIndex.currentTokenIndex++;
            exprList.add(new NumExpr(funcCall, possibleOp,scope));
            return parseNumExprR(tokens, nestLevel, exprList,scope);
        }

        // check for lone function funcCall
        if (funcCall != null) {
            exprList.add(new NumExpr(funcCall, null,scope));
            return exprList;
        }

        //  check for lone id or num
        if (possibleNumber.getTokenType() == TokenType.NUMBER || possibleNumber.getTokenType() == TokenType.ID_KEYWORD) {
            TokenIndex.currentTokenIndex++;
            exprList.add(new NumExpr(new NumType(possibleNumber,scope),scope));
            return exprList;
        }

        // error, goes on to try another expr in expr
        return null;
    }


    public static NumExpr parseNumExpr(ArrayList<Token> tokens, int nestLevel,String scope) throws ParsingException {

        ArrayList<NumExpr> exprList = parseNumExprR(tokens, nestLevel, new ArrayList<>(), scope);
        if (exprList == null) {
            return null;
        }

        boolean isIntExpr = true;


        for (NumExpr exp : exprList) {
            if (exp.numType != null && exp.numType.getNumType() != null) {
                if (!exp.numType.numType.equals("Integer")) {
                    isIntExpr = false;
                    break;
                }
            }
        }

        if (!isIntExpr) {
            int i = 0;
            for (NumExpr exp : exprList) {
                if (exp.numType != null && exp.numType.getNumType() != null) {
                    if (!exp.numType.numType.equals("Double")) {
                        isIntExpr = true;
                        break;
                    }
                }
                i++;
            }
            if (isIntExpr) {
                throw new ParsingException("expression with int op double not allowed, line: " + exprList.get(i).numType.number.getLineNum());
            }
        }

        String ExprType = isIntExpr ? "Integer" : "Double";


        return new NumExpr(exprList, ExprType, scope);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        for (NumExpr n : finalExpr) {
            if (n.numType != null && n.mathOp != null) {
                jottString.append(String.format("%s%s", n.numType.convertToJott(), n.mathOp.getToken()));
            } else if (n.functionCall != null && n.mathOp != null) {
                jottString.append(String.format("%s%s", n.functionCall.convertToJott(), n.mathOp.getToken()));
            } else if (n.functionCall == null && n.mathOp == null && n.numType != null) {
                jottString.append(n.numType.convertToJott());
            } else if (n.functionCall != null) {
                jottString.append(n.functionCall.convertToJott());
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
     * Ensure the code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        // a single function or a change of only functions call can be any type, we dont know yet their so we gotta varify the function type
        // because int epr will take all single function calls made even if that fuction returns a string itll think it returns an int

        // see how many function call are in the expr
        int isSingleFunctionCall = 0;

        // keep track of the function call type (its real type looked up in the table)
        String prevFunctionType = null;

        // going through all the exprs looking for a lone function call
        for (NumExpr n : this.finalExpr) {

            // if we have a number or a var then we know it's a number expr and can be verified
            // for int expr or dbl exp below this for loop
            if (n.functionCall == null) {
                isSingleFunctionCall = 0;
                break;
            }

            // how many function calls we've seen
            isSingleFunctionCall++;
            // getting that functions real return type from table

            // makes sure function exits
            if (!ValidateTable.getScope(scope).functions.containsKey(n.functionCall.name.getToken())) {
                String msg = "call to undefined function: " + n.functionCall.name.getToken();
                String fileName = n.functionCall.name.getFilename();
                int lineNum = n.functionCall.name.getLineNum();
                throw new ParsingException(String.format(String.format("SemanticError:\n%s\n%s:%d",msg,fileName,lineNum)));
            }
            n.functionCall.validateTree();

            String funcType = ValidateTable.getScope(scope).functions.get(n.functionCall.name.getToken()).get(0);
            // if it's the first func call we've seen set the prev type to this type
            if (prevFunctionType == null) {
                prevFunctionType = funcType;
            } else
                // at this point our expr is only made of function falls like foo[] + boo[] +too[]... only functions
                // all of these function return types should match to be a valid expr
                if (!funcType.equals(prevFunctionType)) {
                    String msg = "mis match type in expr: "+this.convertToJott();
                    String fileName = n.functionCall.name.getFilename();
                    int lineNum = n.functionCall.name.getLineNum();
                    throw new ParsingException(String.format(String.format("SemanticError:\n%s\n%s:%d",msg,fileName,lineNum)));
                }
        }
        // if we had an  expr of  function calls that all return types matched then we know that this expr type is really
        // what we were seeing in the table and not just the assumed catch-all int expr for a function
        if (isSingleFunctionCall > 0) {
            this.setType(prevFunctionType);
            return true;
        }

        // this will verify that function return types match the expr type
        prevFunctionType = null;

        // this will varify that function return types match the expr type
        for (NumExpr n : this.finalExpr) {
            // this is checked above but im just keeping it for now
            if (n.functionCall != null) {
                n.functionCall.validateTree();
                if (!ValidateTable.getScope(scope).functions.get(n.functionCall.name.getToken()).get(0).equals(this.exprType)) {
                    throw new ParsingException("func Wrong type in exp: " + ValidateTable.getScope(scope).functions.get(n.functionCall.name.getToken()).get(0));
                }
            }
            // this checks for a var in an expr like : 1 + x that 1) x exists 2) it's been init and 3) its type is okay

            // is a var and not a number ex: is x and NOT like 1 or .2
            if (n.numType != null && n.numType.isVar) {

                ///1) var exits

                if (ValidateTable.isVarDefinedInScope(scope,n.numType.varNumber)) {  // ****************************
                    ArrayList<String> varProperties = ValidateTable.getScope(scope).variables.get(n.numType.varNumber);

                    ///2) var type matches expr type
                    if (prevFunctionType == null) {
                        prevFunctionType = varProperties.get(0);
                    } else {

                        if (varProperties.get(0).equals(prevFunctionType)) {

                            ///3) var has been init

                            if (varProperties.get(1) == null) {
                                throw new ParsingException("use of un-init var: " + n.numType.varNumber + " line:" + n.numType.number.getLineNum());
                            }
                        } else {
                            throw new ParsingException("bad var type in exp: " + ValidateTable.getScope(scope).variables.get(n.numType.varNumber).get(0) + " " + n.numType.varNumber);
                        }
                    }
                } else {

                    throw new ParsingException("use of undefined var: " + n.numType.varNumber + " line:" + n.numType.number.getLineNum());
                }
            }
        }
        this.type = prevFunctionType;
        return true;
    }
}
