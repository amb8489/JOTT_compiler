package grammar;

import main.Token;

import java.util.ArrayList;

public class Program implements JottTree {


    FunctionList funcLst;

    public Program(FunctionList funcLst) {
        this.funcLst = funcLst;
    }

    public static Program parseProgram(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        System.out.println("------------------------PARSING program------------------------");

        FunctionList funclst = FunctionList.parseFunctionList(tokens, nestlevel);
        return new Program(funclst);
    }


    @Override
    public String convertToJott() {
        StringBuilder jstr = new StringBuilder();
        jstr.append(funcLst.convertToJott());
        return jstr.toString();
    }

    @Override
    public String convertToJava() {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }

    protected int getNestLevel() {
        return 0;
    }
}
