%{
  #include <stdio.h>
  #include <stdlib.h>
  int yylex(void);
  void yyerror(char *);
%}
%token DOT LAMBDA OPEN CLOSE UPPER LOWER
%%

space: " ";

%%
void yyerror(char *s){
  fprintf(stderr, "%s\n", s);
}

int main(){
  yyparse();
  return 0;
}
