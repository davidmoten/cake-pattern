import org.junit._
import Assert._
import examples.example2._

@Test
class Example2Test {

  @Test
  def testThatInstancesOfCAreDifferent {
    println("a.c.value=" + Registry.a.c.value)
    println("b.c.value=" + Registry.b.c.value)
    assertFalse(Registry.a.c.value == Registry.b.c.value)
  }

}
