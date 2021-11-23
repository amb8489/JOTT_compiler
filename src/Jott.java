import grammar.JottTree;
import grammar.Program;
import grammar.ValidateTable;
import main.JottParser;
import main.JottTokenizer;
import main.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Jott {
    public static String FileName = null;
    public static void main(String[] args) {

        String jottFileName = args[0];
        String convertedFileName = args[1];


        ValidateTable.FileName = convertedFileName.split("\\.")[0];
        String convertType = args[2];
        System.out.println("converting :"+jottFileName+" to "+convertType+" located in file:" +convertedFileName);

        // step 1
        ArrayList<Token> tokens = JottTokenizer.tokenize(jottFileName);

        // step 2
        JottTree program = JottParser.parse(tokens);
        if (program == null){
            System.exit(0);
        }

        // step 3
        if (! program.validateTree()) {
            System.exit(0);
        }

        // step 4 write to outfile
        String Converted_program = null;

        if (convertType.equals("Jott")) {
             Converted_program = program.convertToJott();
        }else
        if (convertType.equals("C")) {
             Converted_program = program.convertToC();
        }else
        if (convertType.equals("Java")) {

             Converted_program = program.convertToJava();
        }else
        if (convertType.equals("Python")) {
             Converted_program = program.convertToPython();
        }else{
            System.err.println("convert to:"+convertType+" is not suported");
            System.exit(0);
        }

        // write to outfile
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(convertedFileName));
            writer.write(Converted_program);
            writer.close();

        }catch (Exception e){
            System.err.println("error writing to file");
        }
    }
}