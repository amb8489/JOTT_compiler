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
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ValidateTable {
    /**
     * variables hashmap holds the variable type as well as its value.
     * <p>
     * note: value is null for uninitialized variables.
     */
    public static HashMap<String, ArrayList<String>> variables = new HashMap<>();

    /**
     * functions hashmap holds the variable type
     */
    public static HashMap<String, ArrayList<String>> functions = new HashMap<>();

    /**
     * clear all hashmaps
     */

    // built-in functions and there params

    // print takes 1 param of any type
    // input takes 2 params , first is string the second is int
    // concat takes 2 params , first is string the second is string
    // length takes 1 param of type String
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

    public static boolean checkFunctionCall(Token funcName, Params parameters) throws ParsingException {
        // check if function is a built in
        if (BuiltInFunctions.containsKey(funcName.getToken())) {
            // get the parms types for that function
            ArrayList<String> builtinParams = BuiltInFunctions.get(funcName.getToken());

            // check if there are even parameters passed in, all builtins take at least 1
            if (parameters != null) {

//                System.out.println(parameters.paramsList.size()+" "+builtinParams.size());

                // check that the params list is not null (can refoactor this, possibly un needed )
                if (parameters.paramsList != null) {

                    // check correct number of params given for that function
                    if (parameters.paramsList.size() == builtinParams.size()) {

                        // print can take any type so if we have 1 param we know we are set for PRINT
                        if (funcName.getToken().equals("print")) {
                            return true;
                        }

                        // index just to know what param we are on in loop
                        int index = 0;

                        // loop through all params and make sure that each param is the correct type for func
                        for (Params param : parameters.paramsList) {
                            // check expr type
                            param.expr.validateTree();
                            // update if expr is changed (this can be better in the future)
                            if (param.expr.type != null) {
                                param.expr.type = param.expr.expr.type;
                            }

                            // if given param at index matches type of function param at index
                            if (param.expr.type != null && !param.expr.type.equals(builtinParams.get(index))) {

                                //error if type for param is wrong
                                String msg = "function: " + funcName.getToken() + " def takes " + builtinParams + " was given:" + param.expr.type + "| line:" + funcName.getLineNum();
                                throw new ParsingException(msg);
                            }

                            // onto the next param to check
                            index++;
                        }
                        return true;
                    }
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


        return true;

    }

    // clears the value and function tables
    public static void clearTables() {
        variables.clear();
        functions.clear();
    }
}
