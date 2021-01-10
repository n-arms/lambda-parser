package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.Deque;

public class Argument extends Expression {
    private String name;

    public Argument(String name) {
        this.name = name;
    }

    @Override
    public void redefine(String from, String to) {
        if (from.equals(this.name))
            this.name = to;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Argument) && this.name.equals(((Argument) o).getName());
    }

    @Override
    public Expression reduce() {
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public Expression defineArgument(Argument a, Expression e) {
        if (a.equals(this)) {
            return e;
        } else {
            return this;
        }
    }

    @Override
    public Expression getLeft() {
        return this;
    }

    @Override
    public boolean canReduce(Argument a) {
        return !(a.getName().equals(this.name));
    }

    @Override
    public Deque<String> getBound() {
        Deque<String> output = new ArrayDeque<String>();
        output.add(this.name);
        return output;
    }

    @Override
    public Expression copy() {
        return new Argument(this.name);
    }
}
