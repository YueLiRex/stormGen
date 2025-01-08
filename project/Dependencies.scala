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
}
