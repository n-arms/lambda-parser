#include <stdlib.h>
#include <stdio.h>
#include "parser.h"
#include "y.tab.h"
#include "vm/runtime.h"

/*
  let an unsigned* be a "hashmap" of sorts where the index is the value from 0-25 of a given char and the unsigned value is the value
*/

void pointArg(unsigned root, unsigned* scope){
  unsigned* newScope;
  switch ((compileHeap+root) -> type_){
    case 0: //argument
    (compileHeap+root) -> type_ = 3;
    (compileHeap+root) -> a_ = *(scope+((unsigned)((compileHeap+root) -> a_) - 'a'));
    return;
    case 1: //application
    pointArg((compileHeap+root) -> a_, scope);
    pointArg((compileHeap+root) -> b_, scope);
    return;
    case 2: //abstraction
    newScope = (unsigned*) (malloc(26 * sizeof(unsigned)));
    for (int i = 0; i<26; i++)
    newScope[i] = scope[i];
    newScope[((compileHeap+root) -> a_ + compileHeap) -> a_ - 'a'] = (compileHeap+root) -> a_;
    pointArg((compileHeap+root) -> b_, newScope);
    return;
    case 3: //pointer
    pointArg((compileHeap+root) -> a_, scope);
    return;
  }
}

int main(){
  parseFile();
  pointArg(compileHeapSize-1, (unsigned*)(malloc(26*sizeof(unsigned))));

  FILE *fp = fopen("example.heap", "w");
  fprintf(fp, "%d,%d,%d", (compileHeap) -> type_, (compileHeap) -> a_, (compileHeap) -> b_);
  for (int i = 1; i<(compileHeapSize); i++){
    fprintf(fp, ",%d,%d,%d", (compileHeap+i) -> type_, (compileHeap+i) -> a_, (compileHeap+i) -> b_);
  }
  fclose(fp);

  printBlock(compileHeapSize-1);
  printf("\n");

  printf("printing heap with size %d\n", compileHeapSize);
  for (int i = 0; i<(compileHeapSize); i++){
    printf("%d: %d, %d\n", (compileHeap+i) -> type_, (compileHeap+i) -> a_, (compileHeap+i) -> b_);
  }
  //printBlock(compileHeapSize-1);
  return 0;
}
