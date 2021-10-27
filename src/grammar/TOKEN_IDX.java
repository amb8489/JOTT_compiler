package grammar;

import java.util.ArrayList;

public class TOKEN_IDX {
    public static int index = 0;
    public static ArrayList<Integer> savedTokenIndex = new ArrayList<>();

    /**
     * Constructor TODO
     */
    public TOKEN_IDX(){ index = 0; }

    /**
     * TODO
     * @return TODO
     */
    public static int getTokenIndex() { return index; }

    /**
     * TODO
     */
    public static void saveTokenIndex() { savedTokenIndex.add(0, index); }

    /**
     * TODO
     */
    public static void restoreTokenIndex() {
        index = savedTokenIndex.remove(0);
        System.out.println("Restoring to : "+ index);
    }

    /**
     * TODO
     */
    public static void popRestore() { System.out.println(savedTokenIndex.remove(0)+": IDX no longer needed"); }

    /**
     * TODO
     */
    public static void reset() { index = 0; }
}
