package grammar;

import main.Token;

import java.util.ArrayList;

public class BodyStmt extends Body {
    IfStmt possible_if = null;
    WhileLoop possible_while = null;
    Stmt possible_stmt = null;



    public BodyStmt(IfStmt possible_if,WhileLoop possible_while,Stmt possible_stmt,int nestLevel) {
        super(null);

        this.possible_if = possible_if;
        this.possible_while = possible_while;
        this.possible_stmt = possible_stmt;
    }




    // body_stmt -> if_stmt|while_loop|stmt
    public static BodyStmt parseBodyStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("----------------parsing body stmt---------------------");

        // ----------------------check for one of these three----------------------------------;

        // RESTORE TODO ------------------------------------- where left off
        System.out.println("first::::"+tokens.get(TOKEN_IDX.IDX).getToken());

        IfStmt possible_if = IfStmt.parseIfStmt(tokens,nestLevel);

        System.out.println("first::::"+tokens.get(TOKEN_IDX.IDX).getToken());
        if (possible_if != null){
            return new BodyStmt(possible_if,null,null,nestLevel);
        }


        System.out.println("first again::::"+tokens.get(TOKEN_IDX.IDX).getToken());


        WhileLoop possible_while = WhileLoop.parseWhile(tokens,nestLevel);
        if (possible_while != null){
            return new BodyStmt(null,possible_while,null,nestLevel);
        }

        System.out.println("first again::::"+tokens.get(TOKEN_IDX.IDX).getToken());


        Stmt possible_stmt = Stmt.parseStmt(tokens,nestLevel);
        if (possible_stmt != null){
            System.out.println("statment found");
            return new BodyStmt(null,null,possible_stmt,nestLevel);
        }


        // ----------------------Error, sould have been one of these three----------------------------------

        return null;

    }

    @Override
    public String convertToJott() {

        //TODO check what one is not null and print that one
        return "jpt str not completed yet";
    }

    @Override
    public String convertToJava() {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
