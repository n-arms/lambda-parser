package main

/*
===== error.go =====

To be used to deal with any errors encountered during the runtime of the VM

types
* errorLog
* lambdaError
* vmError

functions
* isBlocking
* getTitle
* String

*/

import (
  "strconv"
)

type errorLog struct {
  errors []lambdaError
}

func (e *errorLog) add(err lambdaError, blocking bool) {
  e.errors = append(e.errors, err)
  if blocking && err.isBlocking() {
    panic(e.dump())
  }
}

func (e *errorLog) dump() string {
  var output string
  for index, value := range e.errors {
    output = output + "error " + strconv.Itoa(index+1) + "\n" + value.String()
  }
  return output
}

func (e *errorLog) check(){
  for _, value := range e.errors {
    if value.isBlocking() {
      panic(e.dump())
    }
  }
}

type lambdaError interface {
  isBlocking() bool
  getTitle() string
  String() string
}

type vmError struct {
  title string
  desc string
  blocking bool
}

func (v *vmError) isBlocking() bool {
  return v.blocking
}

func (v *vmError) getTitle() string {
  return v.title
}

func (v *vmError) String() string {
  return v.title + "\n" + v.desc
}
