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

}