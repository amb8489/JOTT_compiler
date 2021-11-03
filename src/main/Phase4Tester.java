package main;

import grammar.ParsingException;
import grammar.Program;

public class Phase4Tester {
	
	public static void main(String[] args) {
		
		Program program = null;
		try {
			program = Program.parseProgram(JottTokenizer.tokenize("src/main/largerValid.jott"));
		} catch (ParsingException e) {
			e.printStackTrace();
		}
		System.out.println("starts here");


		System.out.println(program.convertToJava());

		System.out.println("ends here");
		
	}
	
}
