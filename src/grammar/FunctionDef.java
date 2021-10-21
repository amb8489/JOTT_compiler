package grammar;

public class FunctionDef extends FunctionList {
    public FunctionDef(int nestLevel) {
        super(nestLevel);
    }

    public FunctionDef(int nestLevel, Expr expr, Body body1) {
        super(nestLevel, expr, body1);
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
