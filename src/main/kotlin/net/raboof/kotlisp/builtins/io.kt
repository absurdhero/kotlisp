package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*
import java.io.*


// A program that embeds this library can be set in or out to something else
// For example, tests might capture output or a program might output to its own buffer
var out : Writer = PrintWriter(System.out)
var input : BufferedReader = InputStreamReader(System.`in`).buffered()

val load = Builtin("load") { env, denv, rest ->
    assertLength(rest, 1)
    val path = assertType<Str>(rest.first()).value
    LispParser().evaluate(env, denv, FileInputStream(path).reader().readText()) ?: False
}

val readLine = Builtin("read-line") { env, denv, rest ->
    Str(input.readLine())
}

val printString = Builtin("print-string") { env, denv, rest ->
    assertLength(rest, 1)
    Str(rest.first().print())
}

val print = Builtin("print") { env, denv, rest ->
    assertLength(rest, 1)
    val arg = rest.first()
    when(arg) {
        is Str -> out.appendln(arg.value)
        else -> out.appendln(arg.print())
    }
    out.flush()
    QExpression.Empty
}

val error = Builtin("error") { env, denv, rest ->
    assertLength(rest, 1)
    val msg = assertType<Str>(rest.first()).value
    out.write("error: $msg\n")
    QExpression.Empty
}

