package com.korczak.patterns.typeclass

import com.korczak.patterns.UnitSpec

final class PrettyStringSpec extends UnitSpec {

  def prettyString[T](t: T)(implicit stringifier: PrettyString[T]): String = {
    stringifier.stringify(t)
  }

  def prettyStringNeat[T: PrettyString](t: T): String = {
    implicitly[PrettyString[T]].stringify(t)
  }

  behavior of "Pretty String"

  it should "stringify String" in {
    val jw = "JohnnyWoo"

    prettyString[String](jw) shouldEqual "(" + jw + ")"
    prettyStringNeat[String](jw) shouldEqual "(" + jw + ")"
  }

  it should "stringify Int" in {
    val deg = 360

    prettyString[Int](deg) shouldEqual "(" + deg + ")"
    prettyStringNeat[Int](deg) shouldEqual "(" + deg + ")"
  }


}
