package com.github.narms.lambda;

import java.util.Deque;
import java.util.List;

public abstract class Expression {
    public abstract Expression reduce(List<Argument> env);
    public abstract Expression defineArgument(Argument a, Expression e);
    public abstract Expression getLeft();
    public abstract boolean canReduce(Argument a);
    public abstract void redefine(String from, String to);
    public abstract Deque<String> getBound();
    
    public abstract Expression copy();
    public abstract String toString();
    public abstract Expression alphaReduce(List<String> scope);
    public abstract Expression betaReduce(Argument a, Expression e);
    public abstract List<String> bound();
    public abstract Expression normalize();
    public abstract Expression format();
}
