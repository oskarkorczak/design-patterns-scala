package com.korczak.patterns.typeclass

trait StringParser[T] {
  def parse(s: String): T
}

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


