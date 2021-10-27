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

    String type;

    double Dnum;
    int Inum;
    Token number;



    public NumType(Token possible_num) {
        String str = possible_num.getToken();
        this.number = possible_num;

        try {
            double v = Double.parseDouble(str);
            this.type = "Double";
            this.Dnum = v;
        } catch (NumberFormatException nfe) {
        }

        try {
            int v = Integer.parseInt(str);
            this.type = "Integer";
            this.Inum = v;

        } catch (NumberFormatException nfe) {
        }
    }


    public String convertToJott() {
        return number.getToken();
    }
}