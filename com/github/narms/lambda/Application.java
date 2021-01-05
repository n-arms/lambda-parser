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
        this.left = this.left.reduce();
        this.right = this.right.reduce();
        if (this.left instanceof Function)
        return (((Function)this.left).apply(this.right)).reduce();
        return this;
    }

    @Override
    public String toString() {
        return '(' + this.left.toString() +' '+this.right.toString()+')';
    }

    @Override
    public Expression defineArgument(Argument a, Expression e){
        this.left = this.left.defineArgument(a, e);
        this.right = this.right.defineArgument(a, e);
        return this;
    }

    @Override
    public Expression getLeft(){
        return this.left.getLeft();
    }

    @Override
    public boolean canReduce(Argument a){
        return this.left.canReduce(a) && this.right.canReduce(a);
    }
    
}
