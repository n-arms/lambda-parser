package com.github.narms.lambda.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.narms.lambda.Combinator;

public class Variable extends Expression {
    private String name;
    private Expression value;
    private boolean defined = false;

    public Variable(String name){
        this.name = name;
    }

    @Override
    public Expression copy(Long offset, Set<Long> scope) {
        return new Variable(this.name);
    }

    @Override
    public String toString() {
        if (defined)
        return value.toString();
        return name;
    }

    @Override
    public Expression betaReduce(Argument a, Expression e, Long offset) {
        if (defined)
        value = value.betaReduce(a, e, offset);
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
            value=Combinator.get(name).duplicate();
            value.bind(new HashMap<String, Long>());
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

    @Override
    public void bind(Map<String, Long> scope) {
        return;
    }

    @Override
    public Long lowestID() {
        return 0xffffffffffffffffL;
    }

    @Override
    public Long highestID() {
        return -1L;
    }

    @Override
    public Expression duplicate(){
        return new Variable(this.name);
    }

}
