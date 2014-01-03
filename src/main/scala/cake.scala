/**
 * *
 * Uses the cake pattern in scala to perform dependency injection.
 *
 * In this example B depends on Configuration and A and A depends
 * on Configuration.
 *
 */

trait Configuration {
  def value(key: String): String
}

trait ConfigurationComponent {
  val configuration: Configuration
}

trait AComponent {
  this: ConfigurationComponent =>
  val a: A
  class A {
    val name = configuration.value("a")
  }
}

trait BComponent {
  this: ConfigurationComponent with AComponent =>
    val a: A
    val b: B
  class B {
    val name = configuration.value("b") + a.name
  }
}

class ProductionConfiguration extends Configuration {
  def value(key: String): String = "production." + key
}

class TestingConfiguration extends Configuration {
  def value(key: String): String = "test." + key
}

object ComponentRegistry
  extends ConfigurationComponent
  with AComponent
  with BComponent {

  val configuration = new ProductionConfiguration
  val a = new A()
  val b = new B()
}

object ComponentRegistryTesting
  extends ConfigurationComponent
  with AComponent
  with BComponent {

  val configuration = new TestingConfiguration
  val a = new A()
  val b = new B()
}
