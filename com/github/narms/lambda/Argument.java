package com.github.narms.lambda;

public class Argument extends Expression{
    String name;

    public Argument(String name){
        this.name = name;
    }

    @Override
    public Expression reduce() {
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
