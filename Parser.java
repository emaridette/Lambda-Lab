/* ATiCS Lambda Lab 2024
 * Anagha Ajesh & Ella Marmol
 */

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    public static final HashMap<String, Expression> storedVariables = new HashMap<>();
    private ArrayList<String> tokens;

    /*
     * Add parenthesis around all lambda functions, except for those that are already 
     * surrounded by parenthesis and when the lambda is blocked at the end by parenthesis 
     * out of its scope.
     */

    public void preParser() throws ParseException {
        int i = 0;

        while (i < tokens.size()) {
            String currentToken = tokens.get(i);

            if (currentToken.equals("\\") && (i == 0 || !tokens.get(i - 1).equals("("))) {
                int start = i;
                int parenCounter = 0;

                if (start > 0 && tokens.get (start - 1).equals("(")) {
                    i++;
                } else {
                    while (i < tokens.size()) {
                        currentToken = tokens.get(i);
                        if (currentToken.equals("(")) {
                            ++parenCounter;
                        } else if (currentToken.equals(")")) {
                            --parenCounter;
                            if (parenCounter < -1) {
                                tokens.add(start, "(");
                                tokens.add(++i, ")");
                                break;
                            }
                        }
                        ++i;
                    }
                }
            } else {
                i++;
            }
        }
    }

    /*
     * Given a string, if this string corresponds to an already declared variable,
     * substitute that variable for this string. Otherwise, create a new variable
     * that represents this string.
     */

    private Expression variableScanner(String s) {
        if (storedVars.containsKey(s)) {
            return storedVars.get(s);
        }
        return new Variable(s);
    }

    /*
     * Main helper method for parse. Executes after the pre-parser has been called.
     */

    private Expression parseHelper(int start, int end) {
        if (tokens.get(start).equals("\\")) {
            return new Function(new Variable(tokens.get(start + 1)), parseHelper(start + 3, end));
        } else if (start == end) {
            return variableScanner(tokens.get(start));
        } else {
            int oldEnd = end;
            int parenCounter = 0;
            int rightPartStart = end;
            for (int index = end; index >= start; --index) {
                String currentToken = tokens.get(index);
                if (currentToken.equals(")")) {
                    if (parenCounter == 0) {
                        end = index;
                    }
                    ++parenCounter;
                } else if (currentToken.equals("(")) {
                    --parenCounter;
                    if (parenCounter == 0) {
                        rightPartStart = index;
                        break;
                    }
                } else if (parenCounter == 0) {
                    rightPartStart = index;
                    break;
                }
            }
            
            if (rightPartStart == end) {
                return new Application(parseHelper(start, rightPartStart - 1), variableScanner(tokens.get(end)));
            } else if (rightPartStart == start && end == oldEnd) {
                return parseHelper(start + 1, end - 1);
            } else {
                return new Application(parseHelper(start, rightPartStart - 1),
                        parseHelper(rightPartStart + 1, end - 1));
            }
        }
    }

    /*
     * Main parser method
     */

    public Expression parse(ArrayList<String> tokens) throws ParseException {
        this.tokens = tokens;
    
        if (tokens.size() >= 3 && tokens.get(1).equals("=")) {
            String key = tokens.get(0);
            if (storedVars.containsKey(key)) {
                return null;
            } else {
                tokens.remove(0);
                tokens.remove(0);
                boolean isRunning = tokens.get(0).equals("run");
                if (isRunning) {
                    tokens.remove(0);
                }
                preParser();
                Expression value = parseHelper(0, tokens.size() - 1);
                if (isRunning) {
                    value = Runner.run(value);
                }
                storedVariables.put(key, value);
                return value;
            }
        }
        
        boolean isRunning = tokens.get(0).equals("run");
        if (isRunning) {
            tokens.remove(0);
        }
        preParser();
        Expression value = parseHelper(0, tokens.size() - 1);
        if (isRunning) {
            value = Runner.run(value);
        }
        return value;
    }
}