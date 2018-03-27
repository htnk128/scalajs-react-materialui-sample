package htnk128.scalajs.react.materialui.sample.server
package routes

import javax.inject.Singleton

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import htnk128.scalajs.react.materialui.sample.shared._

@Singleton
class WebRoute {

  val route: Route = pathSingleSlash {
    get {
      complete(htnk128.scalajs.react.materialui.sample.server.views.html.index.render(appName))
    }
  } ~ pathPrefix("assets" / Remaining) { file =>
    encodeResponse {
      getFromResource("public/" + file)
    }
  }
}
