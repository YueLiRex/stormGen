package com.github.stormgen.generator

import scala.util.Random

trait Gen[T] extends Serializable {
  def getValue: T
}

object Gen {
  implicit val intValue: Gen[Int] = new Gen[Int] {
    override def getValue: Int = Random.nextInt(100)
  }

  implicit val longValue: Gen[Long] = new Gen[Long] {
    override def getValue: Long = Random.nextLong(1000000)
  }

  implicit val doubleValue: Gen[Double] = new Gen[Double] {
    override def getValue: Double = Random.nextDouble()
  }

  implicit val stringValue: Gen[String] = new Gen[String] {
    override def getValue: String = Random.alphanumeric.take(16).mkString
  }

  implicit val booleanValue: Gen[Boolean] = new Gen[Boolean] {
    override def getValue: Boolean = Random.nextBoolean()
  }

  implicit def seqValue[T](implicit genT: Gen[T]): Gen[Seq[T]] = new Gen[Seq[T]] {
    override def getValue: Seq[T] = Seq.fill(Random.between(1, 4))(genT.getValue)
  }

  implicit def listValue[T](implicit genT: Gen[T]): Gen[List[T]] = new Gen[List[T]] {
    override def getValue: List[T] = List.fill(Random.between(1, 4))(genT.getValue)
  }

  implicit val nilValue: Gen[Null] = new Gen[Null] {
    override def getValue: Null = null
  }
}
