import Dependencies.*

lazy val root = (project in file("."))
  .settings(
    name := "stormGen",
    scalaVersion := "2.13.15",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= Seq("-Wunused:imports", "-Dcats.effect.warnOnNonMainThreadDetected=false"),
    libraryDependencies ++= Seq(
      shapeless,
      kafkaClients39,
      circeCore,
      circeGeneric,
      ce,
      cek,
      cestd,
      betterModadicFor,
      ceTest,

      ceMunitTest,
      munit
    ),
    Compile / run / fork := true
  )
