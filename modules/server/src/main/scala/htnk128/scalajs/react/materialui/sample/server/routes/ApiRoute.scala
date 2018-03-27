package htnk128.scalajs.react.materialui.sample.server
package routes

import java.nio.ByteBuffer
import javax.inject.{ Inject, Singleton }

import scala.util.{ Failure, Success }

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import boopickle.Default._
import com.typesafe.scalalogging.LazyLogging
import htnk128.scalajs.react.materialui.sample.server.services.ApiService
import htnk128.scalajs.react.materialui.sample.shared.api.Api

@Singleton
class ApiRoute @Inject() (apiService: ApiService) extends LazyLogging {

  val route: Route = path("api" / Segments) { s =>
    entity(as[ByteString]) { bs =>
      val f = Router.route[Api](apiService)(
        autowire.Core.Request(s, Unpickle[Map[String, ByteBuffer]].fromBytes(bs.asByteBuffer))
      ).map(buffer => {
          val data = Array.ofDim[Byte](buffer.remaining())
          buffer.get(data)
          data
        })

      onComplete(f) {
        case Success(bytes) => complete(bytes)
        case Failure(e) =>
          logger.error(e.getMessage, e)
          complete(StatusCodes.InternalServerError)
      }
    }
  }
}

object Router extends autowire.Server[ByteBuffer, Pickler, Pickler] {

  override def read[R: Pickler](p: ByteBuffer): R = Unpickle[R].fromBytes(p)

  override def write[R: Pickler](r: R): ByteBuffer = Pickle.intoBytes(r)
}
