package grammar;

import main.Token;

import java.util.ArrayList;

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
        String filename = "program";
        for (FunctionDef func : this.listOfFunctionDefs) {
            filename = func.id.id.getFilename();
            if (func.id.convertToJott().equals("main")) {
                if (!func.returnType.type.equals("Integer")) {
                    String msg = "function main should return Integer";
                    String fileName = func.id.id.getFilename();
                    int lineNum = func.id.id.getLineNum();
                    throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", msg, fileName, lineNum));
                }
                return true;
            }
        }
        String msg = "program is missing a function called \"main\"";
        throw new ParsingException(String.format(String.format("SemanticError:\n%s\n%s", msg, filename)));
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
        StringBuilder javaString = new StringBuilder();

        for (FunctionDef fd : listOfFunctionDefs) {
        	javaString.append(String.format("%s\n", fd.convertToJava()));
        }
        return javaString.toString();
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
        StringBuilder PyString = new StringBuilder();

        int main_idx_in_lst = 0;

        for (FunctionDef fd : listOfFunctionDefs) {
            if (fd.id.id.getToken().equals("main")){
                break;
            }
            main_idx_in_lst++;
        }

        int idx = 0;
        for (FunctionDef fd : listOfFunctionDefs) {
            if(idx !=  main_idx_in_lst) {
                PyString.append(String.format("%s\n", fd.convertToPython()));
            }
        }



        PyString.append("if __name__ == '__main__':"+"\n\t");
        PyString.append(listOfFunctionDefs.get(main_idx_in_lst).convertToPython());
        return PyString.toString();
    }

    /**
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {


        for (FunctionDef functionDef : listOfFunctionDefs) {
            String scope = functionDef.id.id.getToken();


            // make new table for function so that that table will have its own scope
            ValidateTable.newScope(scope);


            ValidateTable.getScope(scope).functions.put(functionDef.id.convertToJott(), new ArrayList<>() {
                {
                    add(functionDef.returnType.type);
                }
            });
            // setting functionDef params
            if (functionDef.funcDefParams != null) {
                for (FuncDefParams params : functionDef.funcDefParams.functionParameterList) {
                    ValidateTable.getScope(scope).functions.get(functionDef.id.convertToJott()).add(params.identifier.convertToJott());
                    ValidateTable.getScope(scope).functions.get(functionDef.id.convertToJott()).add(params.type.getToken());

                    // adding functin params to scope:


                    if (params.type.getToken().equals("Integer")) {
                        ValidateTable.addVarToScope(scope, params.identifier.convertToJott(), params.type.getToken(), "1");
                    } else if (params.type.getToken().equals("Double")) {

                        ValidateTable.addVarToScope(scope, params.identifier.convertToJott(), params.type.getToken(), "1.0");
                    } else if (params.type.getToken().equals("String")) {
                        ValidateTable.addVarToScope(scope, params.identifier.convertToJott(), params.type.getToken(), "str");
                    } else {
                        ValidateTable.addVarToScope(scope, params.identifier.convertToJott(), params.type.getToken(), "1==1");
                    }


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
