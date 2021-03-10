package main

import (
  "github.com/n-arms/lambda-parser/vm"
  "fmt"
)

func main() {
  r := run.NewRuntime("../../cpp/example.heap", 100)
  fmt.Println(r)
}
