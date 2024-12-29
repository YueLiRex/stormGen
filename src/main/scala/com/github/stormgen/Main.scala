package com.github.stormgen

object Main extends App {

  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

}
