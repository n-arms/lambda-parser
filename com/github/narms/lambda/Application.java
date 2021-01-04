package com.github.narms.lambda;

public class Application extends Expression{

    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right){
        this.right = right;
        this.left = left;
    }

    @Override
    public Expression reduce() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return '(' + this.left.toString() +' '+this.right.toString()+')';
    }
    
}
