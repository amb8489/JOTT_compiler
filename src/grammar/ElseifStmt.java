package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class ElseifStmt {
    private final Expr expression;
    private final Body body;
    private final int nestLevel;

    /**
     * Constructor
     * @param expression TODO
     * @param body TODO
     * @param nestLevel TODO
     */
    public ElseifStmt(Expr expression, Body body, int nestLevel) {
        this.expression = expression;
        this.body = body;
        this.nestLevel = nestLevel;
    }


    //      elseif_lst -> elseif [ b_expr ] { body } elseif_lst | ε TODO <-- what is this

    /**
     * TODO
     * @param tokens TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static ArrayList<ElseifStmt> ParseElsif_lst(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("------------------------PARSING elif------------------------");

        // list of all else if we will encounter if any
        ArrayList<ElseifStmt> elif_lists = new ArrayList<>();


        // looking if there is an "elseif"
        Token elseif = tokens.get(TOKEN_IDX.index);

        // if no elseif then : ε case and return, this is not an error
        if (!elseif.getToken().equals("elseif")) {
            return null;
        }

        // while we did encounter an elseif in the tokens we will keep serching for elseif
        while (elseif.getToken().equals("elseif")) {
            System.out.println("    elif found:" + elseif.getToken());

            // removing elseif
            TOKEN_IDX.index++;

            // ---------------------------looking for [----------------------------------------

            Token L_BRACKET = tokens.get(TOKEN_IDX.index);
            System.out.println("    2nd:" + L_BRACKET.getToken());
            // check for if
            if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected [. Got: ");
                sb.append(L_BRACKET.getTokenType().toString()).append("\n");
                sb.append(L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum());
                throw new ParsingException(sb.toString());
            }
            System.out.println("    3rd:" + L_BRACKET.getToken());
            TOKEN_IDX.index++;
            // -----------------------looking for bool expr-------------------------------------------


            Expr expr = Expr.parseExpr(tokens, nestLevel);

            System.out.println("    XPR FOUND:" + expr.convertToJott());

            // ---------------------------looking for ]----------------------------------------

            Token R_BRACKET = tokens.get(TOKEN_IDX.index);
            System.out.println("    4th:" + R_BRACKET.getToken());
            // check for if
            if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected ]. Got: ");
                sb.append(R_BRACKET.getTokenType().toString()).append("\n");
                sb.append(R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum());
                throw new ParsingException(sb.toString());
            }
            TOKEN_IDX.index++;

            // ---------------------------looking for {----------------------------------------

            Token L_BRACE = tokens.get(TOKEN_IDX.index);
            System.out.println("    5th:" + L_BRACE.getToken());
            // check for if
            if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected {. Got: ");
                sb.append(L_BRACE.getTokenType().toString()).append("\n");
                sb.append(L_BRACE.getFilename() + ":" + L_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            System.out.println("    6th:" + L_BRACE.getToken());
            TOKEN_IDX.index++;


            // ---------------------------looking for body----------------------------------------

            Body body = Body.ParseBody(tokens, nestLevel);

            // ---------------------------looking for }----------------------------------------

            Token R_BRACE = tokens.get(TOKEN_IDX.index);
            System.out.println("    7th:" + R_BRACE.getToken());
            // check for if
            if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
                StringBuilder sb = new StringBuilder();
                sb.append("Syntax error\nInvalid token. Expected }. Got: ");
                sb.append(R_BRACE.getTokenType().toString()).append("\n");
                sb.append(R_BRACE.getFilename() + ":" + R_BRACE.getLineNum());
                throw new ParsingException(sb.toString());
            }
            System.out.println("    8th:" + R_BRACE.getToken());
            TOKEN_IDX.index++;
            // -----------------------adding what was found to list of seen elif stmts-----------------------

            elif_lists.add(new ElseifStmt(expr, body,nestLevel));

            // ---------------------------looking for elif----------------------------------------

            elseif = tokens.get(TOKEN_IDX.index);
        }

        // ---------------------------all done finding elif's----------------------------------------

        return elif_lists;
    }


    // elseif [ b_expr ] { body } TODO <--- what is this

    /**
     * TODO
     * @return TODO
     */
    public String convertToJott() {
        String SPACE = "    ".repeat(this.nestLevel -1);

        String jottString = "elseif[ " +
                expression.convertToJott() + "]{ \n" +
                body.convertToJott() + SPACE + "}";
        return jottString;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToJava() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToC() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public String convertToPython() {
        return null;
    }

    /**
     * TODO
     * @return TODO
     */
    public boolean validateTree() {
        return false;
    }
}
