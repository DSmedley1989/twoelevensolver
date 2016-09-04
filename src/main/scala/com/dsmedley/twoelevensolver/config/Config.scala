package com.dsmedley.twoelevensolver.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.{Config => TConfig}

object Config {

  // Check if mediaman.environment is set, and if it is, use the right config file
  // Otherwise just stick with application.conf
  val environment = Option(System.getProperty("twoelevensolver.environment"))
  private val config = if (environment.isDefined) {
    ConfigFactory.load(environment.get)
  } else {
    ConfigFactory.load
  }

  val host: String = config.getString("service.host")
  val port: Int = config.getInt("service.port")
  val serviceName: String = config.getString("service.name")

  /**
   * Expose the config object so we can pass it to Akka
   */
  def configObject: TConfig = config
}
