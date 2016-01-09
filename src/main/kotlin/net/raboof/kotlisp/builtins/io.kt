package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*
import java.io.*


// A program that embeds this library can be set in or out to something else
// For example, tests might capture output or a program might output to its own buffer
var out : Writer = PrintWriter(System.out)
var input : BufferedReader = InputStreamReader(System.`in`).buffered()

val load = Builtin("load") { env, rest ->
    assertLength(rest, 1)
    val path = assertType<Str>(rest.first()).value
    LispParser().evaluate(env, FileInputStream(path).reader().readText()) ?: False
}

val readLine = Builtin("read-line") { env, rest ->
    Str(input.readLine())
}

val printString = Builtin("print-string") { env, rest ->
    assertLength(rest, 1)
    Str(rest.first().print())
}

val print = Builtin("print") { env, rest ->
    assertLength(rest, 1)
    out.write(rest.first().print() + "\n")
    SExpression.Empty
}

val error = Builtin("error") { env, rest ->
    assertLength(rest, 1)
    val msg = assertType<Str>(rest.first()).value
    out.write("error: $msg\n")
    SExpression.Empty
}

