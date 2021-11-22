package grammar;

import main.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ValidateTable is responsible for validating the tables including list of functions, scopes, variables, etc
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ValidateTable {
    public static HashMap<String, ValidateTable> scopes = new HashMap<>();
    public static HashMap<String, ArrayList<String>> functions = new HashMap<>() {{
        put("print", new ArrayList<>() {{
            add("Void");
            add("any");
        }});
        put("input", new ArrayList<>() {{
            add("String");
            add("String");
            add("Integer");
        }});
        put("concat", new ArrayList<>() {{
            add("String");
            add("String");
            add("String");
        }});
        put("length", new ArrayList<>() {{
            add("Integer");

            add("String");
        }});
    }};

    // ----------------------- all built in function defs -----------------------------------
    public static Map<String, ArrayList<String>> builtInFunctions = new HashMap<>() {{
        put("print", new ArrayList<>() {{
            add("any");
        }});
        put("input", new ArrayList<>() {{
            add("String");
            add("Integer");
        }});
        put("concat", new ArrayList<>() {{
            add("String");
            add("String");
        }});
        put("length", new ArrayList<>() {{
            add("String");
        }});
    }};


    // this functions var and function defs
    public HashMap<String, ArrayList<String>> variables = new HashMap<>();
    public String functionName;

    public ValidateTable(String functionName) {
        this.functionName = functionName;
    }

    /**
     * check the function call
     *
     * @param functionName the name of function to check
     * @param parameters   parameters for the function to be checked
     * @return whether it's valid or not
     * @throws ParsingException if something is wrong, an exception is thrown
     */
    public static boolean checkFunctionCall(Token functionName, Params parameters) throws ParsingException {
        // check if function is a built in

        if (builtInFunctions.containsKey(functionName.getToken())) {

            // get the parms types for that function
            // get the params types for that function
            ArrayList<String> builtinParams = builtInFunctions.get(functionName.getToken());

            // check if there are even parameters passed in, all builtins take at least 1
            if (parameters.paramsList != null) {

//                System.out.println(parameters.paramsList.size()+" "+builtinParams.size());

                // check that the params list is not null (can refoactor this, possibly un needed )

                // check correct number of params given for that function
                if (parameters.paramsList.size() == builtinParams.size()) {

                    // index just to know what param we are on in loop
                    int index = 0;

                    // loop through all params and make sure that each param is the correct type for func
                    for (Params param : parameters.paramsList) {
                        // check expr type
                        param.expr.validateTree();
                        // update if expr is changed (this can be better in the future)
                        if (param.expr.expr.type != null) {
                            param.expr.type = param.expr.expr.type;
                        }

                        // if given param at index matches type of function param at index
                        if ((param.expr.type != null && !param.expr.type.equals(builtinParams.get(index)))) {
                            if (!builtinParams.get(index).equals("any")) {
                                //error if type for param is wrong
                                String message = String.format("function %s params takes %s was given wrong " +
                                                "type in params:%s %s", functionName.getToken(),
                                        builtinParams, param.expr.type, param.expr.convertToJott());
                                String fileName = functionName.getFilename();
                                int lineNum = functionName.getLineNum();
                                throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d",
                                        message, fileName, lineNum));


                            }
                        }

                        // onto the next param to check
                        index++;
                    }
                    return true;
                }
            } else {

                //error is number of params passed in where wrong
                String message = String.format("function %s takes %s params but 0 were given",
                        functionName.getToken(), builtinParams.size());
                String fileName = functionName.getFilename();
                int lineNum = functionName.getLineNum();
                throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", message, fileName, lineNum));
            }
            //error is number of params passed in where wrong
            //error is number of params passed in where wrong
            String message = String.format("function %s takes %s params but %s were given",
                    functionName.getToken(), builtinParams.size(), parameters.paramsList.size());
            String fileName = functionName.getFilename();
            int lineNum = functionName.getLineNum();
            throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", message, fileName, lineNum));
        }


        // check if function is a built in
        if (scopes.containsKey(functionName.getToken())) {

            // get the params types for that function
            ArrayList<String> userDefinedParams = ValidateTable.functions.get(functionName.getToken());

            // check if there are even parameters passed in, all builtins take at least 1
            if (parameters != null) {

//                System.out.println(parameters.paramsList.size()+" "+userDefinedParams.size());

                // check that the params list is not null (can refactor this, possibly un needed)
                if (parameters.paramsList != null) {

                    // check correct number of params given for that function

                    if (parameters.paramsList.size() == (userDefinedParams.size() - 1) / 2) {

                        // index just to know what param we are on in loop
                        int index = 0;

                        // loop through all params and make sure that each param is the correct type for func
                        for (Params param : parameters.paramsList) {
                            // check expr type
                            param.expr.validateTree();
                            // update if expr is changed (this can be better in the future)
                            if (param.expr.expr.type != null) {
                                param.expr.type = param.expr.expr.type;
                            }

                            // if given param at index matches type of function param at index type for defined param
                            // is 1 + 2*idx
                            if (param.expr.type != null && !param.expr.type.equals(userDefinedParams.get(2 +
                                    (2 * index)))) {

                                //error if type for param is wrong
                                String message = String.format("function %s params takes %s was given wrong type in" +
                                                " params:%s %s", functionName.getToken(),
                                        userDefinedParams.subList(1, userDefinedParams.size()),
                                        param.expr.type, param.expr.convertToJott());
                                String fileName = functionName.getFilename();
                                int lineNum = functionName.getLineNum();
                                throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", message,
                                        fileName, lineNum));

                            }

                            // onto the next param to check
                            index++;
                        }
                        return true;
                    }
                }
            } else {
                // if function has no params and more than 0 where given

                if ((userDefinedParams.size() - 1) / 2 != 0) {
                    //error is number of params passed in where wrong
                    String message = String.format("function: %s takes %s params but 0 were given",
                            functionName.getToken(), (userDefinedParams.size() - 1) / 2);
                    String fileName = functionName.getFilename();
                    int lineNum = functionName.getLineNum();
                    throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", message, fileName, lineNum));


                }
                return true;
            }
            //error is number of params passed in where wrong

            String message = String.format("function %s takes %s params but %s were given",
                    functionName.getToken(), (userDefinedParams.size() - 1) / 2, parameters.paramsList.size());
            String fileName = functionName.getFilename();
            int lineNum = functionName.getLineNum();
            throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", message, fileName, lineNum));


        }

        String message = String.format("function %s is undefined", functionName.getToken());
        String fileName = functionName.getFilename();
        int lineNum = functionName.getLineNum();
        throw new ParsingException(String.format("SemanticError:\n%s\n%s:%d", message, fileName, lineNum));

    }

    /**
     * clears the value and function tables
     */
    public static void clearAll() {
        for (String key : scopes.keySet()) {
            scopes.get(key).clearTables();
        }
        scopes.clear();
    }

    /**
     * add a variable to the scope
     *
     * @param scopeName the name of scope
     * @param varName   the name of the variable to be added
     * @param varType   the type of the variable to be added
     * @param varVal    the value of the variable to be added
     */
    public static void addVarToScope(String scopeName, String varName, String varType, String varVal) {
        ArrayList<String> returnAndVal = new ArrayList<>() {{
            add(varType);
            add(varVal);
        }};
        scopes.get(scopeName).variables.put(varName, returnAndVal);
    }

    public static ValidateTable getScope(String scopeName) {
        return scopes.get(scopeName);
    }

    public void clearTables() {
        variables.clear();
        functions.clear();
    }

    public static void newScope(String functionName) {

        scopes.put(functionName, new ValidateTable(functionName));
        addVarToScope(functionName, "True", "Boolean", "1 == 1");
        addVarToScope(functionName, "False", "Boolean", "1 == 0");

    }

    public static boolean isVarDefinedInScope(String scopeName, String varName) {
        return scopes.get(scopeName).variables.containsKey(varName);
    }
}
