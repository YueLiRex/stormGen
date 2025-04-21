import sbt._

object Dependencies {
  lazy val circeVersion = "0.14.10"
  lazy val catsEffectVersion = "3.6.0"

  lazy val kafkaClients39 = "org.apache.kafka" % "kafka-clients" % "3.9.0"
  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"

  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  lazy val circeParser = "io.circe" %% "circe-parser" % circeVersion

  lazy val ce = "org.typelevel" %% "cats-effect" % catsEffectVersion
  lazy val cek = "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion
  lazy val cestd = "org.typelevel" %% "cats-effect-std" % catsEffectVersion
  lazy val betterModadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  lazy val ceTest = "org.typelevel" %% "cats-effect-std" % catsEffectVersion % Test
  lazy val ceMunitTest = "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test
  lazy val munit = "org.scalameta" %% "munit" % "0.7.29" % Test
}
