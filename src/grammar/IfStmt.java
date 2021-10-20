package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

public class IfStmt extends BodyStmt {
    public IfStmt(int nestLevel) {


        super(nestLevel);
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
            // TODO { body } elseif_lst  ||  else { body }

            return new IfStmt(nestLevel);

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
