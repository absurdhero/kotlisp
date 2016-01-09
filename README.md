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
Other than that, the language is most similar to scheme.

examples
--------

Fibonacci:

```lisp
(fun {fib N}
     {if {or (eq N 0) (eq N 1)}
       {1}
       {+ (fib (- N 1)) (fib (- N 2))}
     }
)
```

