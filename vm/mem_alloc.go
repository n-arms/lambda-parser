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

import (
  "io/ioutil"
  "strings"
  "strconv"
)

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

func newRuntime(heapFile string, heapSize uint32) *runtime {
  convertKind := map[string]string {
    "0": "2",
    "1": "4",
    "2": "3",
    "3": "5",
    "4": "6",
    "5": "0",
    "6": "1",
  }


  r := runtime{heap: make([]block, heapSize)}
  data, err := ioutil.ReadFile(heapFile)
  if err != nil {
    r.errors.fatal(vmError{title: "heap FNF", desc: "heap file was not at specified path", blocking: true})
  }

  checkHeap := func(err error, pos int){
    if err != nil {
      r.errors.add(vmError{title: "illegal heap", desc: "error in heap at field: "+strconv.FormatInt(int64(pos), 10),  blocking: false}, false)
    }
  }

  csv := strings.Split(string(data), ",")
  for i, value := range csv {
    if i%3 == 0 {
      parsed, err := strconv.ParseUint(convertKind[strings.TrimSpace(value)], 10, 8)
      r.usedMemory++
      r.setKind(uint32(i/3), byte(parsed))
      checkHeap(err, i)
    }else if i%3 == 1 {
      parsed, err := strconv.ParseUint(value, 10, 32)
      r.setLeft(uint32(i/3), uint32(parsed))
      checkHeap(err, i)
    }else {
      parsed, err := strconv.ParseUint(value, 10, 32)
      r.setRight(uint32(i/3), uint32(parsed))
      checkHeap(err, i)
    }
  }
  r.errors.check()
  return &r
}
