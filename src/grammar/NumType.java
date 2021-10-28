package grammar;

import main.Token;

import java.util.ArrayList;

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
    public double Dnum;
    public int Inum;
    public Token number;



    public NumType(Token num) {
        this.number = num;


        String str = num.getToken();

        try {
            double v = Double.parseDouble(str);
            this.numType = "Double";
            this.Dnum = v;
        } catch (NumberFormatException ignored) {
        }

        try {
            int v = Integer.parseInt(str);
            this.numType = "Integer";
            this.Inum = v;

        } catch (NumberFormatException ignored) {
        }
    }


    public String getNumType() {
        return numType;
    }

    public String convertToJott() {
        return number.getToken();
    }
}