import org.junit._
import Assert._
import examples.example1._

@Test
class Example1Test {
  
  @Test
  def demonstrateRuntimeUsageOfCakePattern{
    println("using production ComponentRegistry")
    println ("a.value="+ Registry.a.value)
    println ("b.value="+ Registry.b.value)
    
    println("using testing ComponentRegistry")
    println ("a.value="+ RegistryTesting.a.value)
    println ("b.value="+ RegistryTesting.b.value)
    
    assertEquals("a-production",Registry.a.value)
    assertEquals("a-test",RegistryTesting.a.value)
    assertEquals("a-production-b-production",Registry.b.value)
    assertEquals("a-test-b-test",RegistryTesting.b.value)
  } 
  
}