package htnk128.scalajs.react.materialui.sample

import scala.concurrent.ExecutionContextExecutor

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.{ Marshaller, _ }
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.MediaTypes._
import akka.stream.{ ActorMaterializer, ActorMaterializerSettings, Supervision }
import com.typesafe.scalalogging.LazyLogging
import htnk128.scalajs.react.materialui.sample.shared._
import play.twirl.api.{ Html, Txt, Xml }

package object server extends LazyLogging {

  implicit lazy val actorSystem: ActorSystem = ActorSystem(s"$appName-system")

  implicit lazy val materializer: ActorMaterializer = ActorMaterializer {
    val decider: Supervision.Decider = e => {
      logger.error(e.getMessage, e)
      Supervision.Resume
    }
    ActorMaterializerSettings(actorSystem).withSupervisionStrategy(decider)
  }

  implicit lazy val ec: ExecutionContextExecutor = actorSystem.dispatcher

  /** Twirl marshallers for Xml, Html and Txt mediatypes */
  implicit val twirlHtmlMarshaller: ToEntityMarshaller[Html] = twirlMarshaller[Html](`text/html`)

  implicit val twirlTxtMarshaller: ToEntityMarshaller[Txt] = twirlMarshaller[Txt](`text/plain`)

  implicit val twirlXmlMarshaller: ToEntityMarshaller[Xml] = twirlMarshaller[Xml](`text/xml`)

  def twirlMarshaller[A](contentType: MediaType): ToEntityMarshaller[A] = {
    Marshaller.StringMarshaller.wrap(contentType)(_.toString)
  }
}
