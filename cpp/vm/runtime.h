//#ifndef RUNTIME_H
//#define RUNTIME_H
#include <stdio.h>
#include <stdlib.h>

struct ListNode{
  unsigned value_;
  struct ListNode *next_;
};
struct LinkedList{
  struct ListNode* first_;
};

unsigned top(struct LinkedList* l);
void pop(struct LinkedList* l);
void push(struct LinkedList* l, unsigned u);


struct Block{
  unsigned a_;
  unsigned b_;
  unsigned short type_; //where the last bit represents the mark
} ;

void garbageCollection(struct LinkedList* free, struct LinkedList* used, unsigned root);
unsigned getBlock(struct LinkedList* free, unsigned usedMemory);
void eval(FILE *fp);
//#endif
