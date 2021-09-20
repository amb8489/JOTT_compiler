/**
 * This class is responsible for tokenizing Jott code.
 *
 * @author Aaron Berghash
 **/


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JottTokenizer {
	
	//error message string
	private static String error_msg = "Syntax Error\nInvalid token \"";

	private static int F = 0;    // finish state
	private static int ER = 21; // error state
	
	private static char space_char = ' '; // for testing against


	// a look up table to make symbol to state in the dfa
	private static Map<String, Integer> lut = Stream.of(new Object[][] {
			{" ", 0},{"#", 1},{",", 2},{"]", 3},{"[", 4},{"{", 5},
			{"}", 6},{"=", 7},{"<", 9},{">", 9},{"/", 10},{"+", 10},
			{"-", 10},{"*", 10},{";", 11},{".", 12},{"digit", 13},
			{"letter", 15},{":", 16},{"!", 17},{"\"", 18},{"\n", 20}
	}).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

	
	// the DFA 
	private static int[][] DFA = {
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
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,14,13,F ,F ,F ,F ,F ,F ,F ,ER},     //13  0123456789 state
			{ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,22,ER,ER,ER,ER,ER,ER,F ,ER},     //14  Decimal state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,15,F ,15,F ,F ,F ,F ,F ,ER},     //15  lettER state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //16  : state
			{ER,ER,ER,ER,ER,ER,ER,19,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER},     //17  ! state
			{18,ER,F ,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,18,ER,18,ER,ER,20 ,ER,ER,ER},     //18  " state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //19  != state
			{F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,F ,ER},     //20  string
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},     //ER  ERror
			{F ,ER,ER,ER,ER,ER,ER,ER,ER,ER,ER,F ,ER,22,ER,ER,ER,ER,ER,ER,F ,ER} };   // 22 decimal number state

	// classify given char from the look up table 
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
	
	// classify a toekn into its token type given where it finished in the DFA
	private static Token tokenClass( String token_str ,String file, int State_finished_at, int line_num){
//		System.out.println("fin at:"+State_finished_at + "  |"+ token_str);
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

		// next state
		int moving_to = 0;

		// reading lines of file
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			// stae machine vars
			String line;
			StringBuilder token = new StringBuilder();
			int curr_line_number = 0;
			int prev = 0;
			int col;
			int curr_state;


			while ((line = br.readLine()) != null) {
				// init state machine for line
				curr_line_number++;
				moving_to = 0;
				curr_state = 0;
				prev = 0;
				token = new StringBuilder();
				
				// adds new line to each line because new line char is removed in readLin()
				line+="\n";
				
				// for each char in line we start to tokenize 
				for (int i = 0; i < line.length(); i++) {
					
					// the char to Proces
					char ch = line.charAt(i);

					//----Processing char-----

					// remebering where we were in the dfa
					prev = moving_to;

					// updating state based on input ch (what col in the DFA to go to)
					col = classify_char(ch);



//					System.out.println(col+ " "+ ch );
					
					// moving to new state based on input
					moving_to = DFA[curr_state][col];
					// current state
					curr_state = moving_to;

					// moved into error state
					if (moving_to == ER) {
						System.out.println(error_msg + token + "\"");

						System.out.println(filename + ":" + curr_line_number);
						return null;
					}
					
					// non error state 
					if (moving_to != 1) {

						// if token is finished
						if (moving_to == F) {
							// if token is not emppty at finish just to be sure
							if (!token.toString().equals("")) {
								
								// add token to the list of found tokens
								tokens.add(tokenClass(token.toString(),filename,prev,curr_line_number));
								
								// reset the token
								token = new StringBuilder();

								// if we finished with a char that we didnt need for the last token but need
								// for the start of the new token (small edge case for single char tokens
								// when tokens are up next to eachother with no space)
								if (ch != space_char && col != 20) {
									token.append(ch);
									tokens.add(tokenClass(token.toString(),filename,col,curr_line_number));
								}
								
								// reset token
							        token = new StringBuilder();
								
							}
						// case where token is not finished
						} else {
							// edge case for strings needing to allow space char
							if (moving_to == 18) {
								token.append(ch);
							// dont want spaces if non string
							} else if (ch != space_char) {
								token.append(ch);
							}
						}
					}
				}
			}

			// if we ended and the token is incomplete or has not landed in finsish but is an okay token
			// basically EOF edge case we finish the token for it and test if finishing it at its current state would be okay or not
			// ex: ID_words can end like: lastWordInLine(EOF) but a string like : "this is a string(EOF) would be incomplete stopping in its
			// current state
			moving_to = DFA[moving_to][0];

			if (moving_to != 0) {
				System.out.println(error_msg + token + "\"");
				System.out.println(filename + ":" + curr_line_number);
				return null;
			}
			if (!token.toString().equals("")) {
				tokens.add(tokenClass(token.toString(),filename,prev,curr_line_number));
			}
		}
		// if file cant be read
		catch (IOException e) {
			System.out.println("COULD NOT FIND FILE "+filename);

			return null;
		}

		return tokens;
	}
}
