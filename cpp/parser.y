%{
  #include <stdio.h>
  #include <stdlib.h>
  #include "parser.h"
  int yylex(void);
  void yyerror(char *);
  extern FILE* yyin;

  unsigned newBlock(unsigned kind, unsigned a, unsigned b) {
    (compileHeap+compileHeapSize) -> type_ = kind;
    (compileHeap+compileHeapSize) -> a_ = a;
    (compileHeap+compileHeapSize) -> b_ = b;
    compileHeapSize++;
    return compileHeapSize-1;
  }
%}

%left '.'
%left ' '
%right ':'

%start program
%token LAMBDA OPEN CLOSE UPPER LOWER LET IN QUOTE NUM NIL IF YES NO


%%


program:    expr
            |
            error
            {
              yyerrok;
            }
            ;
arg:        LOWER
            {
              $$ = $1+97;
            };
expr:       LET ' ' arg '=' expr ' ' IN ' ' expr
            {
              $$ = newBlock(1, newBlock(10, newBlock(0, $3, 0), $9), $5);
            }
            |
            arg
            {
              $$ = newBlock(0, $1, 0);
            }
            |
            LAMBDA arg '.' expr
            {
              $$ = newBlock(2, newBlock(0, $2, 0), $4);
            }
            |
            expr ' ' expr
            {
              $$ = newBlock(1, $1, $3);
            }
            |
            QUOTE text QUOTE
            {
              $$ = $2;
            }
            |
            NUM
            {
              $$ = newBlock(6, $1, 0);
            }
            |
            OPEN expr CLOSE
            {
              $$ = $2;
            }
            |
            operator
            {
              $$ = $1;
            }
            |
            IF
            {
              $$ = newBlock(24, 0, 0);
            }
            |
            YES
            {
              $$ = newBlock(7, 1, 0);
            }
            |
            NO
            {
              $$ = newBlock(7, 0, 0);
            }
            |
            NIL
            {
              $$ = newBlock(5, 0, 0);
            }
            |
            expr ':' expr
            {
              $$ = newBlock(4, $1, $3);
            }
            ;
text:
            {
              $$ = newBlock(5, 0, 0);
            }
            |
            arg text
            {
              $$ = newBlock(4, newBlock(0, $1, 0), $2);
            }
            ;
operator:   '+'
            {
              $$ = newBlock(16, 0, 0);
            }
            |
            '-'
            {
              $$ = newBlock(17, 0, 0);
            }
            |
            '*'
            {
              $$ = newBlock(18, 0, 0);
            }
            |
            '/'
            {
              $$ = newBlock(19, 0, 0);
            }
            |
            '%'
            {
              $$ = newBlock(21, 0, 0);
            }
            |
            '='
            {
              $$ = newBlock(20, 0, 0);
            }
            ;
%%
void yyerror(char *s){
  fprintf(stderr, "%s\n", s);
}

void setup(){
  compileHeap = (struct AbstractBlock*) (malloc(500*sizeof(struct AbstractBlock)));
  compileHeapSize = 0;
}

void parseFile(){
  setup();
  yyin = fopen("example.txt", "r");
  yyparse();
  return;
}
