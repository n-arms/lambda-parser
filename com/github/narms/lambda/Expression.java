package com.github.narms.lambda;

public abstract class Expression {
    public abstract Expression reduce();
    public abstract String toString();
}
