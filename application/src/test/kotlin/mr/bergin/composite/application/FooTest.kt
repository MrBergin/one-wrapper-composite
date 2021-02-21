package mr.bergin.composite.application

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FooTest {
    @Test
    fun `bar is bigger than one`() {
        assertTrue(Foo().bar() > 1)
    }
}