package run

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
  "sync"
)

const (
  nullBlock = byte(iota)
  numberBlock = byte(iota)
  argumentBlock = byte(iota)
  lambdaBlock = byte(iota)
  applicationBlock = byte(iota)
  pointerBlock = byte(iota)
  listBlock = byte(iota)
  additionBlock = byte(16)
  subtractionBlock = byte(17)
  multiplicationBlock = byte(18)
  divisionBlock = byte(19)
)

var convertKind = map[string]string {
  "0": "2",
  "1": "4",
  "2": "3",
  "3": "5",
  "4": "6",
  "5": "0",
  "6": "1",
  "16": "16",
  "17": "17",
  "18": "18",
  "19": "19",
}

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
  case additionBlock:
    return "+"
  case subtractionBlock:
    return "-"
  case multiplicationBlock:
    return "*"
  case divisionBlock:
    return "/"
  }
  return ""
}

type block struct{
  kind byte
  left uint32
  right uint32
}

func getBlock(r *Runtime) uint32{
  if len(r.freeMemory) == 0 {
    if r.usedMemory == r.maxMemory {
      r.errors.segFault(r.maxMemory)
    }
    r.usedMemory ++
    return r.usedMemory - 1
  }

  var output uint32

  for pos, _ := range r.freeMemory {
    // best method i can think of to get any abitrary element from a map
    output = pos
    break
  }
  delete(r.freeMemory, output)
  return output
}

func NewRuntime(heapFile string, heapSize uint32) *Runtime {
  r := Runtime{heap: make([]block, heapSize), maxMemory: heapSize}
  var wg sync.WaitGroup
  wg.Add(3)

  data, err := ioutil.ReadFile(heapFile)
  if err != nil {
    r.errors.fatal(vmError{title: "heap FNF", desc: "heap file was not at specified path", blocking: true})
  }

  csv := strings.Split(string(data), ",")

  go func(){
    for i := 0; i < len(csv); i += 3 {
      parsed, err := strconv.ParseUint(convertKind[csv[i]], 10, 8)
      r.usedMemory++
      r.setKind(uint32(i/3), byte(parsed))
      if err != nil {
        r.errors.add(vmError{title: "illegal heap kind", desc: "error in heap at field: "+strconv.FormatInt(int64(i), 10)})
      }
    }
    wg.Done()
  }()

  go func(){
    for i := 1; i < len(csv); i += 3 {
      parsed, err := strconv.ParseUint(csv[i], 10, 8)
      r.setLeft(uint32(i/3), uint32(parsed))
      if err != nil {
        r.errors.add(vmError{title: "illegal heap left", desc: "error in heap at field: "+strconv.FormatInt(int64(i), 10)})
      }
    }
    wg.Done()
  }()

  go func(){
    for i := 2; i < len(csv); i += 3 {
      parsed, err := strconv.ParseUint(csv[i], 10, 8)
      r.setRight(uint32(i/3), uint32(parsed))
      if err != nil {
        r.errors.add(vmError{title: "illegal heap right", desc: "error in heap at field: "+strconv.FormatInt(int64(i), 10)})
      }
    }
    wg.Done()
  }()

  wg.Wait()
  r.errors.check()
  r.root = r.usedMemory - 1
  return &r
}
