package com.github.narms.lambda.expressions;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.narms.lambda.Token;

public class ParseError extends Expression {
    /** 
     * class for compile-time errors
    */

    private List<Object> failPoints;
    private Deque<Object> tokenLine;
    
    public ParseError(Deque<Object> tokenLine, Object failPoint){
        failPoints = new ArrayList<Object>();
        failPoints.add(failPoint);
        this.tokenLine = tokenLine;
    }

    public ParseError(Deque<Object> tokenLine, List<Object> failPoints){
        this.failPoints = failPoints;
        this.tokenLine = tokenLine;
    }

    @Override
    public Expression copy(Long offset, Set<Long> scope) {
        return this;
    }

    @Override
    public String toString() {
        StringBuffer line = new StringBuffer("ERROR on line ");
        for (Object o: tokenLine){
            line.append(((Token)o).getValue());
        }
        for (Object o: failPoints){
            line.append("\nfailpoint: ");
            line.append(o.toString());
        }
        line.append("\n");
        return line.toString();
    }

    @Override
    public Expression betaReduce(Argument a, Expression e, Long offset) {
        return this;
    }

    @Override
    public List<String> bound() {
        return null;
    }

    @Override
    public Expression normalize() {
        return this;
    }

    @Override
    public Expression format() {
        return this;
    }

    @Override
    public void bind(Map<String, Long> scope) {
        return;
    }

    @Override
    public Long lowestID() {
        return null;
    }

    @Override
    public Long highestID() {
        return null;
    }

    @Override
    public Expression duplicate() {
        return this;
    }
    
}
