package grammar;


import main.Token;
import main.TokenType;

import java.util.ArrayList;

//  params -> expr param |
//  params -> , expr params | ε

public class Params {

    private Expr expr;
    boolean hasComma = false;

    public Params(Expr expr, boolean hasComma) {
        this.expr = expr;
        this.hasComma = hasComma;
    }


    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        if (this.hasComma) {
            jstr.append(",");
        }
        jstr.append(expr + " ");

        return jstr.toString();
    }

    public static ArrayList<Params> parseParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Params------------------------");

        ArrayList<Params> list_of_params = new ArrayList<>();

        // check for next token == ], this means no params

        Token first = tokens.get(0);

        // epsilon case
        if (first.getTokenType() == TokenType.R_BRACKET) {
            return null;
        }

        // looking for expr
        Expr exp = Expr.parseExpr(tokens, nestLevel);
        System.out.println("    1st:" + exp.convertToJott());

        // found expr looking for for with , expr

        list_of_params.add(new Params(exp, false));

        Token comma = tokens.get(0);
        System.out.println("    looking for new param" + comma.getToken());

        while (comma.getTokenType() == TokenType.COMMA) {
            tokens.remove(0);
            exp = Expr.parseExpr(tokens, nestLevel);
            list_of_params.add(new Params(exp, true));
            comma = tokens.get(0);
            System.out.println("    new param found");
            System.out.println("    looking for new param" + comma.getToken());
        }
        return list_of_params;
    }

}
