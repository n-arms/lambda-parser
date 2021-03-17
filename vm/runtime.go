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
  //the amount of memory allocated for the heap: going over this causes a segfault
  maxMemory uint32
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

func (r *Runtime) nextNonPointer(root uint32) uint32 {
  if r.getKind(root) != pointerBlock {
    return root
  }
  return r.nextNonPointer(r.getLeft(root))
}

func (r *Runtime) BlockString(root uint32) string {
  switch r.heap[root].kind {
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
  case booleanBlock:
    if r.getLeft(root) == 0 {
      return "false"
    }
    return "true"
  case refSaveBlock:
    return "\\" + r.BlockString(r.getLeft(root)) + "." + r.BlockString(r.getRight(root))
  }
  return kindString(r.getKind(root))
}

func (r *Runtime) String() string {
  output := []byte("RUNTIME\n")
  for _, value := range r.BlockString(r.root) {
    output = append(output, byte(value))
  }
  output = append(output, '\n')
  for i, value := range r.heap {
    for _, char := range strconv.FormatInt(int64(i), 10)+": "+kindString(value.kind)+" "+strconv.FormatUint(uint64(value.left), 10)+", "+strconv.FormatUint(uint64(value.right), 10)+"\n" {
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

func (r *Runtime) evalOp(root uint32){
  if r.getKind(root) == applicationBlock && r.getKind(r.getLeft(root)) == applicationBlock {
    reduce := func(){
      r.setRight(root, r.eval(r.getRight(root)))
      r.setRight(r.getLeft(root), r.eval(r.getRight(r.getLeft(root))))
    }
    isValid := func() bool{
      return r.getKind(r.getRight(root)) == numberBlock && r.getKind(r.getRight(r.getLeft(root))) == numberBlock
    }
    var temp bool
    switch r.getKind(r.getLeft(r.getLeft(root))) {
    case additionBlock:
      reduce()
      if isValid() {
        r.setKind(root, numberBlock)
        r.setLeft(root, r.getLeft(r.getRight(r.getLeft(root))) + r.getLeft(r.getRight(root)))
      }
    case multiplicationBlock:
      reduce()
      if isValid() {
        r.setKind(root, numberBlock)
        r.setLeft(root, r.getLeft(r.getRight(r.getLeft(root))) * r.getLeft(r.getRight(root)))
      }
    case subtractionBlock:
      reduce()
      if isValid() {
        r.setKind(root, numberBlock)
        r.setLeft(root, r.getLeft(r.getRight(r.getLeft(root))) - r.getLeft(r.getRight(root)))
      }
    case divisionBlock:
      reduce()
      if isValid() {
        r.setKind(root, numberBlock)
        r.setLeft(root, r.getLeft(r.getRight(r.getLeft(root))) / r.getLeft(r.getRight(root)))
      }
    case equalityBlock:
      reduce()
      if isValid() {
        r.setKind(root, booleanBlock)
        temp = r.getLeft(r.getRight(r.getLeft(root))) == r.getLeft(r.getRight(root))
        if temp {
          r.setLeft(root, 1)
        }else{
          r.setLeft(root, 0)
        }

      }
    case moduloBlock:
      reduce()
      if isValid() {
        r.setKind(root, numberBlock)
        r.setLeft(root, r.getLeft(r.getRight(r.getLeft(root))) % r.getLeft(r.getRight(root)))
      }
    default:
      return
    }
  }
}

func (r *Runtime) evalIf(root uint32) {
  if r.getKind(r.getLeft(root)) == ifBlock {
    r.setRight(root, r.eval(r.getRight(root)))
    if r.getKind(r.getRight(root)) != booleanBlock {
      return
    }
    temp := r.new(argumentBlock, 97, 0)
    if r.getLeft(r.getRight(root)) == 1 {
      r.set(root, lambdaBlock, temp, r.new(lambdaBlock, r.new(argumentBlock, 98, 0), r.new(pointerBlock, temp, 0)))
    }else{
      r.set(root, lambdaBlock, r.new(argumentBlock, 98, 0), r.new(lambdaBlock, temp, r.new(pointerBlock, temp, 0)))
    }
  }
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
    r.evalOp(root)
    if r.getKind(root) != applicationBlock {
      return root
    }
    r.evalIf(root)
    if r.getKind(r.getLeft(root)) == lambdaBlock {
      r.setKind(root, pointerBlock)
      r.setLeft(root, r.betaReduce(r.getLeft(root), r.getRight(root)))
      return r.eval(root)
    }else if r.getKind(r.getLeft(root)) == refSaveBlock {
      r.set(r.getLeft(r.getLeft(root)), pointerBlock, r.getRight(root), 0)
      return r.eval(r.getRight(r.getLeft(root)))
    }else {
      r.setLeft(root, r.eval(r.getLeft(root)))
      r.setLeft(root, r.nextNonPointer(r.getLeft(root)))
      r.setRight(root, r.nextNonPointer(r.getRight(root)))
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
  case booleanBlock:
    return root
  case refSaveBlock:
    return root
  case additionBlock:
    return root
  case multiplicationBlock:
    return root
  case subtractionBlock:
    return root
  case divisionBlock:
    return root
  case equalityBlock:
    return root
  case moduloBlock:
    return root
  case ifBlock:
    return root
  }
  r.errors.fatal(vmError{title: "Illegal Kind during runtime evaluation", desc: "Found kind "+strconv.FormatInt(int64(r.getKind(root)), 10) + " at position "+strconv.FormatUint(uint64(root), 10), blocking: true})
  return 0
}

func (r *Runtime) betaReduce(function uint32, arg uint32) uint32 {
  argBlock := getBlock(r)
  r.setKind(argBlock, pointerBlock)
  r.setLeft(argBlock, arg)
  var scope = map[uint32]uint32 {
    r.getLeft(function): argBlock,
  }
  funcCopy, _ := r.copy(r.getRight(function), scope)
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
    return newBlock, scope
  case lambdaBlock:
    return r.copyAppliLambda(root, scope, newBlock)
  case applicationBlock:
    return r.copyAppliLambda(root, scope, newBlock)
  case pointerBlock:
    if _, ok := scope[r.getLeft(root)]; ok {
      r.setLeft(newBlock, scope[r.getLeft(root)])
      return newBlock, scope
    }
    scope[r.getLeft(root)], scope = r.copy(r.getLeft(root), scope)
    r.setLeft(newBlock, scope[r.getLeft(root)])
    return newBlock, scope
  case listBlock:
    return root, scope
  case refSaveBlock:
    return r.copyAppliLambda(root, scope, newBlock)
  case booleanBlock:
    return root, scope
  case additionBlock:
    return root, scope
  case multiplicationBlock:
    return root, scope
  case divisionBlock:
    return root, scope
  case subtractionBlock:
    return root, scope
  case equalityBlock:
    return root, scope
  case moduloBlock:
    return root, scope
  case ifBlock:
    return root, scope
  }
  r.errors.fatal(vmError{title: "Illegal Kind during runtime evaluation", desc: "Found kind "+strconv.FormatInt(int64(r.getKind(root)), 10) + " at position "+strconv.FormatUint(uint64(root), 10), blocking: true})
  return 0, scope
}

func (r *Runtime) Run() {
  r.root = r.eval(r.root)
  //r.root, _ = r.copy(r.root, make(map[uint32]uint32))
}

func (r *Runtime) GetHeap() []string {
  output := make([]string, len(r.heap))
  for index, _ := range r.heap {
    output = append(output, kindString(r.getKind(uint32(index)))+fmt.Sprintf(": {%d, %d}", r.getLeft(uint32(index)), r.getRight(uint32(index))))
  }
  return output
}
