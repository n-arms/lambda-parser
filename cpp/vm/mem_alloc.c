
#include <stdlib.h>
#include <stdio.h>
#include "runtime.h"

unsigned top(struct LinkedList* l){
  return l->first_ -> value_;
}

void pop(struct LinkedList* l){
  struct ListNode* newFirst = l->first_->next_;
  free(l->first_);
  l->first_ = newFirst;
}

void push(struct LinkedList* l, unsigned u){
  struct ListNode* oldFirst = l->first_;
  l->first_ = (struct ListNode*)(malloc(sizeof(struct ListNode)));
  l->first_ -> value_ = u;
  l->first_ -> next_ = oldFirst;
}

void addEnd(struct ListNode* a, struct ListNode* b){
  if (a->next_ == NULL){
    a->next_ = b;
  }else{
    addEnd(a->next_, b);
  }
}

void segfault(){
  printf("SEGFAULT\n");
  fflush(stdout);
  exit(1);
}

unsigned getBlock(struct LinkedList* free, struct LinkedList* used, unsigned* usedMemory, unsigned heapSize){
  if ((*usedMemory) >= heapSize){
    segfault();
    return 0;
  }

  if (free -> first_ != NULL){
    unsigned output = top(free);
    pop(free);
    push(used, output);
    return output;
  }else{
    *usedMemory = *usedMemory + 1;
    push(used, (*usedMemory)-1);
    return (*usedMemory) -1;
  }
}

void mark (struct Block* heap, unsigned root, int prev){
  if (prev == 0){
    (heap+root) -> type_ |= (unsigned short)(1<<15);
  }else{
    (heap+root) -> type_ &=~((unsigned short)(1<<15));
  }

  switch ((heap+root) -> type_){
    case 0:
    return;
    case 1:
    mark(heap, (heap+root) -> a_, prev);
    mark(heap, (heap+root) -> b_, prev);
    return;
    case 2:
    mark(heap, (heap+root) -> a_, prev);
    mark(heap, (heap+root) -> b_, prev);
    return;
    case 3:
    mark(heap, (heap+root) -> a_, prev);
    return;
  }
}

void garbageCollection(struct LinkedList* free, struct LinkedList* used, unsigned root, int prev, struct Block* heap){
  mark(heap, root, prev);
  struct ListNode* current = used -> first_;
  while (current != NULL){
    if (((current -> value_) >> 15) != prev){
      
    }
  }
}
