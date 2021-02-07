package com.github.narms.lambda.parsing;

import java.util.Deque;

import com.github.narms.lambda.expressions.Expression;

public abstract class Construct {
    public abstract String toString();
    public abstract void addObject(Object o);
    public abstract Deque<Expression> parse();
    public abstract boolean isFull();
}
