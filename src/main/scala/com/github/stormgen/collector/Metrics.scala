package com.github.stormgen.collector

import java.util.concurrent.atomic.AtomicInteger

import io.circe.Json

trait Metrics[T] {
  def name: String

  def get: T

  def json: Json
}

class Counter(val name: String) extends Metrics[Int] {
  private val counter = new AtomicInteger(0)

  override def get: Int = counter.intValue()

  override def json: Json = Json.obj(name -> Json.fromInt(counter.intValue()))

  def +(number: Int): Int = counter.addAndGet(number)
}
