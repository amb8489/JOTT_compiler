package grammar;

import main.Token;

import java.util.ArrayList;


// function_def function_list | E

public class FunctionList {

    private  ArrayList<FunctionDef> lstfuncs;


    public FunctionList(ArrayList<FunctionDef> listOfFunctionDefs) {
        this.lstfuncs = listOfFunctionDefs;
    }

    public static FunctionList parseFunctionList(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        System.out.println("------------------------PARSING FunctionList------------------------");

        ArrayList<FunctionDef> lstfuncs = new ArrayList<>();
        System.out.println("looking for function def");

        FunctionDef fd = FunctionDef.parseFunctionDef(tokens,nestlevel);

        if(fd == null){
            System.out.println(" NO init function def");

            return null;
        }
        lstfuncs.add(fd);

        while (TOKEN_IDX.IDX < tokens.size()){
            System.out.println(" looking for more function def");
            fd = FunctionDef.parseFunctionDef(tokens,nestlevel);

            if(fd == null){
                System.out.println("no more function def found ");
                return new FunctionList(lstfuncs);
            }
            lstfuncs.add(fd);

        }
        return new FunctionList(lstfuncs);

    }




    public String convertToJava() {
        return null;
    }

    public String convertToC() {
        return null;
    }

    public String convertToPython() {
        return null;
    }


    public boolean validateTree() {
        return false;
    }

    public String convertToJott() {
        return "func list to jott not imp";
    }
}
