package com.github.stormgen.metrics.web

import cats.effect.IO
import org.http4s.{ HttpApp, HttpRoutes, Response, StaticFile, Status }
import org.http4s.dsl.io._

class MetricsRoutes() {
  private def routes = HttpRoutes.of[IO] { case request @ GET -> Root / "status" =>
    StaticFile.fromResource[IO]("html/index.html", Some(request)).getOrElse(Response().withStatus(Status.NotFound))
  }

  val httpApp: HttpApp[IO] = routes.orNotFound
}
