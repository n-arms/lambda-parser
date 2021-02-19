%{
  #include <stdio.h>
  #include <stdlib.h>
  int yylex(void);
  void yyerror(char *);
%}

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
