package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class FuncDefParams {
    private Identifier id;
    private Token type;
    private ArrayList<FuncDefParams>fplist;

    public FuncDefParams(Identifier id, Token type,ArrayList<FuncDefParams> fplst) {
        this.id = id;
        this.type = type;
        this.fplist = fplst;
    }

    public FuncDefParams(ArrayList<FuncDefParams> fplist) {
        this.fplist = fplist;
    }


//   func_def_params -> id : type func_def_params_t|ε                                                                 <-- DONE


    public static FuncDefParams parseFunctionDefParams(ArrayList<Token> tokens, int nestlevel) throws ParsingException {

        ArrayList<FuncDefParams>fplist = new ArrayList<>();

        // ---------------------------look for id -----------------------------


        Token idd = tokens.get(TOKEN_IDX.IDX);

        if (idd.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("empty params");
            return null;
        }


        while (idd.getTokenType() != TokenType.R_BRACKET) {


            System.out.println("    found id ???:" + idd.getToken());

            if (idd.getTokenType() != TokenType.ID_KEYWORD) {
                System.out.println("TODO ERROR 0");
                return null;
            }
            Identifier id = new Identifier(idd);

            TOKEN_IDX.IDX++;

            // ---------------------------look for : -----------------------------
            Token col = tokens.get(TOKEN_IDX.IDX);

            if (col.getTokenType() != TokenType.COLON) {
                System.out.println("TODO ERROR 1");
            }

            TOKEN_IDX.IDX++;


            // ---------------------------look for type -----------------------------
            Token type = tokens.get(TOKEN_IDX.IDX);

            if (type.getTokenType() != TokenType.ID_KEYWORD) {
                System.out.println("TODO ERROR 2");
                return null;
            }
            TOKEN_IDX.IDX++;


            // ---------------------------look for func def parm t -----------------------------

            fplist.add(new FuncDefParams(id, type, null));


            idd = tokens.get(TOKEN_IDX.IDX);
            if (idd.getTokenType() !=TokenType.COMMA){
                System.out.println("+++++++++++++++++++++++++++"+tokens.get(TOKEN_IDX.IDX));

                break;
            }
            TOKEN_IDX.IDX++;
            idd = tokens.get(TOKEN_IDX.IDX);

        }
        System.out.println("+++++++++++++++++++++++++++"+tokens.get(TOKEN_IDX.IDX));
        return new FuncDefParams(fplist);


    }


    public String convertToJava() {
        return null;
    }

    public String convertToC() {
        return null;
    }


    public String convertToPython() {
        return null;
    }

    public boolean validateTree() {
        return false;
    }

    //   func_def_params -> id : type func_def_params_t|ε                                                                 <-- DONE

    public String convertToJott() {




        StringBuilder jstr = new StringBuilder();

        StringBuilder fParmLst = new StringBuilder();

        fParmLst.append("");

        if (fplist != null) {
            for(FuncDefParams FDP: fplist) {
                fParmLst.append(FDP.convertToJott());
            }
            return fParmLst.toString().substring(0, fParmLst.length()-1);
        }else {


            jstr.append(id.convertToJott() + " : " + type.getToken() + fParmLst+",");

            return jstr.toString();
        }
    }
}
