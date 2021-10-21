package grammar;

import main.Token;

import java.util.ArrayList;

public class Stmt extends BodyStmt {
    public Stmt(int nestLevel) {
        super(null);
    }

    public static Stmt parseStmt(ArrayList<Token> tokens, int nestLevel) {
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
