package grammar;

import main.Token;
import main.TokenType;
import java.util.ArrayList;

/**
 * Body holds the nested code within statements like if, while, and such.
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class Body  {
    public ArrayList<BodyStmt> bodies;
    public ReturnStmt hasReturn;
    public boolean hasGuaranteedReturnFromIf;

    int nestLevel;

    public Body(ArrayList<BodyStmt> bodies, ReturnStmt rs, int nestLevel, boolean guaranteedReturn) {
        this.hasReturn = rs;
        this.bodies = bodies;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturnFromIf = guaranteedReturn;
    }

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Body ParseBody(ArrayList<Token> tokens, int nestLevel) throws ParsingException {

        ArrayList<BodyStmt> bodies = new ArrayList<>();

        if (tokens.get(TOKEN_IDX.index).getTokenType() == TokenType.R_BRACKET) {
            //System.out.println("EMPTY BODY");
            return null;
        }

        // ------------------------ empty case -----------------
        while (tokens.get(TOKEN_IDX.index).getTokenType() != TokenType.R_BRACE) {
            //System.out.println("\tlooking for body");
            BodyStmt bodyStmt = null;
            if (!tokens.get(TOKEN_IDX.index).getToken().equals("return")) {
                 bodyStmt = BodyStmt.parseBodyStmt(tokens, nestLevel);
            }
            //System.out.println("\t--->>>>>>>looking for body");

            if (bodyStmt == null) {
                //System.out.println("EMPTY BODY 2");
                ReturnStmt returnStmt = ReturnStmt.parseReturnStmt(tokens, nestLevel);
//                if (returnStmt == null) { System.out.println("EMPTY return"); }

                boolean hasGuaranteedReturn = false;
                for (BodyStmt bodyStmtElement : bodies) {
                    if (bodyStmtElement.hasGuaranteedReturn) {
                        hasGuaranteedReturn = true;
                        break;
                    }
                }

                return new Body(bodies, returnStmt,nestLevel,hasGuaranteedReturn);
            }
            //System.out.println("\tadding body\t\t\t\t\t-----------" + bodyStmt.convertToJott());
            bodies.add(bodyStmt);
        }

        ReturnStmt returnStmt = ReturnStmt.parseReturnStmt(tokens, nestLevel);

        boolean hasGuaranteedReturn =false;
        for (BodyStmt b : bodies) {
            if (b.hasGuaranteedReturn) {
                hasGuaranteedReturn = true;
                break;
            }
        }

        return new Body(bodies, returnStmt, nestLevel, hasGuaranteedReturn);
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        String space = "    ".repeat(this.nestLevel);

        StringBuilder jottString = new StringBuilder();

        for (BodyStmt b : bodies) {
            jottString.append(space+b.convertToJott() + "\n");
        }
        if (hasReturn != null) {
            jottString.append(space+ hasReturn.convertToJott()+"\n");
        }

        return jottString.toString();
    }

    /**
     * TODO
     * @return TODO
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
