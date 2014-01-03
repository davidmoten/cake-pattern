import org.junit._
import Assert._

@Test
class CakeTest {
  
  @Test
  def demonstrateRuntimeUsageOfCakePattern{
    println("using production ComponentRegistry")
    println ("a.name="+ ComponentRegistry.a.name)
    println ("b.name="+ ComponentRegistry.b.name)
    
    println("using testing ComponentRegistry")
    println ("a.name="+ ComponentRegistryTesting.a.name)
    println ("b.name="+ ComponentRegistryTesting.b.name)
    
    assertEquals("production.a",ComponentRegistry.a.name)
    assertEquals("test.a",ComponentRegistryTesting.a.name)
  } 
  
}