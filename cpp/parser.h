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
  //let 0 be argument, 1 be application, 2 be abstraction, 3 be pointer, 4 be list, 5 be null, 6 be integer
} ;

/*
0 - 9: built in types
  0: argument
  1: application
  2: abstraction -> also to be used as the check-match operator in pattern matching
  3: pointer
  4: list
  5: null
  6: integer
  7: boolean (yay, the most wasteful data type yet!)
  8: floating point magic?
  9: character (instead of using arguments as strings, this will provide compatibility to pattern matching)

10 - 15: pattern matching
  10: error -> to be returned on a failed pattern match
  11: fail -> to be returned on a failed instance of | (doesnt necessarily represent a failed match)
  12: infinix | operator -> essentially a switch statement for pattern matching

16 - 25: arithmetic operators (prefix)
  16: addition
  17: subtraction
  18: multiplication
  19: division
  20: equality
  21: modulo
  22: floor division?
  23: arrow operator
*/

struct AbstractBlock* compileHeap; //the heap used at compile time
unsigned compileHeapSize; //the size of the compile time heap
