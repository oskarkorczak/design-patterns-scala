package com.korczak.patterns.typeclass

import com.korczak.patterns.UnitSpec

final class StringParserSpec extends UnitSpec {

  def parse[T](s: String)(implicit parser: StringParser[T]): T = {
    parser.parse(s)
  }

  def parseNeat[T: StringParser](s: String): T = {
    implicitly[StringParser[T]].parse(s)
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
}
