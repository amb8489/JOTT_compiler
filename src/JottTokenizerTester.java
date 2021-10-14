/*
 * Tester for phase 1 of the Jott Translator
 *
 * This is not an all-inclusive tester. This tests a few of the common cases.
 *
 * @author Scott C Johnson (scj@cs.rit.edu)
 */

import java.util.ArrayList;

public class JottTokenizerTester {

    ArrayList<TestCase> testCases;

    private static class TestCase{
        String testName;
        String fileName;
        ArrayList<Token> tokens;
        boolean error;

        public TestCase(String testName, String fileName, ArrayList<Token> tokens, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.tokens = tokens;
            this.error = error;
        }
    }

    private boolean tokensEqual(Token t1, Token t2){
        return t1.getTokenType() == t2.getTokenType() &&
                t1.getToken().equals(t2.getToken()) &&
                t1.getFilename().equals(t2.getFilename()) &&
                t1.getLineNum() == t2.getLineNum();
    }

    private void createTestCases(){
        this.testCases = new ArrayList<>();
        ArrayList<Token> numTestTokens = new ArrayList<>();
        String filename = "src/testCases/Phase1/number.txt";
        numTestTokens.add(new Token("5", filename, 1, TokenType.NUMBER));
        numTestTokens.add(new Token("5.5", filename, 1, TokenType.NUMBER));
        numTestTokens.add(new Token(".5", filename, 1, TokenType.NUMBER));
        testCases.add(new TestCase( "validNumberTest", filename, numTestTokens, false));

        ArrayList<Token> mathOpTokens = new ArrayList<>();
        String mathOps = "+-*/";
        filename = "src/testCases/Phase1/mathOpsTest.txt";
        for(char c: mathOps.toCharArray()){
            mathOpTokens.add(new Token("" + c, filename, 1, TokenType.MATH_OP));
        }
        testCases.add(new TestCase("MathOps", filename, mathOpTokens, false));

        ArrayList<Token> singleCharTokens = new ArrayList<>();
        filename = "src/testCases/Phase1/singleCharTokens.txt";
        singleCharTokens.add(new Token(",", filename, 1, TokenType.COMMA));
        singleCharTokens.add(new Token("[", filename, 1, TokenType.L_BRACKET));
        singleCharTokens.add(new Token("]", filename, 1, TokenType.R_BRACKET));
        singleCharTokens.add(new Token("{", filename, 1, TokenType.L_BRACE));
        singleCharTokens.add(new Token("}", filename, 1, TokenType.R_BRACE));
        singleCharTokens.add(new Token(";", filename, 1, TokenType.SEMICOLON));
        singleCharTokens.add(new Token(":", filename, 1, TokenType.COLON));
        singleCharTokens.add(new Token("=", filename, 1, TokenType.ASSIGN));
        testCases.add(new TestCase("SingleCharTokens", filename, singleCharTokens, false));

        ArrayList<Token> relOpsTokens = new ArrayList<>();
        filename = "src/testCases/Phase1/relOpsTokens.txt";
        relOpsTokens.add(new Token("<", filename, 1, TokenType.REL_OP));
        relOpsTokens.add(new Token("<=", filename, 1, TokenType.REL_OP));
        relOpsTokens.add(new Token(">", filename, 1, TokenType.REL_OP));
        relOpsTokens.add(new Token(">=", filename, 1, TokenType.REL_OP));
        relOpsTokens.add(new Token("==", filename, 1, TokenType.REL_OP));
        relOpsTokens.add(new Token("!=", filename, 1, TokenType.REL_OP));
        testCases.add(new TestCase("RelOpTokens", filename, relOpsTokens, false));

        ArrayList<Token> idKeywordTokens = new ArrayList<>();
        filename = "src/testCases/Phase1/idKeywords.txt";
        idKeywordTokens.add(new Token("hello", filename, 1, TokenType.ID_KEYWORD));
        idKeywordTokens.add(new Token("Hello", filename, 1, TokenType.ID_KEYWORD));
        idKeywordTokens.add(new Token("Integer", filename, 1, TokenType.ID_KEYWORD));
        idKeywordTokens.add(new Token("Double", filename, 1, TokenType.ID_KEYWORD));
        idKeywordTokens.add(new Token("abc123", filename, 1, TokenType.ID_KEYWORD));
        idKeywordTokens.add(new Token("heLlO", filename, 1, TokenType.ID_KEYWORD));
        idKeywordTokens.add(new Token("Bar123Abc", filename, 1, TokenType.ID_KEYWORD));
        testCases.add(new TestCase("IdKeywordTokens", filename, idKeywordTokens, false));

        ArrayList<Token> stringTokens = new ArrayList<>();
        filename = "src/testCases/Phase1/strings.txt";
        stringTokens.add(new Token("\"foobar\"", filename, 1, TokenType.STRING));
        stringTokens.add(new Token("\"abc1\"", filename, 1, TokenType.STRING));
        stringTokens.add(new Token("\"Hello World\"", filename, 1, TokenType.STRING));
        stringTokens.add(new Token("\"123 abc\"", filename, 1, TokenType.STRING));
        testCases.add(new TestCase("StringTokens", filename, stringTokens, false));

        filename = "src/testCases/Phase1/errorTokens1.txt";
        testCases.add(new TestCase("ErrorTokens1", filename, null, true));

        filename = "src/testCases/Phase1/errorTokens2.txt";
        testCases.add(new TestCase("ErrorTokens2", filename, null, true));

        filename = "src/testCases/Phase1/errorTokens3.txt";
        testCases.add(new TestCase("ErrorTokens3", filename, null, true));

        filename = "src/testCases/Phase1/stringMissingClosing.txt";
        testCases.add(new TestCase("StringMissingClosing", filename, null, true));

        ArrayList<Token> phase1ExampleTokens = new ArrayList<>();
        filename = "src/testCases/Phase1/phase1Example.txt";
        phase1ExampleTokens.add(new Token("main", filename, 2, TokenType.ID_KEYWORD));
        phase1ExampleTokens.add(new Token("[", filename, 2, TokenType.L_BRACKET));
        phase1ExampleTokens.add(new Token("]", filename, 2, TokenType.R_BRACKET));
        phase1ExampleTokens.add(new Token(":", filename, 2, TokenType.COLON));
        phase1ExampleTokens.add(new Token("Integer", filename, 2, TokenType.ID_KEYWORD));
        phase1ExampleTokens.add(new Token("{", filename, 2, TokenType.L_BRACE));
        phase1ExampleTokens.add(new Token("print", filename, 3, TokenType.ID_KEYWORD));
        phase1ExampleTokens.add(new Token("[", filename, 3, TokenType.L_BRACKET));
        phase1ExampleTokens.add(new Token("5", filename, 3, TokenType.NUMBER));
        phase1ExampleTokens.add(new Token("]", filename, 3, TokenType.R_BRACKET));
        phase1ExampleTokens.add(new Token(";", filename, 3, TokenType.SEMICOLON));
        phase1ExampleTokens.add(new Token("print", filename, 5, TokenType.ID_KEYWORD));
        phase1ExampleTokens.add(new Token("[", filename, 5, TokenType.L_BRACKET));
        phase1ExampleTokens.add(new Token("\"foo bar\"", filename, 5, TokenType.STRING));
        phase1ExampleTokens.add(new Token("]", filename, 5, TokenType.R_BRACKET));
        phase1ExampleTokens.add(new Token(";", filename, 5, TokenType.SEMICOLON));
        phase1ExampleTokens.add(new Token("}", filename, 6, TokenType.R_BRACE));
        testCases.add(new TestCase("Phase1ExampleTest", filename, phase1ExampleTokens, false));

        filename = "src/testCases/Phase1/phase1ErrorExample.txt";
        testCases.add(new TestCase("Phase1ErrorExampleTest", filename, null, true));



        // = - =- =- =- =- =- =- = -= -= -= -= -= -= -= - =- =- =- =- =- =- =- =- =- =- = -=- =-= -= -= -
        // = - =- =- =- =- =- =- = -= -= -= -= -= -MY TESTS -= -= -= =- =- =- =- =- =- =- = -=- =-= -= -=
        // = - =- =- =- =- =- =- = -= -= -= -= -= -= -= - =- =- =- =- =- =- =- =- =- =- = -=- =-= -= -= -

        //errors
        filename = "src/testCases/Phase1/decimal_to_letter_error.txt";
        testCases.add(new TestCase(".A_test", filename, null, true));

        filename = "src/testCases/Phase1/incompletestringerror.txt";
        testCases.add(new TestCase("incomplete string", filename, null, true));

        filename = "src/testCases/Phase1/incoplete_dec_error.txt";
        testCases.add(new TestCase("incomplete decimal", filename, null, true));

        filename = "src/testCases/Phase1/incoplete_dec_error2.txt";
        testCases.add(new TestCase("multi line error decimal", filename, null, true));

        // non errors
        ArrayList<Token> multilineTokens = new ArrayList<>();
        filename = "src/testCases/Phase1/multLINE.txt";

        multilineTokens.add(new Token("String", filename, 1, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("foo", filename, 1, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("=", filename, 1, TokenType.ASSIGN));
        multilineTokens.add(new Token("concat", filename, 2, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("[", filename, 2, TokenType.L_BRACKET));
        multilineTokens.add(new Token("\"bar\"", filename, 2, TokenType.STRING));
        multilineTokens.add(new Token(",", filename, 2, TokenType.COMMA));
        multilineTokens.add(new Token("\"baz\"", filename, 2, TokenType.STRING));
        multilineTokens.add(new Token("]", filename, 2, TokenType.R_BRACKET));
        multilineTokens.add(new Token(";", filename, 2, TokenType.SEMICOLON));
        multilineTokens.add(new Token("String", filename, 4, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("foo", filename, 4, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("=", filename, 4, TokenType.ASSIGN));
        multilineTokens.add(new Token("9", filename, 5, TokenType.NUMBER));
        multilineTokens.add(new Token(";", filename, 5, TokenType.SEMICOLON));
        multilineTokens.add(new Token("String", filename, 7, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("foo", filename, 7, TokenType.ID_KEYWORD));
        multilineTokens.add(new Token("=", filename, 7, TokenType.ASSIGN));
        multilineTokens.add(new Token(".2", filename, 8, TokenType.NUMBER));
        multilineTokens.add(new Token(";", filename, 8, TokenType.SEMICOLON));

        testCases.add(new TestCase("multiline", filename, multilineTokens, false));



        //=-=-=-=-=-=-=-=-=-=-=-==-==-=-///
        filename = "src/testCases/Phase1/mult_stat_on_line.txt";

        ArrayList<Token> multiStatement = new ArrayList<>();
        multiStatement.add(new Token("String", filename, 1, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("hello", filename, 1, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("=", filename, 1, TokenType.ASSIGN));
        multiStatement.add(new Token("\"Hello\"", filename, 1, TokenType.STRING));
        multiStatement.add(new Token(";", filename, 1, TokenType.SEMICOLON));
        multiStatement.add(new Token("String", filename, 1, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("world", filename, 1, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("=", filename, 1, TokenType.ASSIGN));
        multiStatement.add(new Token("\" World\"", filename, 1, TokenType.STRING));
        multiStatement.add(new Token(";", filename, 1, TokenType.SEMICOLON));

        multiStatement.add(new Token("String", filename, 2, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("hello", filename, 2, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("=", filename, 2, TokenType.ASSIGN));
        multiStatement.add(new Token("\"Hello\"", filename, 2, TokenType.STRING));
        multiStatement.add(new Token(";", filename, 2, TokenType.SEMICOLON));
        multiStatement.add(new Token("String", filename, 2, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("world", filename, 2, TokenType.ID_KEYWORD));
        multiStatement.add(new Token("=", filename, 2, TokenType.ASSIGN));
        multiStatement.add(new Token("\" World\"", filename, 2, TokenType.STRING));
        multiStatement.add(new Token(";", filename, 2, TokenType.SEMICOLON));
        testCases.add(new TestCase("multiStatement", filename, multiStatement, false));


    }

    private String tokenToString(Token t){
        return String.format("Token %s %s %s:%d", t.getToken(), t.getTokenType().toString(),
                                                  t.getFilename(), t.getLineNum());
    }

    private boolean runTest(TestCase test){
        System.out.println("Running Test: " + test.testName);
        ArrayList<Token> testResults = JottTokenizer.tokenize(test.fileName);
        if(test.error){
            if(testResults != null){
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected error but got a token list");
                return false;
            }
            return true;
        }

        if(testResults == null){
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected a list of tokens, but got null");
            return false;
        }

        if(testResults.size() != test.tokens.size()){
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("\t\tExpected " + test.tokens.size() + " tokens");
            System.err.println("\t\tGot " + testResults.size() + " tokens");
            System.err.println("\t\tTokens:");
            for(Token t: testResults){
                System.err.println("\t\t\t" + tokenToString(t));
            }
            return false;
        }
        int index = 0;
        boolean passed = true;
        for(Token t: testResults){
            if(!tokensEqual(t, test.tokens.get(index))){
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected token at index " + index + " incorrect");
                System.err.println("\t\tExpected: " + tokenToString(test.tokens.get(index)));
                System.err.println("\t\tGot: " + tokenToString(t));
                passed = false;
            }
            index++;
        }

        return passed;
    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottTokenizerTester tester = new JottTokenizerTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for(TestCase test: tester.testCases){
            numTests++;
            if(tester.runTest(test)){
                passedTests++;
                System.out.println("\tPassed\n");
            }
            else{
                System.out.println("\tFailed\n");
            }
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }
}
