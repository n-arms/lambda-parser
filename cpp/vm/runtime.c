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

void buildHeap(unsigned size){
  heapSize = size;
  int c;
  unsigned cycle = 0;
  char *temp = (char *) (malloc(10*sizeof(char)));
  unsigned count = 0;

  usedMemory = 0;
  heap = (struct Block*) (malloc(size * sizeof(struct Block)));

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
    case 4:
    printf("%c", (heap+root) -> a_);
    print((heap+root) -> b_);
    break;
    case 5:
    break;
    case 6:
    printf("%d", (heap+root) -> a_);
    break;
  }
}

unsigned copy(unsigned root, unsigned old, unsigned new){
  unsigned current = getBlock(&freeList, &used, &usedMemory, heapSize);
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
      printf("found matching pointer while beta reducing\n");
      (heap+current) -> a_ = new;
    }else{
      printf("found non-matching pointer while beta reducing\n");
      (heap+current) -> a_ = (heap+root) -> a_;
    }
    return current;
    case 4:
    return root;
    case 5:
    return root;
    case 6:
    return root;
  }
  return 0;
}

unsigned betaReduce(unsigned func, unsigned arg){
  unsigned output = copy((heap+func) -> b_, (heap+func) -> a_, arg);
  printf("returning after beta reduction: ");
  print(output);
  printf("\n\n");
  return output;
}

unsigned evaluate(unsigned root){
  unsigned left = nextNode((heap+root) -> a_);
  switch ((heap+root) -> type_){
    case 0:
    return root; //found arg, returning up chain
    case 1:
    if ((left+heap) -> type_ == 2){ //the far left is a function
      printf("before beta reduction: ");
      print(root);
      printf("\n");
      unsigned output = betaReduce(left, (heap+root) -> b_);
      (heap+root) -> type_ = 3;
      (heap+root) -> a_ = output;
      return evaluate(output);
    }else{
      (heap+root) -> a_ = evaluate((heap+root) -> a_);
      return evaluate(root);
    }
    break;
    case 2:
    return root;
    case 3:
    return evaluate((heap+root) -> a_);
  }
  return -1;
}

unsigned evalBlock(unsigned root){
  printf("evaluating block %d: ", root);
  print(root);
  printf("\n");
  unsigned output = getBlock(&freeList, &used, &usedMemory, heapSize);
  (heap+output) -> type_ = (heap+root) -> type_;
  unsigned left = nextNode((heap+root) -> a_);
  switch ((heap+root) -> type_){
    case 0: //arg
    printf("found arg, returning\n");
    return root;

    case 1: //app
    printf("found application ");
    if ((left + heap) -> type_ == 2){
      printf("found beta-reducable statement, beta reducing\n");
      return betaReduce(left, (heap+root) -> b_);
    }else{
      printf("found nested application, falling down chain\n");
      (heap+output) -> a_ = evalBlock((heap+root) -> a_);
      (heap+output) -> b_ = (heap+root) -> b_;
      if (((heap+output) -> a_ + heap) -> type_ == 2){
        return evalBlock(output);
      }else{
        (heap+output) -> b_ = evalBlock((heap+output) -> b_);
        return output;
      }
    }
    case 2: //abstr
    printf("found anstraction, returning\n");
    return root;

    case 3: //pointer
    printf("found pointer, evaluating\n");
    (heap+output) -> a_ = evalBlock((heap+root) -> a_);
    return output;

    case 4: //string
    printf("found string, returning\n");
    return root;

    case 5: //null
    printf("found null, returning\n");
    return root;

    case 6:
    printf("found integer, returning\n");
    return root;

    default:
    printf("illegal type %d on evalBlock\n", (heap+root) -> type_);
    return 5;
  }
}

void printList(struct LinkedList* l){
  struct ListNode *i = l->first_;
  while (i != NULL){
    printf("%d ", i->value_);
    i = i->next_;
  }
  printf("\n");
}

void printHeap(){
  for (int i = 0; i<usedMemory; i++){
    printf("(%d: %d, %d): ", (heap+i) -> type_, (heap+i) -> a_, (heap+i) -> b_);
    print(i);
    printf("\n");
  }
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
  buildHeap(1000);

  unsigned result = evalBlock(usedMemory-1);
  print(result);
  printf("\n\n");
  printHeap();
  return 0;
}
