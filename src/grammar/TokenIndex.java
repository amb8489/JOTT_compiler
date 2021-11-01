package grammar;

import java.util.ArrayList;

/**
 * TokenIndex is used to keep track of exact token we are working on across multiple classes/objects.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class TokenIndex {
    public static int currentTokenIndex = 0;
    public static ArrayList<Integer> savedTokenIndex = new ArrayList<>(); // acts as a stack

    /**
     * This is the constructor for the token index.
     */
    public TokenIndex() {
        currentTokenIndex = 0;
        savedTokenIndex = new ArrayList<>();
    }

    /**
     * This is the getter for current token index.
     *
     * @return current token index
     */
    public static int getCurrentTokenIndex() {
        return currentTokenIndex;
    }

    /**
     * Remember the token index by putting it in the stack.
     */
    public static void saveCurrentTokenIndex() {
        savedTokenIndex.add(0, currentTokenIndex);
    }

    /**
     * Restore the token index with the saved token index from the stack.
     */
    public static void restoreFromSavedTokenIndexStack() {
        currentTokenIndex = savedTokenIndex.remove(0);
        //System.out.println("Restoring to : "+ index);
    }

    /**
     * Remove the saved token index from the array list.
     */
    public static void popSavedTokenIndexStack() {
        savedTokenIndex.remove(0);
    }

    /**
     * Reset the token index (set it to zero).
     */
    public static void reset() {
        currentTokenIndex = 0;
    }
}
