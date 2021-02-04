# Lambda Calculus Interpreter
This repository is a complete interpreter for classical untyped lambda calculus. It can work line by line via a bash shell, or interpret whole files on lambda calculus statements.

### Installation
* git clone this repository
* while in the root, run ```chmod +x lambda```

### Usage
To start an interpreter session, run ```./lambda``` while in the root. \
To interpret a file, run ```./lambda <filename>``` while in the root.\
By default when an error is found, it ends the interpreter session or stops executing the rest of the program. To supress this behaviour add the ```-g``` flag.

## Classical Untyped Lambda Calculus
Lambda calculus is a method of representing pure functions. In lambda calculus, the only primitive types are functions. 

### Function Definitions
Functions are defined in the form ```λ<argument>.<return>```\
For example: ```λx.x``` is a function that takes in some argument "x" and returns it. Parentheses can be used to clarify a function definition: ```λx.x = (λx.x) = λx.(x)```

### Function Applications
Functions are applied very simply: ```<function> <argument>```\
For example: ```(λx.x) q``` is a function that takes in an argument x and returns it, passed some argument q. To reduce this, we can return the body of the function, with every instance of x replaced with q. ```(λx.x) q = q```

### Functional Currying
In order to support functions with multiple paramters, a process called currying is employed. This is a where functions return *other functions*: ```λa.λb.a```\
If we pass this new curried function an argument, it returns another function: ```(λa.λb.a) (q) = λb.q```\
This syntax is somewhat convoluted, so this interpreter also can handle the following: ```λab.a```\
This is equivilant to ```λa.(λb.a)```

### Combinators
This interpreter allows something else not in classical lambda calculus: combinator definitions. This allows you to store values inside of "combinators", or variables, and use them more than once. When you use a combinator in your code, it searches for a definition. If one is found, it replaces the combinator with the defined value. Combinator names must be a single uppercase letter. Some example code using combinators:
```
K=λab.a
I=λx.x
K I p q
```