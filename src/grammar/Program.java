package grammar;

import main.Token;

import java.util.ArrayList;

/**
 * A program holds a list of functions.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Program implements JottTree {
    FunctionList functionList;

    /**
     * This is the constructor for the program.
     *
     * @param functionList TODO
     */
    public Program(FunctionList functionList) {
        this.functionList = functionList;
    }

    /**
     * Parse the code for a list of functions and return it as a Program object.
     *
     * @param tokens TODO
     * @return a Program object with a list of functions
     * @throws ParsingException TODO
     */
    public static JottTree parseProgram(ArrayList<Token> tokens) throws ParsingException {
        TokenIndex.reset();
        ValidateTable.clearAll();
        return new Program(FunctionList.parseFunctionList(tokens, 0));
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        return functionList.convertToJott();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        StringBuilder javaString = new StringBuilder();
        javaString.append(String.format("public class %s {\n", "Name"));
        javaString.append("private class Helper {\n");
        javaString.append("\tpublic static String input(String message, int amount) {\n");
        javaString.append("\t\tSystem.out.print(message);\n");
        javaString.append("\t\tString read_line = System.console().readLine();\n");
        javaString.append("\t\tif (read_line.length() < amount) {;\n");
        javaString.append("\t\t\treturn read_line;\n");
        javaString.append("\t\t}\n");
        javaString.append("\t\treturn read_line.substring(0, amount);\n");
        javaString.append("\t}\n}\n");
        javaString.append(functionList.convertToJava());
        javaString.append("\n}");
        return javaString.toString();
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        StringBuilder cString = new StringBuilder();
        cString.append("#include <stdio.h>\n#include <string.h>\n#include <stdlib.h>\n#include <stdbool.h>\n");
        cString.append(functionList.convertToC());
        return cString.toString();
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return functionList.convertToPython();
    }

    /**
     * Ensure the code is valid
     *
     * @return whether code is valid or not
     * @throws ParsingException throw an error if the code is not valid
     */
    public boolean validateTree()  {
        ValidateTable.clearAll();

        try {
            functionList.ListHasMain();
            functionList.validateTree();
            return true;
        }catch (ParsingException p){
            return false;
        }
    }
}
