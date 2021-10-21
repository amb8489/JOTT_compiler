package grammar;

import main.Token;

import java.util.ArrayList;

public class BodyStmt extends Body {
    IfStmt possible_if = null;
    WhileLoop possible_while = null;
    Stmt possible_stmt = null;



    public BodyStmt(IfStmt possible_if,WhileLoop possible_while,Stmt possible_stmt,int nestLevel) {
        super(nestLevel);
        this.possible_if = possible_if;
        this.possible_while = possible_while;
        this.possible_stmt = possible_stmt;
    }




    // body_stmt -> if_stmt|while_loop|stmt
    public BodyStmt parseBodyStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("----------------parsing body stmt---------------------");

        // ----------------------check for one of these three----------------------------------;
        IfStmt possible_if = IfStmt.parseIfStmt(tokens,nestLevel);
        if (possible_if != null){
            return new BodyStmt(possible_if,null,null,nestLevel);
        }


        WhileLoop possible_while = WhileLoop.parseWhile(tokens,nestLevel);
        if (possible_while != null){
            return new BodyStmt(null,possible_while,null,nestLevel);
        }


        Stmt possible_stmt = Stmt.parseStmt(tokens,nestLevel);
        if (possible_stmt != null){
            return new BodyStmt(null,null,possible_stmt,nestLevel);
        }


        // ----------------------Error, sould have been one of these three----------------------------------

        Token t = tokens.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("Syntax error\nInvalid token. Expected Expr. Got: ");
            sb.append(t.getTokenType().toString()).append("\n");
            sb.append(t.getFilename() + ":" +t.getLineNum());
            throw new ParsingException(sb.toString());

    }

    @Override
    public String convertToJott() {

        //TODO check what one is not null and print that one
        return null;
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
