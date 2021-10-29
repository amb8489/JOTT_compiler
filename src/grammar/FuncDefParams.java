package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class FuncDefParams {
    public Identifier identifier;
    public Token type;
    public final ArrayList<FuncDefParams> functionParameterList;

    /**
     * Constructor TODO
     * @param identifier TODO
     * @param type TODO
     * @param functionParameterList TODO
     */
    public FuncDefParams(Identifier identifier, Token type, ArrayList<FuncDefParams> functionParameterList) {
        this.identifier = identifier;
        this.type = type;
        this.functionParameterList = functionParameterList;
    }

    /**
     * Constructor TODO
     * @param functionParameterList TODO
     */
    public FuncDefParams(ArrayList<FuncDefParams> functionParameterList) {
        this.functionParameterList = functionParameterList;
    }
    /**
     * Constructor TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static FuncDefParams parseFunctionDefParams(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        ArrayList<FuncDefParams> functionParameterList = new ArrayList<>();

        // ---------------------------look for id -----------------------------


        Token idd = tokens.get(TOKEN_IDX.index);

        if (idd.getTokenType() == TokenType.R_BRACKET) {
            //System.out.println("empty params");
            return null;
        }



        while (idd.getTokenType() != TokenType.R_BRACKET) {
            if (Type.isType(idd)){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. <id>. Got: TYPE \n");
                sb.append(idd.getFilename() + ":" + idd.getLineNum());
                throw new ParsingException(sb.toString());
            }

            //System.out.println("    found id ???:" + idd.getToken());

            if (idd.getTokenType() != TokenType.ID_KEYWORD) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. <id>. Got:");
                sb.append(idd.getTokenType().toString()).append("\n");
                sb.append(idd.getFilename() + ":" + idd.getLineNum());
                throw new ParsingException(sb.toString());
            }
            Identifier id = new Identifier(idd);

            TOKEN_IDX.index++;

            // ---------------------------look for : -----------------------------
            Token col = tokens.get(TOKEN_IDX.index);

            if (col.getTokenType() != TokenType.COLON) {
                //System.out.println("TODO ERROR 1");
            }

            TOKEN_IDX.index++;


            // ---------------------------look for type -----------------------------
            Token type = tokens.get(TOKEN_IDX.index);

            if (!Type.isType(type)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected <Type>. Got: ");
                sb.append(type.getToken().toString()).append("\n");
                sb.append(type.getFilename() + ":" + type.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.index++;


            // ---------------------------look for func def parm t -----------------------------

            functionParameterList.add(new FuncDefParams(id, type, null));


            idd = tokens.get(TOKEN_IDX.index);
            if (idd.getTokenType() !=TokenType.COMMA  && idd.getTokenType() == TokenType.R_BRACKET){
                break;
            }else if(idd.getTokenType() !=TokenType.COMMA) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected ,. Got: ");
                sb.append(idd.getTokenType().toString()).append("\n");
                sb.append(idd.getFilename() + ":" + idd.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.index++;
            idd = tokens.get(TOKEN_IDX.index);

        }
        //System.out.println("+++++++++++++++++++++++++++"+tokens.get(TOKEN_IDX.index));
        return new FuncDefParams(functionParameterList);
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJava() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToC() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToPython() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() throws ParsingException {

        // check that var is not a keyword, if it is it wil throw an error
        for(FuncDefParams param: functionParameterList) {
            if(param.identifier != null){ Identifier.check(param.identifier.id);}
            if(param.type != null){ Type.isType(param.type);}
        }

        return true;
    }

    //   func_def_params -> id : type func_def_params_t|ε                                                                 <-- DONE

    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        StringBuilder functionParameterList = new StringBuilder();

        functionParameterList.append("");

        if (this.functionParameterList != null) {
            for(FuncDefParams FDP: this.functionParameterList) {
                functionParameterList.append(FDP.convertToJott());
            }
            return functionParameterList.substring(0, functionParameterList.length()-1);
        } else {
            jottString.append(identifier.convertToJott() + " : " + type.getToken() + functionParameterList+",");
            return jottString.toString();
        }
    }
}
