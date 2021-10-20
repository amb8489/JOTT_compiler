package main; /**
 * This class is responsible for tokenizing Jott code.
 *
 * @author aaron berghash
 **/


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JottTokenizer {



	//look up table to map chars to their respective class in the dfa
	private static Map<String, Integer> lut = Stream.of(new Object[][] {
			{" ", 0},{"#", 1},
			{",", 2},{"]", 3},
			{"[", 4},{"{", 5},
			{"}", 6},{"=", 7},
			{"<", 9},{">", 9},
			{"/", 10},{"+", 10},
			{"-", 22},{"*", 10},
			{";", 11},{".", 12},
			{"digit", 13},{"letter", 15},
			{":", 16},{"!", 17},
			{"\"", 18},{"\n", 20}
	}).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));


	// the dfa with transition states

	// FINISIH AND ERROR STATE
	private static final int F = 0;    // finish state
	private static final int ER = 21; // error state

	private static final int[][] DFA = {
			{0 ,1 ,2 ,3 ,4 ,5 ,6 ,7 ,ER,9 ,10,11,12,13,22,15,16,17,18,ER,F ,ER,23},     //0  start
			{1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,F ,ER,F},     //1  # state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //2  , state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //3  ] state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //4  [ state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //5  } state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //6  { state
			{F ,ER,ER,ER,ER,ER,ER,8 ,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,F},     //7  = state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //8  == <= >= relitiveOp
			{F ,ER,ER,ER,ER,ER,ER,8 ,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,F},     //9  < > state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //10  /+-* state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //11  ; state
			{ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,22,ER,ER,ER,ER,ER,ER,ER,ER,F},     //12  . state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,12,13,F ,F ,F ,F ,F ,F ,F ,ER,F},     //13  0123456789 state
			{ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,22,ER,ER,ER,ER,ER,ER,F ,ER,F},     //14  Decimal state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,15,F ,15,F ,F ,F ,F ,F ,ER,F},     //15  lettER state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //16  : state
			{ER,ER,ER,ER,ER,ER,ER,19,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,F},     //17  ! state
			{18,ER,F ,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,18,ER,18,ER,ER,20 ,ER,ER,ER,F},     //18  " state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //19  != state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER,F},     //20  string
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F , 0,F},     //ER  ERror
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,22,F ,F ,F ,F ,F ,F ,F , 0,F},
			{10 ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,12 ,13 ,F ,F ,F ,F ,F ,F ,F ,ER}};     //10  /-state

	// helper function to help classify chars
	private static int classify_char(char ch){
		if (Character.isDigit(ch)) {
			return lut.get("digit");
		}
		if (Character.isLetter(ch)) {
			return lut.get("letter");
		}else {
			return lut.get(String.valueOf(ch));
		}
	}

	// classifies tokens based on where they finished in the DFA

	private static Token tokenClass(String token_str , String file, int State_finished_at, int line_num){
		System.out.println("fin at:"+State_finished_at + "  |"+ token_str);
//		System.out.println("token: ("+token_str+")");

		return switch (State_finished_at) {
			case 2 -> new Token(token_str, file, line_num, TokenType.COMMA);
			case 3 -> new Token(token_str, file, line_num, TokenType.R_BRACKET);
			case 4 -> new Token(token_str, file, line_num, TokenType.L_BRACKET);
			case 5 -> new Token(token_str, file, line_num, TokenType.L_BRACE);
			case 6 -> new Token(token_str, file, line_num, TokenType.R_BRACE);
			case 7 -> new Token(token_str, file, line_num, TokenType.ASSIGN);
			case 8, 9, 19 -> new Token(token_str, file, line_num, TokenType.REL_OP);
			case 10 -> new Token(token_str, file, line_num, TokenType.MATH_OP);
			case 11 -> new Token(token_str, file, line_num, TokenType.SEMICOLON);
			case 22, 12, 13 -> new Token(token_str, file, line_num, TokenType.NUMBER);
			case 15 -> new Token(token_str, file, line_num, TokenType.ID_KEYWORD);
			case 16 -> new Token(token_str, file, line_num, TokenType.COLON);
			case 20, 18 -> new Token(token_str, file, line_num, TokenType.STRING);
			default -> null;
		};
	}
	/**
	 * Takes in a filename and tokenizes that file into Tokens
	 * based on the rules oF the Jott Language
	 * @param filename the name oF the file to tokenize; can be relative or absolute path
	 * @return an ArrayList oF Jott Tokens
	 */
	public static ArrayList<Token> tokenize(String filename) {

		// final tokens list
		ArrayList<Token> tokens = new ArrayList<>();

		// reading lines of file
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			// state machine vars
			String line;
			StringBuilder token;
			int curr_line_number = 0;
			int curr_state;

			// for each line in file
			while ((line = br.readLine()) != null) {

				// adding  new line char for help in the DFA
				line+="\n";

				// init state machine for this line
				curr_line_number++;

				// start state machine at start
				curr_state = 0;

				// init token ""
				token = new StringBuilder();

				// for each char in string
				for (int i = 0; i < line.length(); i++) {

					char ch = line.charAt(i);

					// updating state based on input ch
					curr_state = DFA[curr_state][classify_char(ch)];

					// if moved into error state
					if (curr_state == ER) {
						String error_msg = "Syntax Error\nInvalid token \"";
						System.err.println(error_msg + token + "\"");
						System.err.println(filename + ":" + curr_line_number);
						return null;
					}

					// if not a comment
					if (curr_state != 1) {

						// add a space if we are in a string state else add no spaces
						char space_char = ' ';
						if (curr_state == 18) {
							token.append(ch);
						} else if (ch != space_char) {
							token.append(ch);
						}

						// looking ahead at the next char
						if (i + 1 < line.length()) {
							char next_ch = line.charAt(i + 1);

							// next state given current state and next char
							int next = DFA[curr_state][classify_char(next_ch)];

							// if the next char will cause a finish
							if (next == F) {

								// if the token is not the empty token
								if (!token.toString().equals("")) {

									// adding and classifying token
									tokens.add(tokenClass(token.toString(), filename, curr_state, curr_line_number));

									// resting token
									token = new StringBuilder();

									// resetting the state machine on finish of token
									curr_state = 0;
								}
							}
						}
					}
				}
			}
		}
		catch (IOException e) {
			System.err.println("COULD NOT FIND OR READ FILE :"+filename);
			return null;
		}

		// tokens list
		 return tokens;
	}
	public static void main(String[] args) {
		String filename = "src/tokenizerTestCases/number.jott";
		ArrayList<Token> tokens = JottTokenizer.tokenize(filename);

	}

}
