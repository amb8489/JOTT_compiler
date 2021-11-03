package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * This class represents an if statement.
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
    public String scope;

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
                  boolean hasGuaranteedReturn, String scope) {
        this.expr = expr;
        this.body1 = body1;
        this.elseIfStatements = elseIfStatements;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
        this.scope = scope;

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
                  boolean hasGuaranteedReturn, String scope) {

        this.expr = expr;
        this.body1 = body1;
        this.elseIfStatements = elseIfStatements;
        this.body2 = body2;
        this.nestLevel = nestLevel;
        this.hasGuaranteedReturn = hasGuaranteedReturn;
        this.scope = scope;

    }

    public static IfStmt parseIfStmt(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {
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
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected [. Got: %s\n%s:%s", L_BRACKET.getTokenType().toString(), L_BRACKET.getFilename(), L_BRACKET.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // checking for bool expression
        Expr expression = BExpr.parseBExpr(tokens, nestLevel, scope);

        // checking for ]
        Token R_BRACKET = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (R_BRACKET.getTokenType() != TokenType.R_BRACKET) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected ]. Got: %s\n%s:%s", R_BRACKET.getTokenType().toString(), R_BRACKET.getFilename(), R_BRACKET.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // checking for {
        Token L_BRACE = tokens.get(TokenIndex.currentTokenIndex);

        // check for if
        if (L_BRACE.getTokenType() != TokenType.L_BRACE) {
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected {. Got: %s\n%s:%s", L_BRACE.getTokenType().toString(), L_BRACE.getFilename(), L_BRACE.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // checking for body
        boolean allBodiesHasReturn = true;
        Body body1 = Body.ParseBody(tokens, nestLevel, scope);
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
            throw new ParsingException(String.format("Syntax error\nInvalid token. Expected }. Got: %s\n%s:%s",
                    R_BRACE.getTokenType().toString(), R_BRACE.getFilename(), R_BRACE.getLineNum()));
        }
        TokenIndex.currentTokenIndex++;

        // checking for else ifs
        ArrayList<ElseifStmt> elseIfList = ElseifStmt.ParseElsif_lst(tokens, nestLevel, scope);
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
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected {. Got: %s\n%s:%s",
                        LL_BRACE.getTokenType().toString(), LL_BRACE.getFilename(), LL_BRACE.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;

            // checking for body
            Body body2 = Body.ParseBody(tokens, nestLevel, scope);
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
                throw new ParsingException(String.format("Syntax error\nInvalid token. Expected }. Got: %s\n%s:%s",
                        RR_BRACE.getTokenType().toString(), RR_BRACE.getFilename(), RR_BRACE.getLineNum()));
            }
            TokenIndex.currentTokenIndex++;

            // all done
            hasGuaranteedReturn = hasGuaranteedReturn == allBodiesHasReturn;

            return new IfStmt(expression, body1, body2, elseIfList, nestLevel, hasGuaranteedReturn, scope);
        }


        return new IfStmt(expression, body1, elseIfList, nestLevel, false, scope);
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
        StringBuilder javaString = new StringBuilder();
        String space = "\t".repeat(this.nestLevel - 1);

        javaString.append("if ( ");
        javaString.append(String.format("%s ) { \n", this.expr.convertToJava()));
        javaString.append(String.format("%s%s}", body1.convertToJava(), space));

        if (elseIfStatements != null) {
            for (ElseifStmt elseifStmt : elseIfStatements) {
            	javaString.append(elseifStmt.convertToJava());
            }
        }

        if (body2 != null) {
        	javaString.append("else{\n");
        	javaString.append(String.format("%s%s}\n", body2.convertToJava(), space));

        }

        return javaString.toString();
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
        StringBuilder PyString = new StringBuilder();
        String space = "\t".repeat(this.nestLevel - 1);

        PyString.append("if ");
        PyString.append(String.format("%s : \n", this.expr.convertToPython()));
        PyString.append(String.format("%s%s", body1.convertToPython(), space));

        if (elseIfStatements != null) {
            for (ElseifStmt elseifStmt : elseIfStatements) {
                PyString.append(elseifStmt.convertToPython());
            }
        }

        if (body2 != null) {
            PyString.append("else:\n");
            PyString.append(String.format("%s%s\n", body2.convertToPython(), space));

        }

        return PyString.toString();
    }

    /**
     * Ensure the code in the function definition parameters are valid.
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {


        expr.validateTree();
        if (elseIfStatements != null) {
            for (ElseifStmt elif : elseIfStatements) {
                System.out.println(elif.expr.validateTree());
                elif.expr.validateTree();

                if (elif.body != null) {
                    elif.body.validateTree();
                }
            }
        }

        if (body1 != null) {
            body1.validateTree();
        }
        if (body2 != null) {
            body2.validateTree();
        }
        return true;
    }
}
