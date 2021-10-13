/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import java.util.ArrayList;

public class JottParser implements JottTree {
    public JottTreeNode tree;
    private static int tokenIndex;
    private static ArrayList<Token> tokens;

    /**
     * program -> function_list $$                                                              <-- DONE
     * function_list -> function_def function_list                                              <-- MOSTLY DONE, NEED TO ADD SUPPORT FOR MULTIPLE FUNCTIONS
     * function_list -> ε                                                                       <-- DONE
     * function_def -> id [ func_def_params ] : function_return { body }                        <-- DONE
     * func_def_params -> id : type func_def_params_t|ε                                         <-- DONE
     * func_def_params_t -> , id : type func_def_params_t|ε                                     <-- DONE
     * body_stmt -> if_stmt|while_loop|stmt                                                     <-- DONE
     * return_stmt -> return expr end_stmt                                                      <-- MOSTLY DONE, need to implement expr and end_stmt
     * body -> body_stmt body|return_stmt|ε                                                                             <-- DONE (i think)
     * if_stmt -> if [ b_expr ] { body } elseif_lst|if [ b_expr ] { body } elseif_lst else { body }                     TODO
     * elseif_lst -> elseif [ b_expr ] { body } elseif_lst|ε                                                            < --- nearly DONE TODO
     * while_loop -> while [ b_expr ] { body }                                                                          <--- nearly DONE TODO
     * id -> l_char char                                                                                                <-- DONE
     * stmt -> asmt|var_dec|func_call end_stmt                                                                          <-- done
     * func_call -> id [ params ]                                                                                       <-- DONE
     * params -> expr params_t|ε                                                                                        <-- Done
     * params_t -> , expr params_t|ε                                                                                    <-- WORK IN PROGRESS
     * expr -> i_expr|d_expr|s_expr|b_expr|id|func_call                                                                 <-- Done
     * type -> Double|Integer|String|Boolean                                                                            <-- DONE
     * function_return -> type|Void                                                                                     <-- DONE
     * var_dec -> type id end_stmt                                                                                      DONE
     * asmt ->        Double id = d_expr end_stmt                                                                       TODO
     *               |Integer id = i_expr end_stmt
     *               |String id = s_expr end_stmt
     *               |Boolean id = b_expr end_stmt
     *               |id = d_expr end_stmt
     *               |id = i_expr end_stmt
     *               |id = s_expr end_stmt
     *               |id = b_expr end_stmt
     * op -> +|*|/|+|-                                                                                                  <-- DONE
     * rel_op -> >|>=|<|<=|==|!=                                                                                        <-- DONE
     * d_expr -> id|dbl|dbl op dbl|dbl op d_expr|d_expr op dbl|d_expr op d_expr|func_call                               <-- DONE (ERRORS: FUNCTION CALL??)
     * bool -> True|False                                                                                               <-- DONE
     * b_expr -> id|bool|i_expr rel_op i_expr|d_expr rel_op d_expr|s_expr rel_op s_expr|b_expr rel_op b_expr|func_call  <-- done (ERRORS: FUNCTION CALL?? / )
     * i_expr -> id|int|int op int|int op i_expr|i_expr op int|i_expr op i_expr|func_call                               <-- DONE (ERRORS: FUNCTION CALL??)
     * str_literal -> " str "                                                                                           <-- DONE
     * s_expr -> str_literal|id|func_call                                                                               <-- DONE but should be tested
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

        JottTreeNode child1 = new JottTreeNode(JottElement.FUNCTION_DEF);
        JottTreeNode child2 = new JottTreeNode(JottElement.FUNCTION_LIST);

        if (tokenIndex >= tokens.size()) {
            return new JottTreeNode(JottElement.FUNCTION_LIST);
        }

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

        // look for left curly brace
        tokenIndex += 1;
        Token leftCurlyBrace = tokens.get(tokenIndex);
        if (leftCurlyBrace.getTokenType() == TokenType.L_BRACE) {
            System.out.println("{ found");
            jottTreeNode.addChild(new JottTreeNode(leftCurlyBrace));
        } else {
            System.out.println("{ missing");
            return null;
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
            return null;
        }

        // look for right curly brace
        System.out.println("children" + bodyNode.getChildren().size());

        if (bodyNode.getChildren().size() > 0) { tokenIndex += 1; }
        Token rightCurlyBrace = tokens.get(tokenIndex); // TODO: may will not need this
        if (rightCurlyBrace.getTokenType() == TokenType.R_BRACE) {
            jottTreeNode.addChild(new JottTreeNode(rightCurlyBrace));
        } else {
            System.out.println("} missing");
        }

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
            return stmtNode;
        }

        System.out.println("this body_stmm is ultimately invalid");
        return null;
    }

    private static JottTreeNode return_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.RETURN_STMT);

        // ensure return is there
        Token returnToken = tokens.get(tokenIndex);
        if (returnToken.getTokenType() == TokenType.ID_KEYWORD && returnToken.getToken().equals("return")) {
            System.out.println("return found");
            jottTreeNode.addChild(new JottTreeNode(returnToken));
        } else {
            System.out.println("return not found");
            return null;
        }

        // look for expr
        tokenIndex += 1;
        JottTreeNode exprNode = expr(new JottTreeNode(JottElement.EXPR));
        if (exprNode != null) {
            System.out.println("expr found");
            jottTreeNode.addChild(exprNode);
        } else {
            System.out.println("expr not found");
            return null;
        }

        // look for ;
        Token endStmtToken = tokens.get(tokenIndex);
        if (endStmtToken.getTokenType() == TokenType.SEMICOLON) {
            System.out.println("; found");
            jottTreeNode.addChild(new JottTreeNode(endStmtToken));
        } else {
            System.out.println("; missing");
            return null;
        }

        return jottTreeNode;
    }

    private static JottTreeNode body(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.BODY);

        JottTreeNode bodyNode = jottTreeNode;

        // look for body_stmt
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.R_BRACE) {
            System.out.println("found empty body");
            return bodyNode;
        } else if (token.getTokenType() == TokenType.ID_KEYWORD && token.getToken().equals("return")) {
            System.out.println("found return clause");
            JottTreeNode returnStmtNode = return_stmt(new JottTreeNode(JottElement.RETURN_STMT));
            if (returnStmtNode == null) {
                System.out.println("return_stmt not found");
                return null;
            }
            return returnStmtNode;
        } else {
            System.out.println("has something more in this body; need to parse further");
            JottTreeNode bodyStmtNode = new JottTreeNode(JottElement.BODY_STMT);
            body_stmt(bodyStmtNode);
            return bodyStmtNode;
        }
    }

    private static JottTreeNode if_stmt(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.IF_STMT);

        Token ifToken = tokens.get(tokenIndex);
        System.out.println("ifToken: " + ifToken.getTokenType() + " : " + ifToken.getToken());
        if (ifToken.getTokenType() == TokenType.ID_KEYWORD && ifToken.getToken().equals("if"))
        {
            System.out.println("found if");
            jottTreeNode.addChild(new JottTreeNode(ifToken));
        } else {
            System.out.println("if not found");
            return null;
        }

        // look for left bracket
        tokenIndex += 1;
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
        } else {
            System.out.println("[ is missing");
            return null;
        }

        // look for b_expr
        tokenIndex += 1;
        JottTreeNode bExprNode = b_expr(new JottTreeNode(JottElement.B_EXPR));
        if (bExprNode != null) {
            System.out.println("b_expr found");
            jottTreeNode.addChild(bExprNode);
        } else {
            System.out.println("missing b_expr");
            return null;
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
            return null;
        }


        // find elseif
        if (token.getToken().equals("elseif")) {
            System.out.println("found elseif for elseif");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex+=1;
        } else {
            System.out.println("ERROR missing elseif");
            return null;
        }

        // looking for [
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
            tokenIndex+=1;

        } else {
            System.out.println("[ is missing");
            return null;
        }

        //TODO ------------------------------------------------------------------------------b_expr is not null


        Token rightbracket = tokens.get(tokenIndex);
        if (rightbracket.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ]");
            jottTreeNode.addChild(new JottTreeNode(rightbracket));
            tokenIndex+=1;
        } else {
            System.out.println("] is missing");
            return null;
        }

        // looking for {
        Token leftcurly = tokens.get(tokenIndex);
        if (leftcurly.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found {");
            jottTreeNode.addChild(new JottTreeNode(leftcurly));
            tokenIndex+=1;

        } else {
            System.out.println("{ is missing");
            return null;
        }

        //TODO ------------------------------------------------------------------------------body is not null

        // looking for }
        Token rightcurly = tokens.get(tokenIndex);
        if (rightcurly.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found }");
            jottTreeNode.addChild(new JottTreeNode(rightcurly));
            tokenIndex+=1;

        } else {
            System.out.println("} is missing");
            return null;
        }


        if (elseif_lst(jottTreeNode)!=null) {
            System.out.println("found }");
            tokenIndex+=1;

        } else {
            System.out.println("} is missing");
            return null;
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
            return null;
        }

        // look for left bracket
        tokenIndex += 1;
        Token leftBracketToken = tokens.get(tokenIndex);
        if (leftBracketToken.getTokenType() == TokenType.L_BRACKET) {
            System.out.println("found [");
            jottTreeNode.addChild(new JottTreeNode(leftBracketToken));
        } else {
            System.out.println("[ is missing");
            return null;
        }

        //TODO --------------------------------------------------------------------------------------b_expr is not null

        Token rightbracket = tokens.get(tokenIndex);
        if (rightbracket.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ]");
            jottTreeNode.addChild(new JottTreeNode(rightbracket));
            tokenIndex+=1;
        } else {
            System.out.println("] is missing");
            return null;
        }


        // looking for {
        Token leftcurly = tokens.get(tokenIndex);
        if (leftcurly.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found {");
            jottTreeNode.addChild(new JottTreeNode(leftcurly));
            tokenIndex+=1;

        } else {
            System.out.println("{ is missing");
            return null;
        }

        //TODO ------------------------------------------------------------------------------body is not null

        // looking for }
        Token rightcurly = tokens.get(tokenIndex);
        if (rightcurly.getTokenType() == TokenType.L_BRACE) {
            System.out.println("found }");
            jottTreeNode.addChild(new JottTreeNode(rightcurly));
            tokenIndex+=1;

        } else {
            System.out.println("} is missing");
            return null;
        }

//        // look for b_expr
//        JottTreeNode bExprNode = b_expr(jottTreeNode);
//        if (bExprNode != null) {
//            System.out.println("found b_expr for while");
//            jottTreeNode.addChild(new JottTreeNode()bExprNode);
//        } else {
//            System.out.println("invalid b_expr");
//            return null;
//        }

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

        int start_idx = tokenIndex;
        if(asmt(jottTreeNode)!=null){
            return jottTreeNode;
        }
        tokenIndex = start_idx;
        if(var_dec(jottTreeNode) != null){
            return jottTreeNode;
        }

        tokenIndex = start_idx;
        if(func_call(jottTreeNode) != null) {

            tokenIndex += 1;                  ///------- do we need ?
            Token token = tokens.get(tokenIndex);

            if (token.getToken().equals(";")) {
                // add id child
                System.out.println("; exists");
                jottTreeNode.addChild(new JottTreeNode(token));
                return jottTreeNode;
            }else {
                System.out.println("FAILURE to find ; in stmt");
            }
        }
        System.out.println("Failure in statment");

        return null;
    }

    private static JottTreeNode func_call(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.FUNC_CALL);
        int originalTokenIndex = tokenIndex;

        Token idToken = tokens.get(tokenIndex);

        // find id
        if (idToken.getTokenType() == TokenType.ID_KEYWORD) {
            System.out.println("found id for func_call");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), idToken));
        } else {
            System.out.println("id missing for func_call");
            tokenIndex = originalTokenIndex;
            return null;
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
            return null;
        }


        // find parameters
        tokenIndex += 1;
        Token paramToken = tokens.get(tokenIndex);
        JottTreeNode paramsNode = params(new JottTreeNode(JottElement.PARAMS));
        if (paramsNode != null) {
            System.out.println("found parameter(s)");
        } else {
            System.out.println("missing parameter(s)");
            tokenIndex = originalTokenIndex;
            return null;
        }

        // finding "]"
        tokenIndex += 1;
        Token rightBracketToken = tokens.get(tokenIndex);
        if (rightBracketToken.getTokenType() == TokenType.R_BRACKET) {
            System.out.println("found ] for func_call");
            jottTreeNode.addChild(new JottTreeNode(rightBracketToken));
        } else {
            System.out.println("[ missing for func_call");
            tokenIndex = originalTokenIndex;
            return null;
        }
        //success
        return jottTreeNode;
    }

    private static JottTreeNode params(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PARAMS);

        // look for expr
        Token token = tokens.get(tokenIndex);
        JottTreeNode exprNode = expr(new JottTreeNode(JottElement.EXPR));
        if (exprNode != null) {
            System.out.println("an expression found!");
            jottTreeNode.addChild(exprNode);

            //  tokenIDX +=1????????????
            if(params_t(jottTreeNode)!=null){
                tokenIndex+=1;  // ????????
                return jottTreeNode;
            }else {
                System.out.println("Error finding params_t in params");
                return null;
            }
        }
        // epsilon or doesnt matter if we dont have anything
        System.out.println("epsilon");
        return jottTreeNode;
    }

    private static JottTreeNode params_t(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.PARAMS_T);

        // expr params_t
        // ε
        //TODO ------------------------------------------------------------------------------ TODO

        return null;
    }

    private static JottTreeNode expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.EXPR);

        // i_expr|d_expr|s_expr|b_expr|id|func_call

        JottTreeNode funcCall = func_call(new JottTreeNode(JottElement.FUNC_CALL));
        if (funcCall != null) {
            System.out.println("found func_call");
            jottTreeNode.addChild(funcCall);
            return jottTreeNode;
        }

        System.out.println("can't be func_call, trying i_expr");

        JottTreeNode iExprNode = i_expr(new JottTreeNode(JottElement.I_EXPR));
        if (iExprNode != null) {
            System.out.println("found i_expr");
            jottTreeNode.addChild(iExprNode);
            return jottTreeNode;
        }

        JottTreeNode dExprNode = d_expr(new JottTreeNode(JottElement.D_EXPR));
        if (dExprNode != null) {
            System.out.println("found d_expr");
            jottTreeNode.addChild(dExprNode);
            return jottTreeNode;
        }

        JottTreeNode sExprNode = s_expr(new JottTreeNode(JottElement.S_EXPR));
        if (sExprNode != null) {
            System.out.println("found s_expr");
            jottTreeNode.addChild(sExprNode);
            return jottTreeNode;
        }

        JottTreeNode bExprNode = b_expr(new JottTreeNode(JottElement.B_EXPR));
        if (bExprNode != null) {
            System.out.println("found b_expr");
            jottTreeNode.addChild(bExprNode);
            return jottTreeNode;
        }

        // TODO: as a last resort, is this an ID?

        return null;
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
                return null;
            }
        }
    }

    private static JottTreeNode var_dec(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.VAR_DEC);

        // look for type
        if (type(jottTreeNode)==null) {
            return null;
        }

        Token token = tokens.get(tokenIndex);

        if (token.getTokenType() == TokenType.ID_KEYWORD) {
            // add id child
            System.out.println("id exists");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), token));
            tokenIndex+=1;

        } else {
            System.out.println("missing id");
            return null;
        }

         token = tokens.get(tokenIndex);
        if (token.getToken().equals(";")) {
            // add id child
            System.out.println("; exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex+=1;

        } else {
            System.out.println("missing ;");
            return null;
        }

        return jottTreeNode;
    }

    /*
     * asmt ->        Double id = d_expr end_stmt                                                                       TODO
     *               |Integer id = i_expr end_stmt
     *               |String id = s_expr end_stmt
     *               |Boolean id = b_expr end_stmt
     *               |id = d_expr end_stmt
     *               |id = i_expr end_stmt
     *               |id = s_expr end_stmt
     *               |id = b_expr end_stmt
            */
    private static JottTreeNode asmt(JottTreeNode jottTreeNode) {
        Token token;
        System.out.println(JottElement.ASMT);
        // we have a type
        if(type(jottTreeNode)!=null){
            token = tokens.get(tokenIndex);

            // looking for id
            if (token.getTokenType() == TokenType.ID_KEYWORD){
                System.out.println("found id for asmt");
                jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), token));
                tokenIndex+=1;
                token = tokens.get(tokenIndex);

                // looking for =
                if (token.getToken().equals("=") ){
                    System.out.println("found = in type id = IN func asmt");
                    int tokenstart = tokenIndex;

                    // looking for d_expr
                    if (d_expr(jottTreeNode) != null){
                        token = tokens.get(tokenIndex);
                        if(token.getToken().equals(";")){
                            jottTreeNode.addChild(new JottTreeNode(token));
                            return jottTreeNode;
                        }else {
                            System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                            return null;
                        }
                    }
                    // looking for i expr

                    tokenIndex = tokenstart;
                    if (i_expr(jottTreeNode) != null){
                        token = tokens.get(tokenIndex);
                        if(token.getToken().equals(";")){
                            jottTreeNode.addChild(new JottTreeNode(token));
                            return jottTreeNode;
                        }else {
                            System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                            return null;
                        }

                    }
                    tokenIndex = tokenstart;
                    // looking for s expr

                    if (s_expr(jottTreeNode) != null){
                        token = tokens.get(tokenIndex);
                        if(token.getToken().equals(";")){
                            jottTreeNode.addChild(new JottTreeNode(token));
                            return jottTreeNode;
                        }else {
                            System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                            return null;
                        }

                    }
                    tokenIndex = tokenstart;
                    // looking for b expr
                    if (b_expr(jottTreeNode) != null){
                        token = tokens.get(tokenIndex);
                        if(token.getToken().equals(";")){
                            jottTreeNode.addChild(new JottTreeNode(token));
                            return jottTreeNode;
                        }else {
                            System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                            return null;
                        }
                    }

                    System.out.println("FAILURE FOR type id = expr for asmt");
                    return null;

                }else {
                    return null;
                }
            }else {
                System.out.println("FAILURE FOR type id = ...for asmt");
                return null;
            }

        }
        // looking for id = expr ;
        token = tokens.get(tokenIndex);

        if (token.getTokenType() == TokenType.ID_KEYWORD){
            System.out.println("found id for asmt");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), token));
            tokenIndex+=1;
            token = tokens.get(tokenIndex);

            // looking for =
            if (token.getToken().equals("=") ){
                System.out.println("found = in type id = IN func asmt");
                int tokenstart = tokenIndex;

                // looking for d_expr
                if (d_expr(jottTreeNode) != null){
                    token = tokens.get(tokenIndex);
                    if(token.getToken().equals(";")){
                        jottTreeNode.addChild(new JottTreeNode(token));
                        return jottTreeNode;
                    }else {
                        System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                        return null;
                    }
                }
                // looking for i expr

                tokenIndex = tokenstart;
                if (i_expr(jottTreeNode) != null){
                    token = tokens.get(tokenIndex);
                    if(token.getToken().equals(";")){
                        jottTreeNode.addChild(new JottTreeNode(token));
                        return jottTreeNode;
                    }else {
                        System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                        return null;
                    }

                }
                tokenIndex = tokenstart;
                // looking for s expr

                if (s_expr(jottTreeNode) != null){
                    token = tokens.get(tokenIndex);
                    if(token.getToken().equals(";")){
                        jottTreeNode.addChild(new JottTreeNode(token));
                        return jottTreeNode;
                    }else {
                        System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                        return null;
                    }

                }
                tokenIndex = tokenstart;
                // looking for b expr
                if (b_expr(jottTreeNode) != null){
                    token = tokens.get(tokenIndex);
                    if(token.getToken().equals(";")){
                        jottTreeNode.addChild(new JottTreeNode(token));
                        return jottTreeNode;
                    }else {
                        System.out.println("FAILURE FOR (;) in ttype id = expr; for asmt");

                        return null;
                    }
                }

                System.out.println("FAILURE FOR type id = expr for asmt");
                return null;

            }else {
                return null;
            }
        }else {
            System.out.println("FAILURE FOR type id = ...for asmt");
            return null;
        }
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
        }
        System.out.println("missing op");
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
        }
        System.out.println("missing REL_OP");
        return null;
    }


    private static JottTreeNode d_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.D_EXPR);

        JottTreeNode first_token = null;
        // token id or int
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.ID_KEYWORD && tokens.get(tokenIndex+1).getTokenType() != TokenType.L_BRACKET) {// hmmmmmmm ???
            System.out.println("id exists");
            first_token = id(new JottTreeNode(JottElement.ID), token);
        }else if(token.getTokenType() == TokenType.NUMBER){
            first_token = new JottTreeNode(token);
        }else if(func_call(jottTreeNode) != null){
            i_expr(jottTreeNode); // hmmmmmmm ???
        }else {
            System.out.println("ERROR NOT ID OR INT OR FUNCTION CALL");
            return null;
        }

        tokenIndex += 1;

        token = tokens.get(tokenIndex);

        // check for op
        if (token.getTokenType() == TokenType.MATH_OP) {

            JottTreeNode op_token = new JottTreeNode(tokens.get(tokenIndex));

            tokenIndex += 1;
            //check for int op int meaning no stay op
            token = tokens.get(tokenIndex);
            if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.ID_KEYWORD) {
                System.out.println("FOUND OP");

                jottTreeNode.addChild(first_token);
                jottTreeNode.addChild(op_token);
                i_expr(jottTreeNode);
                return jottTreeNode;
            }else {
                jottTreeNode.addChild(first_token);
                jottTreeNode.addChild(op_token);
                if(func_call(jottTreeNode) != null){
                    i_expr(jottTreeNode);
                    return jottTreeNode;
                }
                System.out.println("REPORT ERROR stray op");
                return null;
            }
        }

        System.out.println("FOUND LONE OP|dbl");
        jottTreeNode.addChild(first_token);
        return jottTreeNode;
    }

    private static JottTreeNode b_expr(JottTreeNode jottTreeNode) {
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.ID_KEYWORD){
            System.out.println("ID exists");
            jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), token));
            tokenIndex += 1;
            return jottTreeNode;
        }else if(token.getToken().equals("True") || token.getToken().equals("False")) {
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        }else {


            if (i_expr(jottTreeNode) != null) {
                tokenIndex += 1;
                if (rel_op(jottTreeNode) != null && i_expr(jottTreeNode) != null) {
                        // tokenIndex += 1;          <----------------------------------???????????
                        return jottTreeNode;
                }
                System.out.println("i_expr relOP i_expr Failure");
                return null;
            }else{


            if (d_expr(jottTreeNode) != null) {
                tokenIndex += 1;
                if (rel_op(jottTreeNode) != null && d_expr(jottTreeNode) != null) {
                    // tokenIndex += 1;          <----------------------------------???????????
                    return jottTreeNode;
                }
                System.out.println("d_expr relOP d_expr Failure");
                return null;
            }else if (s_expr(jottTreeNode) != null) {
                tokenIndex += 1;
                if (rel_op(jottTreeNode) != null && s_expr(jottTreeNode) != null) {
                    // tokenIndex += 1;          <----------------------------------???????????
                    return jottTreeNode;
                }
                System.out.println("s_expr relOP s_expr Failure");
                return null;
            }else if (b_expr(jottTreeNode) != null) {
                    tokenIndex += 1;
                    if (rel_op(jottTreeNode) != null && b_expr(jottTreeNode) != null) {
                        // tokenIndex += 1;          <----------------------------------???????????
                        return jottTreeNode;
                    }
                    System.out.println("b_expr relOP b_expr Failure");
                    return null;
                }else if(func_call(jottTreeNode)!=null){
                        // tokenIndex += 1;          <----------------------------------???????????
                        return jottTreeNode;
                    }
                    else{
                        System.out.println("function call Failure");
                        return null;
                    }
        }}
    }

    private static JottTreeNode i_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.I_EXPR);

        JottTreeNode first_token = null;
        // token id or int
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.ID_KEYWORD && tokens.get(tokenIndex+1).getTokenType() != TokenType.L_BRACKET) {// hmmmmmmm ???
            System.out.println("id exists");
            first_token = id(new JottTreeNode(JottElement.ID), token);
        }else if(token.getTokenType() == TokenType.NUMBER){
            first_token = new JottTreeNode(token);
        }else if(func_call(jottTreeNode) != null){
             i_expr(jottTreeNode); // hmmmmmmm ???
        }else if(token.getToken().equals("-") || token.getToken().equals("+")) {
            tokenIndex += 1;
            Token second_token = tokens.get(tokenIndex);
            if (second_token.getTokenType() == TokenType.NUMBER) {
                first_token = new JottTreeNode(new Token(String.format(token.getToken().equals("-") ? "-%s" : "%s", second_token.getToken()), token.getFilename(), token.getLineNum(), token.getTokenType()));
            } else {
                System.out.println("not formatted correctly");
                return null;
            }
        } else {
            System.out.println("ERROR NOT ID OR INT OR FUNCTION CALL");
            return null;
        }

        tokenIndex += 1;

        token = tokens.get(tokenIndex);

        // check for op
        if (token.getTokenType() == TokenType.MATH_OP) {

            JottTreeNode op_token = new JottTreeNode(tokens.get(tokenIndex));

            tokenIndex += 1;
            //check for int op int meaning no stay op
            token = tokens.get(tokenIndex);
            if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.ID_KEYWORD) {
                System.out.println("FOUND OP");

                jottTreeNode.addChild(first_token);
                jottTreeNode.addChild(op_token);
                i_expr(jottTreeNode);
                return jottTreeNode;
            }else {
                    jottTreeNode.addChild(first_token);
                    jottTreeNode.addChild(op_token);
                if(func_call(jottTreeNode) != null){
                    i_expr(jottTreeNode);
                    return jottTreeNode;
                }
                System.out.println("REPORT ERROR stray op");
                return null;
            }
        }

        System.out.println("FOUND LONE OP|int");
        jottTreeNode.addChild(first_token);
        return jottTreeNode;

    }

    private static JottTreeNode str_literal(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.STR_LITERAL);
        Token token = tokens.get(tokenIndex);
        if (token.getTokenType() == TokenType.STRING) {
            System.out.println("String lit exists");
            jottTreeNode.addChild(new JottTreeNode(token));
            tokenIndex += 1;
            return jottTreeNode;
        }
        System.out.println("missing String lit");
        return null;
    }

    private static JottTreeNode s_expr(JottTreeNode jottTreeNode) {
        System.out.println(JottElement.S_EXPR);

        Token token = tokens.get(tokenIndex);

        // checking for string
        if (str_literal(jottTreeNode) == null) {
            tokenIndex += 1;
            Token parameter = tokens.get(tokenIndex);
            if (parameter.getTokenType() == TokenType.ID_KEYWORD) {
                System.out.println("id found");
                jottTreeNode.addChild(id(new JottTreeNode(JottElement.ID), parameter));
            }
            else if(func_call(jottTreeNode) == null){
                return null;
            }
        }
        return jottTreeNode;
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
        JottTree T = parse(testResults);

        System.out.println("\n\nCONVERT BACK INTO JOTT AGAIN USING THE PARSE TREE:");
        System.out.println(T.convertToJott());
//        for (Token t:testResults) {
//            System.out.println(t.getToken()+"\t\t<- "+t.getTokenType());
//        }
    }
}
