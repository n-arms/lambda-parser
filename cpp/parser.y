%{
  #include <stdio.h>
  #include <stdlib.h>
  int yylex(void);
  void yyerror(char *);
%}

%start program
%token DOT LAMBDA OPEN CLOSE UPPER LOWER LET IN

%%

program:
          |
          program line '\n'
          {
            printf("evaluated program line\n");
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
arg:      LOWER;
block:
          |
          block '\n' arg '=' expr
          ;
expr:     arg
          |
          OPEN expr expr CLOSE
          |
          LAMBDA arg DOT expr
          |
          OPEN expr CLOSE
          ;


%%
void yyerror(char *s){
  fprintf(stderr, "%s\n", s);
}

int main(){
  yyparse();
  return 0;
}
