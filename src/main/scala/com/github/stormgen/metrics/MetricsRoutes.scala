package com.github.stormgen.metrics

import cats.effect.IO
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.Response
import org.http4s.StaticFile
import org.http4s.Status
import org.http4s.dsl.io._

class MetricsRoutes(metricsStore: MetricsStore) {
  private def routes = HttpRoutes.of[IO] {
    case request @ GET -> Root / "status" =>
      StaticFile.fromResource[IO]("/html/index.html", Some(request)).getOrElse(Response().withStatus(Status.NotFound))
  }

  val httpApp: HttpApp[IO] = routes.orNotFound
}
