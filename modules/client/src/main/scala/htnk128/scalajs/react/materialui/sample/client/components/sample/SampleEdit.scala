package htnk128.scalajs.react.materialui.sample.client.components.sample

import chandu0101.scalajs.react.components.materialui._
import diode.data.Pot
import diode.react.ReactPot._
import diode.react._
import htnk128.scalajs.react.materialui.sample.client.actions.SampleAction
import htnk128.scalajs.react.materialui.sample.client.components.validation._
import htnk128.scalajs.react.materialui.sample.client.model.MenuItem
import htnk128.scalajs.react.materialui.sample.client.routes.SampleModule
import htnk128.scalajs.react.materialui.sample.client.services.AppCircuit
import htnk128.scalajs.react.materialui.sample.shared.api.sample._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object SampleEdit {

  case class Props(id: Option[Int], selectedPage: MenuItem, ctrl: RouterCtl[MenuItem], proxy: ModelProxy[Pot[SampleContext]])

  case class State(wrapper: ReactConnectProxy[Pot[SampleContext]])

  private val component = ScalaComponent
    .builder[Props]("SampleEdit")
    .initialStateFromProps(P => State(P.proxy.connect(m => m)))
    .renderPS((_, P, S) => S.wrapper(SampleEditMain(P.id, P.selectedPage, P.ctrl, _)))
    .build

  def apply(id: Option[Int], selectedPage: MenuItem, ctrl: RouterCtl[MenuItem]): VdomElement =
    AppCircuit.wrap(_.sample)(p => component(Props(id, selectedPage, ctrl, p)))
}

private[this] object SampleEditMain {

  case class Props(id: Option[Int], selectedPage: MenuItem, ctrl: RouterCtl[MenuItem], proxy: ModelProxy[Pot[SampleContext]])

  case class State(formLayout: SampleEditFormLayout = SampleEditFormLayout(SampleData()))

  case class SampleEditFormLayout(dataModel: SampleData) extends FormLayout[SampleData](dataModel) {

    val name: String = createField[String](_.name, v => Validator(v.nonEmpty, "名称を指定してください"))
  }

  case class Backend(scope: BackendScope[Props, State]) {

    def render(P: Props, S: State): VdomElement = {

      import S.formLayout._
      import htnk128.scalajs.react.materialui.sample.client.components.validation.FormFieldConverters._

      def submitAction(e: ReactEvent): Callback = {
        e.preventDefault()
        validatedDataModel match {
          case Some(data) if data.id == 0 =>
            P.proxy.dispatchCB(SampleAction.SampleEdit(data)) >> P.ctrl.set(SampleModule.Submenu1.List)
          case Some(data) if data.id != 0 =>
            P.proxy.dispatchCB(SampleAction.SampleEdit(data)) >> P.ctrl.set(SampleModule.Submenu1.List)
          case _ => scope.modState(_.copy(formLayout = S.formLayout))
        }
      }

      def deleteAction(e: ReactEvent): Callback = CallbackTo.confirm("削除しますか？") >>= {
        (ok: Boolean) =>
          if (ok) {
            P.proxy.dispatchCB(SampleAction.SampleDelete(currentDataModel)) >> P.ctrl.set(SampleModule.Submenu1.List)
          }
          else Callback.empty
      }

      <.div(
        <.h2(P.selectedPage.title),
        P.proxy().renderPending(_ > 500, _ => <.p("Loading...")),
        P.proxy().renderFailed(_ => <.p("Failed to load")),
        P.proxy().render { _ =>
          <.form(
            ^.className := "form-container",
            ^.onSubmit ==> { (e: ReactEvent) => submitAction(e) },
            getField(name).asTextField("名称", v => Callback(setDataModel(currentDataModel.copy(name = v)))),
            <.div(
              ^.className := "form-buttons",
              MuiRaisedButton(
                label = "登録",
                primary = true,
                `type` = "submit"
              )(),
              MuiRaisedButton(
                label = "削除",
                secondary = true,
                onClick = { (e: ReactEvent) => deleteAction(e) }
              )().when(P.id.nonEmpty),
              MuiRaisedButton(
                label = "キャンセル",
                onClick = { (_: ReactEvent) => P.ctrl.set(SampleModule.Submenu1.List) }
              )()
            )
          )
        }
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("SampleEditMain")
    .initialState(State())
    .renderBackend[Backend]
    .componentWillMount { scope =>
      scope.props.id match {
        case Some(id) => scope.props.proxy.dispatchCB(SampleAction.SampleLoad(id))
        case _ => Callback.empty
      }
    }
    .componentDidUpdate { scope =>
      val currentDataModel = scope.currentState.formLayout.currentDataModel
      val maybeContext = scope.currentProps.proxy().toOption

      maybeContext match {
        case Some(ctx) =>
          Callback.when(currentDataModel == SampleData() && scope.currentProps.id.contains(ctx.edit.id)) {
            scope.setState(State(SampleEditFormLayout(ctx.edit)))
          }
        case _ => Callback.empty
      }
    }
    .build

  def apply(
    id: Option[Int],
    selectedPage: MenuItem,
    ctrl: RouterCtl[MenuItem],
    proxy: ModelProxy[Pot[SampleContext]]
  ): VdomElement =
    component(Props(id, selectedPage, ctrl, proxy))
}
