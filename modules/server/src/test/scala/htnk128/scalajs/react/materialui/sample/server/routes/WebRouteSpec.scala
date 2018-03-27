package htnk128.scalajs.react.materialui.sample.server.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ FlatSpec, Matchers }

class WebRouteSpec extends FlatSpec with Matchers with ScalaFutures with ScalatestRouteTest with MockFactory {

  lazy val routes: Route = new WebRoute().route

  "WebRoute" should "return OK status (GET /)" in {
    val request = HttpRequest(uri = "/")

    request ~> routes ~> check {
      status should ===(StatusCodes.OK)
    }
  }
}
