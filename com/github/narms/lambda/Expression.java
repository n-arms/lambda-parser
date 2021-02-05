package com.github.narms.lambda;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Expression {
    
    public abstract Expression copy(Long offset, Set<Long> scope);
    public abstract String toString();
    public abstract Expression betaReduce(Argument a, Expression e, Long offset);
    public abstract List<String> bound();
    public abstract Expression normalize();
    public abstract Expression format();
    public abstract void bind(Map<String, Long> scope);
    public abstract Long lowestID();
    public abstract Long highestID();
    public abstract Expression duplicate();
}
