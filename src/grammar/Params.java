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

    public Params(Expr expr, boolean hasComma) {
        this.expr = expr;
        this.hasComma = hasComma;
    }

    public Params(ArrayList<Params> params) {

        this.paramslst = params;
    }


    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (Params par : paramslst) {
            if (par.hasComma) {
                jstr.append(",");
            }
            System.out.println(par.expr.convertToJott());
            jstr.append(par.expr.convertToJott() + " ");
        }


        return jstr.toString();
    }

    public static ArrayList<Params> parseParams_r(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Params------------------------");

        ArrayList<Params> list_of_params = new ArrayList<>();

        // check for next token == ], this means no params

        Token first = tokens.get(0);
        System.out.println("    1st:" + first.getToken());

        // epsilon case
        if (first.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }

        // looking for expr
        Expr exp = Expr.parseExpr(tokens, nestLevel);
        System.out.println("    2nd- expr found :" + exp.convertToJott() + "<-----");


        // found expr looking for for with , expr

        list_of_params.add(new Params(exp, false));

        Token comma = tokens.get(0);

        while (comma.getTokenType() == TokenType.COMMA) {
            System.out.println("        looking for new param:" + comma.getToken());

            tokens.remove(0);
            exp = Expr.parseExpr(tokens, nestLevel);

            list_of_params.add(new Params(exp, true));
            comma = tokens.get(0);
            System.out.println("        new param found:" + exp.convertToJott());
            System.out.println("        looking for new param...:" + comma.getToken());
        }
        System.out.println("        looking DONE:" + comma.getToken());

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


}
