package grammar;

public class Identifier {

    public String id;
    public String filename;
    public int lineNum;

    public Identifier(String id, String filename, int lineNum) {
        // TODO CHECK FOR ID BEING BAD
        this.id = id;
        this.filename = filename;
        this.lineNum = lineNum;
    }


    public String convertToJott() {
        return id;
    }
}
