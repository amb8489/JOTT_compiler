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

    private Expr expr;
    boolean hasComma = false;
    ArrayList<Params> paramsList = null;


    /**
     * Constructor
     *
     * @param expr
     * @param hasComma
     */
    public Params(Expr expr, boolean hasComma) {
        this.expr = expr;
        this.hasComma = hasComma;
    }

    /**
     * Constructor
     *
     * @param params
     */
    public Params(ArrayList<Params> params) {
        this.paramsList = params;
    }


    public static ArrayList<Params> parseParams_r(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("------------------------PARSING Params------------------------");


        // ---------------------- list of params --------------------------------

        ArrayList<Params> listOfParams = new ArrayList<>();


        // ---------------------- check for empty list  (epsilon)----------------

        // check for next token == ], this means no params
        Token firstToken = tokens.get(TOKEN_IDX.index);
        //System.out.println("    1st:" + first.getToken());

        // epsilon case
        if (firstToken.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }


        // ---------------------- check for expr ------------------------------------

        // looking for expr
        Expr expr = Expr.parseExpr(tokens, nestLevel);
        //System.out.println("    2nd- expr found :" + exp.convertToJott());
        listOfParams.add(new Params(expr, false));


        // ---------------------- checking for more params, comma started-----------------------

        Token commaToken = tokens.get(TOKEN_IDX.index);
        while (commaToken.getTokenType() == TokenType.COMMA) {
            //System.out.println("        looking for new param:" + comma.getToken());
            TOKEN_IDX.index++;

            // ---------------------- check for expr ------------------------------------

            expr = Expr.parseExpr(tokens, nestLevel);
            listOfParams.add(new Params(expr, true));

            // ---------------------- check for , and look for more ----------------------

            commaToken = tokens.get(TOKEN_IDX.index);


            //System.out.println("        new param found:" + exp.convertToJott());
            //System.out.println("        looking for new param...:" + comma.getToken());
        }

        //System.out.println("        looking DONE:" + comma.getToken());
        // ---------------------- done ------------------------------------

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

    public boolean validateTree() {
        return false;
    }


}
