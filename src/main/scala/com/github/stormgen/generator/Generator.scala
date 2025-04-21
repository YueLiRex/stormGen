package com.github.stormgen.generator

import scala.util.Random

import shapeless._

object Generator {
  implicit def genericRandom[A, R](implicit gen: Generic.Aux[A, R], random: Lazy[Gen[R]]): Gen[A] = new Gen[A] {
    override def getValue: A = gen.from(random.value.getValue)
  }

  implicit val hnilRandom: Gen[HNil] = new Gen[HNil] {
    override def getValue: HNil = HNil
  }

  implicit def hlistRandom[H, T <: HList](implicit hGen: Lazy[Gen[H]], tGen: Gen[T]): Gen[H :: T] = new Gen[H :: T] {
    override def getValue: H :: T = hGen.value.getValue :: tGen.getValue
  }

  def oneOf[T](options: T*): T = {
    val i = Random.between(0, options.size)
    options(i)
  }

  def generate[T](implicit genT: Gen[T]): T = genT.getValue
}
