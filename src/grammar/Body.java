package grammar;

import main.Token;

import java.util.ArrayList;



// body -> body_stmt body|return_stmt|ε                                                                             <-- DONE

public class Body extends FunctionDef {
    public Body(int nestLevel) {
        super(nestLevel);
    }

    public Body(int nestLevel, Expr expr, Body body1) {
        super(nestLevel, expr, body1);
    }

    public static Body ParseBody(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        return null;
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
