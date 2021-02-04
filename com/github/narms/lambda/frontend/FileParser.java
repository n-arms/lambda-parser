package com.github.narms.lambda.frontend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Deque;
import java.util.NoSuchElementException;

import com.github.narms.lambda.Expression;
import com.github.narms.lambda.Lexer;
import com.github.narms.lambda.Parser;

public class FileParser {
    static BufferedReader in;
    static Deque<Expression> parsedLine;
    static String line;
    static int l;
    static String file;
    static boolean errorCatch;
    public static void main(String[] args) throws IOException{
        in = null;
        if (args.length == 2){
            errorCatch = Boolean.parseBoolean(args[0]);
            file = args[1];
        }else{
            System.out.println("    FNF error, only "+args.length+" args");
            throw new IOException();
        }
        try{
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            l=0;
            while ((line=in.readLine())!= null){
                l++;
                try{
                    parsedLine = Parser.parse(Lexer.lex(line));
                }catch (NoSuchElementException e){
                    System.out.println("\tERROR: line "+l+"\n\tparser failed to parse line "+line);
                    if (!errorCatch){
                        if (in != null)
                        in.close();
                        return;
                    }  
                }
                if (parsedLine != null){
                    if (parsedLine.size() > 1){
                        System.out.println("\tERROR\nparser failed to parse line "+line+"\n\tparsed down to "+parsedLine);
                        if (!errorCatch){
                            if (in != null)
                            in.close();
                            return;
                        }
                    }else{
                        System.out.println(parsedLine.peek().format().normalize().format());
                    }
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("\tERROR\n\tfile \""+file+"\" not found");
        }finally{
            if (in != null)
            in.close();
        }
    }
}
