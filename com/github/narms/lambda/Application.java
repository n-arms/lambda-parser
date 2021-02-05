package com.github.narms.lambda;

import java.util.List;
import java.util.Map;

public class Application extends Expression {

    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right) {
        this.right = right;
        this.left = left;
    }

    @Override
    public String toString() {
        return '(' + this.left.toString() + ' ' + this.right.toString() + ')';
    }

    @Override
    public Expression copy() {
        return new Application(left.copy(), right.copy());
    }

    @Override
    public Expression alphaReduce(List<String> scope){
        System.out.println("alpha reducing "+this+" with scope "+scope);
        this.left = this.left.alphaReduce(scope);
        this.right = this.right.alphaReduce(scope);
        return this;
    }

    @Override
    public Expression betaReduce(Argument a, Expression e){
        System.out.println("beta reducing "+this+" with arg "+a+" and expression "+e);
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
        System.out.println("normalizing "+this);
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
        System.out.println("formatting "+this);
        this.left = this.left.format();
        this.right = this.right.format();
        return this;
    }

    @Override
    public void bind(Map<String, Long> scope) {
        this.left.bind(scope);
        this.right.bind(scope);
    }
}
