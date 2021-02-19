%{
  #include <stdio.h>
  #include <stdlib.h>
  int yylex(void);
  void yyerror(char *);
%}

%start program
%token DOT LAMBDA OPEN CLOSE UPPER LOWER

%%

program:
          |
          program line '\n'
          {
            printf("evaluated program line\n");
          }
          ;
line:     expr
          {
            //soon this will include let and letrecs
          }
          ;
arg:      LOWER;
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
