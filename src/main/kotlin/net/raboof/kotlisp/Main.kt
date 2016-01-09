package net.raboof.kotlisp

import jline.UnsupportedTerminal
import java.io.InputStreamReader
import java.io.PrintWriter

fun main(args: Array<String>) {
    // a hack for IntelliJ on Windows
    jline.TerminalFactory.registerFlavor(jline.TerminalFactory.Flavor.WINDOWS, UnsupportedTerminal::class.java)

    val console = jline.console.ConsoleReader()

    console.prompt = "lisp> "

    val out = PrintWriter(console.output)

    // hook up built-in io functions to the console
    net.raboof.kotlisp.builtins.out = out
    net.raboof.kotlisp.builtins.input = InputStreamReader(console.input).buffered()

    val parser = LispParser()

    val env = CoreEnvironment()

    while (true) {
        val line = console.readLine() ?: break

        if (line.toLowerCase() == "exit") {
            break
        }

        try {
            val result = parser.evaluate(env, line)
            if (result != null && result != SExpression.Empty)
                out.println(result.print())
            else if (result == null)
                out.println("error: could not parse")
        } catch (e: RuntimeException) {
            out.println("error: " + e.message)
        }
    }
}