struct AbstractBlock; //a reduced version of the block used during runtime
/*
unsigned pointerOrAtomA;
unsigned pointerOrAtomB;
blockType type;
bit typeOfA;
bit typeOfB;
*/

struct Block* compileHeap; //the heap used at compile time
unsigned compileHeapSize; //the size of the compile time heap
