package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Argument extends Expression {
    public static Set<Long> argumentBindings = new HashSet<Long>(); 
    //TODO replace with treeset for fast highest id calls
    private String name;
    private Long id;

    public Argument(String name) {
        this.name = name;
        this.id = null;
    }

    public Argument(String name, Long id){
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        if (id == null)
        return name;
        StringBuffer output = new StringBuffer();
        output.append((char)(id+97));
        for (int i = 0; i<(int)(id.longValue()/26D); i++){
            output.append('\'');
        }
        return output.toString();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this.id == null || !(o instanceof Argument) || ((Argument)o).getID() == null)
        return false;
        return (this.id.equals(((Argument)o).getID()));
    }

    @Override
    public Expression copy(Long offset, Set<Long> scope) {
        if (this.id==null)
        return new Argument(this.name);
        if (scope.contains(id)){
            Argument.argumentBindings.add(this.id+offset);
            return new Argument(this.name, this.id+offset);
        }
        return new Argument(this.name, this.id);
        
    }

    @Override
    public Expression betaReduce(Argument a, Expression e, Long offset){
        System.out.println("beta reducing "+this+" with arg "+a+" expr "+e+" and offset "+offset);
        if (a.equals(this)){
            Long lowest = e.lowestID();
            while (Argument.argumentBindings.contains(lowest)){
                lowest += offset;
            }
            Expression output = e.copy(lowest-e.lowestID(), new HashSet<Long>());
            return output;
        }
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
        Long i = 0L;
        while (Argument.argumentBindings.contains(i)){
            i++;
        }
        id = i;
        Argument.argumentBindings.add(id);
        return id;
    }

    @Override
    public void bind(Map<String, Long> scope) {
        if (id==null && scope.containsKey(name)){
            id = scope.get(name);
        }else{
            Long i = 0l;
            while (Argument.argumentBindings.contains(i)){
                i++;
            }
            Argument.argumentBindings.add(i);
            id = i;
        }
        
    }

    @Override
    public Long lowestID() {
        return id;
    }

    public static Long biggestID(){
        Long highest = -1L;
        for (Long l: Argument.argumentBindings){
            if (l > highest)
            highest = l;
        }
        return highest;
    }

    @Override
    public Long highestID(){
        return id;
    }

    @Override
    public Expression duplicate(){
        return new Argument(this.name);
    }
}
