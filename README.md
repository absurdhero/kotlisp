kotlisp
=======

A home-made lisp project built on my parser combinator library,
[parsekt](https://github.com/absurdhero/parsekt),
and based loosely on the neat book at http://www.buildyourownlisp.com .

You must `git clone` parsekt and run `mvn install` in order to
compile and run this project.

To run the REPL from the command line run

    mvn exec:java -Dexec.mainClass="net.raboof.kotlisp.MainKt"`

Or run Main.kt in your IDE.


language
--------

This language is a lisp-1 with lexical scoping. It follows some of the
interesting quirks in the Build Your Own Lisp book including q-expressions.
Instead of implementing quote and macros, there is a quoted lisp construct
built into the language denoted by curly braces instead of parenthesis.
Other than that, the language is more similar to scheme than common lisp.


### Features

##### Evaluation Rules

As in many lisps, both functions and data are represented as
lists surrounded by parenthesis.

When a list is evaluated, each item in the list is evaluated first.
The first item in the list must evaluate to a valid function.
The rest of the already-evaluated items in the list are passed to the function.

A function is created with the backslash (\) operator). Here is a function
of two arguments:
```lisp
(\ {arg1 arg2} {body})
```

Defining a function and giving it a name at the same time looks like:

```
{fun {name arg1 arg2} {body})
```

Notice how the arguments and body are not normal lists. They are surrounded
by curly braces instead of parenthesis. This changes the evaluation rules.
Instead of evaluating `{arg1 arg2}`, the evaluator leaves it alone; it evaluates
to itself. This is called a quoted list or q-expression. Other lisps implement
this feature by preceding a normal list with a single-quote character
or by using macros.

#### q-expressions

q-expressions allow for some interesting features without implementing
a quasi-quote or macro system. It isn't as powerful but it is
conceptually simple and avoids the need to implement an `expand` phase
in the evaluator.

Instead of writing out an argument list, we could instead
assemble our argument list {arg1 arg2} with code.

```lisp
(\ {arg1 arg2} {body})

; equivalent to the above:
(\ (map (\ {n} {symbol (concat "arg" n)}) {"1" "2"}) {body})
```

This concatenates "arg" with "1" and "2", and converts the strings to symbol names
suitable for use in an argument list.

If `{body}` were replaced with `{print arg2}`, then this function would print its
second argument. The symbol `arg2` is defined inside the function despite it not
appearing elsewhere in the code.

#### List manipulation

A list of some of the core list manipulation functions and their results:

`(first {a b c}) => a`

`(head {a b c}) => {a}`

`(rest {a b c}) => {b c}`

`(cons a {b c}) => {a b c}`

`(list a b c) => {a b c}`

`(join {a} {b c}) => {a b c}`

`(last {a b c}) => c`

`(nth 1 {a b c}) => b`

Convert a q-expression to an s-expression (i.e. unquote):

`(eval {a b c}) => (a b c)`

More:

`(nil? {}) => #t`

`(list? {x}) => #t`

#### Logic

As in scheme, true is `#t` and false is `#f`.

`(if {cond} {q-expr} {q-expr})` - if condition with an optional else branch

`(and expr*)` - and condition which short-circuits on the first false expression

`(or expr*)` - or condition which short-circuits on the first true expression

`(eq arg1 arg2)` - check equality. Expressions are unfolded and equality is checked for each element

`(boolean? expr*)` - returns `#t` if all given expressions are booleans

#### Environments

There is a lexical environment and a dynamic environment.

When a function is invoked, its parameters are bound in a new environment
and are only visible to code written in the function including
any inner function definitions. This means that lexical environments
have a hierarchy based on function definitions nested in the code.
In addition to definining lexical symbols with function parameter names,
the `=` function can be used to define a symbol in the current scope.

`def` puts a symbol at the top of the lexical environment.
This is called the "global" environment. All of the built-in functions
exist there.

I'm still figuring out how I want dynamic environments to work.
For now, the dynamic environment can be accessed and modified using
the `dynamic` and `dynamic-def` functions.

`dynamic-fun` function is also defined for convenience.

#### IO

`read-line` - read a line from standard input

`print` - print to standard output

`error` - print a formatted error to standard output

`load` - evaluate a file and return the result of the last expression

#### Calling into java

Java interoperability is achieved with a few functions and some clever marshalling.

- `(ctor className params...)`: Constructs an object with any number of parameters
- `(. object methodName params...)`: invokes a method on an object
- `(field-get object propertyName)`: get an object field's value
- `(field-set object propertyName value)`: set an object field's value
- `(int number)`: coerce a long to a java int. This may go away with better marshalling.

The Lisp types are automatically translated into equivalent Java types.
That means that, for example, you can call a java method on a string literal
without having to construct a "java" string first:

```
(. "foo" "concat" "bar")
=> foobar
```

Constructing a java Socket is a more interesting example:

```lisp
(fun {make-socket host port} {ctor "java.net.Socket" host (int port)})
(make-socket "example.net" 80)
```

Examples
--------

Fibonacci:

```lisp
(fun {fib n} {
  if {or (eq n 0) (eq n 1)}
    {1}
    {+ (fib (- n 1)) (fib (- n 2))}
})
```

