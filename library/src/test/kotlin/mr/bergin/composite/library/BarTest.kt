package mr.bergin.composite.library

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BarTest {
    @Test
    fun `baz is bigger than one`() {
        assertTrue(Bar().baz() > 1)
    }
}