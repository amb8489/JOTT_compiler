package grammar;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class TOKEN_IDX {
    public static int index = 0;
    public static ArrayList<Integer> savedTokenIndex = new ArrayList<>();

    /**
     * Constructor TODO
     */
    public TOKEN_IDX() {
        index = 0;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    public static int getTokenIndex() {
        return index;
    }

    /**
     * TODO
     */
    public static void saveTokenIndex() {
        savedTokenIndex.add(0, index);
    }

    /**
     * TODO
     */
    public static void restoreTokenIndex() {
        index = savedTokenIndex.remove(0);
        //System.out.println("Restoring to : "+ index);
    }

    /**
     * TODO
     */
    public static void popRestore() {
        savedTokenIndex.remove(0);
    }

    /**
     * TODO
     */
    public static void reset() {
        index = 0;
    }
}
