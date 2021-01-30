# Lambda Calculus Parser
Code intended to parser and evaluate lambda calculus and combinatory logic. Lambda calculus is a notation for pure functions, and is arguably the first purely function programming language.

This library is intended to:
* Parse basic lambda calculus
* Parse combinators and combinator definitions
* Parse extended lambda calculus

## The lambda calculus
In its basic form, the lambda calculus only has 1 kind of primitive: a function. Functions are represented as follows:\
```λ<argument>.<body>```

Function applications are represented as follows:\
```<funtion1> <value>```

We can represent functions with multiple parameters in a process called curring:\
```λa.λb.a```\
Represents a function that takes in 2 values and returns the first (also called the kestrel). This can we seen if we pass the kestrel some values:\
```(λa.λb.a) (p) (q) = (λb.p) (q) = p```

## The extended lambda calculus
To make curried functions easier to read, this library reduces curried functions down to a form not in the original lambda calculus:
```λa.λb.a = λab.a```