package grammar;


import main.Token;

import java.util.ArrayList;

//  params -> expr param |
//  params -> , expr params | ε

public class ParamsLst {

    private Expr expr;
    ArrayList<ParamsLst> paramsLst;
    boolean hasComma = false;

    public ParamsLst(){


    }


    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        for(ParamsLst p:paramsLst) {
            if (p.hasComma) {
                jstr.append(",");
            }
            jstr.append(expr + " ");
        }
        return jstr.toString();
    }

    public static ArrayList<ParamsLst> parseParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING Params------------------------");

        ArrayList<ParamsLst> list_of_params = new ArrayList<>();

        // check for next token == ], this means no params

        Token typeToken = tokens.remove(0);
        System.out.println("    FIRST:" + typeToken.getToken());
        Type type = new Type(typeToken.getToken(), typeToken.getFilename(), typeToken.getLineNum());
        // first should have no comma or return null

        // all next need , exp till , is not next token
        return list_of_params;
    }

    }
