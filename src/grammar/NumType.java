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
    public String varNumber;

    public Token number;
    public boolean isVar;


    public NumType(Token num) {
        this.number = num;
        String string = num.getToken();

        // is this a double?
        this.isVar = false;

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
            // not a num but a var
            if (this.numType == null) {
                this.isVar = true;
                this.varNumber = string;
            }
        }
    }

    public String getNumType() {
        return numType;
    }

    public String convertToJott() {
        return number.getToken();
    }
}