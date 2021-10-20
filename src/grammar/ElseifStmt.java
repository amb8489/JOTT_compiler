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


        ArrayList<ElseifStmt> elif_lists = new ArrayList<>();

        System.out.println("------------------------PARSING elif------------------------");


        Token elseif = tokens.get(0);
        // ε case
        if (!elseif.getToken().equals("elseif")) {
            return null;
        }


        while(elseif.getToken().equals("elseif")){
            tokens.remove(0);
            System.out.println("    elif 1 found:" + elseif.getToken());

            // ----------------------------------------------------------------------------

            Token L_BRACKET = tokens.remove(0);
            System.out.println("    2nd:"+L_BRACKET.getToken());
            // check for if
            if (L_BRACKET.getTokenType() != TokenType.L_BRACKET){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected [. Got: ");
                sb.append(L_BRACKET.getTokenType().toString()).append("\n");
                sb.append(L_BRACKET.getFilename() + ":" +L_BRACKET.getLineNum());
                throw new ParsingException(sb.toString());
            }

            // ----------------------------------------------------------------------------

            System.out.println("    3rd:"+L_BRACKET.getToken());

            // looking for b expr <-----------------------------
            Expr expr = Expr.parseExpr(tokens,nestLevel);


            // ----------------------------------------------------------------------------

            Token R_BRACKET = tokens.remove(0);
            System.out.println("    4th:"+R_BRACKET.getToken());
            // check for if
            if (R_BRACKET.getTokenType() != TokenType.R_BRACKET){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected ]. Got: ");
                sb.append(R_BRACKET.getTokenType().toString()).append("\n");
                sb.append(R_BRACKET.getFilename() + ":" +R_BRACKET.getLineNum());
                throw new ParsingException(sb.toString());
            }
            // ----------------------------------------------------------------------------

            Token L_BRACE = tokens.remove(0);
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

            Body body = Body.ParseBody(tokens, nestLevel);

            // ----------------------------------------------------------------------------


            Token R_BRACE = tokens.remove(0);
            System.out.println("    7th:"+R_BRACE.getToken());
            // check for if
            if (R_BRACE.getTokenType() != TokenType.R_BRACE){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected }. Got: ");
                sb.append(R_BRACE.getTokenType().toString()).append("\n");
                sb.append(R_BRACE.getFilename() + ":" +R_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            // ----------------------------------------------------------------------------
            System.out.println("    8th:"+R_BRACE.getToken());

            elif_lists.add(new ElseifStmt(expr,body));

            elseif = tokens.get(0);
        }


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
