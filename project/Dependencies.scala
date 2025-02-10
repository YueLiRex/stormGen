import sbt._

object Dependencies {
  lazy val pekkoVersion = "1.1.0"
  lazy val circeVersion = "0.14.10"

  lazy val munit = "org.scalameta" %% "munit" % "0.7.29"
  lazy val kafkaClients39 = "org.apache.kafka" % "kafka-clients" % "3.9.0"
  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"
  lazy val pekkoActor = "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion

  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  lazy val circeParser = "io.circe" %% "circe-parser" % circeVersion

  val ce = "org.typelevel" %% "cats-effect" % "3.5.3"
  val cek = "org.typelevel" %% "cats-effect-kernel" % "3.5.3"
  val cestd = "org.typelevel" %% "cats-effect-std" % "3.5.3"
  val betterModadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  val ceTest = "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test
}
