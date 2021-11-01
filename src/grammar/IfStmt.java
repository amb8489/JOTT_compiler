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
public class IfStmt {
    private final Expr expr;
    private final Body body1;
    private Body body2;
    private final ArrayList<ElseifStmt> elseIfStatements;
    private final int nestLevel;
    public boolean hasGuaranteedReturn;
    public String insideOfFunction;

    /**
     * This is the constructor for an if statement.
     *
     * @param expr                an expression to be evaluated in the if statement
     * @param body1               the body nested in an if statement
     * @param elseIfStatements    are there any else if statements?
     * @param nestLevel           how far deep is this if statement
     * @param hasGuaranteedReturn will there always be a return value?
     */
    public IfStmt(Expr expr,
                  Body body1,
                  ArrayList<ElseifStmt> elseIfStatements,
                  int nestLevel,
                  boolean hasGuaranteedReturn,String insideOfFunction) {
        this.expr = expr;
        this.body1 = body1;
        this.elseIfStatements = elseIfStatements;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
        this.insideOfFunction = insideOfFunction;

    }

    /**
     * This is a constructor for an if statement with extra bodies.
     *
     * @param expr                an expression to be evaluated in the if statement
     * @param body1               the body nested in an if statement
     * @param body2               the second body nested in an if statement
     * @param elseIfStatements    are there any else if statements?
     * @param nestLevel           how far deep is this if statement
     * @param hasGuaranteedReturn will there always be a return value?
     */
    public IfStmt(Expr expr,
                  Body body1,
                  Body body2,
                  ArrayList<ElseifStmt> elseIfStatements,
                  int nestLevel,
                  boolean hasGuaranteedReturn,String insideOfFunction) {

        this.expr = expr;
        this.body1 = body1;
        this.elseIfStatements = elseIfStatements;
        this.body2 = body2;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
        this.insideOfFunction = insideOfFunction;

    }

    public static IfStmt parseIfStmt(ArrayList<Token> tokens, int nestLevel,String insideOfFunction) throws ParsingException {
        Token ifToken = tokens.get(TokenIndex.currentTokenIndex);
        //System.out.println("\t1st:" + ifToken.getToken());
        // check for if
        if (!ifToken.getToken().equals("if")) {
            return null;
        }
        TokenIndex.currentTokenIndex++;

        // checking for [
        Token L_BRACKET = tokens.get(TokenIndex.currentTokenIndex);
        if (L_BRACKET.getTokenType() != TokenType.L_BRACKET) {
            String sb = "Syntax error\nInvalid token. Expected [. Got: " +
                    L_BRACKET.getTokenType().toString() + "\n" +
                    L_BRACKET.getFilename() + ":" + L_BRACKET.getLineNum();
            throw new ParsingException(sb);
        }
        TokenIndex.currentTokenIndex++;

        // checking for bool expression
        Expr expression = Expr.parseExpr(tokens, nestLevel,insideOfFunction);

        // checking for ]
        Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            String sb = "Syntax error\nInvalid token. Expected ]. Got: " +
                    R_BRACKET.getTokenType().toString() + "\n" +
                    R_BRACKET.getFilename() + ":" + R_BRACKET.getLineNum();
            throw new ParsingException(sb);
        }
        TokenIndex.currentTokenIndex++;

        // checking for {
        Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            String sb = "Syntax error\nInvalid token. Expected {. Got: " +
                    L_BRACE.getTokenType().toString() + "\n" +
                    L_BRACE.getFilename() + ":" + L_BRACE.getLineNum();
            throw new ParsingException(sb);
        }
        TokenIndex.currentTokenIndex++;

        // checking for body
        boolean allBodiesHasReturn = true;
        Body body1 = Body.ParseBody(tokens, nestLevel,insideOfFunction);
        if (body1 != null) {
            if (body1.hasReturn == null) {
                allBodiesHasReturn = false;
            }
        } else {
            TokenIndex.currentTokenIndex--;
        }

        // checking for }
        Token R_BRACE = tokens.get(TokenIndex.currentTokenIndex);
        //System.out.println("\t7th:" + R_BRACE.getToken());
        // check for if
        if (R_BRACE.getTokenType() != TokenType.R_BRACE) {
            String sb = "Syntax error\nInvalid token. Expected }. Got: " +
                    R_BRACE.getTokenType().toString() + "\n" +
                    R_BRACE.getFilename() + ":" + R_BRACE.getLineNum();
            throw new ParsingException(sb);
        }
        TokenIndex.currentTokenIndex++;

        // checking for else ifs
        ArrayList<ElseifStmt> elseIfList = ElseifStmt.ParseElsif_lst(tokens, nestLevel,insideOfFunction);
        if (elseIfList != null) {
            for (ElseifStmt elif : elseIfList) {
                if (elif.body.hasReturn == null) {
                    allBodiesHasReturn = false;
                    break;
                }
            }
        }

        //System.out.println("first============: "+tokens.get(TOKEN_IDX.index).getToken());


        // ---------------------- checking for else --------------------------------

        // check for else token
        Token elseToken = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (elseToken.getToken().equals("else")) {
            TokenIndex.currentTokenIndex++;

            // checking for {
            Token LL_BRACE = tokens.get(TokenIndex.currentTokenIndex);

            // check for if
            if (LL_BRACE.getTokenType() != TokenType.L_BRACE) {
                String string = "Syntax error\nInvalid token. Expected {. Got: " +
                        LL_BRACE.getTokenType().toString() + "\n" +
                        LL_BRACE.getFilename() + ":" + LL_BRACE.getLineNum();
                throw new ParsingException(string);
            }
            TokenIndex.currentTokenIndex++;

            // checking for body
            Body body2 = Body.ParseBody(tokens, nestLevel,insideOfFunction);
            boolean hasGuaranteedReturn = false;
            if (body2 != null) {
                if (body2.hasReturn != null) {
                    hasGuaranteedReturn = true;
                }
            }

            // checking for }
            Token RR_BRACE = tokens.get(TokenIndex.currentTokenIndex);
            //System.out.println("\t12th:" + RR_BRACE.getToken());
            // check for if
            if (RR_BRACE.getTokenType() != TokenType.R_BRACE) {
                String sb = "Syntax error\nInvalid token. Expected }. Got: " +
                        RR_BRACE.getTokenType().toString() + "\n" +
                        RR_BRACE.getFilename() + ":" + RR_BRACE.getLineNum();
                throw new ParsingException(sb);
            }
            TokenIndex.currentTokenIndex++;

            // all done
            hasGuaranteedReturn = hasGuaranteedReturn == allBodiesHasReturn;

            return new IfStmt(expression, body1, body2, elseIfList, nestLevel, hasGuaranteedReturn,insideOfFunction);
        }


        return new IfStmt(expression, body1, elseIfList, nestLevel, false,insideOfFunction);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        String space = "\t".repeat(this.nestLevel - 1);

        jottString.append("if [ ");
        jottString.append(String.format("%s ] { \n", this.expr.convertToJott()));
        jottString.append(String.format("%s%s}", body1.convertToJott(), space));

        if (elseIfStatements != null) {
            for (ElseifStmt elseifStmt : elseIfStatements) {
                jottString.append(elseifStmt.convertToJott());
            }
        }

        if (body2 != null) {
            jottString.append("else{\n");
            jottString.append(String.format("%s%s}\n", body2.convertToJott(), space));

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
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() {
        return false;
    }
}
