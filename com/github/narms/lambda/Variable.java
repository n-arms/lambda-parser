package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            value=Combinator.get(name).copy(Combinator.get(name).lowestID(), new HashSet<Long>()).normalize();
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
        // TODO Auto-generated method stub

    }

    @Override
    public Long lowestID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long highestID() {
        // TODO Auto-generated method stub
        return null;
    }

}
