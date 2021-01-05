package com.github.narms.lambda;

public class Token {
    private static final char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char lpar = '(';
    private static final char rpar = ')';
    private static final char dot = '.';
    private static final char lambda = 'λ';

    private StringBuffer value;
    private TokenType type;
    public Token(String value){
        this.value.append(value);
    }
    public Token(char value){
        this.value.append(value);
    }
    public void addChar(char c){
        this.value.append(c);
    }
    public static TokenType getType(char c){
        switch (c){
            case lpar:
            return TokenType.LEFTPAREN;
            case rpar:
            return TokenType.RIGHTPAREN;
            case dot:
            return TokenType.DOT;
            case lambda:
            return TokenType.LAMBDA;
            default:
            for (char l: letters){
                if (l==c)
                return TokenType.ARGUMENT;
            }
            throw new RuntimeException("illegal char "+c);
        }
    }
    public void setType(){
        this.type = getType(this.value.charAt(0));
    }
    public String getValue(){
        return this.value.toString();
    }
    public TokenType getType(){
        return this.type;
    }

}