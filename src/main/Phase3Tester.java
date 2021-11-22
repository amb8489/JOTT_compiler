package main;

import grammar.JottTree;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Phase3Tester {
        ArrayList<main.Phase3Tester.TestCase> testCases;

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
            testCases.add(new main.Phase3Tester.TestCase("funcCallParamInvalid.jott", "funcCallParamInvalid.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("funcNotDefined.jott", "funcNotDefined.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("funcReturnInExpr.jott", "funcReturnInExpr.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("funcWrongParamType.jott", "funcWrongParamType.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("helloWorld.jott", "helloWorld.jott", false ));
            testCases.add(new main.Phase3Tester.TestCase("ifStmtReturns.jott", "ifStmtReturns.jott", false ));
            testCases.add(new main.Phase3Tester.TestCase("largerValid.jott", "largerValid.jott", false ));
            testCases.add(new main.Phase3Tester.TestCase("mainReturnNotint.jott", "mainReturnNotint.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("mismatchedReturn.jott", "mismatchedReturn.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("missingFunctionParams.jott", "missingFuncParams.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("missingMain.jott", "missingMain.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("missingReturn.jott", "missingReturn.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("noReturnif.jott", "noReturnif.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("noReturnWhile.jott", "noReturnWhile.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("providedExample1.jott", "providedExample1.jott", false ));
            testCases.add(new main.Phase3Tester.TestCase("returnid.jott", "returnid.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("validLoop.jott", "validLoop.jott", false ));
            testCases.add(new main.Phase3Tester.TestCase("voidReturn.jott", "voidReturn.jott", true ));
            testCases.add(new main.Phase3Tester.TestCase("whileKeyword.jott", "whileKeyword.jott", true ));
        }

        private boolean parserTest(main.Phase3Tester.TestCase test, String orginalJottCode){
            try {
                ArrayList<Token> tokens = JottTokenizer.tokenize("src/main/" + test.fileName);

                if (tokens == null) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("\t\tExpected a list of tokens, but got null");
                    System.err.println("\t\tPlease verify your tokenizer is working properly");
                    return false;
                }

//                System.out.println(tokenListString(tokens));
                ArrayList<Token> cpyTokens = new ArrayList<>(tokens);
                JottTree root = JottParser.parse(tokens);

                if (!root.validateTree()){
                    root = null;
                }

                if (!test.error && root == null) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("\t\tExpected a JottTree and got null");
                    return false;
                } else if (test.error && root == null) {
                    return true;
                } else if (test.error) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("\t\tExpected a null and got JottTree");
                    System.err.println("what was parsed:\n"+root.convertToJott());
                    return false;
                }

//                System.out.println("Orginal convert.Jott Code:\n");
//                System.out.println(orginalJottCode);
//                System.out.println();

                String jottCode = root.convertToJott();
//                System.out.println("Resulting convert.Jott Code:\n");
//                System.out.println(jottCode);

                try {
                    FileWriter writer = new FileWriter("src/main/parserTestTemp.jott");
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

                ArrayList<Token> newTokens = JottTokenizer.tokenize("src/main/parserTestTemp.jott");

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
//            System.out.println(tokens.size());

            for (Token t: tokens) {
                sb.append(t.getToken());
                sb.append(":");
                sb.append(t.getTokenType().toString());
                sb.append(" ");
            }
            return sb.toString();
        }

        private boolean runTest(main.Phase3Tester.TestCase test){
            System.out.println("Running Test: " + test.testName);
            String orginalJottCode;
            try {
                orginalJottCode = new String(
                        Files.readAllBytes(Paths.get("src/main/" + test.fileName).toAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return parserTest(test, orginalJottCode);

        }

        public static void main(String[] args) throws InterruptedException {
            System.out.println("NOTE: System.err may print at the end. This is fine.");
            main.Phase3Tester tester = new main.Phase3Tester();

            int numTests = 0;
            int passedTests = 0;
            tester.createTestCases();
            for(main.Phase3Tester.TestCase test: tester.testCases){
                numTests++;
                if(tester.runTest(test)){
                    passedTests++;
                    System.out.println("\tPassed\n");
                }
                else{
                    System.out.println("\tFailed\n");
                }

                TimeUnit.MINUTES.sleep((long) .1);

            }

            System.out.printf("Passed: %d/%d%n", passedTests, numTests);
        }
    }

