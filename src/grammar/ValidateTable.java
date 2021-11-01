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

    public static Map<String, ArrayList<String>> BuiltInFunctions  = new HashMap<>() {{
        put("print", new ArrayList<>() {{add("any");}});
        put("input", new ArrayList<>() {{add("String");add("Integer");}});
        put("concat", new ArrayList<>() {{add("String");add("String");}});
        put("length", new ArrayList<>() {{add("String");}});
    }};


    public static boolean cheekFunctionCall(Token funcName, Params parameters) throws ParsingException {
        // check if function is a built in
        if(BuiltInFunctions.containsKey(funcName.getToken())){

            ArrayList<String> builtinParams = BuiltInFunctions.get(funcName.getToken());
            if(parameters != null) {
                System.out.println(parameters.paramsList.size()+" "+builtinParams.size());

                if (parameters.paramsList != null) {

                    // check correct number of params given for that function
                    if (parameters.paramsList.size() == builtinParams.size()) {
                        parameters.convertToJott();

                        // print can take any type
                        if (funcName.getToken().equals("print")) {
                            return true;
                        }

                        int idx = 0;
                        for (Params param : parameters.paramsList) {
                            System.out.println("--> checking for param: " + param.expr.convertToJott());
                            // wrrong type for fucntion params
                            param.expr.validateTree();

                            if(param.expr.type != null) {
                                param.expr.type = param.expr.expr.type;
                            }


                            System.out.println(param.expr.type+" "+builtinParams.get(idx));
                            if (!param.expr.type.equals(builtinParams.get(idx))) {
                                String msg = "function: " + funcName.getToken() + " def takes " + builtinParams.toString()+" was given:"+ param.expr.type +"| line:"+funcName.getLineNum();
                                throw new ParsingException(msg);
                            }

                            idx++;
                        }
                        return true;
                    }
                }
            }else{
                String msg = "function: " +funcName.getToken() + " takes "+builtinParams.size()+" params but 0 were given, line "+funcName.getLineNum();
                throw new ParsingException(msg);
            }

            String msg = "function: " +funcName.getToken() + " takes "+builtinParams.size()+" params but "+parameters.paramsList.size()+" were given, line "+funcName.getLineNum();
            throw new ParsingException(msg);
        }


        return true;

    }


    public static void clearTables() {
        variables.clear();
        functions.clear();
    }
}
