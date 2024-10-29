package kafkasurge.generator

trait Gen[T] extends Serializable {
  def getValue: T
}

object Gen {
  def intValue: Gen[Int] = new Gen[Int] {
    override def getValue: Int = ???
  }

  def longValue: Gen[Long] = new Gen[Long] {
    override def getValue: Long = ???
  }

  def stringValue: Gen[String] = new Gen[String] {
    override def getValue: String = ???
  }

  def booleanValue: Gen[Boolean] = new Gen[Boolean] {
    override def getValue: Boolean = ???
  }

  def seqValue[T]: Gen[Seq[T]] = new Gen[Seq[T]] {
    override def getValue: Seq[T] = ???
  }

  def listValue[T]: Gen[List[T]] = new Gen[List[T]] {
    override def getValue: List[T] = ???
  }

  def oneOf[T](option: T*) = new Gen[T] {
    override def getValue: T = ???
  }
}
