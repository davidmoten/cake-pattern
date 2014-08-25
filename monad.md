##Monads in scala.

Monads are formally defined in a branch of mathematics called *Category Theory* and correspond to a useful functional design pattern in scala. This article aims to describe the formal definition and the relationship to scala.

We firstly define *Category*, *Functor*, *Natural Transformation* and *Monad*.

a **Category** C is 

 * a set of *objects*
 * a set of *morphisms* which are relations between objects
 * a composition operator * for morphisms

The constraints on a category are:

 * every object has an identity morphism
 * the composition operator is associative

A **Functor** from Category A to Category B (A&rarr;B)

 * relates objects in A to objects in B
 * relates morphisms in A to morphisms in B

A Functor also

 * preserves identity morphisms
 * preserves composition of morphisms

Note that for category A there is an obvious identity functor 1<sub>A</sub>:A&rarr;A

A **Natural Transformation** &mu;

 * relates two functors F,G:A&rarr;B
 * relates each object a in A to a morphism &mu;(a): F(a)&rarr;G(a) in category B such that 
for every morphism f:a&rarr;a' in A we have &mu;(a') * F(f) = G(f) * &mu;(a) (commutativity).

a **Monad** has

 * a functor F from Category A to Category A (endomorphic functor) 
 * a natural transformation &nu;:1<sub>A</sub>&rarr;F
 * a natural transformation &mu;:FxF&rarr;F
 * &mu; is associative with F (&mu; * F&mu; = &mu; * &mu;F)
 * &nu; is the effective inverse of &mu;  (&mu; * F&nu; = &mu; * &nu;F = 1<sub>F</sub>)

###Examples in Scala
####Category
objects = Scala Types (Int,String,List[String],..), morphisms = functions between types

####Functor
*ToList: Types->Types* given by *ToList(t)=List[t] for t in Types*, maps morphisms obviously

####Monad: 
 * functor=*ToList*,
 * *&nu;(t)=List[t]*, inserts value into a singleton list
 * *&mu;(t,t)=+*, concatenation of two lists

Now give me one property or theorem of monads in general and its application to the Category of types and functions in Scala.
