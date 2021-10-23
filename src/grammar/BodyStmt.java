package grammar;

import main.Token;

import java.util.ArrayList;

public class BodyStmt {

    public IfStmt possible_if ;
    public WhileLoop possible_while;
    public Stmt possible_stmt;


    public BodyStmt(IfStmt possible_if, WhileLoop possible_while, Stmt possible_stmt, int nestLevel) {

        this.possible_if = possible_if;
        this.possible_while = possible_while;
        this.possible_stmt = possible_stmt;
        System.out.println(convertToJott());
    }


    // body_stmt -> if_stmt|while_loop|stmt
    public static BodyStmt parseBodyStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("----------------parsing body stmt---------------------");

        // ----------------------check for one of these three----------------------------------;

        // RESTORE TODO ------------------------------------- where left off
        System.out.println("first::::" + tokens.get(TOKEN_IDX.IDX).getToken());

        IfStmt possible_if = IfStmt.parseIfStmt(tokens, nestLevel);

        System.out.println("first::::" + tokens.get(TOKEN_IDX.IDX).getToken());
        if (possible_if != null) {
            return new BodyStmt(possible_if, null, null, nestLevel);
        }


        System.out.println("first again::::" + tokens.get(TOKEN_IDX.IDX).getToken());


        WhileLoop possible_while = WhileLoop.parseWhile(tokens, nestLevel);
        if (possible_while != null) {
            return new BodyStmt(null, possible_while, null, nestLevel);
        }

        System.out.println("first again::::" + tokens.get(TOKEN_IDX.IDX).getToken());


        Stmt possible_stmt = Stmt.parseStmt(tokens, nestLevel);
        if (possible_stmt != null) {
            System.out.println("statment found: "+possible_stmt.convertToJott());
            return new BodyStmt(null, null, possible_stmt, nestLevel);
        }

        return null;



    }

    public String convertToJott() {

        if (this.possible_if != null) {
            return this.possible_if.convertToJott();
        }
        if (this.possible_while != null) {
            return this.possible_while.convertToJott();
        }


        if (this.possible_stmt != null) {
            return this.possible_stmt.convertToJott();
        }


        return "nu11";
    }

}
