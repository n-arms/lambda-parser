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
"true"  {
          return (YES);
        }
"false" {
          return (NO);
        }
[a-z]   {
          yylval = yytext[0] - 'a';
          return (LOWER);
        }
"|"     {
          return (LAMBDA);
        }
"."     {
          return ('.');
        }
"("     {
          return(OPEN);
        }
")"     {
          return (CLOSE);
        }
"\n"    ;
"\r"    ;
"="     {
          return ('=');
        }
"LET"   {
          return (LET);
        }
"IN"    {
          return (IN);
        }
"IF"    {
          return (IF);
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
"-"     {
          return ('-');
        }
"/"     {
          return ('/');
        }
"%"     {
          return ('%');
        }
"}"     ;

%%

int yywrap(void){
  return 1;
}
