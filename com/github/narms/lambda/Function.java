package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

    public List<Argument> getArguments() {
        return this.arguments;
    }

    public Expression getBody() {
        return this.body;
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
    public Expression copy(Long offset, Set<Long> scope) {
        LinkedList<Argument> outputCopy = new LinkedList<Argument>();
        Set<Long> newScope = new HashSet<Long>();
        newScope.addAll(scope);
        for (Argument a : arguments){
            newScope.add(a.getID());
            outputCopy.add((Argument) new Argument(a.getName(), a.getID()+offset));
        }
        return new Function(outputCopy, body.copy(offset, newScope));
    }

    @Override
    public Expression betaReduce(Argument a, Expression e, Long offset) {
        System.out.println("beta reducing " + this + " with arg " + a + " and expression " + e);
        this.body = this.body.betaReduce(a, e, offset);
        return this;
    }

    @Override
    public List<String> bound() {
        List<String> output = this.body.bound();
        for (Argument a : arguments)
            output.addAll(a.bound());
        return output;
    }

    @Override
    public Expression normalize() {
        System.out.println("normalizing " + this);
        this.body = this.body.normalize();
        return this;
    }

    @Override
    public Expression format() {
        System.out.println("formatting " + this);
        if (this.body instanceof Function) {
            this.arguments.addAll(((Function) this.body).getArguments());
            this.body = ((Function) this.body).getBody();
        }
        return this;
    }

    public Expression applyArgument(Expression e) {
        this.body = this.body.betaReduce(this.arguments.get(0), e, Argument.biggestID()-e.lowestID()+1);
        this.arguments.remove(0);
        if (this.arguments.size() > 0) {
            return this;
        }
        return this.body;
    }

    @Override
    public void bind(Map<String, Long> scope) {
        System.out.println("binding "+this+" with scope "+scope);
        Map<String, Long> newScope = new HashMap<String, Long>();
        for (Argument a: arguments){
            newScope.put(a.getName(), Long.valueOf(a.genID().longValue()));
        }
        for (Entry<String, Long> entry: scope.entrySet()){
            if (!newScope.containsKey(entry.getKey()))
            newScope.put(entry.getKey(), entry.getValue());
        }
        this.body.bind(newScope);
    }

    @Override
    public Long lowestID() {
        Long lowest = body.lowestID();
        for (Argument a: arguments){
            if (a.getID() != null && a.getID() < lowest)
            lowest = a.getID();
        }
        return lowest;
    }

    @Override
    public Long highestID(){
        Long highest = body.highestID();
        for (Argument a: arguments){
            if (a.getID() != null && a.getID() > highest)
            highest = a.getID();
        }
        return highest;
    }
}
