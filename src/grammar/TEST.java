package grammar;

import main.JottTokenizer;
import main.Token;

import java.util.ArrayList;

public class TEST {


    public static void main(String[] args) throws ParsingException {
        String filename = "src/tokenizerTestCases/number.jott";
        ArrayList<Token> tokens = JottTokenizer.tokenize(filename);

        Expr p = Expr.parseExpr(tokens,0);

        assert p != null;
        System.out.print(p.convertToJott());
    }
}
