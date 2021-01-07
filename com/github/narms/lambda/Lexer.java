package com.github.narms.lambda;

import java.util.ArrayDeque;

public class Lexer {
    public static ArrayDeque<Object> lex(String toLex){
        ArrayDeque<Object> output = new ArrayDeque<Object>();
        for (int i = 0; i<toLex.length(); i++){
            output.add(new Token(toLex.charAt(i)));
        }
        return output;
    }
}
