#include <stdio.h>
#include <stdlib.h>
#include "runtime.h"

struct Block *heap;
FILE *executable;
unsigned usedMemory;

void buildHeap(unsigned heapSize){
  int c;
  unsigned cycle = 0;
  char *temp = (char *) (malloc(10*sizeof(char)));
  unsigned count = 0;

  usedMemory = 0;
  heap = (struct Block*) (malloc(heapSize * sizeof(struct Block)));

  while ((c = fgetc(executable)) != EOF){
    if (((char)c) == ','){
      temp[count] = '\0';
      count = 0;
      if (cycle == 0){
        (heap+usedMemory) -> type_ = atoi(temp);
        cycle++;
      }else if (cycle == 1){
        (heap+usedMemory) -> a_ = atoi(temp);
        cycle++;
      }else{
        (heap+usedMemory) -> b_ = atoi(temp);
        usedMemory++;
        cycle = 0;
      }
    }else{
      temp[count++] = (char) c;
    }
  }
}

int main(){
  executable = fopen("../example.heap", "r");
  buildHeap(100);
  for (int i = 0; i<3; i++){
    printf("(%d: %d, %d)\n", (heap+i) -> type_, (heap+i) -> a_, (heap+i) -> b_);
  }
  free(heap);
  return 0;
}
