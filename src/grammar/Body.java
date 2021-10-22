package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;



// body -> body_stmt body|return_stmt|ε                                                                             <-- DONE

public class Body extends FunctionDef {
    ArrayList<BodyStmt>bodys = null;
    ReturnStmt Hasreturn = null;

    public Body(int nestLevel) {
        super(nestLevel);
    }

    public Body(int nestLevel, Expr expr, Body body1) {
        super(nestLevel, expr, body1);
    }

    public Body(ArrayList<BodyStmt> bodys, ReturnStmt rs) {
        super(null);

    }


    public static Body ParseBody(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        ArrayList<BodyStmt> bodys = new ArrayList<>();

        // ------------------------ empty case -----------------
        while (tokens.get(0).getTokenType() != TokenType.R_BRACKET){
            System.out.println("    looking for body");
            BodyStmt bs =  BodyStmt.parseBodyStmt(tokens,nestLevel);

            System.out.println("    body:::"+bs.convertToJott());



            if (bs != null){
                ReturnStmt rs = ReturnStmt.parseReturnStmt(tokens,nestLevel);
                return new Body(bodys,rs);
            }

            bodys.add(bs);
        }

        return null;
    }


    @Override
    public String convertToJott() {

        StringBuilder jstr = new StringBuilder();

        for(BodyStmt b:bodys){
            jstr.append(b.convertToJott()+"\n");
        }
        if(Hasreturn != null){
            jstr.append(Hasreturn.convertToJott());
        }

        return jstr.toString();
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
