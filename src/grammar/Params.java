package grammar;


import main.Token;

import java.util.ArrayList;

//  params -> expr param |
//  params -> , expr params | ε

public class Params {

    private Expr expr;
    ArrayList<Params> paramsLst;
    boolean hasComma = false;

    public Params(){


    }


    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        for(Params p:paramsLst) {
            if (p.hasComma) {
                jstr.append(",");
            }
            jstr.append(expr + " ");
        }
        return jstr.toString();
    }

    public static ArrayList<Params> parseParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Params------------------------");

        ArrayList<Params> list_of_params = new ArrayList<>();

        // check for next token == ], this means no params

        Token typeToken = tokens.remove(0);
        System.out.println("    FIRST:" + typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
        // first should have no comma or return null

        // all next need , exp till , is not next token
        return list_of_params;
    }

    }
