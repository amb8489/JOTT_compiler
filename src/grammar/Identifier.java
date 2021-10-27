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
    private static final Set<String> idBanList = Set.of("while", "for", "True", "False", "if", "elseif", "else","print");

    Token id;

    /**
     * Constructor TODO
     * @param id TODO
     */
    public Identifier(Token id) { this.id = id; }

    public static void check(Token id) throws ParsingException {
        if (idBanList.contains(id.getToken())) {
            throw new ParsingException(String.format("can't use %s as id: line %d", id.getToken(),id.getLineNum()));
        }
    }

    public String convertToJott() { return id.getToken(); }

    public boolean validateTree() { return false; }
}
