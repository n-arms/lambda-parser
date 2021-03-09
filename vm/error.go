package main

type errorLog struct {
  errors []lambdaError
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
