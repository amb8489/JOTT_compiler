package grammar;

import main.Token;

import java.util.ArrayList;


// function_def function_list | E

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class FunctionList {

    private final ArrayList<FunctionDef> listOfFunctionDefs;

    public FunctionList(ArrayList<FunctionDef> listOfFunctionDefs) {
        this.listOfFunctionDefs = listOfFunctionDefs;
    }

    public static FunctionList parseFunctionList(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        //System.out.println("------------------------PARSING FunctionList------------------------");

        ArrayList<FunctionDef> listOfFunctionDefs = new ArrayList<>();
        //System.out.println("looking for function def");

        FunctionDef functionDef = FunctionDef.parseFunctionDef(tokens, 0);

        if (functionDef == null) {
            //System.out.println(" NO init function def");
            return null;
        }
        listOfFunctionDefs.add(functionDef);

        while (TOKEN_IDX.index < tokens.size()) {
            //System.out.println(" looking for more function def");
            functionDef = FunctionDef.parseFunctionDef(tokens, nestlevel);

            if (functionDef == null) {
                //System.out.println("no more function def found ");
                return new FunctionList(listOfFunctionDefs);
            }
            listOfFunctionDefs.add(functionDef);

        }
        return new FunctionList(listOfFunctionDefs);

    }
    public boolean ListHasMain(){
        for(FunctionDef func: this.listOfFunctionDefs){
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

        for (FunctionDef function: listOfFunctionDefs) {

            boolean r = function.validateTree();
            ValidateTable.functions.put(function.id.convertToJott(),new ArrayList<String>() {
                {add(function.return_tpye.type);
                    //TODO this functions params
                    add(null);}});

            if (!r){
                return false;
            }
        }
        return true;
    }

    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();

        for (FunctionDef fd: listOfFunctionDefs) {
            jstr.append(fd.convertToJott()+"\n");
        }
        return jstr.toString();
    }

}
