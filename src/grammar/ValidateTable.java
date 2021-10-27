package grammar;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidateTable {


    // name ---> [type,value]     val is null for un initialized vars
    public static HashMap<String, ArrayList<String>> variables = new HashMap<>();


    // name ---> [ReturnType, params.. ]     params = tpye

    public static HashMap<String, ArrayList<String>> functions = new HashMap<>();




    public static void clearTables(){
        variables.clear();
        functions.clear();
    }
}
