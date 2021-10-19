package grammar;

public class Type {
    String type;
    String filename;
    int linenum;

    public Type(String token, String filename, int lineNum) {
        this.type = token;
        this.filename = filename;
        this.linenum = lineNum;
    }

    public String convertToJott(){
        return type;
    }

}
