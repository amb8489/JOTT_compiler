package grammar;

import main.JottTokenizer;
import main.Token;

import java.util.ArrayList;

public class TEST {

    /**
     * Used for testing purposes
     * @param args arguments that can be passed into the program
     * @throws ParsingException error!
     */
    public static void main(String[] args) throws ParsingException {
        String filename = "src/tokenizerTestCases/number.jott";
        ArrayList<Token> tokens = JottTokenizer.tokenize(filename);

        Program p = Program.parseProgram(tokens, 0);
        p.validateTree();

        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.print(p.convertToJott());

        System.out.println("-----var table-----");

        for (String key : ValidateTable.variables.keySet()) {
            String type = ValidateTable.variables.get(key).get(0);
            String val = ValidateTable.variables.get(key).get(1);

            System.out.println(type+" "+key + " = "+val);
        }



    }
}
