# Lambda Calculus Compiler
The goal here to is to build an entire functional programming language up from lambda calculus. Current features include numbers, operators, strings, lists and let statements. The language is compiled, and evaluated through a virtual machine. Features to come include:
* Explicitly recursive let statements
* Built in booleans
* Pattern matching with guards

## Classical Untyped Lambda Calculus
Lambda calculus is a method of representing pure functions. In lambda calculus, the only primitive types are functions.

### Function Definitions
Functions are defined in the form ```(|<argument>.<return>)```\
For example: ```(|x.x)``` is a function that takes in some argument "x" and returns it.

### Function Applications
Functions are applied very simply: ```<function> <argument>```\
For example: ```(|x.x) q``` is a function that takes in an argument x and returns it, passed some argument q. To reduce this, we can return the body of the function, with every instance of x replaced with q.

```
(|x.x) q
-> q
```

### Functional Currying
In order to support functions with multiple parameters, a process called currying is employed. This is a where functions return *other functions*: ```(|a.(|b.a))```\
If we pass this new curried function an argument, it returns another function: ```(|a.(|b.)a) q = |b.q```

## Custom Features
This compiler implements a number of features not in traditional lambda calculus.

### Lists
Lists are defined by either an element and a list, or an element and the "nil", or empty list.
```
(a:b:[])
```
and
```
(a b:c:(|a.a) b:[])
```
are both lists.

### Strings
Strings are simply a neater way to write lists of characters:
```
"this is a string"
```
or
```
"but really its just a list in disguise"
```
Note that only lower and upper case letters are allowed in strings as of right now.

### Numbers
Unsigned integers are very easy to work with:
```
42
```
or
```
(|a.a) 3
```

### Operators
Operators are treated like functions, and thus are prefix:
```
(+1 2)
```
or
```
(*3 (/1 2))
```
