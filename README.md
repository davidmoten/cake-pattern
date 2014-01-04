cake-pattern
============

Example of cake pattern in scala (type safe immutable dependency injection by using traits and self-typing).

As a long term user of Guice for injecting class dependencies into constructors I was curious as to how the cake pattern in scala worked.

I read Jonas Boner's blog article and understood it but thought the example could have been clearer and the method described a bit more succintly for the impatient reader like myself.

So what is the cake pattern?
----------------------------- 

I'm going to show you an example indicating how to convert three classes so that dependency injection can be used with the cake pattern.

Consider these classes:

```
trait Configuration {
  def value: String
}

class DefaultConfiguration extends Configuration {
  val value = "production"
}
class TestingConfiguration extends Configuration {
  val value = "test"
}
    
class A(configuration:Configuration){
  val value = "a-" + configuration.value
}
class B(configuration:Configuration, a:A){
  val value = a.value + "-b-"+ configuration.value
} 
```

To apply the cake pattern: 

* wrap Configuration, A and B with their own traits (*Component* traits)
* define constructor dependencies in Components using self types   
* add an abstract singleton instance to each Component trait 
* create a Registry object that instantiates everything 

Let's do it for our example:

```
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
  val a: A
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
```

Now to get a singleton wired up immutable instance of B we call Registry.b (or RegistryTesting.b) and the important thing to notice is that to instantiate B within the Registry object we just called ```new B()``` without any constructor parameters.

Obviously there's some boilerplate involved with setting up the cake pattern, in fact as design patterns go I'd call it bit noisy, somewhat verbose but still elegant. For less verbosity one might consider Guice or Subcut.

Compiling and testing the example
------------------------------------------
This project is setup as a maven project and will be compiled and unit tested as below:

    cd <YOUR_WORKSPACE>
    git clone https://github.com/davidmoten/cake-pattern.git
    cd cake-pattern
    mvn clean test

