package com.github.narms.lambda.frontend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;

import com.github.narms.lambda.Expression;
import com.github.narms.lambda.Lexer;
import com.github.narms.lambda.Parser;

public class FileParser {
    static BufferedReader in;
    static Deque<Expression> parsedLine;
    static String line;
    static int l;
    public static void main(String[] args) throws IOException{
        try{
            in = new BufferedReader(new InputStreamReader(new FileInputStream("example.txt"), "UTF-8"));
            l=0;
            while ((line=in.readLine())!= null){
                l++;
                parsedLine = Parser.parse(Lexer.lex(line));
                if (parsedLine != null){
                    if (parsedLine.size() > 1){
                        System.out.println("ERROR\nparser could not reduce line "+l+ " to a 1 root tree\n"+line);
                    }else{
                        System.out.println(parsedLine.peek().normalize().format());
                    }
                }
            }
        }catch (Exception e){
            System.out.println("error");
        }finally{

        }
    }
}
