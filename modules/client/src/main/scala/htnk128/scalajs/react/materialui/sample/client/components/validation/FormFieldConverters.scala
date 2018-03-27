package htnk128.scalajs.react.materialui.sample.client.components.validation

import java.util.UUID

import scala.scalajs.js

import chandu0101.scalajs.react.components.materialui._
import japgolly.scalajs.react._
import japgolly.scalajs.react.raw.ReactNode
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object FormFieldConverters {

  implicit class TextField(f: FormField[_]) {

    private def errorMessage(f: FormField[_]): Option[String] =
      f.currentValidationResult match {
        case Some(cvr) if cvr != ValidationResult.Success => Some(cvr.errorMessage)
        case _ => None
      }

    def asTextField(name: String, action: String => Callback): VdomElement = {
      MuiTextField(
        floatingLabelText = VdomArray(""),
        defaultValue = f.getValue().asInstanceOf[String],
        onChange = js.defined((_, s) => action(s)),
        errorText = errorMessage(f)
          .map(et => js.defined(et))
          .getOrElse(js.undefined)
          .map(_.asInstanceOf[ReactNode]),
        key = js.defined(UUID.randomUUID().toString)
      )()
    }
  }
}
