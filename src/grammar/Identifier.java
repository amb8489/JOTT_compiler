package grammar;

import main.Token;

import java.util.Set;

public class Identifier {
    private static Set<String> IdBanList = Set.of("while", "for", "True", "False", "if", "elseif", "else","print");

    Token id = null;


    public Identifier(Token id) {
        this.id = id;

    }
    public static void check(Token id) throws ParsingException {

        if(IdBanList.contains(id.getToken())){
            throw new ParsingException("cant use "+id.getToken()+" as id");
        }
    }


    public String convertToJott() {
        return id.getToken();
    }
    public boolean validateTree() {
        return false;
    }

}
