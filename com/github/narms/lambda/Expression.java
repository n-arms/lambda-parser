package com.github.narms.lambda;

import java.util.Deque;

public abstract class Expression {
    public abstract Expression reduce();
    public abstract String toString();
    public abstract Expression defineArgument(Argument a, Expression e);
    public abstract Expression getLeft();
    public abstract boolean canReduce(Argument a);
    public abstract void redefine(String from, String to);
    public abstract Deque<String> getBound();
    public abstract Expression copy();
}
