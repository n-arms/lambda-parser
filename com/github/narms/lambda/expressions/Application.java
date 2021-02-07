package com.github.narms.lambda.expressions;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Expression copy(Long offset, Set<Long> scope) {
        return new Application(left.copy(offset, scope), right.copy(offset, scope));
    }

    @Override
    public Expression betaReduce(Argument a, Expression e, Long offset){
        this.left = this.left.betaReduce(a, e, offset);
        this.right = this.right.betaReduce(a, e, offset);
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
            //this.left = this.left.alphaReduce(this.right.bound());
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

    @Override
    public void bind(Map<String, Long> scope) {
        this.left.bind(scope);
        this.right.bind(scope);
    }

    @Override
    public Long lowestID() {
        Long leftLow = left.lowestID();
        Long rightLow = right.lowestID();
        if (leftLow != null && rightLow != null && rightLow > leftLow)
        return leftLow;
        if (rightLow==null)
        return leftLow;
        return rightLow;
    }

    @Override
    public Long highestID() {
        Long leftLow = left.lowestID();
        Long rightLow = right.lowestID();
        if (leftLow != null && rightLow != null && rightLow > leftLow)
        return rightLow;
        if (rightLow==null)
        return rightLow;
        return leftLow;
    }

    @Override
    public Expression duplicate(){
        return new Application(left.duplicate(), right.duplicate());
    }
}
