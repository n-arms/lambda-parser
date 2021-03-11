package run

/*
===== Runtime.go =====

To be used to implement the evaluation and reduction elements of the runtime

types
- Runtime

functions
- getKind
- getLeft
- getRight
- set
- new
- BlockString
*/

import (
  "strconv"
  "fmt"
)

type Runtime struct {
  //the Runtime heap
  heap []block
  //increment usedMemory and use it to generate a new block if free is empty
  usedMemory uint32
  //a set of memory that was allocated and then garbage collected
  freeMemory map[uint32]struct{}
  //the error log
  errors errorLog
  //the point of entry for the heap
  root uint32
}

func (r *Runtime) getKind(root uint32) byte {
  return r.heap[root].kind
}

func (r *Runtime) getLeft(root uint32) uint32 {
  return r.heap[root].left
}

func (r *Runtime) getRight(root uint32) uint32 {
  return r.heap[root].right
}

func (r *Runtime) setKind(root uint32, kind byte) {
  r.heap[root].kind = kind
}

func (r *Runtime) setLeft(root uint32, value uint32) {
  r.heap[root].left = value
}

func (r *Runtime) setRight(root uint32, value uint32) {
  r.heap[root].right = value
}

func (r *Runtime) set(root uint32, kind byte, left uint32, right uint32) uint32 {
  r.heap[root] = block{kind: kind, left: left, right: right}
  return root
}

func (r *Runtime) new(kind byte, left uint32, right uint32) uint32 {
  return r.set(getBlock(r), kind, left, right)
}

func (r *Runtime) GetRoot() uint32 {
  return r.root
}

func (r *Runtime) setRoot(root uint32) {
  r.root = root
  }

func (r *Runtime) BlockString(root uint32) string {
  switch r.heap[root].kind {
  case nullBlock:
    return "NULL"
  case numberBlock:
    return strconv.FormatUint(uint64(r.getLeft(root)), 10)
  case argumentBlock:
    return string(r.getLeft(root))
  case lambdaBlock:
    return "|" + r.BlockString(r.getLeft(root)) + "." + r.BlockString(r.getRight(root))
  case applicationBlock:
    return "("+r.BlockString(r.getLeft(root)) + " " + r.BlockString(r.getRight(root)) + ")"
  case pointerBlock:
    return r.BlockString(r.getLeft(root))
  case listBlock:
    return r.BlockString(r.getLeft(root)) + ":" + r.BlockString(r.getRight(root))
  }
  return ""
}

func (r *Runtime) String() string {
  output := []byte("RUNTIME\n")
  for _, value := range r.BlockString(r.root) {
    output = append(output, byte(value))
  }
  output = append(output, '\n')
  for _, value := range r.heap {
    for _, char := range kindString(value.kind)+" "+strconv.FormatUint(uint64(value.left), 10)+", "+strconv.FormatUint(uint64(value.right), 10)+"\n" {
      output = append(output, byte(char))
    }
  }

  output = append(output, '\n')
  output = append(output, '\n')

  for key, _ := range r.freeMemory {
    output = append(output, byte(key))
    output = append(output, '\n')
  }

  return string(output)
}

func (r *Runtime) eval(root uint32) uint32 {
  switch r.getKind(root) {
  case nullBlock:
    return root
  case numberBlock:
    return root
  case argumentBlock:
    return root
  case lambdaBlock:
    return root
  case applicationBlock:
    if r.getKind(r.getLeft(root)) == lambdaBlock {
      r.setKind(root, pointerBlock)
      r.setLeft(root, r.betaReduce(r.getLeft(root), r.getRight(root)))
      return root
    }else {
      r.setLeft(root, r.eval(r.getLeft(root)))
      if r.getKind(r.getLeft(root)) == lambdaBlock {
        return r.eval(root)
      }
      return root
    }
  case pointerBlock:
    r.setLeft(root, r.eval(r.getLeft(root)))
    return r.getLeft(root)
  case listBlock:
    return root
  }
  r.errors.fatal(vmError{title: "Illegal Kind during runtime evaluation", desc: "Found kind "+strconv.FormatInt(int64(r.getKind(root)), 10) + " at position "+strconv.FormatUint(uint64(root), 10), blocking: true})
  return 0
}

func (r *Runtime) betaReduce(function uint32, arg uint32) uint32 {
  fmt.Printf("beta reducing at %d with arg %d\n", function, arg)
  funcCopy, _ := r.copy(r.getRight(function), make(map[uint32]uint32))
  r.setLeft(r.getLeft(funcCopy), arg)
  fmt.Println(r.BlockString(funcCopy))
  return funcCopy
}

func (r *Runtime) copyAppliLambda(root uint32, scope map[uint32]uint32, newBlock uint32) (newRoot uint32, newScope map[uint32]uint32) {
  /*
  1. check if the left value is in the scope
  2. if it is, use a pointer to that
  3. if it isnt do a copy on the left value, and set the value in the scope to that
  4. repeat on right side
  5. use the resulting scope as the return value, and use the same newBlock that was passed
  */

  var ls uint32
  var rs uint32

  if val, ok := scope[r.getLeft(root)]; ok {
    r.setLeft(newBlock, val)
  }else {
    ls, scope = r.copy(r.getLeft(root), scope)
    scope[r.getLeft(root)] = ls
    r.setLeft(newBlock, ls)
  }

  if val, ok := scope[r.getRight(root)]; ok {
    r.setRight(newBlock, val)
  }else {
    rs, scope = r.copy(r.getRight(root), scope)
    scope[r.getRight(root)] = rs
    r.setRight(newBlock, rs)
  }

  return newBlock, scope
}

func (r *Runtime) copy(root uint32, scope map[uint32]uint32) (newRoot uint32, newScope map[uint32]uint32) {
  newBlock := getBlock(r)
  r.setKind(newBlock, r.getKind(root))
  r.setLeft(newBlock, r.getLeft(root))

  switch r.getKind(root) {
  case nullBlock:
    return newBlock, scope
  case numberBlock:
    return newBlock, scope
  case argumentBlock:
    fmt.Println("copying argument, returning")
    return newBlock, scope
  case lambdaBlock:
    return r.copyAppliLambda(root, scope, newBlock)
  case applicationBlock:
    return r.copyAppliLambda(root, scope, newBlock)
  case pointerBlock:
    fmt.Print("copying pointer: ")
    if _, ok := scope[r.getLeft(root)]; ok {
      fmt.Println("setting from scope")
      r.setLeft(newBlock, scope[r.getLeft(root)])
      return newBlock, scope
    }
    fmt.Println("copying new pointer to scope")
    scope[r.getLeft(root)], scope = r.copy(r.getLeft(root), scope)
    r.setLeft(newBlock, scope[r.getLeft(root)])
    return newBlock, scope
  case listBlock:
    return root, scope
  }
  r.errors.fatal(vmError{title: "Illegal Kind during runtime evaluation", desc: "Found kind "+strconv.FormatInt(int64(r.getKind(root)), 10) + " at position "+strconv.FormatUint(uint64(root), 10), blocking: true})
  return 0, scope
}

func (r *Runtime) Run() {
  //r.root = r.eval(r.root)
  r.root, _ = r.copy(r.root, make(map[uint32]uint32))
}

func (r *Runtime) GetHeap() []string {
  output := make([]string, len(r.heap))
  for index, _ := range r.heap {
    output = append(output, kindString(r.getKind(uint32(index)))+fmt.Sprintf(": {%d, %d}", r.getLeft(uint32(index)), r.getRight(uint32(index))))
  }
  return output
}
