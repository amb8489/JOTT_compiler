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
public class IfStmt {
    private final Expr expression;
    private final Body body1;
    private Body body2;
    private final ArrayList<ElseifStmt> elseIfStatements;
    private final int nestLevel;
    public boolean hasGuaranteedReturn;

    public IfStmt(Expr expression,
                      Body body1,
                      ArrayList<ElseifStmt> elseIfStatements,
                      int nestLevel,
                      boolean hasGuaranteedReturn) {
        this.expression = expression;
        this.body1 = body1;
        this.elseIfStatements = elseIfStatements;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
    }

    public IfStmt(Expr expression,
                      Body body1,
                      Body body2,
                      ArrayList<ElseifStmt> elseIfStatements,
                      int nestLevel,
                      boolean hasGuaranteedReturn) {

        this.expression = expression;
        this.body1 = body1;
        this.elseIfStatements = elseIfStatements;
        this.body2 = body2;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        String SPACE = "\t".repeat(this.nestLevel -1);

        jstr.append("if [ ");
        jstr.append(this.expression.convertToJott() + " ] { \n");
        jstr.append(body1.convertToJott() +SPACE+ "}");

        if (elseIfStatements != null){
            for(ElseifStmt e: elseIfStatements){
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

        Token IfToken = tokens.get(TOKEN_IDX.index);
        System.out.println("\t1st:" + IfToken.getToken());
        // check for if
        if (!IfToken.getToken().equals("if")) {
            return null;
        }
        TOKEN_IDX.index++;
        // ---------------------- checking for [ ----------------------------------

        Token L_BRACKET = tokens.get(TOKEN_IDX.index);
        System.out.println("\t2nd:" + L_BRACKET.getToken());
        // check for if
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            String sb = "Syntax error\nInvalid token. Expected [. Got: " +
                    L_BRACKET.getTokenType().toString() + "\n" +
                    L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum();
            throw new ParsingException(sb);
        }
        TOKEN_IDX.index++;
        // ---------------------- checking for bool expression ------------------------------
//        System.out.println("    3rd EXPR FOUND:"+tokens.get(0).getToken());

        Expr expression = Expr.parseExpr(tokens, nestLevel);

        System.out.println("\t3rd EXPR FOUND:" + expression.convertToJott());
        System.out.println("------>" + tokens.get(TOKEN_IDX.index).getToken());


        // ---------------------- checking for ] ----------------------------------

        Token R_BRACKET = tokens.get(TOKEN_IDX.index);
        System.out.println("\t4th:" + R_BRACKET.getToken());
        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            String sb = "Syntax error\nInvalid token. Expected ]. Got: " +
                    R_BRACKET.getTokenType().toString() + "\n" +
                    R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum();
            throw new ParsingException(sb);
        }
        TOKEN_IDX.index++;
        // ---------------------- checking for { ----------------------------------

        Token L_BRACE = tokens.get(TOKEN_IDX.index);
        System.out.println("\t5th:" + L_BRACE.getToken());
        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            String sb = "Syntax error\nInvalid token. Expected {. Got: " +
                    L_BRACE.getTokenType().toString() + "\n" +
                    L_BRACE.getFilename() + ":" + L_BRACE.getLineNum();
            throw new ParsingException(sb);
        }
        TOKEN_IDX.index++;

        // ---------------------- checking for body -------------------------------

        Body body1 = Body.ParseBody(tokens, nestLevel);
        if (body1 != null) {
            System.out.println("\t6th BODY :" + body1.convertToJott());
        } else {
            System.out.println("\t6th EMPTY BODY");
            TOKEN_IDX.index--;
        }

        // ---------------------- checking for } ----------------------------------


        Token R_BRACE = tokens.get(TOKEN_IDX.index);
        System.out.println("\t7th:" + R_BRACE.getToken());
        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            String sb = "Syntax error\nInvalid token. Expected }. Got: " +
                    R_BRACE.getTokenType().toString() + "\n" +
                    R_BRACE.getFilename() + ":" + R_BRACE.getLineNum();
            throw new ParsingException(sb);
        }
        TOKEN_IDX.index++;

        System.out.println("first============: "+tokens.get(TOKEN_IDX.index).getToken());
        // ---------------------- checking for elif's --------------------------------
        ArrayList<ElseifStmt> elseIfList = ElseifStmt.ParseElsif_lst(tokens, nestLevel);
        System.out.println("first============: "+tokens.get(TOKEN_IDX.index).getToken());


        // ---------------------- checking for else --------------------------------

        // check for else token
        Token elseToken = tokens.get(TOKEN_IDX.index);
        System.out.println("\t9th:" + elseToken.getToken());
        // check for if
        if (elseToken.getToken().equals("else")) {
            TOKEN_IDX.index++;


            // ---------------------- checking for { --------------------------------

            Token LL_BRACE = tokens.get(TOKEN_IDX.index);
            System.out.println("\t10th:" + LL_BRACE.getToken());
            // check for if
            if (LL_BRACE.getTokenType() != TokenType.L_BRACE) {
                String sb = "Syntax error\nInvalid token. Expected {. Got: " +
                        LL_BRACE.getTokenType().toString() + "\n" +
                        LL_BRACE.getFilename() + ":" + LL_BRACE.getLineNum();
                throw new ParsingException(sb);
            }
            TOKEN_IDX.index++;


            // ---------------------- checking for body -------------------------------

            System.out.println("\t11th:body");
            Body body2 = Body.ParseBody(tokens, nestLevel);
            boolean hasGuaranteedReturn= false;
            if (body2 != null) {
                if (body2.hasReturn != null) {
                    hasGuaranteedReturn = true;
                }
            }
            // ---------------------- checking for } --------------------------------


            Token RR_BRACE = tokens.get(TOKEN_IDX.index);
            System.out.println("\t12th:" + RR_BRACE.getToken());
            // check for if
            if (RR_BRACE.getTokenType() != TokenType.R_BRACE) {
                String sb = "Syntax error\nInvalid token. Expected }. Got: " +
                        RR_BRACE.getTokenType().toString() + "\n" +
                        RR_BRACE.getFilename() + ":" + RR_BRACE.getLineNum();
                throw new ParsingException(sb);
            }
            TOKEN_IDX.index++;

            // ---------------------- all done with else --------------------------------

//            if (elseTok == null){
//                System.out.println("else if IS NULLLL");
//                TOKEN_IDX.IDX++;
//            }
            return new IfStmt(expression, body1, body2, elseIfList, nestLevel, hasGuaranteedReturn);
        }
        // ---------------------- all done no else --------------------------------

        // need to add somthing about elif being null <---------------------------TODO
        return new IfStmt(expression, body1, elseIfList, nestLevel, false);
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
