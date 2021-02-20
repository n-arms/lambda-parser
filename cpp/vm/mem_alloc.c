
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
