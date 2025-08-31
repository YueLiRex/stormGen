package com.github.stormgen.generator

import munit.FunSuite
import shapeless._

class GeneratorSuite extends FunSuite {

  test("Generator.generate produces a value using Gen") {
    val intValue = Generator.generate[Int]
    assert(intValue >= 0 && intValue < 100)
    val strValue = Generator.generate[String]
    assertEquals(strValue.length, 16)
  }

  test("Generator.oneOf returns one of the provided options") {
    val options = Seq("a", "b", "c")
    val result = Generator.oneOf(options: _*)
    assert(options.contains(result))
  }

  test("Generator.hnilRandom returns HNil") {
    val hnil = Generator.hnilRandom.getValue
    assertEquals(hnil, HNil)
  }

  test("Generator.hlistRandom returns HList with correct types and values") {
    val hlistGen = Generator.hlistRandom[Int, String :: HNil](
      shapeless.Lazy(implicitly[Gen[Int]]),
      Generator.hlistRandom[String, HNil](shapeless.Lazy(implicitly[Gen[String]]), Generator.hnilRandom)
    )
    val hlist = hlistGen.getValue
    assert(hlist.head.isInstanceOf[Int])
    assert(hlist.tail.head.isInstanceOf[String])
    assert(hlist.tail.tail == HNil)
  }

  test("Generator.genericRandom derives Gen for case class") {
    case class Foo(a: Int, b: String)
    implicit val genFoo: Gen[Foo] = new Gen[Foo] {
      override def getValue: Foo = Foo(Gen.intValue.getValue, Gen.stringValue.getValue)
    }
    val foo = Generator.generate[Foo]
    assert(foo.a >= 0 && foo.a < 100)
    assert(foo.b.length == 16)
  }
}
