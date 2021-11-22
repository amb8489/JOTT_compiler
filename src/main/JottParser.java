package main;

import grammar.JottTree;
import grammar.ParsingException;
import grammar.Program;
import grammar.ValidateTable;

import java.util.ArrayList;

/**
 * This class is responsible for tokenizing convert.Jott code.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a convert.Jott Parse Tree.
     * @param tokens the ArrayList of convert.Jott tokens to parse
     * @return the root of the convert.Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){

        try {
            JottTree tree = Program.parseProgram(tokens);
            return tree;
        } catch (Exception | ParsingException e) {
            return null;
        }
    }
}
