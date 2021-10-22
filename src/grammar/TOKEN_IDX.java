package grammar;

import java.util.ArrayList;

public class TOKEN_IDX {
    public static int IDX = 0;
    public static ArrayList<Integer> saved_token_IDX;

    public TOKEN_IDX(){
        IDX = 0;
    }

    public static int getToken_IDX() {
        return IDX;
    }

    public  static void save_token_IDX() { saved_token_IDX.add(0, IDX); }

    public  static void restore_token_IDX() {
        IDX = saved_token_IDX.remove(0);
    }

    public  static void popRestore() {
        saved_token_IDX.remove(0);
    }


}
