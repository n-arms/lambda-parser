%{
  #include <stdlib.h>
  #include <stdio.h>
  #include "y.tab.h"
  extern int yylval;
%}

%%

" " ;

[a-z] {
        yylval = yytext[0] - 'a';
        return (LOWER);
      }
[A-Z] {
        yylval = yytext[0] - 'A';
        return (UPPER);
      }
\\    {
        return (LAMBDA);
      }
"."   {
        return (DOT);
      }
"("   {
        return(OPEN);
      }
")"   {
        return (CLOSE);
      }
"\n"  {
        return ('\n');
      }

%%

int yywrap(void){
  return 1;
}
