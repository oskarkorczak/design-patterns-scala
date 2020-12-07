package com.korczak.patterns.typeclass

import com.korczak.patterns.UnitSpec

final class StringParserSpec extends UnitSpec {

  def parse[T](s: String)(implicit parser: StringParser[T]): T = {
    parser.parse(s)
  }

  def parseNeat[T: StringParser](s: String): T = {
    implicitly[StringParser[T]].parse(s)
  }

  implicit def ParseTuple[T, V](implicit p1: StringParser[T], p2: StringParser[V]) = {
    new StringParser[(T, V)] {
      override def parse(s: String): (T, V) = {
        val Array(left, right) = s.split('=')
        (p1.parse(left), p2.parse(right))
      }
    }
  }

  implicit def ParseSequence[T](implicit parser: StringParser[T]) = {
    new StringParser[Seq[T]] {
      override def parse(s: String): Seq[T] = {
        s.split(',').toSeq.map(parser.parse)
      }
    }
  }

  behavior of "String parser"

  it should "parse Int" in {
    val str = "64"

    parse[Int](str) shouldEqual 64
    parseNeat[Int](str) shouldEqual 64
  }

  it should "parse Double" in {
    val str = "64.8"

    parse[Double](str) shouldEqual 64.8
    parseNeat[Double](str) shouldEqual 64.8
  }

  it should "parse Boolean" in {
    val str = "true"

    parse[Boolean](str) shouldEqual true
    parseNeat[Boolean](str) shouldEqual true
  }

  it should "parse Tuples of [Int, Double]" in {
    val str = "15=240.5"

    parse[(Int, Double)](str) shouldEqual (15, 240.5)
  }

  it should "parse Tuples of [Int, Boolean]" in {
    val str = "15=true"

    parse[(Int, Boolean)](str) shouldEqual (15, true)
  }

  it should "parse Tuples of [Double, Boolean]" in {
    val str = "240.5=false"

    parse[(Double, Boolean)](str) shouldEqual (240.5, false)
  }

  it should "parse Sequence of Ints" in {
    val seq = "1,2,3,4,5,6,7,8"

    parse[Seq[Int]](seq) shouldEqual Seq(1, 2, 3, 4, 5, 6, 7, 8)
  }

  it should "parse Sequence of Booleans" in {
    val seq = "true,true,false,true,true,true,false"

    parse[Seq[Boolean]](seq) shouldEqual Seq(true, true, false, true, true, true, false)
  }
}
