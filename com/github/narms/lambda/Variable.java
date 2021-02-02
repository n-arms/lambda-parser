package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.List;

public class Variable extends Expression {
    private String name;
    private Expression value;
    private boolean defined = false;

    public Variable(String name){
        this.name = name;
    }

    @Override
    public Expression copy() {
        return new Variable(this.name);
    }

    @Override
    public String toString() {
        if (defined)
        return value.toString();
        return name;
    }

    @Override
    public Expression alphaReduce(List<String> scope) {
        if (defined)
        value = value.alphaReduce(scope);
        return this;
    }

    @Override
    public Expression betaReduce(Argument a, Expression e) {
        if (defined)
        value = value.betaReduce(a, e);
        return this;
    }

    @Override
    public List<String> bound() {
        if (defined)
        return value.bound();
        return new ArrayList<String>();
    }

    @Override
    public Expression normalize() {
        if (Combinator.inScope(name)){
            value=Combinator.get(name).copy().normalize();
            defined=true;
            return value;
        }
        return this;
    }

    @Override
    public Expression format() {
        if (defined)
        value = value.format();
        return this;
    }
}
