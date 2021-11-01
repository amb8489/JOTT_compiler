package grammar;

import main.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ValidateTable holds
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 */
public class ValidateTable {

    //------------------a hashmap of all functions name to functions tables-----------------
    public static HashMap<String, ValidateTable> Scopes = new HashMap<>();
    public static HashMap<String, ArrayList<String>> functions = new HashMap<>();

    // ----------------------- all built in funciton defs -----------------------------------
    public static Map<String, ArrayList<String>> BuiltInFunctions = new HashMap<>() {{
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
    public String funcName;

    public ValidateTable(String funcName){
        this.funcName = funcName;
    }





    /**
     * clear all hashmaps
     */

    public static boolean checkFunctionCall(String scope,Token funcName, Params parameters) throws ParsingException {
        // check if function is a built in
        if (BuiltInFunctions.containsKey(funcName.getToken())) {
            System.out.println("BUILTIN FUNCTION");

            // get the parms types for that function
            ArrayList<String> builtinParams = BuiltInFunctions.get(funcName.getToken());

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
                        if ((param.expr.type != null && !param.expr.type.equals(builtinParams.get(index)) )) {
                            if(! builtinParams.get(index).equals("any")) {
                                //error if type for param is wrong
                                String msg = "function: " + funcName.getToken() + " def takes " + builtinParams + " was given:" + param.expr.type + "| line:" + funcName.getLineNum();
                                throw new ParsingException(msg);
                            }
                        }

                        // onto the next param to check
                        index++;
                    }
                    return true;
                }
            } else {

                //error is number of params passed in where wrong
                String message = "function: " + funcName.getToken() + " takes " + builtinParams.size() + " params but 0 were given, line " + funcName.getLineNum();
                throw new ParsingException(message);
            }
            //error is number of params passed in where wrong
            String message = "function: " + funcName.getToken() + " takes " + builtinParams.size() + " params but " + parameters.paramsList.size() + " were given, line " + funcName.getLineNum();
            throw new ParsingException(message);
        }


        // check if function is a built in
        if (Scopes.containsKey(funcName.getToken())) {
            System.out.println("USER DEFINED FUNCTION");

            // get the parms types for that function
            ArrayList<String> userDefinedParams = ValidateTable.functions.get(funcName.getToken());

            // check if there are even parameters passed in, all builtins take at least 1
            if (parameters != null) {

//                System.out.println(parameters.paramsList.size()+" "+userDefinedParams.size());

                // check that the params list is not null (can refoactor this, possibly un needed )
                if (parameters.paramsList != null) {

                    // check correct number of params given for that function
                    if (parameters.paramsList.size() == (userDefinedParams.size()-1)/2) {

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

                            // if given param at index matches type of function param at index type for defined param is 1 + 2*idx
                            System.out.println("("+param.expr.type+")"+ "("+userDefinedParams.get((2*index+2))+")");
                            if (param.expr.type != null && !param.expr.type.equals(userDefinedParams.get(2+(2*index)))) {

                                //error if type for param is wrong
                                String msg = "function: " + funcName.getToken() + " def takes " + userDefinedParams.subList(1,userDefinedParams.size()) + " was given:" + param.expr.type + "| line:" + funcName.getLineNum();
                                throw new ParsingException(msg);
                            }

                            // onto the next param to check
                            index++;
                        }
                        return true;
                    }
                }
            } else {
                // if function has no params and more than 0 where given
                if ((userDefinedParams.size()-1)/2 !=0) {
                    //error is number of params passed in where wrong
                    String message = "function: " + funcName.getToken() + " takes " + (userDefinedParams.size() - 1) / 2 + " params but 0 were given, line " + funcName.getLineNum();
                    throw new ParsingException(message);
                }
                return true;
            }
            //error is number of params passed in where wrong
            String message = "function: " + funcName.getToken() + " takes " + (userDefinedParams.size()-1)/2  + " params but " + parameters.paramsList.size() + " were given, line " + funcName.getLineNum();
            throw new ParsingException(message);
        }

        throw new ParsingException("undefined function: line "+ funcName.getLineNum());

    }

    // clears the value and function tables


    public static void clearAll() {
        for (String key : Scopes.keySet()) {
         Scopes.get(key).clearTables();
        }
        Scopes.clear();
    }



    public static void addVarToScope(String scopeName, String varName, String varType,String varVal) {
        ArrayList<String> returnAndVal = new ArrayList<>() {{add(varType);add(varVal);}};
        System.out.println(String.format("adding %s %s to scope %s",varType,varName,scopeName));
        Scopes.get(scopeName).variables.put(varName,returnAndVal);
    }

    public static ValidateTable getScope(String scopeName) {
        return Scopes.get(scopeName);
    }

    public void clearTables() {
        variables.clear();
        functions.clear();
    }

    public static void newScope(String functionName) {
        Scopes.put(functionName,new ValidateTable(functionName));
    }


    public static boolean isVarDefinedInScope(String scopeName,String varName){
        return Scopes.get(scopeName).variables.containsKey(varName);
    }



}
