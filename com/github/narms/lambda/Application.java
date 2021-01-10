package com.github.narms.lambda;

import java.util.Deque;

public class Application extends Expression {

    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right) {
        this.right = right;
        this.left = left;
    }

    @Override
    public Expression reduce() {
        this.left = this.left.reduce();
        this.right = this.right.reduce();
        for (String s: this.left.getBound()){
            this.right.redefine(s, s+"'");
        }
        if (this.left instanceof Function)
            return (((Function) this.left).apply(this.right)).reduce();
        return this;
    }

    @Override
    public String toString() {
        return '(' + this.left.toString() + ' ' + this.right.toString() + ')';
    }

    @Override
    public Expression defineArgument(Argument a, Expression e) {
        this.left = this.left.defineArgument(a, e);
        this.right = this.right.defineArgument(a, e);
        return this;
    }

    @Override
    public Expression getLeft() {
        return this.left.getLeft();
    }

    @Override
    public boolean canReduce(Argument a) {
        return this.left.canReduce(a) && this.right.canReduce(a);
    }

    @Override
    public void redefine(String from, String to) {
        this.left.redefine(from, to);
        this.right.redefine(from, to);
    }

    @Override
    public Deque<String> getBound() {
        Deque<String> output = this.left.getBound();
        output.addAll(this.right.getBound());
        return output;
    }
}
