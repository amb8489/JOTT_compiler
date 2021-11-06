package main;

import grammar.JottTree;
import grammar.ParsingException;
import grammar.Program;

import java.util.ArrayList;

public class Phase4Tester {
	
	public static void main(String[] args) {
		
		try {

			// step 1
			ArrayList<Token> tokens = JottTokenizer.tokenize("src/tokenizerTestCases/phase4.jott");

			// step 2
			JottTree program = Program.parseProgram(tokens);

			// step 3
			if (program.validateTree()) {
				System.out.println("---------------------JOTT----------------------------------------------------");

				System.out.print(program.convertToJott());
				System.out.println("---------------------JAVA----------------------------------------------------");

				System.out.println(program.convertToJava());
				System.out.println("---------------------PYTHON----------------------------------------------------");

				System.out.println(program.convertToPython());
				System.out.println("---------------------C----------------------------------------------------");

				System.out.println(program.convertToC());
			}

		} catch (ParsingException e) {
			e.printStackTrace();
		}
		
		

	}
	
}
