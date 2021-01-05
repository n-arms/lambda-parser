package com.github.narms.lambda;

import java.util.Deque;

public abstract class Construct {
    public abstract String toString();
    public abstract void addObject(Object o);
    public abstract Deque<Expression> parse();
    public abstract boolean isFull();
}
