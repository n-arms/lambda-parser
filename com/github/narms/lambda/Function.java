package com.github.narms.lambda;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Function extends Expression {
    // where the last element of the arraylist is the first argument in the lamnbda
    private List<Argument> arguments;
    private Expression body;

    public Function(LinkedList<Argument> arguments, Expression body) {
        this.arguments = arguments;
        this.body = body;
    }

    public Function(FunctionConstruct function) {
        this.arguments = function.getArguments();
        this.body = function.getBody();
    }

    @Override
    public Expression reduce() {
        this.body = this.body.reduce();
        if (this.body instanceof Function) {
            this.arguments.addAll(((Function) this.body).getArguments());

            this.body = ((Function) this.body).getBody();
        }
        return this;
    }

    public List<Argument> getArguments() {
        return this.arguments;
    }

    public Expression getBody() {
        return this.body;
    }

    public Expression apply(Expression e) {
        return this.defineArgument(this.arguments.get(0), e);
    }

    @Override
    public void redefine(String from, String to) {
        this.body.redefine(from, to);
        for (Argument a : arguments)
            a.redefine(from, to);
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append('λ');
        for (int i = 0; i < arguments.size(); i++) {
            output.append(arguments.get(i).toString());
        }
        output.append('.');
        output.append(this.body.toString());
        return output.toString();
    }

    @Override
    public Expression defineArgument(Argument a, Expression e) {
        for (int i = 0; i < this.arguments.size(); i++) {
            if (this.arguments.get(i).equals(a)) {
                this.arguments.remove(i);
                break;
            }
        }
        this.body = this.body.defineArgument(a, e);

        if (this.arguments.size() > 0) {
            return this;
        }
        return this.body;

    }

    @Override
    public Expression getLeft() {
        return this;
    }

    @Override
    public boolean canReduce(Argument a) {
        for (Argument i : this.arguments) {
            if (a.equals(i)) {
                return false;
            }
        }
        return this.body.canReduce(a);
    }

    @Override
    public Deque<String> getBound() {
        Deque<String> output = body.getBound();
        for (Argument a: arguments)
        output.add(a.getName());
        return output;
    }
}
