package htnk128.scalajs.react.materialui.sample.client.actions

import diode._
import diode.data._
import diode.util._
import htnk128.scalajs.react.materialui.sample.shared.api._

object AppAction {

  case class Authenticated(user: User) extends Action
}

class AppHandler[M](modelRW: ModelRW[M, Pot[AppContext]]) extends ActionHandler(modelRW) {
  implicit val runner: RunAfterJS = new RunAfterJS

  override def handle: PartialFunction[Any, ActionResult[M]] = {
    case AppAction.Authenticated(user) =>
      updated(Ready(AppContext(Some(user))))
  }
}
