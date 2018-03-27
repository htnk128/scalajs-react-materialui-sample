package htnk128.scalajs.react.materialui.sample.client.services

import diode._
import diode.data._
import diode.react.ReactConnector
import htnk128.scalajs.react.materialui.sample.client.actions._
import htnk128.scalajs.react.materialui.sample.shared.api.AppContext
import htnk128.scalajs.react.materialui.sample.shared.api.sample.SampleContext

case class RootModel(
  app: Pot[AppContext],
  sample: Pot[SampleContext]
)

object AppCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {

  override protected def initialModel = RootModel(Empty, Empty)

  override protected val actionHandler: AppCircuit.HandlerFunction = composeHandlers(
    new AppHandler(zoomRW(_.app)((m, v) => m.copy(app = v))),
    new SampleHandler(zoomRW(_.sample)((m, v) => m.copy(sample = v)))
  )
}
