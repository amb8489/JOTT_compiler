package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

/**
 * BExpr is a boolean expression.
 * <p>
 * BExpr can be
 * 1) bool
 * 2) numExpr rel_op numExpr
 * 3) sExpr rel_op sExpr
 * 4) bExpr rel_op bExpr
 *
 * @author Aaron Berghash (amb8489@rit.edu)
 * @author Connor Switenky (cs4331@rit.edu)
 * @author Jake Peverly (jzp7326@rit.edu)
 * @author Kaitlyn DeCola (kmd8594@rit.edu)
 */
public class BExpr extends Expr {
    public ArrayList<BExpr> finalExpr;
    public Expr expr;
    public Token bool;
    public Token relOp;
    public String scope;
    public String exprType;
    public int lineNum;
    public String fileName;

    /**
     * This is the constructor for a boolean expression class.
     *
     * @param finalExp TODO blah
     */
    public BExpr(ArrayList<BExpr> finalExp, String scope, int lineNum, String fileName) {
        super(null, null, null);
        this.finalExpr = finalExp;
        this.scope = scope;
        this.lineNum = lineNum;
        this.fileName = fileName;
    }

    /**
     * This is a constructor for a boolean expression.
     *
     * @param bool TODO blah
     */
    public BExpr(Token bool, String scope, String exprType) {
        super(null, null, null);
        this.bool = bool;
        this.scope = scope;
        this.exprType = exprType;


    }

    /**
     * This is a constructor for a boolean expression.
     *
     * @param bool  TODO blah
     * @param relOp TODO blah
     */
    public BExpr(Token bool, Token relOp, String scope, String exprType) {
        super(null, null, null);
        this.bool = bool;
        this.relOp = relOp;
        this.scope = scope;
        this.exprType = exprType;

    }

    /**
     * This is a constructor for a boolean expression.
     *
     * @param expr
     * @param relOP TODO blah
     */
    public BExpr(Expr expr, Token relOP, String scope, String exprType) {
        super(null, null, null);
        this.relOp = relOP;
        this.expr = expr;
        this.scope = scope;
        this.exprType = exprType;

    }

    /**
     * This is a constructor for a boolean expression.
     *
     * @param expr TODO blah
     */
    public BExpr(Expr expr, String scope, String exprType) {
        super(null, null, null);
        this.expr = expr;
        this.scope = scope;
        this.exprType = exprType;
    }

    /**
     * Parse boolean expressions
     *
     * @param tokens      a list of tokens to parse
     * @param booleanList a list of booleans to look through
     * @param nestLevel   how deep is this?
     * @return an array of parsed boolean expressions
     * @throws ParsingException if anything went wrong, an exception with details will be thrown
     */
    public static ArrayList<BExpr> parseBExpr_r(int nestLevel,
                                                ArrayList<Token> tokens,
                                                ArrayList<BExpr> booleanList,
                                                String scope)
            throws ParsingException {


        String exprType;

        Token possibleBool = tokens.get(TokenIndex.currentTokenIndex);
        boolean isBool = false;
        Expr possibleExpr = null;

        if ("true false".contains(possibleBool.getToken())) {
            TokenIndex.currentTokenIndex++;
            isBool = true;
            exprType = "bool";

        } else {
            NumExpr possibleNumExpr = NumExpr.parseNumExpr(tokens, nestLevel, scope);
            exprType = possibleNumExpr.exprType;

            possibleExpr = possibleNumExpr;

            if (possibleExpr == null) {
                possibleExpr = SExpr.parseSExpr(tokens, nestLevel, scope);
                exprType = "String";
                if (possibleExpr == null) {
                    String message = String.format("Syntax error\nInvalid token. Expected ;. Got: %s\n%s:%s",
                            possibleBool.getTokenType().toString(),
                            possibleBool.getFilename(),
                            possibleBool.getLineNum());
                    throw new ParsingException(message);
                }
            }
//            System.out.println(exprType);

        }
        Token possibleRelOp = tokens.get(TokenIndex.currentTokenIndex);

        if (isBool) {
            if (possibleRelOp.getTokenType() == TokenType.REL_OP) {
                TokenIndex.currentTokenIndex++;
                booleanList.add(new BExpr(possibleBool, possibleRelOp, scope, exprType));
                return parseBExpr_r(nestLevel, tokens, booleanList, scope);
            }

            // lone bool
            booleanList.add(new BExpr(possibleBool, scope, exprType));
            return booleanList;

        } else {
            if (possibleRelOp.getTokenType() == TokenType.REL_OP) {
                TokenIndex.currentTokenIndex++;
                booleanList.add(new BExpr(possibleExpr, possibleRelOp, scope, exprType));
                return parseBExpr_r(nestLevel, tokens, booleanList, scope);
            }
            // lone expr
            booleanList.add(new BExpr(possibleExpr, scope, exprType));
            return booleanList;
        }
    }

    /**
     * TODO
     *
     * @param tokens    TODO
     * @param nestLevel TODO
     * @return TODO
     * @throws ParsingException TODO
     */
    public static Expr parseBExpr(ArrayList<Token> tokens, int nestLevel, String scope) throws ParsingException {
        ArrayList<BExpr> f = parseBExpr_r(nestLevel, tokens, new ArrayList<>(), scope);
        int lineNum = tokens.get(TokenIndex.currentTokenIndex).getLineNum();
        String fileName = tokens.get(TokenIndex.currentTokenIndex).getFilename();

        return new BExpr(f, scope, lineNum, fileName);
    }

    /**
     * Return this object as a Jott code.
     *
     * @return a stringified version of this object as Jott code
     */
    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();

        for (BExpr n : finalExpr) {
            // bool relOp
            if (n.bool != null && n.relOp != null) {
                jottString.append(String.format("%s %s", n.bool.getToken(), n.relOp.getToken()));
            }

            // bool
            if (n.bool != null && n.relOp == null) {
                jottString.append(String.format("%s ", n.bool.getToken()));
            }

            // exp relOp
            if (n.expr != null && n.relOp != null) {
                jottString.append(String.format("%s%s ", n.expr.convertToJott(), n.relOp.getToken()));
            }

            // exp
            if (n.expr != null && n.relOp == null) {
                jottString.append(String.format("%s ", n.expr.convertToJott()));
            }
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

        for (BExpr n : finalExpr) {
            // bool relOp
            if (n.bool != null && n.relOp != null) {
            	javaString.append(String.format("%s %s", n.bool.getToken(), n.relOp.getToken()));
            }

            // bool
            if (n.bool != null && n.relOp == null) {
            	javaString.append(String.format("%s ", n.bool.getToken()));
            }

            // exp relOp
            if (n.expr != null && n.relOp != null) {
            	javaString.append(String.format("%s%s ", n.expr.convertToJava(), n.relOp.getToken()));
            }

            // exp
            if (n.expr != null && n.relOp == null) {
            	javaString.append(String.format("%s ", n.expr.convertToJava()));
            }
        }

        return javaString.toString();
    }

    /**
     * Return this object as a C code.
     *
     * @return a stringified version of this object as C code
     */
    public String convertToC() {
        StringBuilder cString = new StringBuilder();

        for (BExpr n : finalExpr) {
            // bool relOp
            if (n.bool != null && n.relOp != null) {
            	cString.append(String.format("%s %s", n.bool.getToken(), n.relOp.getToken()));
            }

            // bool
            if (n.bool != null && n.relOp == null) {
            	cString.append(String.format("%s ", n.bool.getToken()));
            }

            // exp relOp
            if (n.expr != null && n.relOp != null) {
            	cString.append(String.format("%s%s ", n.expr.convertToC(), n.relOp.getToken()));
            }

            // exp
            if (n.expr != null && n.relOp == null) {
            	cString.append(String.format("%s ", n.expr.convertToC()));
            }
        }

        return cString.toString();
    }

    /**
     * Return this object as a Python code.
     *
     * @return a stringified version of this object as Python code
     */
    public String convertToPython() {
        StringBuilder PyString = new StringBuilder();

        for (BExpr n : finalExpr) {
            // bool relOp
            if (n.bool != null && n.relOp != null) {
                PyString.append(String.format("%s %s", n.bool.getToken(), n.relOp.getToken()));
            }

            // bool
            if (n.bool != null && n.relOp == null) {
                PyString.append(String.format("%s ", n.bool.getToken()));
            }

            // exp relOp
            if (n.expr != null && n.relOp != null) {
                PyString.append(String.format("%s%s ", n.expr.convertToPython(), n.relOp.getToken()));
            }

            // exp
            if (n.expr != null && n.relOp == null) {
                PyString.append(String.format("%s ", n.expr.convertToPython()));
            }
        }

        return PyString.toString();
    }

    /**
     * Ensure the boolean expression code is valid
     *
     * @return whether code is valid or not
     */
    public boolean validateTree() throws ParsingException {

        String previousType = null;
        if (finalExpr != null) {
            for (BExpr bExpr : finalExpr) {

                bExpr.expr.validateTree();
                if (bExpr.expr.type != null) {
                    bExpr.exprType = bExpr.expr.type;
                }


                if (previousType == null) {
                    previousType = bExpr.exprType;
                }
                if (!previousType.equals(bExpr.exprType)) {
                    // Failure
                    String message = String.format("mis matched types in bool expr: %s relOp %s",
                            previousType, bExpr.exprType);
                    String fileName = this.fileName;
                    int lineNum = this.lineNum;
                    throw new ParsingException(String.format("SemanticError:\n %s\n%s:%d", message, fileName, lineNum));


                }
                previousType = bExpr.exprType;
            }
        }
        return false;
    }
}
