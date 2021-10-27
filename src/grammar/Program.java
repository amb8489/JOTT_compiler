package grammar;

import main.Token;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Program implements JottTree {
    FunctionList functionList;

    /**
     * Constructor TODO
     * @param functionList TODO
     */
    public Program(FunctionList functionList) {
        this.functionList = functionList;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestlevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Program parseProgram(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        System.out.println("------------------------PARSING program------------------------");
        TOKEN_IDX.reset();
        ValidateTable.clearTables();

        FunctionList funclst = FunctionList.parseFunctionList(tokens, 0);

        return new Program(funclst);
    }

    /**
     * TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    @Override
    public boolean validateTree() throws ParsingException{
        if (!functionList.ListHasMain()) { throw new ParsingException("Program is missing or has an incorrectly spelled main"); }
        return functionList.validateTree();
    }

    /**
     * TODO
     * @return TODO
     */
    @Override
    public String convertToJott() {
        return functionList.convertToJott();
    }

    /**
     * TODO
     * @return TODO
     */
    @Override
    public String convertToJava() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    @Override
    public String convertToC() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    @Override
    public String convertToPython() {
        return null;
    }
}
