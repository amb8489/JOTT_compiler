package grammar;

import main.Token;

import java.util.ArrayList;

public class WhileLoop extends BodyStmt {
    public WhileLoop(int nestLevel) {
        super(nestLevel);
    }

    public static WhileLoop parseWhile(ArrayList<Token> tokens, int nestLevel) {
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
