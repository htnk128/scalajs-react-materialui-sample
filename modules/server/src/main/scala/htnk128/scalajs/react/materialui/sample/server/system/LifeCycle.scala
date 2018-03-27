package htnk128.scalajs.react.materialui.sample.server
package system

import com.typesafe.scalalogging.LazyLogging
import htnk128.scalajs.react.materialui.sample.server.config.AppConfig
import htnk128.scalajs.react.materialui.sample.server.routes.{ ApiRoute, WebRoute }

sealed trait LifeCycle {

  def init(): Unit

  def destroy(): Unit
}

case class DBLifeCycle() extends LifeCycle with LazyLogging {

  import scalikejdbc.config._

  override def init(): Unit = {
    logger.info("Start initialization of DB.")

    DBs.setupAll()

    logger.info("End initialization of DB.")
  }

  override def destroy(): Unit = {
    logger.info("Start destroy of DB.")

    DBs.closeAll()

    logger.info("End destroy of DB.")
  }
}

case class WebAppLifeCycle() extends LifeCycle with LazyLogging {

  import scala.concurrent.Await
  import scala.concurrent.duration.Duration

  import akka.http.scaladsl.Http
  import akka.http.scaladsl.server.Directives._

  lazy private val appConfig = DIProvider.get(classOf[AppConfig])
  lazy private val route = {
    DIProvider.get(classOf[WebRoute]).route ~
      DIProvider.get(classOf[ApiRoute]).route
  }
  lazy private val serverBindingFuture = Http().bindAndHandle(route, appConfig.host, appConfig.port)

  override def init(): Unit = {
    logger.info("Start initialization of web app.")

    val fb = for (binding <- serverBindingFuture) yield binding
    Await.ready(fb, Duration.Inf)

    logger.info("End initialization of web app.")
  }

  override def destroy(): Unit = {
    logger.info("Start destroy of web app.")

    val ft = for {
      binding <- serverBindingFuture
      _ <- binding.unbind()
      terminate <- actorSystem.terminate()
    } yield terminate
    Await.ready(ft, Duration.Inf)

    logger.info("End destroy of web app.")
  }
}
