import Dependencies.*

lazy val root = (project in file("."))
  .settings(
    name := "stormGen",
    scalaVersion := "2.13.14",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= Seq("-Wunused:imports", "-Dcats.effect.warnOnNonMainThreadDetected=false"),
    libraryDependencies ++= Seq(
      shapeless,
      kafkaClients39,
      circeCore,
      ce,
      cek,
      cestd,
      betterModadicFor,
      ceTest,

      munit % Test
    ),
    Compile / run / fork := true
  )
