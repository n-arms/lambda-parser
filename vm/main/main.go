package main

import (
  "github.com/n-arms/lambda-parser/vm"
  "fmt"
)

func main() {
  r := run.NewRuntime("../../cpp/example.heap", 50)
  fmt.Println(r)
  r.Run()
  fmt.Println("new root:", r.GetRoot())
  fmt.Println(r)
}
