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
            if (Type.isType(idd)){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. <id>. Got: TYPE \n");
                sb.append(idd.getFilename() + ":" + idd.getLineNum());
                throw new ParsingException(sb.toString());
            }

            System.out.println("    found id ???:" + idd.getToken());

            if (idd.getTokenType() != TokenType.ID_KEYWORD) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. <id>. Got:");
                sb.append(idd.getTokenType().toString()).append("\n");
                sb.append(idd.getFilename() + ":" + idd.getLineNum());
                throw new ParsingException(sb.toString());
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

            if (!Type.isType(type)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected <Type>. Got: ");
                sb.append(type.getToken().toString()).append("\n");
                sb.append(type.getFilename() + ":" + type.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.IDX++;


            // ---------------------------look for func def parm t -----------------------------

            fplist.add(new FuncDefParams(id, type, null));


            idd = tokens.get(TOKEN_IDX.IDX);
            if (idd.getTokenType() !=TokenType.COMMA  && idd.getTokenType() == TokenType.R_BRACKET){
                break;
            }else if(idd.getTokenType() !=TokenType.COMMA) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected ,. Got: ");
                sb.append(idd.getTokenType().toString()).append("\n");
                sb.append(idd.getFilename() + ":" + idd.getLineNum());
                throw new ParsingException(sb.toString());
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
