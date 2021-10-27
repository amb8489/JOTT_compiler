package grammar;

import main.Token;

import java.util.ArrayList;

public class NumType {

    String type;

    double Dnum;
    int Inum;
    Token number;

    public NumType(String type, double v,Token t) {
        this.type = type;
        this.Dnum = v;
        this.number =t;

    }
    public NumType(String type, int v,Token t) {
        this.type = type;
        this.Inum = v;
        this.number =t;
    }

    public NumType ParseNumType(Token token) throws ParsingException {
        System.out.println("-------------------PARSING Num TYPE-----------------");
        String str = token.getToken();
        try {
            double v = Double.parseDouble(str);
            String type = "Double";
            return new NumType(type,v,token);
        } catch (NumberFormatException nfe) {
        }
        try {
            int v = Integer.parseInt(str);
            String type = "Integer";
            return new NumType(type,v,token);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
    public String convertToJott() {
        return number.getToken();
    }
}