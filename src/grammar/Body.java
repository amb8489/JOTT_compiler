package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * A body can be empty, contains a return statement, or holds one or more body statements.
 * It is found within a function, if, elseif, else, while, and such.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Body {
    public ArrayList<BodyStmt> bodies;
    public ReturnStmt hasReturn;
    public boolean hasGuaranteedReturnFromIf;
    int nestLevel;
    public String scope;

    /**
     * This is the constructor for a body class.
     *
     * @param bodies           can hold nothing, a single class, or multiple classes
     * @param returnStmt       return statement, if any
     * @param nestLevel        how deep is this body
     * @param guaranteedReturn whether return is always possible?
     */
    public Body(ArrayList<BodyStmt> bodies, ReturnStmt returnStmt, int nestLevel, boolean guaranteedReturn,
                String scope) {
        this.hasReturn = returnStmt;
        this.bodies = bodies;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturnFromIf = guaranteedReturn;
        this.scope = scope;
    }

    /**
     * Start recursively parsing this body
     *
     * @param tokens    pass in an arraylist of tokens to be parsed
     * @param nestLevel how deep is this body
     * @return the parsed result in form of a body object
     * @throws ParsingException throw an error if any
     */
    public static Body ParseBody(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {
        ArrayList<BodyStmt> bodies = new ArrayList<>();

        if (tokens.get(TokenIndex.currentTokenIndex).getTokenType() == TokenType.R_BRACKET) {
            //System.out.println("EMPTY BODY");
            return null;
        }

        // ------------------------ empty case -----------------
        while (tokens.get(TokenIndex.currentTokenIndex).getTokenType() != TokenType.R_BRACE) {
            //System.out.println("\tlooking for body");
            BodyStmt bodyStmt = null;
            if (!tokens.get(TokenIndex.currentTokenIndex).getToken().equals("return")) {
                bodyStmt = BodyStmt.parseBodyStmt(tokens, nestLevel, scope);
            }

            if (bodyStmt == null) {
                ReturnStmt returnStmt = ReturnStmt.parseReturnStmt(tokens, nestLevel, scope);

                boolean hasGuaranteedReturn = false;
                for (BodyStmt bodyStmtElement : bodies) {
                    if (bodyStmtElement.hasGuaranteedReturn) {
                        hasGuaranteedReturn = true;
                        break;
                    }
                }

                return new Body(bodies, returnStmt, nestLevel, hasGuaranteedReturn, scope);
            }
            bodies.add(bodyStmt);
        }

        ReturnStmt returnStmt = ReturnStmt.parseReturnStmt(tokens, nestLevel, scope);

        boolean hasGuaranteedReturn = false;
        for (BodyStmt b : bodies) {
            if (b.hasGuaranteedReturn) {
                hasGuaranteedReturn = true;
                break;
            }
        }

        return new Body(bodies, returnStmt, nestLevel, hasGuaranteedReturn, scope);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        String space = "\t".repeat(this.nestLevel);

        StringBuilder jottString = new StringBuilder();

        for (BodyStmt body : bodies) {
            jottString.append(String.format("%s%s\n", space, body.convertToJott()));
        }

        if (hasReturn != null) {
            jottString.append(String.format("%s%s\n", space, hasReturn.convertToJott()));
        }

        return jottString.toString();
    }

    /**
     * Return this object as a Java code.
     *
     * @return a stringified version of this object as Java code
     */
    public String convertToJava() {
        return null;
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        return null;
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        return null;
    }

    /**
     * Ensure the code within the body and the body itself is valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {
        if (bodies != null) {
            for (BodyStmt bodyStmt : bodies) {
                bodyStmt.validateTree();
            }
        }
        if (hasReturn != null) {
            hasReturn.validateTree();
        }
        return true;
    }
}
