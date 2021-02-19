void setup();
void parseFile();
void printBlock(unsigned head);
struct AbstractBlock; //a reduced version of the block used during runtime
/*
unsigned pointerOrAtomA;
unsigned pointerOrAtomB;
blockType type;
bit typeOfA;
bit typeOfB;
*/
struct AbstractBlock{
  unsigned a_;
  unsigned b_;
  unsigned char type_;
  //let 0 be argument, 1 be application, 2 be abstraction and 3 be pointer
} ;

struct AbstractBlock* compileHeap; //the heap used at compile time
unsigned compileHeapSize; //the size of the compile time heap
