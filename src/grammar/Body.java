package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;



// body -> body_stmt body|return_stmt|ε                                                                             <-- DONE

public class Body extends FunctionDef {
    public ArrayList<BodyStmt>bodys = null;
    public ReturnStmt Hasreturn = null;



    public Body(ArrayList<BodyStmt> bodys, ReturnStmt rs) {
        super(new Identifier(null));
        this.Hasreturn = rs;
        this.bodys = bodys;
    }

    public Body(Object bodys) {
        super(null);
    }


    public static Body ParseBody(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        ArrayList<BodyStmt> bodys = new ArrayList<>();

        // ------------------------ empty case -----------------
        while (tokens.get(TOKEN_IDX.IDX).getTokenType() != TokenType.R_BRACKET){
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
