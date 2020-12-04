package com.korczak.patterns.typeclass

trait PrettyString[T] {
  def stringify(t: T): String
}

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
