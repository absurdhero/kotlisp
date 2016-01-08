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

}