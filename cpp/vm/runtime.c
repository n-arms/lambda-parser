#include <stdio.h>
#include <stdlib.h>
#include "runtime.h"

struct Block *heap;
FILE *executable;
unsigned usedMemory;
unsigned heapSize;
struct LinkedList used;
struct LinkedList freeList;
//let 0 be argument, 1 be application, 2 be abstraction and 3 be pointer

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
        push(&used, usedMemory);
        usedMemory++;
        cycle = 0;
      }
    }else{
      temp[count++] = (char) c;
    }
  }
  temp[count] = '\0';
  (heap+usedMemory) -> b_ = atoi(temp);
  push(&used, usedMemory);
  usedMemory++;
  free(temp);
}

unsigned nextNode(unsigned root){
  if ((heap+root) -> type_ != 3){
    return root;
  }else{
    return nextNode((heap+root) -> a_);
  }
}

unsigned copy(unsigned root, unsigned old, unsigned new){
  unsigned current = getBlock(&freeList, &used, &usedMemory);
  (heap+current) -> type_ = (heap+root) -> type_;
  switch ((heap+root) -> type_){
    case 0:
    (heap+current) -> a_ = (heap+root) -> a_;
    return current;
    case 1:
    (heap+current) -> a_ = copy((heap+root) -> a_, old, new);
    (heap+current) -> b_ = copy((heap+root) -> b_, old, new);
    return current;
    case 2:
    (heap+current) -> a_ = copy((heap+root) -> a_, old, new);
    (heap+current) -> b_ = copy((heap+root) -> b_, old, new);
    return current;
    case 3:
    if ((heap+root) -> a_ == old){
      (heap+current) -> a_ = new;
    }else{
      (heap+current) -> a_ = (heap+root) -> b_;
    }
    return current;
  }
  return 0;
}

unsigned betaReduce(unsigned func, unsigned arg){
  return copy((heap+func) -> b_, (heap+func) -> a_, arg);
}

void print(unsigned root){
  switch ((heap+root) -> type_){
    case 0:
    printf("%d", (heap+root) -> a_);
    break;
    case 1:
    printf("( ");
    print((heap+root) -> a_);
    printf(" ");
    print((heap+root) -> b_);
    printf(" )");
    break;
    case 2:
    printf("|");
    print((heap+root) -> a_);
    printf(".");
    print((heap+root) -> b_);
    break;
    case 3:
    print((heap+root) -> a_);
    break;
  }
}

unsigned evaluate(unsigned root){
  printf("evaluating at root %u\n", root);
  print(root);
  printf("\n");
  unsigned left = nextNode((heap+root) -> a_);
  switch ((heap+root) -> type_){
    case 0:
    printf("found arg with value %u\n", (root+heap) -> a_);
    return root;
    case 1:
    printf("found application\n");
    if ((left+heap) -> type_ == 2){
      printf("found function on left side\n");
      (heap+root) -> type_ = 3;
      (heap+root) -> a_ = betaReduce(left, (heap+root) -> b_);
      print(root);
      printf("\n");
      evaluate(root);
      return root;
    }else{
      printf("missed function on ls, falling down tree\n");
      evaluate((heap+root) -> a_);
      evaluate(root);
      return root;
    }
    break;
    case 2:
    printf("found lambda, returning\n");
    return root;
    case 3:
    printf("found pointer\n");
    evaluate((heap+root) -> a_);
    return root;
    break;
  }
  return -1;
}

void printList(struct LinkedList* l){
  struct ListNode *i = l->first_;
  while (i != NULL){
    printf("%d ", i->value_);
    i = i->next_;
  }
  printf("\n");
}

/*
  execution algorithm:
  - as long as the current is an application, fall left and add a pointer to the cell to the stack
  - as soon as you reach a lambda, replace the application with a copy of the lambda body beta reduced with a pointer to the argument
  - as soon as you reach an argument, reverse 1 up the stack
  - continue the process.
*/



int main(){
  freeList.first_ = NULL;
  used.first_ = NULL;
  executable = fopen("../example.heap", "r");
  buildHeap(100);
  printf("\ncurrent memory used: %u\n", usedMemory);
  for (int i = 0; i<usedMemory; i++){
    printf("(%d: %d, %d)\n", (heap+i) -> type_, (heap+i) -> a_, (heap+i) -> b_);
  }

  printf("\n");
  printList(&freeList);
  printList(&used);
  print(evaluate(usedMemory-1));
  printf("\n\n");
  for (int i = 0; i<usedMemory; i++){
    printf("(%d: %d, %d)\n", (heap+i) -> type_, (heap+i) -> a_, (heap+i) -> b_);
  }

  free(heap);
  return 0;
}
