package com.github.stormgen.generator

import munit.FunSuite

class GenSuite extends FunSuite {

  test("Gen[Int] generates an Int within 0 to 99") {
    val value = implicitly[Gen[Int]].getValue
    assert(value >= 0 && value < 100)
  }

  test("Gen[Long] generates a Long within 0 to 999999") {
    val value = implicitly[Gen[Long]].getValue
    assert(value >= 0L && value < 1000000L)
  }

  test("Gen[Double] generates a Double between 0.0 and 1.0") {
    val value = implicitly[Gen[Double]].getValue
    assert(value >= 0.0 && value < 1.0)
  }

  test("Gen[String] generates a 16-character alphanumeric string") {
    val value = implicitly[Gen[String]].getValue
    assertEquals(value.length, 16)
    assert(value.forall(_.isLetterOrDigit))
  }

  test("Gen[Boolean] generates a Boolean") {
    val value = implicitly[Gen[Boolean]].getValue
    assert(value == true || value == false)
  }

  test("Gen[Seq[Int]] generates a Seq[Int] of size 1 to 3") {
    val value = implicitly[Gen[Seq[Int]]].getValue
    assert(value.length >= 1 && value.length <= 3)
    assert(value.forall(_.isInstanceOf[Int]))
  }

  test("Gen[List[String]] generates a List[String] of size 1 to 3") {
    val value = implicitly[Gen[List[String]]].getValue
    assert(value.length >= 1 && value.length <= 3)
    assert(value.forall(_.isInstanceOf[String]))
  }

  test("Gen[Null] always returns null") {
    val value = implicitly[Gen[Null]].getValue
    assertEquals(value, null)
  }
}
