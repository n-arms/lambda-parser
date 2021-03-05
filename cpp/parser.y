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
    }else{
      return;
    }
  }
%}

%start program
%token DOT LAMBDA OPEN CLOSE UPPER LOWER LET IN QUOTE

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
line:     expr
          |
          LET block '\n' IN '\n' expr
          {
            
          }
          ;
arg:      LOWER
          {
            $$ = $1+97;
          };
block:
          |
          block '\n' arg '=' expr
          |
          block '\n' arg '=' string
          ;
expr:     arg
          {
            (compileHeap+compileHeapSize) -> a_ = $1;
            (compileHeap+compileHeapSize) -> type_ = 0;
            compileHeapSize++;
            $$ = compileHeapSize-1;
          }
          |
          OPEN expr expr CLOSE
          {
            (compileHeap+compileHeapSize) -> type_ = 1;
            (compileHeap+compileHeapSize) -> a_ = $2;
            (compileHeap+compileHeapSize) -> b_ = $3;
            compileHeapSize++;
            $$ = compileHeapSize-1;
          }
          |
          LAMBDA arg DOT expr
          {
            (compileHeap+compileHeapSize) -> type_ = 0;
            (compileHeap+compileHeapSize) -> a_ = $2;
            compileHeapSize++;
            (compileHeap+compileHeapSize) -> type_ = 2;
            (compileHeap+compileHeapSize) -> a_ = compileHeapSize-1;
            (compileHeap+compileHeapSize) -> b_ = $4;
            compileHeapSize++;
            $$ = compileHeapSize-1;
          }
          |
          OPEN expr CLOSE
          {
            $$ = $2;
          }
          ;
text:
          {
            (compileHeap+compileHeapSize) -> type_ = 5;
            compileHeapSize++;
            $$ = compileHeapSize-1;
          }
          |
          arg text
          {
            (compileHeap+compileHeapSize) -> type_ = 4;
            (compileHeap+compileHeapSize) -> a_ = $1;
            (compileHeap+compileHeapSize) -> b_ = $2;
            compileHeapSize++;
            $$ = compileHeapSize-1;
          }
          ;

string:   QUOTE text QUOTE
          {
            printf("found string\n");
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
