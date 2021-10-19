package grammar;

public class FuncDefParamsList extends FuncDefParams {
    public FuncDefParamsList(int nestLevel) {
        super(nestLevel);
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
