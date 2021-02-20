#include <stdio.h>
struct ListNode{
  unsigned value_;
  struct ListNode *next_;
} ;
struct Block{
  unsigned a_;
  unsigned b_;
  unsigned short type_; //where the last bit represents the mark
} ;
void garbageCollection(struct ListNode free, struct ListNode used, unsigned root);
unsigned getBlock();
void eval(FILE *fp);
