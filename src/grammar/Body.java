package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


// body -> body_stmt body|return_stmt|ε                                                                             <-- DONE

public class Body  {
    public ArrayList<BodyStmt> bodys = null;
    public ReturnStmt Hasreturn = null;


    public Body(ArrayList<BodyStmt> bodys, ReturnStmt rs) {
        this.Hasreturn = rs;
        this.bodys = bodys;
    }



    // TODO NOT CORRECT
    public static Body ParseBody(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        ArrayList<BodyStmt> bodys = new ArrayList<>();

        if (tokens.get(TOKEN_IDX.IDX).getTokenType() == TokenType.R_BRACKET) {
            System.out.println("EMPTY BODY");
            return null;
        }

        // ------------------------ empty case -----------------
        while (tokens.get(TOKEN_IDX.IDX).getTokenType() != TokenType.R_BRACE) {
            System.out.println("    looking for body");
            BodyStmt bs = BodyStmt.parseBodyStmt(tokens, nestLevel);
            System.out.println("    --->>>>>>>looking for body");

            if (bs == null) {
                System.out.println("EMPTY BODY 2");
                ReturnStmt rs = ReturnStmt.parseReturnStmt(tokens, nestLevel);
                return new Body(bodys, rs);
            }
            System.out.println("    adding body                         -----------" + bs.convertToJott());
            bodys.add(bs);
        }
        ReturnStmt rs = ReturnStmt.parseReturnStmt(tokens, nestLevel);

        return new Body(bodys, null);
    }


    public String convertToJott() {

        StringBuilder jstr = new StringBuilder();

        for (BodyStmt b : bodys) {
            jstr.append(b.convertToJott() + "\n");
        }
        if (Hasreturn != null) {
            jstr.append(Hasreturn.convertToJott());
        }

        return jstr.toString();
    }


}
