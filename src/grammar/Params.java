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

    private Expr expression;
    boolean hasComma = false;
    ArrayList<Params> paramsList = null;


    // ---------------------- cconstructors --------------------------------

    public Params(Expr expression, boolean hasComma) {
        this.expression = expression;
        this.hasComma = hasComma;
    }

    public Params(ArrayList<Params> params) {

        this.paramsList = params;
    }


    public static ArrayList<Params> parseParams_r(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        //System.out.println("------------------------PARSING Params------------------------");


        // ---------------------- list of params --------------------------------

        ArrayList<Params> list_of_params = new ArrayList<>();


        // ---------------------- check for empty list  (epsilon)----------------

        // check for next token == ], this means no params
        Token first = tokens.get(TOKEN_IDX.index);
        //System.out.println("    1st:" + first.getToken());

        // epsilon case
        if (first.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }


        // ---------------------- check for expr ------------------------------------

        // looking for expr
        Expr exp = Expr.parseExpr(tokens, nestLevel);
        //System.out.println("    2nd- expr found :" + exp.convertToJott());
        list_of_params.add(new Params(exp, false));


        // ---------------------- checking for more params, comma started-----------------------

        Token comma = tokens.get(TOKEN_IDX.index);
        while (comma.getTokenType() == TokenType.COMMA) {
            //System.out.println("        looking for new param:" + comma.getToken());
            TOKEN_IDX.index++;

            // ---------------------- check for expr ------------------------------------

            exp = Expr.parseExpr(tokens, nestLevel);
            list_of_params.add(new Params(exp, true));

            // ---------------------- check for , and look for more ----------------------

            comma = tokens.get(TOKEN_IDX.index);


            //System.out.println("        new param found:" + exp.convertToJott());
            //System.out.println("        looking for new param...:" + comma.getToken());
        }

        //System.out.println("        looking DONE:" + comma.getToken());
        // ---------------------- done ------------------------------------

        return list_of_params;
    }

    public static Params parseParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        ArrayList<Params> f = parseParams_r(tokens, nestLevel);
        if (f == null) {
            return null;
        }
        Params p = new Params(f);
        //System.out.println("->>>" + p.convertToJott() + "<<<-");
        return p;

    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (Params par : paramsList) {
            if (par.hasComma) {
                jstr.append(",");
            }
            jstr.append(par.expression.convertToJott() );
        }


        return jstr.toString();
    }
    public boolean validateTree() {
        return false;
    }


}
