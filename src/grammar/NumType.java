package grammar;

import main.Token;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class NumType {

    public String numType;
    public double doubleNumber;
    public int integerNumber;
    public Token number;



    public NumType(Token num) {
        this.number = num;
        String string = num.getToken();

        // is this a double?
        try {
            double number = Double.parseDouble(string);
            this.numType = "Double";
            this.doubleNumber = number;
        } catch (NumberFormatException ignored) {
            // hmm, so this must be something else
        }

        // is this an integer?
        try {
            int number = Integer.parseInt(string);
            this.numType = "Integer";
            this.integerNumber = number;

        } catch (NumberFormatException ignored) {
            // hmm, so this must be something else
        }
    }


    public String getNumType() {
        return numType;
    }

    public String convertToJott() {
        return number.getToken();
    }
}