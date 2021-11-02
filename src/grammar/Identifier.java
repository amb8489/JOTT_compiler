package grammar;

import main.Token;

import java.util.Set;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Identifier {
    private static final Set<String> idBanList = Set.of("while", "for", "True", "False", "if", "elseif", "else", "print", "concat", "length", "input");

    Token id;
    public String scope;

    /**
     * This is the constructor for an identifier.
     *
     * @param id the name of this identifier
     */
    public Identifier(Token id, String scope) {
        this.id = id;
        this.scope = scope;
    }

    public static void check(Token id) throws ParsingException {
        if (idBanList.contains(id.getToken())) {
            throw new ParsingException(String.format("use of keyword \"%s\" as variable name: line %d", id.getToken(), id.getLineNum()));
        }
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        return id.getToken();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return null;
    }

    /**
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        return false;
    }
}
