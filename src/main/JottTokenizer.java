package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for tokenizing Jott code.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class JottTokenizer {

	private static final int F = 0;		// finish state is represented as 0
	private static final int ER = 21;	// error state is represented as 21

	// the dfa with transition states
	private static final int[][] DFA = {
			{0, 1, 2, 3, 4, 5, 6, 7, ER, 9, 10, 11, 12, 13, 22, 15, 16, 17, 18, ER, F, ER, 23},				// 0  start
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, F, ER, F},							// 1  # state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 2  , state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 3  ] state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 4  [ state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 5  } state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 6  { state
			{F, ER, ER, ER, ER, ER, ER, 8, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},					// 7  = state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 8  relOp state
			{F, ER, ER, ER, ER, ER, ER, 8, ER, ER, ER, ER,  F, F, F, F, F, F, F, ER, F, ER, F},				// 9  < > state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 10  /+-* state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},     					// 11  ; state
			{ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, 22, ER, ER, ER, ER, ER, ER, ER, ER, F},	// 12  . state
			{F, F, F, F, F, F, F, F, F, F, F, F, 12, 13, F, F, F, F, F, F, F, ER, F},     					// 13  0123456789 state
			{ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, 22, ER, ER, ER, ER, ER, ER, F, ER, F},		// 14  decimal state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, 15, F, 15, F, F, F, F, F, ER, F,F,},     					// 15  letter state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},     					// 16  : state
			{ER, ER, ER, ER, ER, ER, ER, 19,ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, ER, F},		// 17  ! state
			{18, ER, F, ER, ER, ER, ER, ER, ER, ER, ER, F, ER, 18, ER, 18, ER, ER, 20 ,ER, ER, ER, F},		// 18  " state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 19  != state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, ER, F},							// 20  string state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, 0, F},     						// 21  error state
			{F, F, F, F, F, F, F, F, F, F, F, F, F, 22, F, F, F, F, F, F, F, 0, F,22},							// 22  bad state
			{F, F, F, F, F, F, F, F, F, F, F, F, 12, 13, F, F, F, F, F, F, F, ER,23}};     					// 23  helper

	// look up table is used to map chars to their respective class in the dfa
	private static final Map<String, Integer> lookUpTable =
			Stream.of(
					new Object[][] {
							{" ", 0}, {"#", 1}, {",", 2}, {"]", 3},
							{"[", 4}, {"{", 5}, {"}", 6}, {"=", 7},
							{"<", 9}, {">", 9}, {"/", 10}, {"+", 10},
							{"-", 22}, {"*", 10}, {";", 11}, {".", 12},
							{"digit", 13}, {"letter", 15}, {":", 16}, {"!", 17},
							{"\"", 18}, {"\n", 20}
					}
			).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

	/**
	 * Classifies this character as a digit or a letter then returns the state from the look-up table.
	 * @param character the provided character
	 * @return returns the state from the look-up table
	 */
	private static int classifyCharacter(char character) {
		if (Character.isDigit(character)) {
			return lookUpTable.get("digit");
		}
		if (Character.isLetter(character)) {
			return lookUpTable.get("letter");
		}else {
			if (lookUpTable.get(String.valueOf(character)) == null) {
				return -1;
			}
			return lookUpTable.get(String.valueOf(character));
		}
	}

	/**
	 * TODO: <-- add description
	 * @param tokenString blah
	 * @param file blah
	 * @param stateFinishedAt blah
	 * @param lineNumber blah
	 * @return blah
	 */
	private static Token tokenClass(String tokenString, String file, int stateFinishedAt, int lineNumber){
		System.out.printf("token: (%s)%n", tokenString);

		return switch (stateFinishedAt) {
			case 2 			-> new Token(tokenString, file, lineNumber, TokenType.COMMA);
			case 3 			-> new Token(tokenString, file, lineNumber, TokenType.R_BRACKET);
			case 4 			-> new Token(tokenString, file, lineNumber, TokenType.L_BRACKET);
			case 5 			-> new Token(tokenString, file, lineNumber, TokenType.L_BRACE);
			case 6 			-> new Token(tokenString, file, lineNumber, TokenType.R_BRACE);
			case 7 			-> new Token(tokenString, file, lineNumber, TokenType.ASSIGN);
			case 8, 9, 19 	-> new Token(tokenString, file, lineNumber, TokenType.REL_OP);
			case 10,23 		-> new Token(tokenString, file, lineNumber, TokenType.MATH_OP);
			case 11 		-> new Token(tokenString, file, lineNumber, TokenType.SEMICOLON);
			case 22, 12, 13 -> new Token(tokenString, file, lineNumber, TokenType.NUMBER);
			case 15 		-> new Token(tokenString, file, lineNumber, TokenType.ID_KEYWORD);
			case 16 		-> new Token(tokenString, file, lineNumber, TokenType.COLON);
			case 20, 18 	-> new Token(tokenString, file, lineNumber, TokenType.STRING);
			default 		-> null;
		};
	}

	/**
	 * Takes in a filename and tokenizes that file into token based on the rules of the Jott Language.
	 * @param filename the name oF the file to tokenize; can be relative or absolute path
	 * @return an ArrayList oF Jott Tokens
	 */
	public static ArrayList<Token> tokenize(String filename) {
		// final tokens list
		ArrayList<Token> tokens = new ArrayList<>();

		// reading lines of file
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			// state machine variables
			String line;
			StringBuilder tokenString;
			int currentLineNumber = 0;
			int currentState;

			// for each line in file
			int LastNonSpaceState = -1;
			boolean firstMinusSeen = true;



			while ((line = br.readLine()) != null) {

				line += "\n"; 					// adding  new line char for help in the DFA
				currentLineNumber++; 			// init state machine for this line
				currentState = 0; 				// start state machine at start
				tokenString = new StringBuilder();  	// init token ""

				// for each char in string
				int i = 0;
				while (i < line.length()) {
					char character = line.charAt(i);

					if (classifyCharacter(character) != -1) {
						currentState = DFA[currentState][classifyCharacter(character)]; // updating state based on input ch
//						System.out.println(currentState);

						if (currentState != 0 && currentState != 23){
							LastNonSpaceState = currentState;
						}


						// if moved into error state
						if (currentState == ER) {
							String error_msg = "Syntax Error\nInvalid token \"";
							System.err.printf("%s%s\"%n", error_msg, tokenString);
							System.err.printf("%s:%s%n", filename, currentLineNumber);
							return null;
						}


						// if not a comment
						if (currentState != 1) {

							// add a space if we are in a string state else add no spaces
							char space_char = ' ';

							if (currentState == 18) {
								tokenString.append(character);
							} else if (character != space_char) {
								tokenString.append(character);
							}

							// looking ahead at the next char
							if (i + 1 < line.length()) {
								// next state given current state and next char
								while (classifyCharacter(line.charAt(i + 1)) == -1) {
									i++;
								}

								char nextCharacter = line.charAt(i + 1);
								int next = DFA[currentState][classifyCharacter(nextCharacter)];



								if(currentState == 23){
//									System.out.println("last non space state :"+LastNonSpaceState);


									if (firstMinusSeen && !(LastNonSpaceState == 7 || LastNonSpaceState == 10 || LastNonSpaceState == 9) ){
//										System.out.println("first - seen  prev non s ste: "+LastNonSpaceState);

										Token token = tokenClass(tokenString.toString(), filename, currentState, currentLineNumber);
										if (token != null) {
											tokens.add(token);
										}
										tokenString = new StringBuilder(); // restart tokenString
										LastNonSpaceState = currentState;

										currentState = 0; // restart the state machine on finish of tokenString
										firstMinusSeen = false;

									}else {
//										System.out.println("appart of neg   prev non s ste: "+LastNonSpaceState);
										firstMinusSeen = true;
										LastNonSpaceState = currentState;

									}

								}
								else{
									// if the next char will cause a finish
									if (next == F) {
										// if the token is not the empty token
										if (!tokenString.toString().isEmpty()) {

											// adding and classifying token
											Token token = tokenClass(tokenString.toString(), filename, currentState, currentLineNumber);
											if (token != null) {
												tokens.add(token);
											}
											tokenString = new StringBuilder(); // restart tokenString
											currentState = 0; // restart the state machine on finish of tokenString
										}
									}
								}
							}
						}
					}
					i++;
					if (currentState != 0 && currentState != 23){
						LastNonSpaceState = currentState;
					}
					if (currentState != 0 && currentState != 23){
						firstMinusSeen = true;
					}
				}
			}
		}
		catch (IOException e) {
			System.err.printf("COULD NOT FIND OR READ FILE : %s%n", filename);
			return null;
		}

		// tokens list
		return tokens;
	}

	/**
	 * TODO <-- add description
	 * @param args blah
	 */
	public static void main(String[] args) {
		String filename = "src/tokenizerTestCases/number.jott";
		ArrayList<Token> tokens = JottTokenizer.tokenize(filename);
	}

}
