package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class ElseifStmt {
    private Expr exp;
    private Body body;

    public ElseifStmt(Expr exp,Body body) {
        this.exp = exp;
        this.body = body;
    }


    //      elseif_lst -> elseif [ b_expr ] { body } elseif_lst | ε
    public static ArrayList<ElseifStmt> ParseElsif_lst(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING elif------------------------");

        // list of all else if we will encounter if any
        ArrayList<ElseifStmt> elif_lists = new ArrayList<>();


        // looking if there is an "elseif"
        Token elseif = tokens.get(TOKEN_IDX.IDX);

        // if no elseif then : ε case and return, this is not an error
        if (!elseif.getToken().equals("elseif")) {
            return null;
        }

        // while we did encounter an elseif in the tokens we will keep serching for elseif
        while(elseif.getToken().equals("elseif")){
            System.out.println("    elif found:" + elseif.getToken());

            // removing elseif
            TOKEN_IDX.IDX++;

            // ---------------------------looking for [----------------------------------------

            Token L_BRACKET = tokens.get(TOKEN_IDX.IDX);
            System.out.println("    2nd:"+L_BRACKET.getToken());
            // check for if
            if (L_BRACKET.getTokenType() != TokenType.L_BRACKET){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected [. Got: ");
                sb.append(L_BRACKET.getTokenType().toString()).append("\n");
                sb.append(L_BRACKET.getFilename() + ":" +L_BRACKET.getLineNum());
                throw new ParsingException(sb.toString());
            }
            System.out.println("    3rd:"+L_BRACKET.getToken());
            TOKEN_IDX.IDX++;
            // -----------------------looking for bool expr-------------------------------------------


            Expr expr = Expr.parseExpr(tokens,nestLevel);


            // ---------------------------looking for ]----------------------------------------

            Token R_BRACKET = tokens.get(TOKEN_IDX.IDX);
            System.out.println("    4th:"+R_BRACKET.getToken());
            // check for if
            if (R_BRACKET.getTokenType() != TokenType.R_BRACKET){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected ]. Got: ");
                sb.append(R_BRACKET.getTokenType().toString()).append("\n");
                sb.append(R_BRACKET.getFilename() + ":" +R_BRACKET.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.IDX++;

            // ---------------------------looking for {----------------------------------------

            Token L_BRACE = tokens.get(TOKEN_IDX.IDX);
            System.out.println("    5th:"+L_BRACE.getToken());
            // check for if
            if (L_BRACE.getTokenType() != TokenType.L_BRACE){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected {. Got: ");
                sb.append(L_BRACE.getTokenType().toString()).append("\n");
                sb.append(L_BRACE.getFilename() + ":" +L_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            System.out.println("    6th:"+L_BRACE.getToken());
            TOKEN_IDX.IDX++;


            // ---------------------------looking for body----------------------------------------

            Body body = Body.ParseBody(tokens, nestLevel);

            // ---------------------------looking for }----------------------------------------

            Token R_BRACE = tokens.get(TOKEN_IDX.IDX);
            System.out.println("    7th:"+R_BRACE.getToken());
            // check for if
            if (R_BRACE.getTokenType() != TokenType.R_BRACE){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected }. Got: ");
                sb.append(R_BRACE.getTokenType().toString()).append("\n");
                sb.append(R_BRACE.getFilename() + ":" +R_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            System.out.println("    8th:"+R_BRACE.getToken());
            TOKEN_IDX.IDX++;
            // -----------------------adding what was found to list of seen elif stmts-----------------------

            elif_lists.add(new ElseifStmt(expr,body));

            // ---------------------------looking for elif----------------------------------------

            elseif = tokens.get(TOKEN_IDX.IDX);
        }

        // ---------------------------all done finding elif's----------------------------------------

        return elif_lists;
    }

    // elseif [ b_expr ] { body }
    public String convertToJott() {

        StringBuilder jstr = new StringBuilder();
        jstr.append("elseif [ ");
        jstr.append(exp.convertToJott() + " ] { ");
        jstr.append(body.convertToJott() + " }\n");
        return jstr.toString();
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
