package grammar;

import main.Token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ValidateTable {

    // name ---> [type,value]     val is null for un initialized vars
    public static HashMap<String, ArrayList<String>> variables = new HashMap<>();


    // name ---> [ReturnType, param1,param1_type, param2,param2_type ]
    // where param  id  = 1 + 2*n ,    where n is the idx of the params starting at 0
    //              ids type = 2 + 2*n

    // name ---> [ReturnType, params.. ]     params = type
    public static HashMap<String, ArrayList<String>> functions = new HashMap<>();





    // built in functions and there params

    // print takes 1 param of any type
    // input takes 2 params , first is string the second is int
    // concat takes 2 params , first is string the second is string
    // length takes 1 param of type String
    public static Map<String, ArrayList<String>> BuiltInFunctions  = new HashMap<>() {{
        put("print", new ArrayList<>() {{add("any");}});
        put("input", new ArrayList<>() {{add("String");add("Integer");}});
        put("concat", new ArrayList<>() {{add("String");add("String");}});
        put("length", new ArrayList<>() {{add("String");}});
    }};


    public static boolean cheekFunctionCall(Token funcName, Params parameters) throws ParsingException {
        // check if function is a built in
        if(BuiltInFunctions.containsKey(funcName.getToken())){
            // get the parms types for that function
            ArrayList<String> builtinParams = BuiltInFunctions.get(funcName.getToken());

            // check if there are even parameters passed in, all builtins take at least 1
            if(parameters != null) {

//                System.out.println(parameters.paramsList.size()+" "+builtinParams.size());

                // check that the params list is not null (can refoactor this, possibly un needed )
                if (parameters.paramsList != null) {

                    // check correct number of params given for that function
                    if (parameters.paramsList.size() == builtinParams.size()) {

                        // print can take any type so if we have 1 param we know we are set for PRINT
                        if (funcName.getToken().equals("print")) {
                            return true;
                        }

                        // idx just to know what param we are on in loop
                        int idx = 0;

                        // loop though all params and make sure that each param is the correct typr for func
                        for (Params param : parameters.paramsList) {
//                            System.out.println("--> checking for param: " + param.expr.convertToJott());

                            // check expr type
                            param.expr.validateTree();
                            // update if expr is changed (this can be better in the future)
                            if(param.expr.type != null) {
                                param.expr.type = param.expr.expr.type;
                            }


//                            System.out.println(param.expr.type+" "+builtinParams.get(idx));

                            // if given param at idx matches type of fucntion param at idx
                            if (!param.expr.type.equals(builtinParams.get(idx))) {

                                //error if type for param is wrong
                                String msg = "function: " + funcName.getToken() + " def takes " + builtinParams.toString()+" was given:"+ param.expr.type +"| line:"+funcName.getLineNum();
                                throw new ParsingException(msg);
                            }

                            // onto the next param to check
                            idx++;
                        }
                        return true;
                    }
                }
            }else{

                //error is number of params passed in where wrong
                String msg = "function: " +funcName.getToken() + " takes "+builtinParams.size()+" params but 0 were given, line "+funcName.getLineNum();
                throw new ParsingException(msg);
            }
            //error is number of params passed in where wrong
            String msg = "function: " +funcName.getToken() + " takes "+builtinParams.size()+" params but "+parameters.paramsList.size()+" were given, line "+funcName.getLineNum();
            throw new ParsingException(msg);
        }


        return true;

    }

    // clears the value and function tables
    public static void clearTables() {
        variables.clear();
        functions.clear();
    }
}
