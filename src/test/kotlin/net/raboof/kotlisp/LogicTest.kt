import net.raboof.kotlisp.EvalHarness
import org.junit.Test
import kotlin.test.assertEquals

class LogicTest : EvalHarness() {

    @Test fun comparisons() {
        assertEquals("#t", eval("(> 2 -5)"))
        assertEquals("#f", eval("(> 2 3)"))
        assertEquals("#t", eval("(< 2 9)"))
        assertEquals("#t", eval("(>= 2 2)"))
        assertEquals("#t", eval("(>= 9 2)"))
        assertEquals("#t", eval("(<= 2 2)"))
        assertEquals("#t", eval("(<= 2 9)"))
    }

    @Test fun ifCondition() {
        assertEquals("1", eval("(if {#t} {1} {2})"))
        assertEquals("2", eval("(if {#f} {1} {2})"))
        assertEquals("1", eval("(if {< 5 10} {1} {2})"))
        assertEquals("2", eval("(if {> 5 10} {1} {2})"))
    }

    @Test fun equality() {
        assertTrue("(eq 1 1)")
        assertFalse("(eq 2 1)")

        assertTrue("(eq {{1}} {{1}})")
        assertFalse("(eq {{2}} {{1}})")

        assertTrue("(eq #f #f)")
        assertFalse("(eq #f #t)")

        assertTrue("(eq {a} {a})")
        assertFalse("(eq {a} {b})")
    }
}