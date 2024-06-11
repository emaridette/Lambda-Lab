/* ATiCS Lambda Lab 2024
 * Anagha Ajesh & Ella Marmol
 */

import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {
    static final ArrayList<String> characters = new ArrayList<>(Arrays.asList("\\", ".", "(", ")", "λ", "="));
    static final String spaceString = " ";

    /*
     * A lexer (or "tokenizer") converts an input into tokens that
     * eventually need to be interpreted.
     *
     * Given the input
     * (\bat .bat flies)cat λg.joy! )
     * you should output the ArrayList of strings
     * [(, \, bat, ., bat, flies, ), cat, \, g, ., joy!, )]
     *
     */
    public ArrayList<String> tokenize(String input) {

        //tokenlist
        ArrayList<String> tokens = new ArrayList<>();

        //traverse the input
        for (int i = 0; i < input.length() && input.charAt(i) != ';';) {
            String currentChar = input.substring(i, i + 1);

            
            if (currentChar.equals(";")) {
                break;
            } else if (currentChar.equals(spaceString)) {
                //skip spaces
                ++i;
            } else if (characters.contains(currentChar)) {
                //check for special characters
                if (currentChar.equals("λ")) {
                    tokens.add("\\");
                } else {
                   tokens.add(currentChar);
                }
                ++i;
            } else {
                int start = i;
                while (!(i == input.length() || input.charAt(i) == ' '
                        || characters.contains(input.substring(i, i + 1)))) {
                    ++i;
                }
                tokens.add(input.substring(start, i));
            }
        }
        return tokens;
    }
}
