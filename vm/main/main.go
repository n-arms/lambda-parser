package main

import (
  "github.com/n-arms/lambda-parser/vm"
  "fmt"
  "os"
  "strconv"
)

func main() {
  heapSize, err := strconv.ParseUint(os.Args[2], 10, 32)
  if err != nil {
    fmt.Println("Illegal Number passed to HeapSize")
    os.Exit(1)
  }
  r := run.NewRuntime(os.Args[1], uint32(heapSize))
  fmt.Println(r)
  r.Run()
  fmt.Println("new root:", r.GetRoot())
  fmt.Println(r)
}
