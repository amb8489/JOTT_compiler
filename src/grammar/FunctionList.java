package grammar;

import main.Token;

import java.util.ArrayList;


// function_def function_list | E

public class FunctionList {

    private ArrayList<FunctionDef> lstfuncs;


    public FunctionList(ArrayList<FunctionDef> listOfFunctionDefs) {
        this.lstfuncs = listOfFunctionDefs;
    }

    public static FunctionList parseFunctionList(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        System.out.println("------------------------PARSING FunctionList------------------------");

        ArrayList<FunctionDef> lstfuncs = new ArrayList<>();
        System.out.println("looking for function def");

        FunctionDef fd = FunctionDef.parseFunctionDef(tokens, 0);

        if (fd == null) {
            System.out.println(" NO init function def");

            return null;
        }
        lstfuncs.add(fd);

        while (TOKEN_IDX.IDX < tokens.size()) {
            System.out.println(" looking for more function def");
            fd = FunctionDef.parseFunctionDef(tokens, nestlevel);

            if (fd == null) {
                System.out.println("no more function def found ");
                return new FunctionList(lstfuncs);
            }
            lstfuncs.add(fd);

        }
        return new FunctionList(lstfuncs);

    }
    public boolean ListHasMain(){
        for(FunctionDef func: this.lstfuncs){
            if(func.id.convertToJott().equals("main")){
                return true;
            }
        }
        return false;
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


    public boolean validateTree() throws ParsingException {


        for (FunctionDef function: lstfuncs) {
            boolean r = function.validateTree();
            if (!r){
                return false;
            }
        }
        return true;
    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (FunctionDef fd: lstfuncs) {
            jstr.append(fd.convertToJott()+"\n");
        }
        return jstr.toString();
    }

}
