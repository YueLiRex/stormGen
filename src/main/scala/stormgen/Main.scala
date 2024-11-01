package stormgen

import stormgen.generator.Generator
import generator.Generator._
object Main extends App {

  case class School(name: String, address: String)
  case class Test(name: String, age: Int, adult: Boolean, hobbies: Seq[String], historySchool: Seq[School])

  (1 to 5).foreach { i =>
    println(Generator.generate[Test])
  }
}
