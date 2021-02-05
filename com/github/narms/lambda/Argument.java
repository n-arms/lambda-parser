package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Argument extends Expression {
    public static List<String> argumentBindings = new ArrayList<String>();
    private String name;
    private Long id;

    public Argument(String name) {
        this.name = name;
        this.id = null;
    }

    @Override
    public String toString() {
        return "<"+this.name+", "+id+">";
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Argument) && this.id.equals(((Argument) o).getID());
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

    public Long getID(){
        return id;
    }

    public Long genID(){
        id = Long.valueOf(argumentBindings.size());
        argumentBindings.add(name);
        return id;
    }

    @Override
    public void bind(Map<String, Long> scope) {
        if (id==null && scope.containsKey(name))
        id = scope.get(name);
    }
}
