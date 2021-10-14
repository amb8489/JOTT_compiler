/*
  Jott parser tester. This will test the parsing phase of the Jott
  project.

  This tester assumes a working and valid tokenizer.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JottParserTester {
    ArrayList<TestCase> testCases;

    private static class TestCase{
        String testName;
        String fileName;
        boolean error;

        public TestCase(String testName, String fileName, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.error = error;
        }
    }

    private boolean tokensEqualNoFileData(Token t1, Token t2){
        return t1.getTokenType() == t2.getTokenType() &&
                t1.getToken().equals(t2.getToken());
    }

    private void createTestCases(){
        this.testCases = new ArrayList<>();
        testCases.add(new TestCase("provided writeup example1", "providedExample1.txt", false ));
        testCases.add(new TestCase("provided writeup example2 (error)", "providedExample2.txt", true ));
        testCases.add(new TestCase("provided writeup example3 (error)", "providedExample3.txt", true ));
        testCases.add(new TestCase("provided writeup example4 (error)", "providedExample4.txt", true ));
        testCases.add(new TestCase("provided writeup example5 (error)", "providedExample5.txt", true ));
        testCases.add(new TestCase("hello world", "helloWorld.txt", false ));
        testCases.add(new TestCase("1foo error (error)", "1foo.txt", true ));
        testCases.add(new TestCase("return <id> type mismatch", "returnId.txt", false ));
        testCases.add(new TestCase("type:var error (error)", "paramOrderSwapped.txt", true ));
        testCases.add(new TestCase("missing expr (error)", "missingExp.txt", true ));
        testCases.add(new TestCase("missingBrace (error)", "missingBrace.txt", true ));
        testCases.add(new TestCase("elseif without if (error)", "elseIfNoIf.txt", true ));
        testCases.add(new TestCase("missing return", "missingReturn.txt", false ));
        testCases.add(new TestCase("Void not valid param type (error)", "voidParam.txt", true ));
        testCases.add(new TestCase("function not defined", "funcNotDefined.txt", false ));
        testCases.add(new TestCase("mismatch return type", "mismatchedReturn.txt", false ));
        testCases.add(new TestCase("function call param type not matching", "funcCallParamInvalid.txt", false ));
        testCases.add(new TestCase("single expression program (error)", "singleExpr.txt", true ));
        testCases.add(new TestCase("valid while loop", "validLoop.txt", false ));
        testCases.add(new TestCase("missing main", "missingMain.txt", false ));
        testCases.add(new TestCase("main must be integer", "mainReturnNotInt.txt", false ));
        testCases.add(new TestCase("i_expr relop d_expr function return", "funcReturnInExpr.txt", false ));
        testCases.add(new TestCase("invalid asmt stmt (error)", "invalidAsmtStmt.txt", true ));
        testCases.add(new TestCase("missing comma in func_def_params (error)", "missingCommaParams.txt", true ));
        testCases.add(new TestCase("while is keyword, cannot be used as id", "whileKeyword.txt", false ));
        testCases.add(new TestCase("expr by itself (error)", "loneExpr.txt", true ));
        testCases.add(new TestCase("code after return (error)", "codeAfterReturn.txt", true ));
        testCases.add(new TestCase("lone minus (error)", "loneMinus.txt", true ));
        testCases.add(new TestCase("else without if (error)", "elseNoIf.txt", true ));
        testCases.add(new TestCase("missing closing } (error)", "missingClosing.txt", true ));
    }

    private boolean parserTest(TestCase test, String orginalJottCode){
        try {
            ArrayList<Token> tokens = JottTokenizer.tokenize("src/testCases/Phase2/" + test.fileName);

            if (tokens == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a list of tokens, but got null");
                System.err.println("\t\tPlease verify your tokenizer is working properly");
                return false;
            }


            System.out.println(tokenListString(tokens));
            ArrayList<Token> cpyTokens = new ArrayList<>(tokens);
            JottTree root = JottParser.parse(tokens);










            if (!test.error && root == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a JottTree and got null");

                return false;
            } else if (test.error && root == null) {
                return true;
            } else if (test.error) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a null and got JottTree");
                System.out.println("{{{{  "+root.convertToJott()+" }}}}");
                return false;
            }

            System.out.println("Orginal Jott Code:\n");
            System.out.println(orginalJottCode);
            System.out.println();

            String jottCode = root.convertToJott();
            System.out.println("Resulting Jott Code:\n");
            System.out.println(jottCode);

            try {
                FileWriter writer = new FileWriter("src/testCases/Phase2/parserTestTemp.txt");
                if (jottCode == null) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("Expected a program string; got null");
                    return false;
                }
                writer.write(jottCode);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<Token> newTokens = JottTokenizer.tokenize("src/testCases/Phase2/parserTestTemp.txt");

            if (newTokens == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("Tokenization of files dot not match.");
                System.err.println("Similar files should have same tokenization.");
                System.err.println("Expected: " + tokenListString(tokens));
                System.err.println("Got: null");
                return false;
            }

            if (newTokens.size() != cpyTokens.size()) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("Tokenization of files dot not match.");
                System.err.println("Similar files should have same tokenization.");
                System.err.println("Expected: " + tokenListString(cpyTokens));
                System.err.println("Got:    : " + tokenListString(newTokens));
                return false;
            }

            for (int i = 0; i < newTokens.size(); i++) {
                Token n = newTokens.get(i);
                Token t = cpyTokens.get(i);

                if (!tokensEqualNoFileData(n, t)) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("Token mismatch: Tokens do not match.");
                    System.err.println("Similar files should have same tokenization.");
                    System.err.println("Expected: " + tokenListString(cpyTokens));
                    System.err.println("Got     : " + tokenListString(newTokens));
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("Unknown Exception occured.");
            e.printStackTrace();
            return false;
        }
    }

    private String tokenListString(ArrayList<Token> tokens){
        StringBuilder sb = new StringBuilder();
        for (Token t: tokens) {
            sb.append(t.getToken());
            sb.append(":");
            sb.append(t.getTokenType().toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    private boolean runTest(TestCase test){
        System.out.println("Running Test: " + test.testName);
        String orginalJottCode;
        try {
            orginalJottCode = new String(
                    Files.readAllBytes(Paths.get("src/testCases/Phase2/" + test.fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return parserTest(test, orginalJottCode);

    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottParserTester tester = new JottParserTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for(JottParserTester.TestCase test: tester.testCases){
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