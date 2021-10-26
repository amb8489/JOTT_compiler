package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;


// body -> body_stmt body|return_stmt|ε                                                                             <-- DONE

public class Body  {
    public ArrayList<BodyStmt> bodys = null;
    public ReturnStmt Hasreturn = null;
    int nestlevel;

    public Body(ArrayList<BodyStmt> bodys, ReturnStmt rs,int nestlevel) {
        this.Hasreturn = rs;
        this.bodys = bodys;
        this.nestlevel = nestlevel;
    }



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
                if (rs == null) {
                    System.out.println("EMPTY return");

                }
                return new Body(bodys, rs,nestLevel);
            }
            System.out.println("    adding body                         -----------" + bs.convertToJott());
            bodys.add(bs);
        }
        ReturnStmt rs = ReturnStmt.parseReturnStmt(tokens, nestLevel);

        return new Body(bodys, null,nestLevel);
    }


    public String convertToJott() {
        String SPACE = "    ".repeat(this.nestlevel);

        StringBuilder jstr = new StringBuilder();

        for (BodyStmt b : bodys) {
            jstr.append(SPACE+b.convertToJott() + "\n");
        }
        if (Hasreturn != null) {
            jstr.append(SPACE+Hasreturn.convertToJott()+"\n");
        }

        return jstr.toString();
    }
    public boolean validateTree() {
        return false;
    }


}
