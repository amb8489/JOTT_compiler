package grammar;

import main.Token;

import java.util.Set;

public class Identifier {
    private static Set<String> IdBanList = Set.of("while", "for", "True", "False", "if", "elseif", "else","print");

    Token id = null;


    public Identifier(Token id) {
        check(id);
        this.id = id;

    }
    public static void check(Token id){

        if(IdBanList.contains(id.getToken())){
            System.out.println("cant use "+id.getToken()+" as id");
        }
    }


    public String convertToJott() {
        return id.getToken();
    }
}
