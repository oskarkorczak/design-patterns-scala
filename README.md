# Functional design patterns in Scala
Examples of functional design patterns in Scala. 

## Type-class
Type class is a functional design pattern derived from Haskell (obviously). 

### Mechanics
There are three steps needed in order to define a Typeclass: 

* `trait` defining action, which later on will be implemented for a number of types
* `companion object` holding `trait` implementations for number of types (`String`, `Int`, `Double` etc)
* `generic method with implicit` allowing to make space for implicit implementation resolution based on provided type `T`

#### Trait
Action tells that any type `T` will be converted to String potentially with some additions: 

``` java
trait PrettyString[T] {
  def stringify(t: T): String
}
```

#### Companion object
Implementations for necessary types comes next:

```
object PrettyString {

  implicit object StringPrettyString extends PrettyString[String] {
    override def stringify(t: String): String = {
      s"($t)"
    }
  }

  implicit object IntPrettyString extends PrettyString[Int] {
    override def stringify(t: Int): String = {
      s"(${t.toString})"
    }
  }
}
```

#### Generic method with implicit
Two ways of writing generic method allowing for implicit resolution based on passed type `T`:

```
def prettyString[T](t: T)(implicit stringifier: PrettyString[T]): String = {
    stringifier.stringify(t)
}
```

In Scala Typeclass is used so often that there is a handy abbreviation called [Context Bound][context-bound-docs] allowing to shorten the syntax above to:  

```
def prettyString[T: PrettyString](t: T): String = {
    implicitly[PrettyString[T]].stringify(t)
}
```

where `prettyString[T: PrettyString]` is an equivalent to `(implicit stringifier: PrettyString[T])`, also note `implicitly[PrettyString[T]]` in the implementation. 







[context-bound-docs]: https://docs.scala-lang.org/tutorials/FAQ/context-bounds.html#what-is-a-context-bound