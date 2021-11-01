package grammar;

import main.Token;

import java.util.ArrayList;


// function_def function_list | E

/**
 * A function list holds a list of function definitions.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class FunctionList {

    private final ArrayList<FunctionDef> listOfFunctionDefs;

    /**
     * This is the constructor for a function list.
     *
     * @param listOfFunctionDefs a list of function definitions
     */
    public FunctionList(ArrayList<FunctionDef> listOfFunctionDefs) {
        this.listOfFunctionDefs = listOfFunctionDefs;
    }

    public static FunctionList parseFunctionList(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        ArrayList<FunctionDef> listOfFunctionDefs = new ArrayList<>();

        FunctionDef functionDef = FunctionDef.parseFunctionDef(tokens, 0);

        listOfFunctionDefs.add(functionDef);

        while (TokenIndex.currentTokenIndex < tokens.size()) {
            listOfFunctionDefs.add(FunctionDef.parseFunctionDef(tokens, nestlevel));
        }
        return new FunctionList(listOfFunctionDefs);

    }

    public boolean ListHasMain() throws ParsingException {
        for (FunctionDef func : this.listOfFunctionDefs) {
            if (func.id.convertToJott().equals("main")) {
                if (!func.returnType.type.equals("Integer")) {
                    throw new ParsingException("MAIN DOES NOT RETURN INT");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        for (FunctionDef fd : listOfFunctionDefs) {
            jottString.append(String.format("%s\n", fd.convertToJott()));
        }
        return jottString.toString();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return null;
    }

    /**
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {

        for (FunctionDef functionDef : listOfFunctionDefs) {
            ValidateTable.functions.put(functionDef.id.convertToJott(), new ArrayList<>() {
                {
                    add(functionDef.returnType.type);
                }
            });
            // setting functionDef params
            if (functionDef.funcDefParams != null) {
                for (FuncDefParams params : functionDef.funcDefParams.functionParameterList) {
                    ValidateTable.functions.get(functionDef.id.convertToJott()).add(params.identifier.convertToJott());
                    ValidateTable.functions.get(functionDef.id.convertToJott()).add(params.type.getToken());
                }
            }

            boolean r = functionDef.validateTree();
            // setting functionDef id and its return type


            if (!r) {
                return false;
            }
        }
        return true;
    }
}
