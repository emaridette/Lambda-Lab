import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	
	/*
	 * Turns a set of tokens into an expression.  Comment this back in when you're ready.
	 */

	/*
	public Expression parse(ArrayList<String> tokens) throws ParseException {
		Variable var = new Variable(tokens.get(0));
		
		// This is nonsense code, just to show you how to thrown an Exception.
		// To throw it, type "error" at the console.
		if (var.toString().equals("error")) {
			throw new ParseException("User typed \"Error\" as the input!", 0);
		}
		
		return var;
	}
	*/
	
	public ArrayList<String> tokens;
	public HashMap<String, Expression> storedVariables = new HashMap<>();

	
	public void preParser() throws ParseException {
		int i = 0;
		
	    while (i < tokens.size()) {
	        String currentToken = tokens.get(i);
	        
	        if (currentToken.equals("\\") && (i == 0 || !tokens.get(i - 1).equals("("))) {
	            int start = i;
	            int parenCounter = 0;
	            
	            if (start > 0 && tokens.get(start - 1).equals("(")) {
	                i++;
	            } else { 
	                while (i < tokens.size()) {
	                    currentToken = tokens.get(i);
	                    if (currentToken.equals("(")) {
	                        ++parenCounter;
	                    } else if (currentToken.equals(")")) {
	                        --parenCounter;
	                        if (parenCounter < -1) {
	                            throw new ParseException("no open paren", i);
	                        } else if (parenCounter == -1) {
	                            tokens.add(start, "(");
	                            tokens.add(++i, ")");
	                            break;
	                        }
	                    }
	                    ++i;
	                }
	               
	                if (parenCounter == 0) {
	                    tokens.add(start, "(");
	                    tokens.add(++i, ")");
	                }
	            }
	        } else {
	            i++;
	        }
	    }
	}
	
	public Expression variableChecker(String str) {
		if (storedVariables.containsKey(str)) {
			return storedVariables.get(str);
		}
		return new Variable(str);
	}
	
	public Expression parseHelper(int start, int end) {
		if (start == end) {
			return variableChecker(tokens.get(start));
		}
		else {
			int prevEnd = end;
			int parenCount = 0;
			int rightStart = end;
			
			for (int i = end; i >= start; i--) {
				String currentToken = tokens.get(i);
				if (currentToken.equals(")")) {
					if (parenCount == 0) {
						end = i;
					}
					++parenCount;
				}
				else if (currentToken.equals("(")) {
					--parenCount;
					if (parenCount == 0) {
						rightStart = i;
						break;
					}
				}
				else if (parenCount == 0) {
					rightStart = i;
					break;
				}
			}
			if (rightStart == end) {
				return new Application(parseHelper(start, rightStart - 1), variableChecker(tokens.get(end)));
			}
			else if (rightStart == start && end == prevEnd) {
				return parseHelper(start + 1, end - 1);
			}
			else {
				return new Application(parseHelper(start, rightStart - 1), parseHelper(rightStart + 1, end - 1));
			}
		}
	}
	
	public Expression parse(ArrayList<String> tokens) throws ParseException {
		this.tokens = tokens;
		
		if (tokens.size() >= 3 && tokens.get(1).equals("=")) {
			String key = tokens.get(0);
			if (storedVariables.containsKey(key)) {
				return null;
			}
			else {
				tokens.remove(0);
				tokens.remove(0);
				preParser();
				Expression value = parseHelper(0, tokens.size() - 1);
				storedVariables.put(key, value);
				return value;
			}
		}
		preParser();
		return parseHelper(0, tokens.size() - 1);
	}
}
