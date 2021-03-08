package main

/*
===== runtime.go =====

To be used to implement the evaluation and reduction elements of the runtime

types
- runtime

functions
- getKind
- getLeft
- getRight
- set
- new
- blockString
*/

import (
  "fmt"
  "strconv"
)

type runtime struct {
  heap []block
  usedMemory uint32
}

func (r *runtime) getKind(root uint32) byte {
  return r.heap[root].kind
}

func (r *runtime) getLeft(root uint32) uint32 {
  return r.heap[root].left
}

func (r *runtime) getRight(root uint32) uint32 {
  return r.heap[root].right
}

func (r *runtime) set(root uint32, kind byte, left uint32, right uint32) uint32 {
  r.heap[root] = block{kind: kind, left: left, right: right}
  return root
}

func (r *runtime) new(kind byte, left uint32, right uint32) uint32 {
  return r.set(getBlock(r), kind, left, right)
}

func (r *runtime) blockString(root uint32) string {
  switch r.heap[root].kind {
  case nullBlock:
    return "NULL"
  case numberBlock:
    return strconv.FormatUint(uint64(r.getLeft(root)), 10)
  case argumentBlock:
    return string(r.getLeft(root))
  case lambdaBlock:
    return "|" + r.blockString(r.getLeft(root)) + "." + r.blockString(r.getRight(root))
  case applicationBlock:
    return "("+r.blockString(r.getLeft(root)) + " " + r.blockString(r.getRight(root)) + ")"
  case pointerBlock:
    return r.blockString(r.getLeft(root))
  case listBlock:
    return r.blockString(r.getLeft(root)) + ":" + r.blockString(r.getRight(root))
  }
  return ""
}

func main() {
  r := runtime{heap: make([]block, 10), usedMemory: 0}
  r.new(argumentBlock, 97, 0)
  r.new(argumentBlock, 98, 0)
  r.new(applicationBlock, 0, 1)
  fmt.Println(r.blockString(2))
}
