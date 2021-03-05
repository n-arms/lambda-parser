#!/usr/bin/env bash

yacc -d parser.y
lex lexer.l
cc y.tab.c lex.yy.c compiler.c
