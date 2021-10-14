/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import java.util.ArrayList;
import java.util.Set;

public class JottParser implements JottTree {
    public JottTreeNode tree;
    private static int tokenIndex;
    private static ArrayList<Token> tokens;
    public static int Failed = 0;

    private static Set<String> IdBanList = Set.of("while", "for", "True", "False", "if", "elseif", "else");
    // TODO: add print, concat, Enter to this list in the future


    /**
     * program -> function_list $$                                                                                      <-- DONE
     * function_list -> function_def function_list                                                                      <-- DONE
     * function_list -> ε                                                                                               <-- DONE
     * function_def -> id [ func_def_params ] : function_return { body }                                                <-- DONE
     * func_def_params -> id : type func_def_params_t|ε                                                                 <-- DONE
     * func_def_params_t -> , id : type func_def_params_t|ε                                                             <-- DONE
     * body_stmt -> if_stmt|while_loop|stmt                                                                             <-- DONE
     * return_stmt -> return expr end_stmt                                                                              <-- DONE
     * body -> body_stmt body|return_stmt|ε                                                                             <-- DONE
     * if_stmt -> if [ b_expr ] { body } elseif_lst
     *            |if [ b_expr ] { body } elseif_lst else { body }                                                      <-- DONE
     * elseif_lst -> elseif [ b_expr ] { body } elseif_lst|ε                                                            <-- DONE
     * while_loop -> while [ b_expr ] { body }                                                                          <-- DONE
     * id -> l_char char                                                                                                <-- DONE
     * stmt -> asmt|var_dec|func_call end_stmt                                                                          <-- DONE
     * func_call -> id [ params ]                                                                                       <-- DONE
     * params -> expr params_t|ε                                                                                        <-- DONE
     * params_t -> , expr params_t|ε                                                                                    <-- DONE
     * expr -> i_expr|d_expr|s_expr|b_expr|id|func_call                                                                 <-- DONE
     * type -> Double|Integer|String|Boolean                                                                            <-- DONE
     * function_return -> type|Void                                                                                     <-- DONE
     * var_dec -> type id end_stmt                                                                                      <-- DONE
     * asmt ->        Double id = d_expr end_stmt                                                                       <-- DONE
     *               |Integer id = i_expr end_stmt
     *               |String id = s_expr end_stmt`
     *               |Boolean id = b_expr end_stmt
     *               |id = d_expr end_stmt
     *               |id = i_expr end_stmt
     *               |id = s_expr end_stmt
     *               |id = b_expr end_stmt
     * op -> +|*|/|+|-                                                                                                  <-- DONE
     * rel_op -> >|>=|<|<=|==|!=                                                                                        <-- DONE
     * d_expr -> id|dbl|dbl op dbl|dbl op d_expr|d_expr op dbl|d_expr op d_expr|func_call                               <-- NOT NEEDED IN PHASE 2
     * bool -> True|False                                                                                               <-- DONE
     * b_expr -> id|bool|i_expr rel_op i_expr|d_expr rel_op d_expr|s_expr rel_op s_expr|b_expr rel_op b_expr|func_call  <-- WORK IN PROGRESS (REDO & IMPROVE)
     * i_expr -> id|int|int op int|int op i_expr|i_expr op int|i_expr op i_expr|func_call                               <-- DONE
     * str_literal -> " str "                                                                                           <-- DONE
     * s_expr -> str_literal|id|func_call                                                                               <-- DONE
     * --------------------------------------------------------------------------------------------------------------------------------
     * */

    /** removed, but here for future record
     *  char -> l_char|u_char|digit                                                              <-- REMOVED
     *  l_char -> a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z                            <-- REMOVED
     *  u_char -> A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z                            <-- REMOVED
     *  digit -> 0|1|2|3|4|5|6|7|8|9                                                             <-- REMOVED
     *  sign -> -|+|ε                                                                            <-- REMOVED
     *  dbl -> sign digit . digit digit                                                          <-- REMOVED
     *  int -> sign digit digit                                                                  <-- REMOVED
     *  str -> char str|space str|ε                                                              <-- REMOVED
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


        // no children?
        if (tokenIndex >= tokens.size()) { return new JottTreeNode(JottElement.FUNCTION_LIST); }

        // find function_def
        JottTreeNode functionDefNode = function_def(new JottTreeNode(JottElement.FUNCTION_DEF));
        jottTreeNode.addChild(functionDefNode);


        tokenIndex += 1;
        System.out.println("DONE <------------------- looking for new function");
        JottTreeNode functionListNode = function_list(new JottTreeNode(JottElement.FUNCTION_LIST));
        jottTreeNode.addChild(functionListNode);

        return jottTreeNode;
    }

    /**
     * function_def -> id [ func_def_params ] : function_return { body }
     * **/
    private static JottTreeNode function_def(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNCTION_DEF);

        Token idToken = tokens.get(tokenIndex);

        System.out.println(idToken.getToken());
        // look for id
        if (idToken.getTokenType() == TokenType.ID_KEYWORD) {
            // add id child
            System.out.println("id exists");

            if (IdBanList.contains(idToken.getToken())) {
                System.out.println("banned keyword");
                Failed=1; return null;
            }

            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), idToken));
        } else {
            System.out.println("missing id");
            Failed=1; return null;
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
            Failed=1; return null;
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
                Failed=1; return null;
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
            Failed=1; return null;
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
                    Failed=1; return null;
                }
            }
        }

        // look for left curly brace
        tokenIndex += 1;
        Token leftCurlyBrace = tokens.get(tokenIndex);
        if (leftCurlyBrace.getTokenType() == TokenType.L_BRACE) {
            System.out.println("{ found");
            jottTreeNode.addChild(new JottTreeNode(leftCurlyBrace));
        } else {
            System.out.println("{ missing");
            Failed=1; return null;
        }

        // look for body
        tokenIndex += 1;
        JottTreeNode bodyNode = new JottTreeNode(JottElement.BODY);
        bodyNode = body(bodyNode);
        if (bodyNode != null) {
            System.out.println("found body");
            jottTreeNode.addChild(bodyNode);
        } else {
            System.out.println("missing body");
            Failed=1; return null;
        }

        // look for right curly brace
        System.out.println("children" + bodyNode.getChildren().size());

        Token rightCurlyBrace = tokens.get(tokenIndex);
        if (rightCurlyBrace.getTokenType() == TokenType.R_BRACE) {
            jottTreeNode.addChild(new JottTreeNode(rightCurlyBrace));
        } else {
            System.out.println("} missing");
        }

        return jottTreeNode;

    }

    private static JottTreeNode function_def_params(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNC_DEF_PARAMS);

        // look for the parameter
        Token parameter = tokens.get(tokenIndex);
        JottTreeNode newNode = new JottTreeNode(JottElement.FUNC_DEF_PARAMS);
        if (parameter.getTokenType() == TokenType.ID_KEYWORD) {
            System.out.println("id exists");

            if (IdBanList.contains(parameter.getToken())) {
                System.out.println("banned keyword");
                Failed=1; return null;
            }

            newNode.addChild(id(new JottTreeNode(JottElement.ID), parameter));
        } else {
            System.out.println("missing id");
            Failed=1; return null;
        }

        // look for the colon
        tokenIndex += 1;
        Token colon = tokens.get(tokenIndex);
        if (colon.getTokenType() == TokenType.COLON) {
            System.out.println("colon exists");
            newNode.addChild(new JottTreeNode(colon));
        } else {
            System.out.println("missing colon");
            Failed=1; return null;
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
                Failed=1; return null;
            }
        }

        // look if the next token is another parameter
        tokenIndex += 1;
        Token parameter2 = tokens.get(tokenIndex);
        if (parameter2.getTokenType() == TokenType.R_BRACKET) {
            // ] is here, no more parameters
            return newNode;
        } else {
            // apparently there may be more parameters or something else
            newNode.addChild(function_def_params_t(new JottTreeNode(JottElement.FUNC_DEF_PARAMS_T)));
        }
        return newNode;
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
            Failed=1; return null;
        }

        // find id
        tokenIndex += 1;
        Token parameter = tokens.get(tokenIndex);
        if (parameter.getTokenType() == TokenType.ID_KEYWORD) {
            System.out.println("id found");

            if (IdBanList.contains(parameter.getToken())) {
                System.out.println("banned keyword");
                Failed=1; return null;
            }

            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), parameter));
        } else {
            System.out.println("id is missing");
            Failed=1; return null;
        }

        // find colon
        tokenIndex += 1;
        Token colon = tokens.get(tokenIndex);
        if (colon.getTokenType() == TokenType.COLON) {
            System.out.println("colon found");
            jottTreeNode.addChild(new JottTreeNode(colon));
        } else {
            System.out.println("colon is missing");
            Failed=1; return null;
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
                Failed=1; return null;
            }
        }

        // is this end (ex: ]) or look for more parameters?
        tokenIndex += 1;
        Token parameter2 = tokens.get(tokenIndex);
        if (parameter2.getTokenType() != TokenType.R_BRACKET) {
            // apparently there may be more parameters or something else
            jottTreeNode.addChild(function_def_params_t(new JottTreeNode(JottElement.FUNC_DEF_PARAMS_T)));
        }
        return jottTreeNode;
    }

    private static JottTreeNode body_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.BODY_STMT);

        // try if statement
        JottTreeNode ifStmtNode = if_stmt(new JottTreeNode(JottElement.IF_STMT));
        if (ifStmtNode != null) {
            jottTreeNode.addChild(ifStmtNode);
            return jottTreeNode;
        }

        System.out.println("if statement not found; trying while loop");

        // try while loop statement
        JottTreeNode whileLoopNode = while_loop(new JottTreeNode(JottElement.WHILE_LOOP));
        if (whileLoopNode != null) {
            jottTreeNode.addChild(whileLoopNode);
            return jottTreeNode;
        }

        System.out.println("while loop not found; trying stmt");

        // try statement statement
        JottTreeNode stmtNode = stmt(new JottTreeNode(JottElement.STMT));
        if (stmtNode != null) {
            jottTreeNode.addChild(stmtNode);
            return jottTreeNode;
        }

        System.out.println("this body_stmm is ultimately invalid");
        Failed=1; return null;
    }

    private static JottTreeNode return_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.RETURN_STMT);

        int originalTokenIndex = tokenIndex;

        // ensure return is there
        Token returnToken = tokens.get(tokenIndex);
        if (returnToken.getTokenType() == TokenType.ID_KEYWORD && returnToken.getToken().equals("return")) {
            System.out.println("return found");
            jottTreeNode.addChild(new JottTreeNode(returnToken));
        } else {
            System.out.println("return not found");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

        // look for expr
        tokenIndex += 1;
        System.out.println(tokens.get(tokenIndex).getToken());
        JottTreeNode exprNode = expr(new JottTreeNode(JottElement.EXPR));
        if (exprNode != null) {
            System.out.println("expr found");
            jottTreeNode.addChild(exprNode);
        } else {
            System.out.println("expr not found");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

        // look for ;

        Token endStmtToken = tokens.get(tokenIndex);
        if (endStmtToken.getTokenType() == TokenType.SEMICOLON) {
            System.out.println("; found");
            jottTreeNode.addChild(new JottTreeNode(endStmtToken));
        } else {
            System.out.println(String.format("; missing. found '%s' instead", endStmtToken.getToken()));
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

        return jottTreeNode;
    }

    private static JottTreeNode body(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.BODY);


        // look for body_stmt
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.R_BRACE) { // ε
            System.out.println("found empty body");
            return jottTreeNode;
        } else if (token.getTokenType() == TokenType.ID_KEYWORD && token.getToken().equals("return")) { // return_stmt
            System.out.println("found return clause");

            JottTreeNode returnStmtNode = return_stmt(new JottTreeNode(JottElement.RETURN_STMT));
            if (returnStmtNode == null) {
                System.out.println("return_stmt not found");
                Failed=1; return null;
            }

            tokenIndex += 1;
            return returnStmtNode;
        } else { // body_stmt body
            System.out.println("has something more in this body; need to parse further");

            // look for body_stmt
            JottTreeNode bodyStmtNode = body_stmt(new JottTreeNode(JottElement.BODY_STMT));
            if (bodyStmtNode != null) {
                jottTreeNode.addChild(bodyStmtNode);
            } else {
                System.out.println("could not find body_stmt");
                Failed=1; return null;
            }

            JottTreeNode bodyNode = body(new JottTreeNode(JottElement.BODY));
            if (bodyNode != null && bodyNode.getChildren().size() > 0) {
                System.out.println("found one more body!");
                jottTreeNode.addChild(bodyNode);
            } else {
                System.out.println("could not find more body");
            }

            return jottTreeNode;
        }
    }

    private static JottTreeNode if_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.IF_STMT);

        // find if
        Token ifToken = tokens.get(tokenIndex);
        System.out.println("ifToken: " + ifToken.getTokenType() + " : " + ifToken.getToken());
        if (ifToken.getTokenType() == TokenType.ID_KEYWORD && ifToken.getToken().equals("if"))
        {
            System.out.println("found if");
            jottTreeNode.addChild(new JottTreeNode(ifToken));
        } else {
            System.out.println("if not found");
            Failed=1; return null;
        }

        // look for left bracket [
        tokenIndex += 1;
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
        } else {
            System.out.println("[ is missing");
            Failed=1; return null;
        }

        // look for b_expr
        tokenIndex += 1;
        JottTreeNode bExprNode = b_expr(new JottTreeNode(JottElement.B_EXPR));
        if (bExprNode != null) {
            System.out.println("b_expr found");
            jottTreeNode.addChild(bExprNode);
        } else {
            System.out.println("missing b_expr");
            Failed=1; return null;
        }

        // look for right bracket ]
        Token rightBracketToken = tokens.get(tokenIndex);
        System.out.println("rightBracketToken: " + rightBracketToken.getToken());
        if (rightBracketToken.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ]");
            jottTreeNode.addChild(new JottTreeNode(rightBracketToken));
        } else {
            System.out.println("] missing");
            Failed=1; return null;
        }

        // look for left curly brace {
        tokenIndex += 1;
        Token leftCurlyBraceToken = tokens.get(tokenIndex);
        if (leftCurlyBraceToken.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found {");
            jottTreeNode.addChild(new JottTreeNode(leftCurlyBraceToken));
        } else {
            System.out.println("{ missing");
            Failed=1; return null;
        }


        tokenIndex += 1;

        // looking for body
        if (body(jottTreeNode)==null){

            Failed=1; return null;
        }

        // looking for }

        Token rightCurlyBraceToken = tokens.get(tokenIndex);
        if (rightCurlyBraceToken.getTokenType() == TokenType.R_BRACE) {
            System.out.println("found }--------------");
            jottTreeNode.addChild(new JottTreeNode(rightCurlyBraceToken));
        } else {
            System.out.println("} missing");
            Failed=1; return null;
        }

        // look for left curly brace elseif_lst
        tokenIndex += 1;
        Token next = tokens.get(tokenIndex);



        if(elseif_lst(jottTreeNode)!=null){
            tokenIndex-=1;
        }
//            jottTreeNode.addChild(new JottTreeNode(next));
//
//            tokenIndex+=1;
//            next = tokens.get(tokenIndex);
//            if (next.getToken().equals("{")) {
//                jottTreeNode.addChild(new JottTreeNode(next));
//                tokenIndex+=1;
//
//                System.out.println(tokens.get(tokenIndex).getToken()+"--------------34-234-32-423-4-23-4");
//
//                elseif_lst(jottTreeNode);
//
//                next = tokens.get(tokenIndex);
//                if ( next.getToken().equals("}")) {
//                    jottTreeNode.addChild(new JottTreeNode(next));
//                    tokenIndex+=1;
//
//                }else {
//                    System.out.println("missing }");
//                    Failed=1; return null;
//                }
//
//
//                }else {
//                System.out.println("missing{");
//                Failed=1; return null;
//            }



        next = tokens.get(tokenIndex);
        System.out.println(next.getToken()+"------312312312312312321312312312312");

        if (next.getToken().equals("else")) {
            System.out.println("else");

            jottTreeNode.addChild(new JottTreeNode(next));
            tokenIndex+=1;
            next = tokens.get(tokenIndex);
            // looking for { body }
            if (next.getToken().equals("{")){
                jottTreeNode.addChild(new JottTreeNode(next));

                tokenIndex+=1;

                if(body(jottTreeNode)!=null){
                    next = tokens.get(tokenIndex);


                    if (next.getToken().equals("}")) {
                        jottTreeNode.addChild(new JottTreeNode(next));
                        System.out.println(next.getToken()+"------fjlkshfdlskafjsdlkfjlkdsjf");
                        tokenIndex+=1;
                        return jottTreeNode;
                    }else {
                        System.out.println("missing }");
                    }
                }
                else {
                    System.out.println("body missing");
                }
            }else {
                System.out.println("missing {");
                Failed=1; return null;
            }

        } else {

            return jottTreeNode;
        }




        return jottTreeNode;
    }

    private static JottTreeNode elseif_lst(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.ELSEIF_LST);
        Token token = tokens.get(tokenIndex);

        // find elseif
        if (token.getToken().equals("elseif")) {
            System.out.println("found elseif for elseif");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex+=1;

        } else {
            System.out.println("ERROR missing elseif");
            Failed=1; return null;
        }

        // looking for [
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
            tokenIndex+=1;
        } else {
            System.out.println("[ is missing");
            Failed=1; return null;
        }


        if(b_expr(jottTreeNode)== null){
            System.out.println("b expr FAILED");
            Failed=1; return null;
        }
        System.out.println("b expr found");



        Token rightbracket = tokens.get(tokenIndex);
        if (rightbracket.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ]");
            jottTreeNode.addChild(new JottTreeNode(rightbracket));
            tokenIndex+=1;
        } else {
            System.out.println("] is missing");
            Failed=1; return null;
        }

        // looking for {
        Token leftcurly = tokens.get(tokenIndex);
        if (leftcurly.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found {");
            jottTreeNode.addChild(new JottTreeNode(leftcurly));
            tokenIndex+=1;

        } else {
            System.out.println("{ is missing");
            Failed=1; return null;
        }

        if(body(jottTreeNode)== null){
            System.out.println("BODY ERROR IN IFELSE_LST");
            Failed=1; return null;
        }



        // looking for }
        Token rightcurly = tokens.get(tokenIndex);
        if (rightcurly.getTokenType() == TokenType.R_BRACE) {
            System.out.println("found }");
            jottTreeNode.addChild(new JottTreeNode(rightcurly));
            tokenIndex+=1;

        } else {
            System.out.println("} is missing");
            Failed=1; return null;
        }

        if (elseif_lst(jottTreeNode)!=null) {
            System.out.println("found elseif_lst");
            tokenIndex+=1;

        }
        // epsilon
        return jottTreeNode;
    }

    private static JottTreeNode while_loop(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.WHILE_LOOP);

        Token whileToken = tokens.get(tokenIndex);
        if (whileToken.getTokenType() == TokenType.ID_KEYWORD && whileToken.getToken().equals("while"))
        {
            System.out.println("found while");
            jottTreeNode.addChild(new JottTreeNode(whileToken));
        } else {
            System.out.println("while not found");
            Failed=1; return null;
        }

        // look for left bracket
        tokenIndex += 1;
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
        } else {
            System.out.println("[ is missing");
            Failed=1; return null;
        }
        tokenIndex += 1;
        ////////////------ checking for bool expr

        if(b_expr(jottTreeNode)== null){
            System.out.println("b expr FAILED");
            Failed=1; return null;
        }
        System.out.println("b expr found");
        ////////////////////////////////

        Token rightbracket = tokens.get(tokenIndex);
        if (rightbracket.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ]");
            jottTreeNode.addChild(new JottTreeNode(rightbracket));
            tokenIndex+=1;
        } else {
            System.out.println("] is missing");
            Failed=1; return null;
        }


        // looking for {
        Token leftcurly = tokens.get(tokenIndex);
        if (leftcurly.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found {");
            jottTreeNode.addChild(new JottTreeNode(leftcurly));
            tokenIndex+=1;
        } else {
            System.out.println("{ is missing");
            Failed=1; return null;
        }

        if(body(jottTreeNode) == null){
            System.out.println("BODY ERROR IN WHILE LOOP");
            Failed=1; return null;
        }
        System.out.println("found body");
        // looking for }
//        tokenIndex -= 1;

        Token rightcurly = tokens.get(tokenIndex);
        if (rightcurly.getTokenType() == TokenType.R_BRACE) {
            System.out.println("found }");
            jottTreeNode.addChild(new JottTreeNode(rightcurly));


        } else {

            System.out.println("} is missing");
            Failed=1; return null;
        }

        return jottTreeNode;
    }

    private static JottTreeNode id(JottTreeNode jottTreeNode, Token token) {
        System.out.println(JottElement.ID);
        JottTreeNode child = new JottTreeNode(token);
        jottTreeNode.addChild(child);
        return jottTreeNode;
    }

    private static JottTreeNode stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.STMT);

        int originalTokenIndex = tokenIndex;

        System.out.println("stmt - trying asmt");
        JottTreeNode asmtNode = asmt(new JottTreeNode(JottElement.ASMT));
        if (asmtNode != null) {
            System.out.println("found asmt");
            jottTreeNode.addChild(asmtNode);

            Token endStmtNode = tokens.get(tokenIndex);
            System.out.println(endStmtNode.getTokenType());
            if (endStmtNode.getTokenType() == TokenType.SEMICOLON) {
                jottTreeNode.addChild(new JottTreeNode(endStmtNode));
                tokenIndex += 1;
                return jottTreeNode;
            } else {
                System.out.println("missing end_stmt");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }
        }

        System.out.println("stmt - asmt didn't work, trying var_dec");
        JottTreeNode varDecNode = var_dec(new JottTreeNode(JottElement.VAR_DEC));
        if (varDecNode != null) {
            System.out.println("found var_dec");
            jottTreeNode.addChild(varDecNode);

            tokenIndex += 1;
            Token endStmtNode = tokens.get(tokenIndex);
            if (endStmtNode.getTokenType() == TokenType.SEMICOLON) {
                jottTreeNode.addChild(new JottTreeNode(endStmtNode));
                tokenIndex += 1;
                return jottTreeNode;
            } else {
                System.out.println("missing end_stmt");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }
        }

        System.out.println("stmt - var_dec didn't work, trying func_call");
        JottTreeNode funcCallNode = func_call(new JottTreeNode(JottElement.FUNC_CALL));
        if (funcCallNode != null) {
            System.out.println("stmt - found func_call");
            jottTreeNode.addChild(funcCallNode);

            tokenIndex += 1;
            Token endStmtNode = tokens.get(tokenIndex);
            if (endStmtNode.getTokenType() == TokenType.SEMICOLON) {
                jottTreeNode.addChild(new JottTreeNode(endStmtNode));
                tokenIndex += 1;
                return jottTreeNode;
            } else {
                System.out.println("missing end_stmt");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }
        }

        System.out.println("stmt - func_call didn't work. ran out of options. error!");
        Failed=1; return null;
    }

    private static JottTreeNode func_call(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNC_CALL);
        int originalTokenIndex = tokenIndex;

        Token idToken = tokens.get(tokenIndex);




        // find id
        if (idToken.getTokenType() == TokenType.ID_KEYWORD) {
            System.out.println(String.format("found id for func_call (%s)", idToken.getToken()));

            if (IdBanList.contains(idToken.getToken())) {
                System.out.println("banned keyword");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }

            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), idToken));
        } else {
            System.out.println("id missing for func_call");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

        // find [
        tokenIndex += 1;
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [ for func_call");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
        } else {
            System.out.println("[ missing for func_call");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }



        // find parameters
        tokenIndex += 1;

        Token tempToken = tokens.get(tokenIndex);
        if (tempToken.getTokenType() == TokenType.R_BRACKET) {
            jottTreeNode.addChild(new JottTreeNode(JottElement.PARAMS));
            jottTreeNode.addChild(new JottTreeNode(tempToken));
            return jottTreeNode;
        }

        JottTreeNode paramsNode = params(new JottTreeNode(JottElement.PARAMS));
        if (paramsNode != null) {
            System.out.println("found parameter(s)");
            jottTreeNode.addChild(paramsNode);
        } else {
            System.out.println("missing parameter(s)");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

        // finding "]"
        Token rightBracketToken = tokens.get(tokenIndex);
        if (rightBracketToken.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ] for func_call");
            jottTreeNode.addChild(new JottTreeNode(rightBracketToken));
        } else {
            System.out.println("] missing for func_call");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }
        //success
        return jottTreeNode;
    }

    private static JottTreeNode params(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PARAMS);

        int originalTokenIndex = tokenIndex;
        // look for expr
        JottTreeNode exprNode = expr(new JottTreeNode(JottElement.EXPR));
        if (exprNode != null) {
            System.out.println("an expression found!");
            jottTreeNode.addChild(exprNode);
        } else {
            System.out.println("an expression not found!");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

        // look for params_t
        JottTreeNode paramsTNode = params_t(new JottTreeNode(JottElement.PARAMS_T));
        if (paramsTNode != null) {
            System.out.println("found paramsT");
            jottTreeNode.addChild(paramsTNode);
        } else {
            System.out.println("no more params");
        }

        return jottTreeNode;
    }

    private static JottTreeNode params_t(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PARAMS_T);

        int originalTokenIndex = tokenIndex;

        // find comma
        Token commaToken = tokens.get(tokenIndex);
        if (commaToken.getTokenType() == TokenType.COMMA) {
            System.out.println("params_t - found more parameter");

            jottTreeNode.addChild(new JottTreeNode(commaToken));

            // find expression
            tokenIndex += 1;
            JottTreeNode exprNode = expr(new JottTreeNode(JottElement.EXPR));
            if (exprNode != null) {
                System.out.println("an expression found!");
                jottTreeNode.addChild(exprNode);
            } else {
                System.out.println("an expression not found!");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }

            // find more params_t
            JottTreeNode paramsTNode = params_t(new JottTreeNode(JottElement.PARAMS_T));
            if (paramsTNode != null) {
                System.out.println("found paramsT");
                jottTreeNode.addChild(paramsTNode);
            } else {
                System.out.println("no more params");
            }

            return jottTreeNode;

        } else {
            System.out.println("params_t - no more parameter");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }
    }

    private static JottTreeNode expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.EXPR);


        // i_expr|d_expr|s_expr|b_expr|id|func_call


        System.out.println("expr - trying i_expr");


        JottTreeNode iExprNode = i_expr(new JottTreeNode(JottElement.I_EXPR));
        if (iExprNode != null) {
            System.out.println("found i_expr");
            jottTreeNode.addChild(iExprNode);
            return jottTreeNode;
        }

        System.out.println("expr - i_expr didn't work, trying d_expr");

        JottTreeNode dExprNode = d_expr(new JottTreeNode(JottElement.D_EXPR));
        if (dExprNode != null) {
            System.out.println("found d_expr");
            jottTreeNode.addChild(dExprNode);
            return jottTreeNode;
        }

        System.out.println("expr - d_expr didn't work, trying s_expr");

        JottTreeNode sExprNode = s_expr(new JottTreeNode(JottElement.S_EXPR));
        if (sExprNode != null) {
            System.out.println("found s_expr");
            jottTreeNode.addChild(sExprNode);
            return jottTreeNode;
        }

        System.out.println("expr - s_expr didn't work, trying b_expr");

        JottTreeNode bExprNode = b_expr(new JottTreeNode(JottElement.B_EXPR));
        if (bExprNode != null) {
            System.out.println("found b_expr");
            jottTreeNode.addChild(bExprNode);
            return jottTreeNode;
        }

        System.out.println("expr - b_expr didn't work, trying id keyword");

        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.ID_KEYWORD) {
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), token));

            if (IdBanList.contains(token.getToken())) {
                System.out.println("banned keyword");
                Failed=1; return null;
            }

            return jottTreeNode;
        }

        System.out.println("expr - trying func_call");
        JottTreeNode funcCall = func_call(new JottTreeNode(JottElement.FUNC_CALL));
        if (funcCall != null) {
            System.out.println("expr - found func_call");
            jottTreeNode.addChild(funcCall);
            tokenIndex+=1;
            return jottTreeNode;
        }

        System.out.println("expr - all options don't work, no idea what to do now");
        Failed=1; return null;
    }

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
                Failed=1; return null;
            }
        }
    }

    private static JottTreeNode var_dec(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.VAR_DEC);

        Token typeToken = tokens.get(tokenIndex);
        System.out.println("VAR DEC");
        System.out.println(typeToken.getToken());
        System.out.println(typeToken.getTokenType());

        int originalTokenIndex = tokenIndex;
        // look for type
        if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {

            // find var type
            if (typeToken.getToken().equals("Double") ||
                    typeToken.getToken().equals("Integer") ||
                    typeToken.getToken().equals("String") ||
                    typeToken.getToken().equals("Boolean")) {
                jottTreeNode.addChild(new JottTreeNode(typeToken));
//                System.out.println(String.format("var_dec - type exists (%s)", typeToken.getToken()));
            } else {
//                System.out.println("var_dec - missing type");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }

            // find var id
            tokenIndex += 1;
            Token idToken = tokens.get(tokenIndex);
            if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {
//                System.out.println(String.format("var_dec - found var id (%s)", idToken.getToken()));

                if (IdBanList.contains(idToken.getToken())) {
//                    System.out.println("banned keyword");
                    tokenIndex = originalTokenIndex;
                    Failed=1; return null;
                }

                jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), idToken));
                return jottTreeNode;
            } else {
//                System.out.println("var_dec - missing var id");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }
        } else {
//            System.out.println("not var dec");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }
    }

    private static JottTreeNode asmt(JottTreeNode jottTreeNode) {


        int originalTokenIndex = tokenIndex;

        /**
         * asmt ->        Double id = d_expr end_stmt                                                                       DONE
         *               |Integer id = i_expr end_stmt
         *               |String id = s_expr end_stmt`
         *               |Boolean id = b_expr end_stmt
         *               |id = d_expr end_stmt
         *               |id = i_expr end_stmt
         *               |id = s_expr end_stmt
         *               |id = b_expr end_stmt
         **/

        Token typeToken = tokens.get(tokenIndex);
//        System.out.println(typeToken.getToken());
//        System.out.println(typeToken.getTokenType());

        if (typeToken.getTokenType() == TokenType.ID_KEYWORD) {
//            System.out.println("found id");

            // look for var type
            if (typeToken.getToken().equals("Double") ||
                    typeToken.getToken().equals("Integer") ||
                    typeToken.getToken().equals("String") ||
                    typeToken.getToken().equals("Boolean")) {
                jottTreeNode.addChild(new JottTreeNode(typeToken));
//                System.out.println(String.format("Type exists (%s)", typeToken.getToken()));
                tokenIndex += 1;
            } else {
//                System.out.println("didn't find type");
            }

            // look for var name
            Token varIdToken = tokens.get(tokenIndex);
            if (varIdToken.getTokenType() == TokenType.ID_KEYWORD) {
//                System.out.println(String.format("found var id (%s)", varIdToken.getToken()));
                jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), varIdToken));
            } else {
//                System.out.println("did not find var id");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }

            // look for equal symbol
            tokenIndex += 1;
            Token equalSymbol = tokens.get(tokenIndex);
            if (equalSymbol.getTokenType() == TokenType.ASSIGN) {
//                System.out.println("assign symbol found");
                jottTreeNode.addChild(new JottTreeNode(equalSymbol));
            } else {
//                System.out.println("assign symbol not found");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }

//            System.out.println("looking for an expression; starting with i_expr");
            // look for expression
            tokenIndex += 1;
            JottTreeNode iExprNode = i_expr(new JottTreeNode(JottElement.I_EXPR));
            if (iExprNode != null) {
//                System.out.println("found i_expr");
                jottTreeNode.addChild(iExprNode);
                return jottTreeNode;
            }

//            System.out.println("i_expr failed; looking for d_expr");
            JottTreeNode dExprNode = d_expr(new JottTreeNode(JottElement.D_EXPR));
            if (dExprNode != null) {
//                System.out.println("found d_expr");
                jottTreeNode.addChild(dExprNode);
                return jottTreeNode;
            }

//            System.out.println("d_expr failed; looking for s_expr");
            JottTreeNode sExprNode = s_expr(new JottTreeNode(JottElement.S_EXPR));
            if (sExprNode != null) {
//                System.out.println("found s_expr");
                jottTreeNode.addChild(sExprNode);
                return jottTreeNode;
            }

//            System.out.println("s_expr failed; looking for b_expr");
            JottTreeNode bExprNode = b_expr(new JottTreeNode(JottElement.B_EXPR));
            if (bExprNode != null) {
//                System.out.println("found b_expr");
                jottTreeNode.addChild(bExprNode);
                return jottTreeNode;
            }

//            System.out.println("ran out of options; no expressions found");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;

        } else {
//            System.out.println("not asmt");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }

    }

    // will need to ensure this works
    private static JottTreeNode op(JottTreeNode jottTreeNode) {
//        System.out.println(JottElement.OP);

        Token token = tokens.get(tokenIndex);

        if (token.getTokenType() == TokenType.MATH_OP){
//            System.out.println("op exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        }
//        System.out.println("missing op");
        Failed=1; return null;
    }

    // will need to ensure this works
    private static JottTreeNode rel_op(JottTreeNode jottTreeNode) {
//        System.out.println(JottElement.REL_OP);

        Token token = tokens.get(tokenIndex);

        if (token.getTokenType() == TokenType.REL_OP) {
//            System.out.println("REL_OP exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        }
//        System.out.println("missing REL_OP");
        Failed=1; return null;
    }

    private static JottTreeNode d_expr(JottTreeNode jottTreeNode) {
        // not needed in phrase 2, will implement in phrase 3
        Failed=1; return null;
    }

    private static JottTreeNode b_expr(JottTreeNode jottTreeNode) {
        Token token = tokens.get(tokenIndex);

        int originalTokenIndex = tokenIndex;

        // is this an id
//        System.out.println(token.getToken()+"---------------------------------");
        if (token.getTokenType() == TokenType.ID_KEYWORD || token.getToken().equals("True") || token.getToken().equals("False")){
//            System.out.println("ID exists");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), token));
            tokenIndex += 1;
            return jottTreeNode;

        }else {


            if (i_expr(jottTreeNode) != null) {
                tokenIndex += 1;
                if (rel_op(jottTreeNode) != null && i_expr(jottTreeNode) != null) {
                        // tokenIndex += 1;          <----------------------------------???????????
                        return jottTreeNode;
                }
//                System.out.println("i_expr relOP i_expr Failure");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }else{


            if (d_expr(jottTreeNode) != null) {
                tokenIndex += 1;
                if (rel_op(jottTreeNode) != null && d_expr(jottTreeNode) != null) {
                    // tokenIndex += 1;          <----------------------------------???????????
                    return jottTreeNode;
                }
//                System.out.println("d_expr relOP d_expr Failure");
                Failed=1; return null;
            }else if (s_expr(jottTreeNode) != null) {
                tokenIndex += 1;
                if (rel_op(jottTreeNode) != null && s_expr(jottTreeNode) != null) {
                    // tokenIndex += 1;          <----------------------------------???????????
                    return jottTreeNode;
                }
//                System.out.println("s_expr relOP s_expr Failure");
                Failed=1; return null;
            }else if (b_expr(jottTreeNode) != null) {
                    tokenIndex += 1;
                    if (rel_op(jottTreeNode) != null && b_expr(jottTreeNode) != null) {
                        // tokenIndex += 1;          <----------------------------------???????????
                        return jottTreeNode;
                    } else {
//                        System.out.println("b_expr relOP b_expr Failure");
                        tokenIndex = originalTokenIndex;
                        Failed=1; return null;
                    }

                }else if(func_call(jottTreeNode)!=null){
                        // tokenIndex += 1;          <----------------------------------???????????
                        return jottTreeNode;
                    }
                    else{
//                        System.out.println("function call Failure");
                        tokenIndex = originalTokenIndex;
                        Failed=1; return null;
                    }
        }}
    }

    private static JottTreeNode i_expr(JottTreeNode jottTreeNode) {
//        System.out.println(JottElement.I_EXPR);


        int originalTokenIndex = tokenIndex;

        /** id|
         * int|
         * int op int|
         * int op i_expr|
         * i_expr op int|
         * i_expr op i_expr|
         * func_call **/

        Token firstToken = tokens.get(tokenIndex);
//        System.out.println(firstToken.getToken());
//        System.out.println(firstToken.getTokenType());

        if (firstToken.getToken().equals("-") || firstToken.getToken().equals("+")) { // [+/- int] +30, -30, -50, +5, etc
            tokenIndex += 1;
            Token secondToken = tokens.get(tokenIndex);
            if (secondToken.getTokenType() == TokenType.NUMBER) {
//                System.out.println("[+/- int] found");
                JottTreeNode intNode = new JottTreeNode(JottElement.INT);
                intNode.addChild(new JottTreeNode(new Token(String.format(firstToken.getToken().equals("-") ? "-%s" : "%s", secondToken.getToken()), secondToken.getFilename(), secondToken.getLineNum(), secondToken.getTokenType())));
                jottTreeNode.addChild(intNode);
            } else {
//                System.out.println("[+/- int] not formatted correctly");
                tokenIndex = originalTokenIndex;
                Failed=1; return null;
            }
        } else if (firstToken.getTokenType() == TokenType.NUMBER) { // [int] 30, 50, 42, etc
//            System.out.println("[int] found");
            JottTreeNode intNode = new JottTreeNode(JottElement.INT);
            intNode.addChild(new JottTreeNode(firstToken));
            jottTreeNode.addChild(intNode);
        } else if (firstToken.getTokenType() == TokenType.ID_KEYWORD) {
//            System.out.println("[id] found");

            JottTreeNode funcCallNode = func_call(new JottTreeNode(JottElement.FUNC_CALL));
            if (funcCallNode != null) {
//                System.out.println("found a function call");
                jottTreeNode.addChild(funcCallNode);
            } else {
//                System.out.println("not a function call");
                jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), firstToken));
            }
        } else {
//            System.out.println("no idea what this is");
            tokenIndex = originalTokenIndex;
            Failed=1; return null;
        }


        tokenIndex += 1;
        Token opToken = tokens.get(tokenIndex);
        if (opToken.getTokenType() == TokenType.MATH_OP) {
//            System.out.println(String.format("found op (%s)", opToken.getToken()));
            jottTreeNode.addChild(new JottTreeNode(opToken));

            tokenIndex += 1;
            JottTreeNode recursiveIExprNode = i_expr(new JottTreeNode(JottElement.I_EXPR));
            jottTreeNode.addChild(recursiveIExprNode);
            return jottTreeNode;
        } else {
            // no op here
//            System.out.println("no op here");
            return jottTreeNode;
        }

//        tokenIndex += 1;
//        Token opToken = tokens.get(tokenIndex);
//        if (opToken.getTokenType() == TokenType.MATH_OP) {
//            System.out.println("found op");
//            jottTreeNode.addChild(new JottTreeNode(opToken));
//
//            tokenIndex += 1;
//            JottTreeNode iExprNode = i_expr(new JottTreeNode(JottElement.I_EXPR));
//            if (iExprNode != null) {
//                jottTreeNode.addChild(iExprNode);
//                tokenIndex += 1;
//            } else {
//                tokenIndex = originalTokenIndex;
//                Failed=1; return null;
//            }
//        } else {
//            // no op here
//            System.out.println("no op here");
//            tokenIndex = originalTokenIndex + 1;
//            return jottTreeNode;
//        }

    }

    private static JottTreeNode str_literal(JottTreeNode jottTreeNode) {
//        System.out.println(JottElement.STR_LITERAL);
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.STRING) {
//            System.out.println("String lit exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        } else {
//            System.out.println("missing String lit");
            Failed=1; return null;
        }
    }

    private static JottTreeNode s_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.S_EXPR);

        int originalTokenIndex = tokenIndex;

        // checking for string
        JottTreeNode strLiteralNode = str_literal(new JottTreeNode(JottElement.STR_LITERAL));
        if (strLiteralNode == null) {
            tokenIndex += 1;
            // look for func_call
            JottTreeNode funcCallNode = func_call(new JottTreeNode(JottElement.FUNC_CALL));
            if (funcCallNode != null) {
//                System.out.println("s_expr - found func_call");
                jottTreeNode.addChild(funcCallNode);
                return funcCallNode;
            }

            Token parameter = tokens.get(tokenIndex);
            if (parameter.getTokenType() == TokenType.ID_KEYWORD) {
//                System.out.println("id found");
                jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), parameter));
                return jottTreeNode;
            }

//            System.out.println("could not find anything");
            tokenIndex = originalTokenIndex;

            Failed=1; return null;
        } else {
//            System.out.println("found str_literal");
            jottTreeNode.addChild(strLiteralNode);
            return jottTreeNode;
        }
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

//
//        System.out.println("******************\nPRINTING THE PARSE TREE OUT");
//        printTree(jottParser.tree);
        if(jottParser.Failed == 1){
            Failed=1; return null;
        }
        return jottParser;
    }


    private static String jottstr = "";

    private String convertToJottRecursive(JottTreeNode curr) {
        if(curr != null){
            if (curr.isTerminal()) {
                jottstr += curr.getToken().getToken() + " ";
            }
            if (curr.getJottElement() == JottElement.VOID) {
                jottstr += "Void ";
            }

            for (JottTreeNode child : curr.getChildren()) {
                convertToJottRecursive(child);
            }
        }

        return jottstr;
    }
    @Override
    public String convertToJott() {
        return convertToJottRecursive(this.tree);
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
        Failed=1; return null;
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
        JottTree T = parse(testResults);

        if (Failed == 0) {
            System.out.println("\n\nCONVERT BACK INTO JOTT AGAIN USING THE PARSE TREE:");
            System.out.println(T.convertToJott());
        }

//        for (Token t:testResults) {
//            System.out.println(t.getToken()+"\t\t<- "+t.getTokenType());
//        }
    }
}
