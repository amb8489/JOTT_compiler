package grammar;

import main.JottTokenizer;
import main.Token;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class TEST {

    /**
     * Used for testing purposes
     *
     * @param args arguments that can be passed into the program
     * @throws ParsingException error!
     */
    public static void main(String[] args) throws ParsingException {
        String filename = "src/tokenizerTestCases/number.jott";
        ArrayList<Token> tokens = JottTokenizer.tokenize(filename);

        Program p = Program.parseProgram(tokens);
        p.validateTree();

        System.out.print(p.convertToJott());
        System.out.println("---------------------------------------------------------------------------------------------------");

        System.out.println("-----final var table-----");

        for (String scope : ValidateTable.scopes.keySet()) {
            System.out.println("    >>>>>>scope "+scope+"<<<<<<");
            for (String key : ValidateTable.getScope(scope).variables.keySet()) {
                String type = ValidateTable.getScope(scope).variables.get(key).get(0);
                String val = ValidateTable.getScope(scope).variables.get(key).get(1);
                System.out.println("        "+type + " " + key + " = " + val);
            }
        }

        System.out.println();


        System.out.println("-----final func table-----");


            for (String key : ValidateTable.functions.keySet()) {
                String type = ValidateTable.functions.get(key).get(0);

                System.out.print(key + " returns " + type + " with args: [ ");
                for (String s : ValidateTable.functions.get(key).subList(1, ValidateTable.functions.get(key).size())) {
                    System.out.print(s + " ");
                }
                System.out.println(" ]");

            }
    }
}
