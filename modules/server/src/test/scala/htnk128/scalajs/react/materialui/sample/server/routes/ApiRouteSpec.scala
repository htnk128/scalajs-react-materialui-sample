package htnk128.scalajs.react.materialui.sample.server.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import htnk128.scalajs.react.materialui.sample.server.services.ApiService
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ FlatSpec, Matchers }

class ApiRouteSpec extends FlatSpec with Matchers with ScalaFutures with ScalatestRouteTest with MockFactory {

  lazy val routes: Route = new ApiRoute(ApiService).route

  "ApiRoute" should "return Internal Server Error status (POST /api/hoge)" in {
    val request = HttpRequest(method = HttpMethods.POST, uri = "/api/hoge")

    request ~> routes ~> check {
      status should ===(StatusCodes.InternalServerError)
    }
  }
}
