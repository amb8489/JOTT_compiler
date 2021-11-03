package main;

import grammar.ParsingException;
import grammar.Program;

public class Phase4Tester {
	
	public static void main(String[] args) {
		
		Program program = null;
		try {
			program = Program.parseProgram(JottTokenizer.tokenize("src/tokenizerTestCases/phase4.jott"));
		} catch (ParsingException e) {
			e.printStackTrace();
		}
		
		
		System.out.println(program.convertToJava());
		
	}
	
}
