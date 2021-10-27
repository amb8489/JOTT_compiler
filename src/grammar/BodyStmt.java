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
public class BodyStmt {
    public IfStmt possibleIf;
    public WhileLoop possibleWhile;
    public Stmt possibleStmt;
    public int nestLevel;
    public boolean hasGuaranteedReturn;

    /**
     * Constructor TODO
     * @param possibleIf TODO
     * @param possibleWhile TODO
     * @param possibleStmt TODO
     * @param nestLevel TODO
     * @param hasGuaranteedReturn TODO
     */
    public BodyStmt(IfStmt possibleIf, WhileLoop possibleWhile, Stmt possibleStmt, int nestLevel, boolean hasGuaranteedReturn) {
        this.possibleIf = possibleIf;
        this.possibleWhile = possibleWhile;
        this.possibleStmt = possibleStmt;
        System.out.println(convertToJott());
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @throws ParsingException TODO
     */
    public static BodyStmt parseBodyStmt(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("----------------parsing body stmt---------------------");

        // ----------------------check for one of these three----------------------------------;

        System.out.println("first::::" + tokens.get(TOKEN_IDX.index).getToken());

        IfStmt possibleIf = IfStmt.parseIfStmt(tokens, nestLevel+1);

        System.out.println("first::::" + tokens.get(TOKEN_IDX.index).getToken());
        if (possibleIf != null) {
            return new BodyStmt(possibleIf, null, null, nestLevel,possibleIf.hasGuaranteedReturn);
        }


        System.out.println("first again::::" + tokens.get(TOKEN_IDX.index).getToken());


        WhileLoop possibleWhile = WhileLoop.parseWhile(tokens, nestLevel+1);
        if (possibleWhile != null) {
            return new BodyStmt(null, possibleWhile, null, nestLevel,false);
        }

        System.out.println("first again::::" + tokens.get(TOKEN_IDX.index).getToken());


        Stmt possibleStmt = Stmt.parseStmt(tokens, nestLevel);

        if (possibleStmt != null) {
            System.out.println("statement found: " + possibleStmt.convertToJott());
            return new BodyStmt(null, null, possibleStmt, nestLevel+1,false);
        }

        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        String Space = "\t".repeat(this.nestLevel);

        if (this.possibleIf != null) { return this.possibleIf.convertToJott(); }
        if (this.possibleWhile != null) { return this.possibleWhile.convertToJott(); }
        if (this.possibleStmt != null) { return this.possibleStmt.convertToJott(); }

        return "nu11";
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }

}
