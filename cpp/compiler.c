#include <stdlib.h>
#include <stdio.h>
#include "parser.h"
#include "y.tab.h"
#include "vm/runtime.h"

/*
  let an unsigned* be a "hashmap" of sorts where the index is the value from 0-25 of a given char and the unsigned value is the value
*/

void pointArg(unsigned root, unsigned* scope, unsigned char* used){
  unsigned* newScope;
  unsigned char* newUsed;
  switch ((compileHeap+root) -> type_){
    case 0: //argument
    if (used[((compileHeap+root) -> a_) - 'a'] == 1){
      (compileHeap+root) -> type_ = 3;
      (compileHeap+root) -> a_ = *(scope+((unsigned)((compileHeap+root) -> a_) - 'a'));
    }
    return;
    case 1: //application
    pointArg((compileHeap+root) -> a_, scope, used);
    pointArg((compileHeap+root) -> b_, scope, used);
    return;
    case 2: //abstraction
    newScope = (unsigned*) (malloc(26 * sizeof(unsigned)));
    newUsed = (unsigned char*) (malloc(26 * sizeof(unsigned char)));
    for (int i = 0; i<26; i++){
      newScope[i] = scope[i];
      newUsed[i] = used[i];
    }
    newScope[((compileHeap+root) -> a_ + compileHeap) -> a_ - 'a'] = (compileHeap+root) -> a_;
    newUsed[((compileHeap+root) -> a_ + compileHeap) -> a_ - 'a'] = 1;
    pointArg((compileHeap+root) -> b_, newScope, newUsed);
    return;
    case 3: //pointer
    pointArg((compileHeap+root) -> a_, scope, used);
    return;
  }
}

int main(){
  parseFile();
  pointArg(compileHeapSize-1, (unsigned*)(malloc(26*sizeof(unsigned))), (unsigned char*)(malloc(26*sizeof(unsigned char))));

  FILE *fp = fopen("example.heap", "w");
  fprintf(fp, "%d,%d,%d", (compileHeap) -> type_, (compileHeap) -> a_, (compileHeap) -> b_);
  for (int i = 1; i<(compileHeapSize); i++){
    fprintf(fp, ",%d,%d,%d", (compileHeap+i) -> type_, (compileHeap+i) -> a_, (compileHeap+i) -> b_);
  }
  fclose(fp);
  return 0;
}
