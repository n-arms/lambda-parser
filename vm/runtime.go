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
      r.setLeft(r.getLeft(root), r.betaReduce(r.getLeft(root), r.getRight(root)))
      return root
    }else {
      r.setLeft(r.getLeft(root), r.eval(r.getLeft(root)))
      if r.getKind(r.getLeft(root)) == lambdaBlock {
        return r.eval(root)
      }
      return root
    }
  case pointerBlock:
    r.setLeft(r.getLeft(root), r.eval(r.getLeft(root)))
    return r.getLeft(root)
  case listBlock:
    return root
  }
  r.errors.fatal(vmError{title: "Illegal Kind during runtime evaluation", desc: "Found kind "+strconv.FormatInt(int64(r.getKind(root)), 10) + " at position "+strconv.FormatUint(uint64(root), 10), blocking: true})
  return 0
}

func (r *Runtime) betaReduce(function uint32, arg uint32) uint32 {
  return 0
}

func (r *Runtime) copy(root uint32) uint32 {
  return 0
}

func (r *Runtime) Run() {
  r.root = r.eval(r.root)
}

func (r *Runtime) GetHeap() []string {
  output := make([]string, len(r.heap))
  for index, _ := range r.heap {
    output = append(output, kindString(r.getKind(uint32(index)))+fmt.Sprintf(": {%d, %d}", r.getLeft(uint32(index)), r.getRight(uint32(index))))
  }
  return output
}
