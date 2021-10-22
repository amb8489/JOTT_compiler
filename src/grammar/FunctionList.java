package grammar;

import main.Token;

import java.util.ArrayList;


// function_def function_list | E

public class FunctionList extends Program{

    private  ArrayList<FunctionDef> lstfuncs =null;


    public FunctionList(ArrayList<FunctionDef> listOfFunctionDefs) {
        super(null);
        this.lstfuncs = listOfFunctionDefs;
    }

    public static FunctionList parseFunctionList(ArrayList<Token> tokens, int nestlevel) throws ParsingException {

        ArrayList<FunctionDef> lstfuncs = new ArrayList<>();

        FunctionDef fd = FunctionDef.parseFunctionDef(tokens,nestlevel);
        if(fd == null){
            return null;
        }

        while (true){
            lstfuncs.add(fd);
            fd = FunctionDef.parseFunctionDef(tokens,nestlevel);
            if(fd == null){
                return new FunctionList(lstfuncs);
            }
        }
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
