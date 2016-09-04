package com.dsmedley.twoelevensolver.api.http

import akka.actor.{Actor, ActorRefFactory}
import spray.http.{HttpResponse, StatusCodes}
import spray.routing._

trait ServiceAPI extends HttpService {
  this: Actor =>

  protected def restfulExceptions = ExceptionHandler {
    case unknown: Throwable => {
      complete(HttpResponse(status = StatusCodes.InternalServerError))
    }
  }

  protected def routes: Route = {
    // test endpoint
    path("ping") {
      complete(200, "PONG")
    }
  }
}

class Service extends ServiceAPI with Actor {
  def actorRefFactory: ActorRefFactory = context
  def receive: Receive = runRoute(routes)
}
