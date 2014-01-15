package examples {

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

}

package examples.example1 {

  /**
   * *
   * Uses the cake pattern in scala to perform dependency injection.
   *
   * In this example B depends on Configuration and A and A depends
   * on Configuration.
   *
   */

  import examples._

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
      val value = a.value + "-b-" + configuration.value
    }
  }

  trait Components
    extends ConfigurationComponent
    with AComponent
    with BComponent

  object Registry extends Components {
    val configuration = new DefaultConfiguration
    val a = new A()
    val b = new B()
  }

  object RegistryTesting extends Components {
    val configuration = new TestingConfiguration
    val a = new A()
    val b = new B()
  }
}

package examples.example2 {

  /**
   * *
   * Uses the cake pattern in scala to perform dependency injection.
   *
   * In this example we build on example 1 providing A and B with different instances of C
   * which is itself dependent on configuration.
   *
   * ```
   * B -> Configuration
   *   -> A
   *   -> C
   * A -> Configuration
   *   -> C (different instance)
   * C -> Configuration
   * ```
   */

  import examples._
  import java.util.UUID._

  trait AComponent {
    this: ConfigurationComponent with CComponent =>
    val a: A
    class A {
      val c = new C()
      val value = "a-" + configuration.value + "-" + c.value
    }
  }

  trait BComponent {
    this: ConfigurationComponent with AComponent with CComponent =>
    val b: B
    class B {
      val c = new C()
      val value = a.value + "-b-" + configuration.value + "-" + c.value
    }
  }

  trait CComponent {
    this: ConfigurationComponent =>
    class C {
      val value = "c-" + configuration.value + "-" +
        randomUUID.toString.substring(1, 5)
    }
  }

  object Registry
    extends ConfigurationComponent
    with AComponent
    with BComponent
    with CComponent {

    val configuration = new DefaultConfiguration
    val a = new A()
    val b = new B()
  }

}