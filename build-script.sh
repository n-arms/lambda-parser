#!/bin/bash

path="com/github/narms/lambda/"

function usage(){
    echo "usage: ./build_script.sh [-b]"
}

function build_files(){
    javac -d classes ${path}{Application,Argument,Combinator,Construct,Expression,Function,FunctionConstruct,Lexer,ParenConstruct,Parser,Token,TokenType,Variable,frontend/FileParser,frontend/ShellParser}.java
}

if [[ $# -eq 1 ]]; then
    if [[ $1 -eq "-b" ]]; then
        build_files
        exit 0
    else
        echo $(usage)
        exit 1
    fi
else
    echo $(usage)
    exit 1
fi
