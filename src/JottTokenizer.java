/**
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
	// vars
	private static int F =0;    // finish state
	private static int ER = 21; // error state
	private static char space_char = ' ';
	private static String error_msg = "Syntax Error\nInvalid token \"";


	//look up table to map chars to their respective class in the dfa

	private static Map<String, Integer> lut = Stream.of(new Object[][] {
			{" ", 0},
			{"#", 1},
			{",", 2},
			{"]", 3},
			{"[", 4},
			{"{", 5},
			{"}", 6},
			{"=", 7},
			{"<", 9},
			{">", 9},
			{"/", 10},
			{"+", 10},
			{"-", 10},
			{"*", 10},
			{";", 11},
			{".", 12},
			{"digit", 13},
			{"letter", 15},
			{":", 16},
			{"!", 17},
			{"\"", 18},
			{"\n", 20}
	}).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));


	// the dfa with transition states
	private static final int[][] DFA = {
			{0 ,1 ,2 ,3 ,4 ,5 ,6 ,7 ,ER,9 ,10,11,12,13,22,15,16,17,18,ER,0 ,ER},     //0  start
			{1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,1 ,0 ,ER},     //1  # state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //2  , state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //3  ] state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //4  [ state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //5  } state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //6  { state
			{F ,ER,ER,ER,ER,ER,ER,8 ,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER},     //7  = state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //8  == <= >= relitiveOp
			{F ,ER,ER,ER,ER,ER,ER,8 ,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,0 ,ER},     //9  < > state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //10  /+-* state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,0 ,ER},     //11  ; state
			{ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,22,ER,ER,ER,ER,ER,ER,ER,ER},     //12  . state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,12,13,F ,F ,F ,F ,F ,F ,F ,ER},     //13  0123456789 state
			{ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,22,ER,ER,ER,ER,ER,ER,F ,ER},     //14  Decimal state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,15,F ,15,F ,F ,F ,F ,F ,ER},     //15  lettER state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //16  : state
			{ER,ER,ER,ER,ER,ER,ER,19,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER},     //17  ! state
			{18,ER,F ,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,18,ER,18,ER,ER,20 ,ER,ER,ER},     //18  " state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //19  != state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //20  string
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},     //ER  ERror
			{F ,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,22,ER,ER,ER,ER,ER,ER,F ,ER} };   // 22 decimal number state

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

	private static Token tokenClass( String token_str ,String file, int State_finished_at, int line_num){
		System.out.println("fin at:"+State_finished_at + "  |"+ token_str);

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
			case 0,20, 18 -> new Token(token_str, file, line_num, TokenType.STRING);
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

		// next state
		int moving_to = 0;

		// reading lines of file
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			// state machine vars
			String line;
			StringBuilder token = new StringBuilder();
			int curr_line_number = 0;
			int prev = 0;
			int col;
			int curr_state;

			// for each line in file
			while ((line = br.readLine()) != null) {

				// init state machine for this line
				curr_line_number++;
				moving_to = 0;
				curr_state = 0;
				prev = 0;

				// starting token
				token = new StringBuilder();

				// adding  new line char for help in the DFA
				line+="\n";

				// for each char in string
				for (int i = 0; i < line.length(); i++) {

					char ch = line.charAt(i);

					// remembering where we were
					prev = moving_to;

					// updating state based on input ch
					col = classify_char(ch);
					moving_to = DFA[curr_state][col];
					curr_state = moving_to;

					// if not a comment
					if (curr_state != 1) {

						// moved into error state
						if (curr_state == ER) {
							System.err.println(error_msg + token + "\"");
							System.err.println(filename + ":" + curr_line_number);
							return null;
						}
						
						// add a space if we are in a string state
						if (curr_state == 18) {
							token.append(ch);
							// if not in string we want to ignore the spaces
						} else if (ch != space_char) {
							token.append(ch);
						}

						// lookign ahead at the next char
						if (i + 1 < line.length()) {
							char next_ch = line.charAt(i + 1);
							int next = DFA[curr_state][classify_char(next_ch)];

							// if the next char will cause a finish
							if (next == F) {
								// if the token is not the empty token
								if (!token.toString().equals("")) {
									tokens.add(tokenClass(token.toString(), filename, curr_state, curr_line_number));
									token = new StringBuilder();
									curr_state = 0;
								}
							}
						}
					}
				}
			}
		}
		// if file cant be read
		catch (IOException e) {
			System.err.println("COULD NOT FIND OR READ FILE :"+filename);
			return null;
		}

		// tokens list
		return tokens;
	}
}
