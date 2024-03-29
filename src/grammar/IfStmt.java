package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class IfStmt {
    private Expr exp;
    private Body body1;
    private Body body2;
    private ArrayList<ElseifStmt> elif;
    private int nestlevel;
    public boolean HasGuaranteedReturn;
    public IfStmt(int nestLevel, Expr exp, Body body1, ArrayList<ElseifStmt> elif,int nestlevel,boolean HasGuaranteedReturn) {

        this.exp = exp;
        this.body1 = body1;
        this.elif = elif;
        this.nestlevel = nestlevel;
        this.HasGuaranteedReturn = HasGuaranteedReturn;
    }

    public IfStmt(int nestLevel, Expr exp, Body body1, Body body2, ArrayList<ElseifStmt> elif,int nestlevel,boolean HasGuaranteedReturn) {

        this.exp = exp;
        this.body1 = body1;
        this.elif = elif;
        this.body2 = body2;
        this.nestlevel = nestlevel;
        this.HasGuaranteedReturn = HasGuaranteedReturn;
    }


    public String convertToJott() {



        StringBuilder jstr = new StringBuilder();
        String SPACE = "    ".repeat(this.nestlevel-1);

        jstr.append("if [ ");
        jstr.append(this.exp.convertToJott() + " ] { \n");
        jstr.append(body1.convertToJott() +SPACE+ "}");

        if (elif != null){
            for(ElseifStmt e:elif){
                jstr.append(e.convertToJott()+"");
            }
        }
        if (body2 != null){
            jstr.append("else{\n");
            jstr.append(body2.convertToJott() +SPACE+"}\n");

        }
        return jstr.toString();

    }


    //     * if_stmt ->  if [ b_expr ] { body } elseif_lst
//     *           | if [ b_expr ] { body } elseif_lst else { body }
    public static IfStmt parseIfStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING IF STMT------------------------");


        // ---------------------- checking for if ----------------------------------

        Token IfToken = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    1st:" + IfToken.getToken());
        // check for if
        if (!IfToken.getToken().equals("if")) {
            return null;
        }
        TOKEN_IDX.IDX++;
        // ---------------------- checking for [ ----------------------------------

        Token L_BRACKET = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    2nd:" + L_BRACKET.getToken());
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected [. Got: ");
            sb.append(L_BRACKET.getTokenType().toString()).append("\n");
            sb.append(L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;
        // ---------------------- checking for bool expr ------------------------------
//        System.out.println("    3rd EXPR FOUND:"+tokens.get(0).getToken());

        Expr expr = Expr.parseExpr(tokens, nestLevel);

        System.out.println("    3rd EXPR FOUND:" + expr.convertToJott());
        System.out.println("------>" + tokens.get(TOKEN_IDX.IDX).getToken());


        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    4th:" + R_BRACKET.getToken());
        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected ]. Got: ");
            sb.append(R_BRACKET.getTokenType().toString()).append("\n");
            sb.append(R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;
        // ---------------------- checking for { ----------------------------------

        Token L_BRACE = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    5th:" + L_BRACE.getToken());
        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected {. Got: ");
            sb.append(L_BRACE.getTokenType().toString()).append("\n");
            sb.append(L_BRACE.getFilename() + ":" + L_BRACE.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;

        // ---------------------- checking for body -------------------------------

        Body body1 = Body.ParseBody(tokens, nestLevel);
        if (body1 != null) {
            System.out.println("    6th BODY :" + body1.convertToJott());
        } else {
            System.out.println("    6th EMPTY BODY");
            TOKEN_IDX.IDX--;
        }

        // ---------------------- checking for } ----------------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    7th:" + R_BRACE.getToken());
        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected }. Got: ");
            sb.append(R_BRACE.getTokenType().toString()).append("\n");
            sb.append(R_BRACE.getFilename() + ":" + R_BRACE.getLineNum());
            throw new ParsingException(sb.toString());
        }
        TOKEN_IDX.IDX++;

        System.out.println("first============: "+tokens.get(TOKEN_IDX.IDX).getToken());
        // ---------------------- checking for elif's --------------------------------
        ArrayList<ElseifStmt> elsif_lst = ElseifStmt.ParseElsif_lst(tokens, nestLevel);
        System.out.println("first============: "+tokens.get(TOKEN_IDX.IDX).getToken());


        // ---------------------- checking for else --------------------------------

        // check for else token
        Token elseToken = tokens.get(TOKEN_IDX.IDX);
        System.out.println("    9th:" + elseToken.getToken());
        // check for if
        if (elseToken.getToken().equals("else")) {
            TOKEN_IDX.IDX++;


            // ---------------------- checking for { --------------------------------

            Token LL_BRACE = tokens.get(TOKEN_IDX.IDX);
            System.out.println("    10th:" + LL_BRACE.getToken());
            // check for if
            if (LL_BRACE.getTokenType() != TokenType.L_BRACE) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected {. Got: ");
                sb.append(LL_BRACE.getTokenType().toString()).append("\n");
                sb.append(LL_BRACE.getFilename() + ":" + LL_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.IDX++;


            // ---------------------- checking for body -------------------------------

            System.out.println("    11th:body");
            Body body2 = Body.ParseBody(tokens, nestLevel);
            boolean HasGuaranteedReturn= false;
            if(body2 != null) {
                if (body2.Hasreturn != null) {
                    HasGuaranteedReturn = true;
                }
            }
            // ---------------------- checking for } --------------------------------


            Token RR_BRACE = tokens.get(TOKEN_IDX.IDX);
            System.out.println("    12th:" + RR_BRACE.getToken());
            // check for if
            if (RR_BRACE.getTokenType() != TokenType.R_BRACE) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected }. Got: ");
                sb.append(RR_BRACE.getTokenType().toString()).append("\n");
                sb.append(RR_BRACE.getFilename() + ":" + RR_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.IDX++;

            // ---------------------- all done with else --------------------------------

//            if (elseTok == null){
//                System.out.println("else if IS NULLLL");
//                TOKEN_IDX.IDX++;
//            }
            return new IfStmt(nestLevel, expr, body1, body2, elsif_lst,nestLevel,HasGuaranteedReturn);
        }
        // ---------------------- all done no else --------------------------------

        // need to add somthing about elif being null <---------------------------TODO
        return new IfStmt(nestLevel, expr, body1, elsif_lst,nestLevel,false);
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

}
