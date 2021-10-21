package grammar;

import main.Token;
import main.TokenType;

import java.util.ArrayList;

// s_expr -> str_literal|id|func_call

public class SExpr extends Expr {
    private Token stringLit = null;
    private Token id = null;
    private FuncCall funcCall = null;
    int nest_level;

    public SExpr(Token stringLit, Token id, FuncCall funcCall, int nestlvl) {
        super(null);
        this.stringLit = stringLit;
        this.id = id;
        this.funcCall = funcCall;
        this.nest_level = nestlvl;
    }


    public static Expr parseSExpr(ArrayList<Token> tokens, int nestLevel) throws ParsingException {
        System.out.println("-------------------parsing s_expr--------------------------");


        Token possible_str = tokens.get(0);

        // ----------------------check for string------------------
        if (possible_str.getTokenType() == TokenType.STRING) {

            tokens.remove(0);
            return new SExpr(possible_str, null, null, nestLevel);
        }

        // ----------------------check for id------------------

        if (possible_str.getTokenType() == TokenType.ID_KEYWORD) {
            tokens.remove(0);
            return new SExpr(null, possible_str, null, nestLevel);
        }
        // ----------------------check for func call------------------
        FuncCall f = FuncCall.ParseFuncCall(tokens, nestLevel);
        if (f != null) {
            tokens.remove(0);
            return new SExpr(null, null, f, nestLevel);
        }

        return null;
    }

    @Override
    public String convertToJott() {

        if (stringLit != null){
            return stringLit.getToken();
        }
        if (id != null){
            return id.getToken();
        }
        return funcCall.convertToJott();
    }

}
