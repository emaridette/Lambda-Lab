
import java.util.ArrayList;

public class Lexer {
	
	/*
	 * A lexer (or "tokenizer") converts an input into tokens that
	 * eventually need to be interpreted.
	 * 
	 * Given the input 
	 *    (\bat  .bat flies)cat  λg.joy! )
	 * you should output the ArrayList of strings
	 *    [(, \, bat, ., bat, flies, ), cat, \, g, ., joy!, )]
	 *
	 */
	public ArrayList<String> tokenize(String input) {
		ArrayList<String> tokens = new ArrayList<String>();

		// This next line is definitely incorrect!
		// tokens.add(input);
		
		String currentToken = "";
		
		// ((cu (a λboo.a boo) )) day
		
		for (int i = 0; i < input.length(); i++) {
			char currentChar = input.charAt(i);
			
			if (currentChar == '(' || currentChar == ')' || currentChar == '.' || currentChar == ' ' || currentChar == '\\') {
				if (!currentToken.isEmpty()) {
					tokens.add(currentToken);
					currentToken = "";
				}
				if (currentChar != ' ') {
					tokens.add(Character.toString(currentChar));
				}
			}
			else if (currentChar == ';') {
				break;
			}
			else {
				currentToken += currentChar;
			}
		}
		
		
		if (!currentToken.isEmpty()) {
			tokens.add(currentToken);
		}
		
		return tokens;
	}



}