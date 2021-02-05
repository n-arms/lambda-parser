package com.github.narms.lambda;

import java.util.List;
import java.util.Map;

public abstract class Expression {
    
    public abstract Expression copy();
    public abstract String toString();
    public abstract Expression alphaReduce(List<String> scope);
    public abstract Expression betaReduce(Argument a, Expression e);
    public abstract List<String> bound();
    public abstract Expression normalize();
    public abstract Expression format();
    public abstract void bind(Map<String, Long> scope);
}
