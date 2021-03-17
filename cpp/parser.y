%{
  #include <stdio.h>
  #include <stdlib.h>
  #include "parser.h"
  int yylex(void);
  void yyerror(char *);
  extern FILE* yyin;

  void printBlock(unsigned head){
    if ((compileHeap+head) -> type_ == 0){
      printf("%c", (char)((compileHeap+head) -> a_));
    }else if ((compileHeap+head) -> type_ == 1){
      printf("( ");
      printBlock((compileHeap+head) -> a_);
      printf(" ");
      printBlock((compileHeap+head) -> b_);
      printf(" )");
    }else if ((compileHeap+head) -> type_ == 2){
      printf("|");
      printBlock((compileHeap+head) -> a_);
      printf(".");
      printBlock((compileHeap+head) -> b_);
    }else if ((compileHeap+head) -> type_ == 3){
      printBlock((compileHeap+head) -> a_);
    }else if ((compileHeap+head) -> type_ == 4){
      printf("%c", (char)(((compileHeap+head) -> a_ + compileHeap) -> a_));
      printBlock((compileHeap+head) -> b_);
    }else if ((compileHeap+head) -> type_ == 5){
      return;
    }else if ((compileHeap+head) -> type_ == 6){
      printf("%d", (compileHeap+head) -> a_);
    }
  }

  unsigned newBlock(unsigned kind, unsigned a, unsigned b) {
    (compileHeap+compileHeapSize) -> type_ = kind;
    (compileHeap+compileHeapSize) -> a_ = a;
    (compileHeap+compileHeapSize) -> b_ = b;
    compileHeapSize++;
    return compileHeapSize-1;
  }
%}

%left ' '
%right ':'
%start program
%token DOT LAMBDA OPEN CLOSE UPPER LOWER LET IN QUOTE NUM NIL


%%

program:
            |
            program line '\n'
            |
            program error '\n'
            {
              yyerrok;
            }
            ;
line:       expr
            |
            LET '\n' arg '=' expr '\n' IN '\n' expr
            {
              newBlock(1, newBlock(2, newBlock(0, $3, 0), $9), $5);
            }
            ;
arg:        LOWER
            {
              $$ = $1+97;
            };
expr:       arg
            {
              $$ = newBlock(0, $1, 0);
            }
            |
            expr ' ' expr
            {
              $$ = newBlock(1, $1, $3);
            }
            |
            OPEN LAMBDA arg DOT expr CLOSE
            {
              $$ = newBlock(2, newBlock(0, $3, 0), $5);
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
            OPEN list CLOSE
            {
              $$ = $2;
            }
            |
            OPEN operator expr ' ' expr CLOSE
            {
              $$ = newBlock(1, newBlock(1, $2, $3), $5);
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
list:       NIL
            {
              $$ = newBlock(5, 0, 0);
            }
            |
            expr ':' list
            {
              $$ = newBlock(4, $1, $3);
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
            ;
%%
void yyerror(char *s){
  fprintf(stderr, "%s\n", s);
}

void setup(){
  compileHeap = (struct AbstractBlock*) (malloc(100*sizeof(struct AbstractBlock)));
  compileHeapSize = 0;
}

void parseFile(){
  setup();
  yyin = fopen("example.txt", "r");
  yyparse();
  return;
}
