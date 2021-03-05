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
void addEnd(struct ListNode* a, struct ListNode* b);

struct Block{
  unsigned a_;
  unsigned b_;
  unsigned short type_; //where the last bit represents the mark
} ;

void garbageCollection(struct LinkedList* free, struct LinkedList* used, unsigned root, int prev, struct Block* heap);
unsigned getBlock(struct LinkedList* free, struct LinkedList* used, unsigned* usedMemory, unsigned heapSize);
void eval(FILE *fp);
