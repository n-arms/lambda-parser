package com.github.narms.lambda;

import java.util.ArrayDeque;

public class Lexer {
    public static ArrayDeque<Token> lex(String toLex){
        ArrayDeque<Token> output = new ArrayDeque<Token>();
        for (int i = 0; i<toLex.length(); i++){
            output.add(new Token(toLex.charAt(i)));
        }
        return output;
    }
}
