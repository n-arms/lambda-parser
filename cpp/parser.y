%{
  #include <stdio.h>
  #include <stdlib.h>
  #include "parser.h"
  int yylex(void);
  void yyerror(char *);

  struct AbstractBlock{
    unsigned a_;
    unsigned b_;
    unsigned char type_;
    //let 0 be argument, 1 be application, 2 be abstraction and 3 be pointer
  } ;

  void printBlock(unsigned head){
    if ((compileHeap+head) -> type_ == 0){
      printf("%c", (char)((compileHeap+head) -> a_));
    }else if ((compileHeap+head) -> type_ == 1){
      printf("( ");
      printBlock((compileHeap+head) -> a_);
      printf(" ");
      printBlock((compileHeap+head) -> b_);
      printf(" )");
    }else{
      printf("\\");
      printBlock((compileHeap+head) -> a_);
      printf(".");
      printBlock((compileHeap+head) -> b_);
    }
  }
%}

%start program
%token DOT LAMBDA OPEN CLOSE UPPER LOWER LET IN

%%

program:
          |
          program line '\n'
          {
            printBlock($2);
            printf("\n");
          }
          |
          program error '\n'
          {
            yyerrok;
          }
          ;
line:     expr
          |
          LET block '\n' IN '\n' expr
          ;
arg:      LOWER
          {
            $$ = $1+97;
          };
block:
          |
          block '\n' arg '=' expr
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

%%
void yyerror(char *s){
  fprintf(stderr, "%s\n", s);
}

int main(){
  compileHeap = (struct AbstractBlock*) (malloc(100*sizeof(struct AbstractBlock)));
  compileHeapSize = 0;
  yyparse();
  return 0;
}
