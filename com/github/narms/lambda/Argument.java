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

    public String getName(){
        return this.name;
    }

    @Override
    public Expression defineArgument(Argument a, Expression e){
        if (a.getName().equals(this.name)){
            return e;
        }else{
            return this;
        }
    }

    @Override
    public Expression getLeft(){
        return this;
    }

    
    
}
