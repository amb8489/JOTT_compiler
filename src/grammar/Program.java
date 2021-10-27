package grammar;

import main.Token;

import java.text.ParseException;
import java.util.ArrayList;

public class Program implements JottTree {


    FunctionList funcLst;

    public Program(FunctionList funcLst) {
        this.funcLst = funcLst;
    }

    public static Program parseProgram(ArrayList<Token> tokens, int nestlevel) throws ParsingException {
        System.out.println("------------------------PARSING program------------------------");
        TOKEN_IDX.reset();
        FunctionList funclst = FunctionList.parseFunctionList(tokens, 0);
        return new Program(funclst);
    }



    @Override
    public boolean validateTree() throws ParsingException{
        if( !funcLst.ListHasMain()){
            throw new ParsingException("Program is missing or has an incorrecly spelled main");
        }

        return funcLst.validateTree();
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



}
