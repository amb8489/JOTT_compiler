package grammar;

import main.Token;

public class Identifier {

    Token id = null;


    public Identifier(Token id) {
        // TODO CHECK FOR ID BEING BAD
        this.id = id;

    }


    public String convertToJott() {
        return id.getToken();
    }
}
