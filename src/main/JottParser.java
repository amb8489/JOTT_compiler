package main; /**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import grammar.JottTree;
import grammar.ParsingException;
import grammar.Program;

import java.util.ArrayList;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){

        try {
            Program p = Program.parseProgram(tokens, 0);
            p.validateTree();
            return p;
        } catch (Exception | ParsingException e) {
            return null;
        }
    }
}
