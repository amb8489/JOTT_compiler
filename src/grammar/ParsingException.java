package grammar;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ParsingException extends Throwable {

    /**
     * This is the constructor to make a parse exception.
     *
     * @param toString the reason for this exception
     */
    public ParsingException(String toString) {
        System.err.println(toString);
    }
}
