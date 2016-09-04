package com.dsmedley.twoelevensolver

import akka.actor.{ActorSystem, Props}
import akka.io.{IO => AkkaIO}
import com.dsmedley.twoelevensolver.api.http.Service
import com.dsmedley.twoelevensolver.config.Config
import spray.can.Http

object Main {
  /**
   * Start off the HTTP service
   */
  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem(Config.serviceName, Config.configObject)
    val httpService = actorSystem.actorOf(Props[Service], Config.serviceName)

    AkkaIO(Http) ! Http.Bind(httpService, Config.host, Config.port)
  }
}
