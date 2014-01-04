/**
 * *
 * Uses the cake pattern in scala to perform dependency injection.
 *
 * In this example B depends on Configuration and A and A depends
 * on Configuration.
 *
 */

trait Configuration {
  def value: String
}

class DefaultConfiguration extends Configuration {
  val value = "production"
}

class TestingConfiguration extends Configuration {
  val value = "test"
}

trait ConfigurationComponent {
  val configuration: Configuration
}

trait AComponent {
  this: ConfigurationComponent =>
  val a: A
  class A {
    val value = "a-" + configuration.value
  }
}

trait BComponent {
  this: ConfigurationComponent with AComponent =>
  val b: B
  class B {
    val value = a.value + "-b-"+ configuration.value
  }
}

object Registry
  extends ConfigurationComponent
  with AComponent
  with BComponent {

  val configuration = new DefaultConfiguration
  val a = new A()
  val b = new B()
}

object RegistryTesting
  extends ConfigurationComponent
  with AComponent
  with BComponent {

  val configuration = new TestingConfiguration
  val a = new A()
  val b = new B()
}

