package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//  params -> expr param |
//  params -> , expr params | ε


/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Params {
    public Expr expr;
    boolean hasComma = false;
    public ArrayList<Params> paramsList = null;
    public String insideOfFunction;

    /**
     * This is a constructor for Params.
     *
     * @param expr     this holds the param
     * @param hasComma whether if this expression has a comma or not
     */
    public Params(Expr expr, boolean hasComma) {
        this.expr = expr;
        this.hasComma = hasComma;
    }

    /**
     * This is the constructor for params.
     *
     * @param params this is a list of params
     */
    public Params(ArrayList<Params> params) {
        this.paramsList = params;
    }

    public static ArrayList<Params> parseParams_r(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        // a list of params
        ArrayList<Params> listOfParams = new ArrayList<>();

        // check for an empty list (epsilon)

        // check for next token == ], this means no params
        Token firstToken = tokens.get(TokenIndex.currentTokenIndex);

        // epsilon case
        if (firstToken.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }

        // looking for expr
        Expr expr = Expr.parseExpr(tokens, nestLevel);
        listOfParams.add(new Params(expr, false));

        // checking for more params, comma started
        Token commaToken = tokens.get(TokenIndex.currentTokenIndex);
        while (commaToken.getTokenType() == TokenType.COMMA) {
            TokenIndex.currentTokenIndex++;

            // check for expr
            expr = Expr.parseExpr(tokens, nestLevel);
            listOfParams.add(new Params(expr, true));

            // check for , and look for more
            commaToken = tokens.get(TokenIndex.currentTokenIndex);
        }

        // done
        return listOfParams;
    }

    public static Params parseParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        ArrayList<Params> params = parseParams_r(tokens, nestLevel);
        if (params == null) {
            return null;
        }
        //System.out.println("->>>" + p.convertToJott() + "<<<-");
        return new Params(params);

    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        for (Params param : paramsList) {
            if (param.hasComma) {
                jottString.append(",");
            }
            jottString.append(param.expr.convertToJott());
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
    public boolean validateTree() {
        return false;
    }


}
