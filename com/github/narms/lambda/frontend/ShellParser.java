package com.github.narms.lambda.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.HashMap;
import java.util.NoSuchElementException;

import com.github.narms.lambda.Lexer;
import com.github.narms.lambda.expressions.Expression;
import com.github.narms.lambda.parsing.Parser;


public class ShellParser {
    static BufferedReader in;
    static Deque<Expression> parsedLine;
    static String line;
    static boolean errorCatch;
    static Expression parsedExpr;
    public static void main(String[] args) throws IOException{
        errorCatch = Boolean.parseBoolean(args[0]);
        in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("> ");
        while (!(line = in.readLine()).equals("quit")){
            try{
                parsedLine = Parser.parse(Lexer.lex(line));
                if (parsedLine != null){
                    if (parsedLine.size() != 1){
                        System.out.println("    ERROR\nparser failed to parse line "+line);
                        if (!errorCatch){
                            if (in != null)
                            in.close();
                            return;
                        }
                    }else{
                        parsedExpr = parsedLine.peek();
                        parsedExpr.bind(new HashMap<String, Long>());
                        System.out.println(parsedExpr.format().normalize().format());
                    }
                }
            }catch (NoSuchElementException e){
                System.out.println("    ERROR\nparser failed to parse line "+line);
                if (!errorCatch){
                    if (in != null)
                    in.close();
                    return; 
                }
                   
            }
            System.out.print("> ");
        }
        in.close();
    }
}
