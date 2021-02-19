#include <stdlib.h>
#include <stdio.h>
#include "parser.h"
#include "y.tab.h"

int main(){
  parseFile();
  for (int i = 0; i<compileHeapSize; i++){
    printf("(%d: %d, %d)\n", (compileHeap+i) -> type_, (compileHeap+i) -> a_, (compileHeap+i) -> b_);
  }
  return 0;
}
