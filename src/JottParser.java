/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import java.io.Console;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class JottParser implements JottTree {
    public JottTreeNode tree;
    private static int tokenIndex;
    private static ArrayList<Token> tokens;

    /**
     * program -> function_list $$
     * function_list -> function_def function_list
     * function_list -> ε
     * function_def -> id [ func_def_params ] : function_return { body }
     * func_def_params -> id : type func_def_params_t|ε
     * func_def_params_t -> , id : type func_def_params_t|ε
     * body_stmt -> if_stmt|while_loop|stmt
     * return_stmt -> return expr end_stmt
     * body -> body_stmt body|return_stmt|ε
     * end_stmt -> ;
     * if_stmt -> if [ b_expr ] { body } elseif_lst|if [ b_expr ] { body } elseif_lst else { body }
     * elseif_lst -> elseif [ b_expr ] { body } elseif_lst|ε
     * while_loop -> while [ b_expr ] { body }
     * char -> l_char|u_char|digit
     * l_char -> a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z
     * u_char -> A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z
     * digit -> 0|1|2|3|4|5|6|7|8|9
     * sign -> -|+|ε
     * id -> l_char char
     * stmt -> asmt|var_dec|func_call end_stmt
     * func_call -> id [ params ]
     * params -> expr params_t|ε
     * params_t -> , expr params_t|ε
     * expr -> i_expr|d_expr|s_expr|b_expr|id|func_call
     * type -> Double|Integer|String|Boolean
     * function_return -> type|Void
     * var_dec -> type id end_stmt
     * asmt -> Double id = d_expr end_stmt|Integer id = i_expr end_stmt|String id = s_expr end_stmt|Boolean id = b_expr end_stmt|id = d_expr end_stmt|id = i_expr end_stmt|id = s_expr end_stmt|id = b_expr end_stmt
     * op -> +|*|/|+|-
     * rel_op -> >|>=|<|<=|==|!=
     * dbl -> sign digit . digit digit
     * d_expr -> id|dbl|dbl op dbl|dbl op d_expr|d_expr op dbl|d_expr op d_expr|func_call
     * bool -> True|False
     * b_expr -> id|bool|i_expr rel_op i_expr|d_expr rel_op d_expr|s_expr rel_op s_expr|b_expr rel_op b_expr|func_call
     * int -> sign digit digit
     * i_expr -> id|int|int op int|int op i_expr|i_expr op int|i_expr op i_expr|func_call
     * str_literal -> " str "
     * str -> char str|space str|ε
     * s_expr -> str_literal|id|func_call
     */

    /**
     * program -> function_list $$
     * **/
    private static JottTreeNode program(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PROGRAM);

        JottTreeNode child1 = new JottTreeNode(JottElement.$$); // TODO: do we really need this?
        JottTreeNode child2 = new JottTreeNode(JottElement.FUNCTION_LIST);

        jottTreeNode.addChild(child1); // terminal
        jottTreeNode.addChild(function_list(child2)); // non-terminal
        return jottTreeNode;
    }

    /**
     * function_list -> function_def function_list
     * function_list -> ε
     */
    private static JottTreeNode function_list(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNCTION_LIST);

        JottTreeNode child1 = new JottTreeNode(JottElement.FUNCTION_DEF);
        JottTreeNode child2 = new JottTreeNode(JottElement.FUNCTION_LIST);

        jottTreeNode.addChild(function_def(child1));
//        jottTreeNode.addChild(function_list(child2)); // TODO: check if there are more functions
        return jottTreeNode;
    }

    /**
     * function_def -> id [ func_def_params ] : function_return { body }
     * **/
    private static JottTreeNode function_def(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNCTION_DEF);

        Token idToken = tokens.get(tokenIndex);

        // look for id
        if (idToken.getTokenType() == TokenType.ID_KEYWORD) {
            // add id child
            System.out.println("id exists");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), idToken));
        } else {
            System.out.println("missing id");
            return null;
        }

        // look for [
        tokenIndex += 1;
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET)
        {
            System.out.println("[ exists");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
        } else {
            System.out.println("missing [");
            return null;
        }

        // look for func_def_params or ]
        tokenIndex += 1;
        Token parameterToken = tokens.get(tokenIndex);
        if (parameterToken.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("] exists, no parameters");
            jottTreeNode.addChild(new JottTreeNode(parameterToken));
        } else {
            // handle this as a parameter
            jottTreeNode.addChild(function_def_params(jottTreeNode));

            Token rightBracketToken = tokens.get(tokenIndex);
            if (rightBracketToken.getTokenType() == TokenType.R_BRACKET) {
                System.out.println("] exists, has parameter(s)");
                jottTreeNode.addChild(new JottTreeNode(rightBracketToken));
            } else {
                System.out.println("missing ]");
                return null;
            }
        }

        // look for :
        tokenIndex += 1;
        Token colon = tokens.get(tokenIndex);
        if (colon.getTokenType() == TokenType.COLON) {
            System.out.println(": exists");
            jottTreeNode.addChild(new JottTreeNode(colon));
        } else {
            System.out.println("missing :");
            return null;
        }

        // look for return value
        tokenIndex += 1;
        Token functionReturn = tokens.get(tokenIndex);
        System.out.println(functionReturn.getTokenType());
        if (functionReturn.getTokenType() == TokenType.ID_KEYWORD) {
            JottTreeNode functionReturnNode = new JottTreeNode(JottElement.FUNCTION_RETURN);

            if (functionReturn.getToken().equals("Void")) { // is this void?
                System.out.println("function return found (Void)");
                jottTreeNode.addChild(new JottTreeNode(JottElement.VOID));
            } else {
                JottTreeNode typeNode = type(jottTreeNode);

                if (typeNode != null) { // this is a type
                    System.out.println(String.format("function return found (%s type)", typeNode.getToken()));
                    functionReturnNode.addChild(typeNode);
                    jottTreeNode.addChild(functionReturnNode);
                } else { // not a type, so no idea what this is
                    System.out.println("function return missing");
                    return null;
                }
            }
        }

        // left curly brace
        tokenIndex += 1;
        Token leftCurlyBrace = tokens.get(tokenIndex);
        if (leftCurlyBrace.getTokenType() == TokenType.L_BRACE) {
            System.out.println("{ found");
            jottTreeNode.addChild(new JottTreeNode(leftCurlyBrace));
        } else {
            System.out.println("{ missing");
            return null;
        }

        // body
        tokenIndex += 1;
        JottTreeNode bodyNode = new JottTreeNode(JottElement.BODY);
        bodyNode = body(bodyNode);
        jottTreeNode.addChild(bodyNode);

        // look for right curly brace
//                    tokenIndex += 1;
//                    Token rightCurlyBrace = tokens.get(tokenIndex); // TODO: may will not need this
//                    if (rightCurlyBrace.getTokenType() == TokenType.R_BRACE) {
//                        System.out.println("} found");
//                        jottTreeNode.addChild(new JottTreeNode(rightCurlyBrace));
//                    } else {
//                        System.out.println("} missing");
//                        return null;
//                    }

        return jottTreeNode;


        // TODO: Work in Progress

    }

    private static JottTreeNode function_def_params(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNC_DEF_PARAMS);

        // look for the parameter
        Token parameter = tokens.get(tokenIndex);
        JottTreeNode newNode = new JottTreeNode(JottElement.FUNC_DEF_PARAMS);
        if (parameter.getTokenType() == TokenType.ID_KEYWORD) {
            System.out.println("id exists");
            newNode.addChild(id(new JottTreeNode(JottElement.ID), parameter));
        } else {
            System.out.println("missing id");
            return null;
        }

        // look for the colon
        tokenIndex += 1;
        Token colon = tokens.get(tokenIndex);
        if (colon.getTokenType() == TokenType.COLON) {
            System.out.println("colon exists");
            newNode.addChild(new JottTreeNode(colon));
        } else {
            System.out.println("missing colon");
            return null;
        }

        // look for the type
        tokenIndex += 1;
        Token type = tokens.get(tokenIndex);
        if (type.getTokenType() == TokenType.ID_KEYWORD) {
            JottTreeNode typeNode = type(jottTreeNode);
            if (typeNode != null) {
                newNode.addChild(typeNode);
            } else {
                System.out.println("not a type");
                return null;
            }
        }

        // look if the next token is another parameter
        tokenIndex += 1;
        Token parameter2 = tokens.get(tokenIndex);
        if (parameter2.getTokenType() == TokenType.R_BRACKET) {
            // ] is here, no more parameters
            System.out.println("] exists");
            newNode.addChild(new JottTreeNode(parameter2));
            return newNode;
        } else {
            // apparently there may be more parameters or something else
            newNode.addChild(function_def_params_t(new JottTreeNode(JottElement.FUNC_DEF_PARAMS_T)));
        }
        return newNode;

        // TODO: loop with function_def_params_t until reach ]

    }

    private static JottTreeNode function_def_params_t(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNC_DEF_PARAMS_T);

        // find comma
        Token comma = tokens.get(tokenIndex);
        if (comma.getTokenType() == TokenType.COMMA) {
            System.out.println("comma found");
            jottTreeNode.addChild(new JottTreeNode(comma));
        } else {
            System.out.println("comma is missing");
            return null;
        }

        // find id
        tokenIndex += 1;
        Token parameter = tokens.get(tokenIndex);
        if (parameter.getTokenType() == TokenType.ID_KEYWORD) {
            System.out.println("id found");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), parameter));
        } else {
            System.out.println("id is missing");
            return null;
        }

        // find colon
        tokenIndex += 1;
        Token colon = tokens.get(tokenIndex);
        if (colon.getTokenType() == TokenType.COLON) {
            System.out.println("colon found");
            jottTreeNode.addChild(new JottTreeNode(colon));
        } else {
            System.out.println("colon is missing");
            return null;
        }

        // find type
        tokenIndex += 1;
        Token type = tokens.get(tokenIndex);
        if (type.getTokenType() == TokenType.ID_KEYWORD) {
            JottTreeNode typeNode = type(jottTreeNode);

            if (typeNode != null) {
                jottTreeNode.addChild(typeNode);
            } else {
                System.out.println("not a type");
                return null;
            }
        }

        // is this end (ex: ]) or look for more parameters?
        tokenIndex += 1;
        Token parameter2 = tokens.get(tokenIndex);
        if (parameter2.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("] exists");
            jottTreeNode.addChild(new JottTreeNode(parameter2));
        } else {
            // apparently there may be more parameters or something else
            jottTreeNode.addChild(function_def_params_t(new JottTreeNode(JottElement.FUNC_DEF_PARAMS_T)));
        }
        return jottTreeNode;
    }

    private static JottTreeNode body_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.BODY_STMT);

        return null;
    }

    private static JottTreeNode return_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.RETURN_STMT);
        return null;
    }

    private static JottTreeNode body(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.BODY);

        JottTreeNode bodyNode = jottTreeNode;
        System.out.println(bodyNode);

        return bodyNode;
    }

    private static JottTreeNode end_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.END_STMT);
        return null;
    }

    private static JottTreeNode if_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.IF_STMT);

        return null;
    }

    private static JottTreeNode elseif_lst(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.ELSEIF_LST);
        return null;
    }

    private static JottTreeNode while_loop(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.WHILE_LOOP);
        return null;
    }

    private static JottTreeNode _char(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.CHAR);
        return null;
    }

    private static JottTreeNode l_char(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.L_CHAR);

        return null;
    }

    private static JottTreeNode u_char(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.U_CHAR);
        return null;
    }

    private static JottTreeNode digit(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.DIGIT);
        return null;
    }

    private static JottTreeNode sign(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.SIGN);
        return null;
    }

    private static JottTreeNode id(JottTreeNode jottTreeNode, Token token) {
        System.out.println(JottElement.ID);
        JottTreeNode child = new JottTreeNode(token);
        jottTreeNode.addChild(child);
        return jottTreeNode;
    }

    private static JottTreeNode stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.STMT);
        return null;
    }

    private static JottTreeNode func_call(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNC_CALL);
        return null;
    }

    private static JottTreeNode params(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PARAMS);
        return null;
    }

    private static JottTreeNode params_t(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PARAMS_T);
        return null;
    }

    private static JottTreeNode expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.EXPR);
        return null;
    }

    // modified
    private static JottTreeNode type(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.TYPE);

        Token token = tokens.get(tokenIndex);

        switch (token.getToken()) {
            case "Integer", "Double", "String", "Boolean" -> {
                System.out.println(String.format("Type exists (%s)", token.getToken()));
                JottTreeNode typeNode = new JottTreeNode(JottElement.TYPE);
                typeNode.addChild(new JottTreeNode(token));
                return typeNode;
            }
            default -> {
                System.out.println("missing Type");
                return null;
            }
        }
    }

    private static JottTreeNode var_dec(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.VAR_DEC);
        return null;
    }

    private static JottTreeNode asmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.ASMT);
        return null;
    }

    // will need to ensure this works
    private static JottTreeNode op(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.OP);

        Token token = tokens.get(tokenIndex);

        if (token.getTokenType() == TokenType.MATH_OP){
            System.out.println("op exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        } else {
            System.out.println("missing op");
        }
        return null;
    }

    // will need to ensure this works
    private static JottTreeNode rel_op(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.REL_OP);

        Token token = tokens.get(tokenIndex);

        if (token.getTokenType() == TokenType.REL_OP) {
            System.out.println("REL_OP exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        } else {
            System.out.println("missing REL_OP");
        }
        return null;
    }

    private static JottTreeNode dbl(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.DBL);
        return null;
    }

    private static JottTreeNode d_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.D_EXPR);
        return null;
    }

    private static JottTreeNode _bool(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.BOOL);
        Token token = tokens.get(tokenIndex);

        switch (token.getToken()) {
            case "True", "False" -> {
                System.out.println(String.format("BOOL exists (%s)", token.getToken()));
                return new JottTreeNode(token);
            }
            default -> {
                System.out.println("missing BOOl");
                return null;
            }
        }
    }

    private static JottTreeNode b_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.B_EXPR);
        return null;
    }

    private static JottTreeNode _int(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.INT);
        return null;
    }

    private static JottTreeNode i_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.I_EXPR);
        return null;
    }

    private static JottTreeNode str_literal(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.STR_LITERAL);
        return null;
    }

    private static JottTreeNode str(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.STR);
        return null;
    }

    private static JottTreeNode s_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.S_EXPR);
        return null;

    }

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param _tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> _tokens){
        JottParser jottParser = new JottParser();

        tokens = _tokens;
        tokenIndex = 0;
        jottParser.tree = new JottTreeNode(JottElement.PROGRAM);
        program(jottParser.tree);


        System.out.println("******************\nPRINTING THE PARSE TREE OUT");
        printTree(jottParser.tree);
        return jottParser;
    }

    @Override
    public String convertToJott() {
        return null;
    }

    /** don't implement this yet **/
    @Override
    public String convertToJava() {
        return null;
    }

    /** don't implement this yet **/
    @Override
    public String convertToC() {
        return null;
    }

    /** don't implement this yet **/
    @Override
    public String convertToPython() {
        return null;
    }

    /** don't implement this yet **/
    @Override
    public boolean validateTree() {
        return false;
    }


    /** remove me later, for testing purposes **/
    public static void printTree(JottTreeNode jottTreeNode) {
        System.out.print(String.format("******************\n%s", jottTreeNode.getJottElement()));

        ArrayList<JottTreeNode> jottChildren = jottTreeNode.getChildren();
        if (jottChildren.size() <= 0) {
            System.out.println(" has no children");
        } else {
            System.out.println(String.format(" has %d %s:", jottChildren.size(), jottChildren.size() <= 1 ? "child. that is" : "children. those are"));
            for (JottTreeNode jottChild : jottChildren) {
                if (jottChild == null) {
                    System.out.println("\tERROR: this child is null");
                } else {
                    if (jottChild.isTerminal()) {
                        System.out.println(String.format("\t%s (terminal)", jottChild.getToken().getToken()));
                    } else {
                        System.out.println(String.format("\t%s (non-terminal)", jottChild.getJottElement()));
                    }
                }
            }

            for (JottTreeNode jottChild : jottChildren) {
                if (jottChild != null && !jottChild.isTerminal() && jottChild.getJottElement() != JottElement.VOID) {
                    printTree(jottChild);
                }
            }
        }
    }



    public static void main(String[] args) {
        String filename = "src/testCases/temp/test.jott";
        ArrayList<Token> testResults = JottTokenizer.tokenize(filename);
        parse(testResults);
//        for (Token t:testResults) {
//            System.out.println(t.getToken()+"\t\t<- "+t.getTokenType());
//        }
    }
}
