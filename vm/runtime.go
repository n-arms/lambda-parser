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
