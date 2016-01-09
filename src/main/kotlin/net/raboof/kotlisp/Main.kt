package net.raboof.kotlisp

import jline.UnsupportedTerminal
import java.io.PrintWriter

fun main(args: Array<String>) {
    // a hack for IntelliJ on Windows
    jline.TerminalFactory.registerFlavor(jline.TerminalFactory.Flavor.WINDOWS, UnsupportedTerminal::class.java)

    val console = jline.console.ConsoleReader()

    console.prompt = "lisp> "

    val out = PrintWriter(console.output);

    val parser = LispParser()

    val env = CoreEnvironment()

    while (true) {
        val line = console.readLine() ?: break

        if (line.toLowerCase() == "exit") {
            break;
        }

        try {
            out.println(parser.evaluate(env, line)?.print() ?: "error: could not parse")
        } catch (e: RuntimeException) {
            out.println("error: " + e.message)
        }
    }
}