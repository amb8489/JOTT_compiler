package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class IfStmt extends BodyStmt {
    private Expr exp;
    private Body body1;
    private Body body2;
    private ElseifStmt elif;

    public IfStmt(int nestLevel, Expr exp,Body body1,ElseifStmt elif) {
        super(nestLevel);
        this.exp = exp;
        this.body1 = body1;
        this.elif = elif;

    }
    public IfStmt(int nestLevel, Expr exp,Body body1,Body body2,ElseifStmt elif) {
        super(nestLevel);
        this.exp = exp;
        this.body1 = body1;
        this.elif = elif;
        this.body2 = body2;

    }



    // -----------------------------------   TODO
    @Override
    public String convertToJott() {
        return null;
    }




//     * if_stmt ->  if [ b_expr ] { body } elseif_lst
//     *           | if [ b_expr ] { body } elseif_lst else { body }
    public static IfStmt parseIfStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING IF STMT------------------------");



        Token IfToken = tokens.remove(0);
        System.out.println("    1st:"+IfToken.getToken());
        // check for if
        if (!IfToken.getToken().equals("if")){
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected if. Got: ");
            sb.append(IfToken.getTokenType().toString()).append("\n");
            sb.append(IfToken.getFilename() + ":" +IfToken.getLineNum());
            throw new ParsingException(sb.toString());
        }
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

        Body body1 = Body.ParseBody(tokens, nestLevel);

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

        ElseifStmt elif = ElseifStmt.ParseElsif_lst(tokens, nestLevel);


        // ----------------------------------------------------------------------------

        // check for else token
        Token elseToken = tokens.get(0);
        System.out.println("    9th:"+elseToken.getToken());
        // check for if
        if (elseToken.getToken().equals("else")){
            tokens.remove(0);



            Token LL_BRACE = tokens.remove(0);
            System.out.println("    10th:"+LL_BRACE.getToken());
            // check for if
            if (LL_BRACE.getTokenType() != TokenType.L_BRACE){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected {. Got: ");
                sb.append(LL_BRACE.getTokenType().toString()).append("\n");
                sb.append(LL_BRACE.getFilename() + ":" +LL_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }

            System.out.println("    11th:body");

            Body body2 = Body.ParseBody(tokens, nestLevel);

            // ----------------------------------------------------------------------------


            Token RR_BRACE = tokens.remove(0);
            System.out.println("    12th:"+RR_BRACE.getToken());
            // check for if
            if (RR_BRACE.getTokenType() != TokenType.R_BRACE){
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected }. Got: ");
                sb.append(RR_BRACE.getTokenType().toString()).append("\n");
                sb.append(RR_BRACE.getFilename() + ":" +RR_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }

            // need to add somthing about elif being null <---------------------------TODO
            return new IfStmt( nestLevel, expr, body1, body2, elif);
        }

        // need to add somthing about elif being null <---------------------------TODO
        return new IfStmt( nestLevel, expr, body1, elif);
    }

        @Override
    public String convertToJava() {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
