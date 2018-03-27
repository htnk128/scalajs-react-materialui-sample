package htnk128.scalajs.react.materialui.sample.server.config

import javax.inject.{ Inject, Singleton }

import com.typesafe.config.{ Config, ConfigFactory }

@Singleton
class AppConfig @Inject() () {

  lazy protected final val underlying: Config = ConfigFactory.load()

  private val app = underlying.getConfig("app")

  val host: String = app.getString("host")
  val port: Int = app.getInt("port")
  val publicHost: String = app.getString("public.host")
}
