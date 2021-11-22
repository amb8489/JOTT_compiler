package grammar;

import main.Token;

import java.util.ArrayList;

/**
 * Body statements are descendents of a body class. Those hold statements such as an if statement, a while loop, or a
 * plain statement.
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
    public String scope;

    /**
     * This is a constructor for a body statement.
     *
     * @param possibleIf          TODO
     * @param possibleWhile       TODO
     * @param possibleStmt        TODO
     * @param nestLevel           TODO
     * @param hasGuaranteedReturn TODO
     */
    public BodyStmt(IfStmt possibleIf, WhileLoop possibleWhile, Stmt possibleStmt, int nestLevel,
                    boolean hasGuaranteedReturn, String scope) {
        this.possibleIf = possibleIf;
        this.possibleWhile = possibleWhile;
        this.possibleStmt = possibleStmt;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
        this.scope = scope;

    }

    /**
     * Parse a body statement
     *
     * @param tokens    a list of tokens to parse
     * @param nestLevel how deep
     * @throws ParsingException if anything went wrong, throw an exception with details in it
     */
    public static BodyStmt parseBodyStmt(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {
        IfStmt possibleIf = IfStmt.parseIfStmt(tokens, nestLevel + 1, scope);

        if (possibleIf != null) {
            return new BodyStmt(possibleIf, null, null, nestLevel,
                    possibleIf.hasGuaranteedReturn, scope);
        }

        WhileLoop possibleWhile = WhileLoop.parseWhile(tokens, nestLevel + 1, scope);
        if (possibleWhile != null) {
            return new BodyStmt(null, possibleWhile, null, nestLevel,
                    false, scope);
        }

        Stmt possibleStmt = Stmt.parseStmt(tokens, nestLevel, scope);

        if (possibleStmt != null) {
            //System.out.println("statement found: " + possibleStmt.convertToJott());
            return new BodyStmt(null, null, possibleStmt, nestLevel + 1,
                    false, scope);
        }

        return null;
    }

    /**
     * Return this object as a convert.Jott code.
     *
     * @return a stringified version of this object as convert.Jott code
     */
    public String convertToJott() {
        if (this.possibleIf != null) {
            return this.possibleIf.convertToJott();
        }
        if (this.possibleWhile != null) {
            return this.possibleWhile.convertToJott();
        }
        if (this.possibleStmt != null) {
            return this.possibleStmt.convertToJott();
        }

        return null;
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        if (this.possibleIf != null) {
            return this.possibleIf.convertToJava();
        }
        if (this.possibleWhile != null) {
            return this.possibleWhile.convertToJava();
        }
        if (this.possibleStmt != null) {
            return this.possibleStmt.convertToJava();
        }

        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        if (this.possibleIf != null) {
            return this.possibleIf.convertToC();
        }
        if (this.possibleWhile != null) {
            return this.possibleWhile.convertToC();
        }
        if (this.possibleStmt != null) {
            return this.possibleStmt.convertToC();
        }

        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        if (this.possibleIf != null) {
            return this.possibleIf.convertToPython();
        }
        if (this.possibleWhile != null) {
            return this.possibleWhile.convertToPython();
        }
        if (this.possibleStmt != null) {
            return this.possibleStmt.convertToPython();
        }

        return null;
    }

    /**
     * Ensure the code within the body is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        if (this.possibleIf != null) {
            return possibleIf.validateTree();
        }
        if (this.possibleWhile != null) {
            return possibleWhile.validateTree();
        }
        if (this.possibleStmt != null) {
            return possibleStmt.validateTree();
        }
        return true;
    }
}
