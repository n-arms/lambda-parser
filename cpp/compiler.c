#include <stdlib.h>
#include <stdio.h>
#include "parser.h"
#include "y.tab.h"

int main(){
  parseFile();
  FILE *fp = fopen("example.heap", "w");
  fprintf(fp, "%d,%d,%d", (compileHeap) -> type_, (compileHeap) -> a_, (compileHeap) -> b_);
  for (int i = 1; i<(compileHeapSize); i++){
    fprintf(fp, ",%d,%d,%d", (compileHeap+i) -> type_, (compileHeap+i) -> a_, (compileHeap+i) -> b_);
  }
  fclose(fp);
  return 0;
}
