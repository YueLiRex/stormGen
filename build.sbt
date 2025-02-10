import Dependencies.*

lazy val root = (project in file("."))
  .settings(
    name := "stormGen",
    scalaVersion := "2.13.14",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions += "-Wunused:imports",
    libraryDependencies ++= Seq(
      shapeless,
      kafkaClients39,
      pekkoActor,
      circeCore,
      ce,
      cek,
      cestd,
      betterModadicFor,
      ceTest,

      munit % Test
    ),
    fork := true
  )
