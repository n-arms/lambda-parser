package com.github.narms.lambda;

import java.util.Deque;
import java.util.List;

public class Application extends Expression {

    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right) {
        this.right = right;
        this.left = left;
    }

    @Override
    public Expression reduce(List<Argument> env) {
        this.left = this.left.reduce(env);
        this.right = this.right.reduce(env);
        for (String s: this.left.getBound()){
            if (!in(env, s)){
                this.right.redefine(s, s+"'");
            }
        }
        if (this.left instanceof Function)
            return (((Function) this.left).apply(this.right));//.reduce(env);
        return this;
    }

    private boolean in(List<Argument> env, String s){
        for (Argument arg: env){
            if (s.equals(arg.getName())){
                return true;
            }
        }
        return false;
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

    @Override
    public Expression copy() {
        return new Application(left.copy(), right.copy());
    }

    @Override
    public Expression alphaReduce(List<String> scope){
        this.left = this.left.alphaReduce(scope);
        this.right = this.right.alphaReduce(scope);
        return this;
    }

    @Override
    public Expression betaReduce(Argument a, Expression e){
        this.left = this.left.betaReduce(a, e);
        this.right = this.right.betaReduce(a, e);
        return this;
    }

    @Override
    public List<String> bound(){
        List<String> output = this.left.bound();
        output.addAll(this.right.bound());
        return output;
    }

    @Override
    public Expression normalize(){
        this.left = this.left.normalize();
        this.right = this.right.normalize();
        if (this.left instanceof Function){
            this.left = this.left.alphaReduce(this.right.bound());
            this.left = ((Function)this.left).applyArgument(this.right);
            return this.left.normalize();
        }
        return this;
    }

    @Override
    public Expression format(){
        this.left = this.left.format();
        this.right = this.right.format();
        return this;
    }
}
