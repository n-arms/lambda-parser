package com.github.narms.lambda;

import java.util.ArrayList;

public class Function extends Expression{
    private ArrayList<Argument> arguments;
    private Expression body;
    public Function(ArrayList<Argument> arguments, Expression body){
        this.arguments = arguments;
        this.body = body;
    }
    public Expression apply(Argument a){
        return null;
    }

    @Override
    public Expression reduce() {
        //TODO functional reduction
        return null;
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append('λ');
        for (Argument a: this.arguments){
            output.append(a.toString());
        }
        output.append('.');
        output.append(this.body.toString());
        return output.toString();

    }
}
