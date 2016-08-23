package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LambdaTest : EvalHarness() {

    @Test fun numberOfArguments() {
        eval("(def {plusone} (\\ {a} {+ a 1}))")
        assertEquals("3", eval("(plusone 2)"))
    }

    @Test fun argumentOrder() {
        eval("(def {minus} (\\ {a b} {- a b}))")
        assertEquals("90", eval("(minus 100 10)"))
    }

    @Test fun overrideBuiltin() {
        eval("(def {-} (\\ {a b} {+ a b}))")
        assertEquals("3", eval("(- 1 2)"))
    }

    @Test fun partialApplication() {
        eval("(def {add-mul} (\\ {x y} {+ x (* x y)}))")
        eval("(def {add-mul-ten} (add-mul 10))")
        assertEquals("510", eval("(add-mul-ten 50)"))
    }

    @Test fun multipleBodiesShareSingleEnvironment() {
        eval("(def {add-mul} (\\ {x y} {= {y} (* x y)} {+ x y}))")
        eval("(def {add-mul-ten} (add-mul 10))")
        assertEquals("510", eval("(add-mul-ten 50)"))
    }

    @Test fun overrideEnvironment() {
        eval("(def {plusone} (\\ {a} {+ a 1}))")
        eval("(def {a b} 100 10)")
        assertEquals("11", eval("(plusone b)"))
    }

    @Test fun checkArity() {
        eval("(def {plusone} (\\ {a} {+ a 1}))")

        assertFailsWith(IllegalArgumentException::class, {
            eval("(plusone 2 3)")
        })
    }

    @Test fun varargs() {
        eval("(fun {plus & args} {eval (join {+} args)})")
        assertEquals("5", eval("(plus 2 3)"))
        assertEquals("1", eval("(plus 1)"))
        assertEquals("10", eval("(plus 1 2 3 4)"))
        assertEquals("0", eval("(plus)"))
    }

    @Test fun recursion() {
        eval("""
        (fun {fib N}
          {if {or (eq N 0) (eq N 1)}
            {1}
            {+ (fib (- N 1)) (fib (- N 2))}
          }
        )
            """)

        assertEquals("1", eval("(fib 0)"))
        assertEquals("1", eval("(fib 1)"))
        assertEquals("2", eval("(fib 2)"))
        assertEquals("3", eval("(fib 3)"))
        assertEquals("5", eval("(fib 4)"))
    }
}