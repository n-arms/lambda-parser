package main

/*
===== mem_alloc.go =====

To be used to implement GC and memory allocation

types
- block

constantss
- block kind enum

functions
- kindString
- getBlock

*/

const (
  nullBlock = byte(iota)
  numberBlock = byte(iota)
  argumentBlock = byte(iota)
  lambdaBlock = byte(iota)
  applicationBlock = byte(iota)
  pointerBlock = byte(iota)
  listBlock = byte(iota)
)

func kindString(kind byte) string {
  switch kind {
  case nullBlock:
    return "NULL"
  case numberBlock:
    return "number"
  case argumentBlock:
    return "argument"
  case lambdaBlock:
    return "lambda abstraction"
  case applicationBlock:
    return "application"
  case pointerBlock:
    return "pointer"
  case listBlock:
    return "list"
  }
  return ""
}

type block struct{
  kind byte
  left uint32
  right uint32
}

func getBlock(r *runtime) uint32{
  r.usedMemory ++
  return r.usedMemory - 1
}
