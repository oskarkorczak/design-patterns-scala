# Functional design patterns in Scala
Examples of functional design patterns in Scala. 

## Type-class
Type class is a functional design pattern derived from Haskell (obviously). 

### Basic Mechanics
There are three steps needed in order to define a Typeclass: 

* `trait` defining action, which later on will be implemented for a number of types
* `companion object` holding `trait` implementations for number of types (`Boolean`, `Int`, `Double` etc)
* `generic method with implicit` allowing to make space for implicit implementation resolution based on provided type `T`

#### Trait
Action tells that `String` will be converted to `T` 

``` scala
trait StringParser[T] {
  def parse(s: String): T
}
```

#### Companion object
Implementations for necessary types comes next:

``` scala
object StringParser {
  implicit object ParseInt extends StringParser[Int] {
     override def parse(s: String): Int = s.toInt
  }

  implicit object ParseDouble extends StringParser[Double] {
     override def parse(s: String): Double = s.toDouble
  }

  implicit object ParseBoolean extends StringParser[Boolean] {
    override def parse(s: String): Boolean = s.toBoolean
  }
}
```

#### Generic method with implicit
Two ways of writing generic method allowing for implicit resolution based on passed type `T`:

``` scala
def parse[T](s: String)(implicit parser: StringParser[T]): T = {
  parser.parse(s)
}
```

In Scala Typeclass is used so often that there is a handy abbreviation called [Context Bound][context-bound-docs] allowing to shorten the syntax above to:  

``` scala
def parse[T: StringParser](s: String): T = {
  implicitly[StringParser[T]].parse(s)
}
```

where `parse[T: StringParser]` is an equivalent of `(implicit parser: StringParser[T])`, also note `implicitly[StringParser[T]]` in the implementation. 


### Recursive Inference

Having type-classes defined for basic types, they could be used for inferencing more complex types like `Tuple[T, V]` or `Seq[T]`. 

### Tuple[T, V]
In order to parse tuples we need to have below code, dynamically defining the `StringParser[T]` and putting there two other implicit parsers. 

```scala
implicit def ParseTuple[T, V](implicit p1: StringParser[T], p2: StringParser[V]) = {
  new StringParser[(T, V)] {
    override def parse(s: String): (T, V) = {
      val Array(left, right) = s.split('=')
      (p1.parse(left), p2.parse(right))
    }
  }
}
```


### Seq[T]

Now, we can define sequence parser in a similar way as above. 

```scala
  implicit def ParseSequence[T](implicit parser: StringParser[T]) = {
    new StringParser[Seq[T]] {
      override def parse(s: String): Seq[T] = {
        s.split(',').toSeq.map(parser.parse)
      }
    }
  }


```

### Seq[Tuple[T, V]]

Having above, recursive parsers defined, we may go even further and combine recursive parsers, to get more nesting for free. Here is the sample usage, based on the fact that all above is already defined. There is no additional parser needed. We can basically start using it straight away:

```scala
parse[Seq[(Int, Boolean)]]("1=true,2=false,3=true,4=false")
```

and it produces:

```scala
Seq((1, true), (2, false), (3, true), (4, false))
```












[context-bound-docs]: https://docs.scala-lang.org/tutorials/FAQ/context-bounds.html#what-is-a-context-bound