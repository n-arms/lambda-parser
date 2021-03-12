%{
  #include <stdlib.h>
  #include <stdio.h>
  #include "y.tab.h"
  extern int yylval;
%}

%%

" "     {
          return ' ';
        }

[a-z]   {
          yylval = yytext[0] - 'a';
          return (LOWER);
        }
"|"     {
          return (LAMBDA);
        }
"."     {
          return (DOT);
        }
"("     {
          return(OPEN);
        }
")"     {
          return (CLOSE);
        }
"\n"    {
          return ('\n');
        }
"="     {
          return ('=');
        }
"LET"   {
          return (LET);
        }
"IN"    {
          return (IN);
        }
[A-Z]   {
          yylval = yytext[0] - 'A';
          return (UPPER);
        }
"\""    {
          return (QUOTE);
        }
[0-9]+  {
          yylval = atoi(yytext);
          return (NUM);
        }
":"     {
          return (':');
        }
"[]"    {
          return (NIL);
        }
"+"     {
          return ('+');
        }
"*"     {
          return ('*');
        }

%%

int yywrap(void){
  return 1;
}
