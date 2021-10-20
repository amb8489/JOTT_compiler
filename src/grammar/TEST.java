package grammar;

import main.JottTokenizer;
import main.Token;

import java.util.ArrayList;

public class TEST {


    public static void main(String[] args) throws ParsingException {
        String filename = "src/tokenizerTestCases/number.jott";
        ArrayList<Token> tokens = JottTokenizer.tokenize(filename);

        ArrayList<Params> p = Params.parseParams(tokens,1);

        for (Params params :p) {
            System.out.print(params.convertToJott());
        }
    }
}
