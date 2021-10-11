import java.util.ArrayList;

public class JottParserTester {



    public static void main(String[] args) {
        String phase2TestPath = "src/tokenizerTestCases/Phase2/";

        for (int i = 1; i <= 25; i++) {
            String filename = String.format("%sproblem%d.jott", phase2TestPath, i);

            System.out.println(String.format("Problem %d (%s)\n=============", i, filename));

            try {
                ArrayList<Token> tokens = JottTokenizer.tokenize(filename);

                for (Token token : tokens) {
                    System.out.println(token);
                }
            } catch (Exception e) {
                System.out.println(e);
            }



        }
    }
}
