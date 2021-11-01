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


    /**
     * A constructor for a NumType.
     *
     * @param number a numerical value whether it be a double or an integer
     */
    public NumType(Token number) {
        this.number = number;
        String string = number.getToken();

        // is this a double?
        this.isVar = false;

        try {
            double doubleNumber = Double.parseDouble(string);
            this.numType = "Double";
            this.doubleNumber = doubleNumber;
        } catch (NumberFormatException ignored) {
            // hmm, so this must be something else
        }

        // is this an integer?
        try {
            int integerNumber = Integer.parseInt(string);
            this.numType = "Integer";
            this.integerNumber = integerNumber;

        } catch (NumberFormatException ignored) {
            // not a number but a var
            if (this.numType == null) {
                this.isVar = true;
                this.varNumber = string;
            }
        }
    }

    public String getNumType() {
        return numType;
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        return number.getToken();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return null;
    }
}