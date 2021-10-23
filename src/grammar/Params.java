package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//  params -> expr param |
//  params -> , expr params | ε

public class Params {

    private Expr expr;
    boolean hasComma = false;
    ArrayList<Params> paramslst = null;


    // ---------------------- cconstructors --------------------------------

    public Params(Expr expr, boolean hasComma) {
        this.expr = expr;
        this.hasComma = hasComma;
    }

    public Params(ArrayList<Params> params) {

        this.paramslst = params;
    }


    public static ArrayList<Params> parseParams_r(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Params------------------------");


        // ---------------------- list of params --------------------------------

        ArrayList<Params> list_of_params = new ArrayList<>();


        // ---------------------- check for empty list  (epsilon)----------------

        // check for next token == ], this means no params
        Token first = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    1st:" + first.getToken());

        // epsilon case
        if (first.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }


        // ---------------------- check for expr ------------------------------------

        // looking for expr
        Expr exp = Expr.parseExpr(tokens, nestLevel);
        System.out.println("    2nd- expr found :" + exp.convertToJott());
        list_of_params.add(new Params(exp, false));


        // ---------------------- checking for more params, comma started-----------------------

        Token comma = tokens.get(TOKEN_IDX.IDX);
        while (comma.getTokenType() == TokenType.COMMA) {
            System.out.println("        looking for new param:" + comma.getToken());
            TOKEN_IDX.IDX++;

            // ---------------------- check for expr ------------------------------------

            exp = Expr.parseExpr(tokens, nestLevel);
            list_of_params.add(new Params(exp, true));

            // ---------------------- check for , and look for more ----------------------

            comma = tokens.get(TOKEN_IDX.IDX);


            System.out.println("        new param found:" + exp.convertToJott());
            System.out.println("        looking for new param...:" + comma.getToken());
        }

        System.out.println("        looking DONE:" + comma.getToken());
        // ---------------------- done ------------------------------------

        return list_of_params;
    }

    public static Params parseParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        ArrayList<Params> f = parseParams_r(tokens, nestLevel);
        if (f == null) {
            return null;
        }
        Params p = new Params(f);
        System.out.println("->>>" + p.convertToJott() + "<<<-");
        return p;

    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (Params par : paramslst) {
            if (par.hasComma) {
                jstr.append(",");
            }
            jstr.append(par.expr.convertToJott() );
        }


        return jstr.toString();
    }


}
