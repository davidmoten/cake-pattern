cake-pattern
============
As a long term user of Guice for injecting class dependencies into constructors I was curious as to how the cake pattern in Scala worked.

I read [Jonas Boner's blog article](http://jonasboner.com/real-world-scala-dependency-injection-di/) and followed 
it but thought the example could have been clearer and the method described a bit more succintly for the impatient reader like myself.

So what is the cake pattern?
----------------------------- 

The cake pattern uses features of self types and mixins in Scala to enable apparently parameter-less construction of objects.

Lets convert the classes below to use dependency injection (DI) with the cake pattern:

```scala
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

This is how you would instantiate the classes without DI:
```scala
val configuration = new Configuration
val a = new A(configuration)
val b = new B(configuration,a)
```

To apply the cake pattern: 

* wrap *Configuration*, *A* and *B* with their own traits (*Component* traits)
* move constructor dependencies to Components using self types   
* add an abstract instance to each Component trait 
* create a *Registry* object that instantiates everything 

Let's do it for our example classes:

```scala
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
```

Now to get a singleton wired up immutable instance of *B* we call ```Registry.b``` (or ```RegistryTesting.b```) and the important thing to notice 
is that to instantiate *B* within the *Registry* object we just called ```new B()``` without any constructor parameters.

Obviously there's some boilerplate involved with setting up the cake pattern, in fact as design patterns go I'd call it bit noisy, somewhat verbose 
 but still elegant. For less verbosity one might consider [Subcut](https://github.com/dickwall/subcut) or Guice.

Full source code for this example is in package examples.example1 in 
[src/main/scala/examples.scala](https://github.com/davidmoten/cake-pattern/blob/master/src/main/scala/examples.scala)

Injecting non-singleton instances
-----------------------------------
The above example  demonstrates how to wire up singleton instances (singleton in terms of the scope of Registry) of *A*,*B* and *Configuration*. 
Another common use case for injection is to wire in non-singleton instances (that themselves may have dependencies 
on singleton or non-singleton instances). Example 2 demonstrates this:

These are the classes we are going to convert using the cake pattern:

```scala
class A(configuration:Configuration, c:C){
  val value = "a-" + configuration.value + "-" + c.value
}
class B(configuration:Configuration, a:A, c:C){
  val value = a.value + "-b-" + configuration.value + "-" + c.value
} 
class C(configuration:Configuration){
  val value = "c-" + configuration.value + "-" +
        randomUUID.toString.substring(1, 5)
}
```
This is how you would instantiate the classes without DI:
```scala
val configuration = new Configuration
val a = new A(configuration,new C(configuration))
val b = new B(configuration,a,new C(configuration))
```

To wire in a non-singleton instance *C* into *A* and another into *B*:
* wrap *C* with a *Component* trait
* define constructor dependencies using self types
* do *NOT* add an abstract instance of *C* to *CComponent* trait
* instantiate *C* inside class *A* and class *B* using ```val c = new C()```

This is how it looks:
```scala
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
```

Full source code for this example is in package examples.example2 in 
[src/main/scala/examples.scala](https://github.com/davidmoten/cake-pattern/blob/master/src/main/scala/examples.scala)

Compiling and testing the examples
------------------------------------------
Get the source:

    cd <YOUR_WORKSPACE>
    git clone https://github.com/davidmoten/cake-pattern.git
    cd cake-pattern
    
Build with maven:

    mvn clean test
    
or build with sbt:

    sbt clean test
