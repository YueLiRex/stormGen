package com.github.stormgen.metrics

import cats.effect.IO
import cats.effect.Resource
import com.comcast.ip4s._
import org.http4s.HttpApp
import org.http4s.ember.server._
import org.http4s.server.Server

object MetricsServer {
  def apply(metricsStore: MetricsStore): Resource[IO, Server] = {
    val routes = new MetricsRoutes(metricsStore)

    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"10303")
      .withHttpApp(routes.httpApp)
      .build
  }
}
