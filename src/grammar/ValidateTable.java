package grammar;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidateTable {


    // name ---> [type,value]     val is null for un initialized vars
    public static HashMap<String, ArrayList<String>> variables = new HashMap<>();


    // name ---> [ReturnType, param1,param1_type, param2,param2_type ]
    // where param  id  = 1 + 2*n ,    where n is the idx of the params starting at 0
    //              ids type = 2 + 2*n

    public static HashMap<String, ArrayList<String>> functions = new HashMap<>();




    public static void clearTables(){
        variables.clear();
        functions.clear();
    }
}
