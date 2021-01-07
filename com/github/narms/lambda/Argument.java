package com.github.narms.lambda;

import java.util.Map;
import java.util.Deque;
import java.util.HashMap;

public class Argument extends Expression {
    private String name;
    private long id;
    private static long currentIdentifier = 0;
    private static Map<String, Long> enviroment = new HashMap<String, Long>();
    
    public Argument(String name){
        this.name = name;
        if (enviroment.containsKey(name)){
            id = enviroment.get(name);
        }else{
            currentIdentifier ++;
            id = currentIdentifier;
            enviroment.put(name, id);
        }
    }

    public static void undefine(Deque<Argument> arguments){
        while (!arguments.isEmpty()){
            enviroment.remove(arguments.pop().getName());
        }
    }

    public static void undefine(Argument a){
        enviroment.remove(a.getName());
    }

    public static void clearEnviroment(){
        enviroment.clear();
    }

    @Override
    public boolean equals(Object o){
        return (o instanceof Argument) && this.id == ((Argument)o).getID() && this.name.equals(((Argument)o).getName());
    }

    @Override
    public Expression reduce() {
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName(){
        return this.name;
    }

    public long getID(){
        return this.id;
    }

    @Override
    public Expression defineArgument(Argument a, Expression e){
        if (a.equals(this)){
            return e;
        }else{
            return this;
        }
    }

    @Override
    public Expression getLeft(){
        return this;
    }
    
    @Override
    public boolean canReduce(Argument a){
        return !(a.getName().equals(this.name));
    }

    
    
}
