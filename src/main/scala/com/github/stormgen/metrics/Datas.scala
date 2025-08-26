package com.github.stormgen.metrics

object Datas {
  trait Data {
    def +(data: Data): Data
    def show: String
  }

  case object Empty extends Data {
    override def +(data: Data): Data = data

    override def show: String = "Empty"
  }

  case class SentMessage(number: Long) extends Data {
    override def +(data: Data): Data = data match {
      case SentMessage(n) => SentMessage(number + n)
      case Empty => this
    }

    override def show: String = s"Sent $number messages"
  }
}
