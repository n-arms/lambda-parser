%{
  #include <stdlib.h>
  #include <stdio.h>
  #include "y.tab.h"
  extern int yylval;
%}

%%

" " ;

%%

int yywrap(void){
  return 1;
}
