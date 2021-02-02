package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.List;

public class Argument extends Expression {
    private String name;

    public Argument(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Argument) && this.name.equals(((Argument) o).getName());
    }

    @Override
    public Expression copy() {
        return new Argument(this.name);
    }

    @Override
    public Expression alphaReduce(List<String> scope){
        boolean isFinished = false;
        while (!isFinished){
            isFinished = true;
            for (String s: scope){
                if (s.equals(this.name)){
                    isFinished = false;
                    this.name = this.name+'\'';
                }
            }
        }
        return this;
    }

    @Override
    public Expression betaReduce(Argument a, Expression e){
        if (a.equals(this))
        return e.copy();
        return this;
    }

    @Override
    public List<String> bound(){
        List<String> output = new ArrayList<String>();
        output.add(name);
        return output;
    }

    @Override
    public Expression normalize(){
        return this;
    }

    @Override
    public Expression format(){
        return this;
    }
}
