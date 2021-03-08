package main

import (
  "fmt"
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


func main() {
  r := runtime{heap: make([]block, 10), usedMemory: 0}
  r.set(0, argumentBlock, 97, 0)
  r.set(1, argumentBlock, 98, 0)
  r.set(2, applicationBlock, 0, 1)
  fmt.Println(kindString(r.getKind(2)))
}
