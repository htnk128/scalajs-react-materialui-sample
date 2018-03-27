package htnk128.scalajs.react.materialui.sample.client.components.sample

import scala.language.existentials
import scala.scalajs.js

import chandu0101.scalajs.react.components.materialui.MuiSvgIcon._
import chandu0101.scalajs.react.components.materialui._
import diode.data.Pot
import diode.react.ReactPot._
import diode.react._
import htnk128.scalajs.react.materialui.sample.client.actions.SampleAction
import htnk128.scalajs.react.materialui.sample.client.components.DataTable
import htnk128.scalajs.react.materialui.sample.client.model.MenuItem
import htnk128.scalajs.react.materialui.sample.client.routes.SampleModule
import htnk128.scalajs.react.materialui.sample.client.services.AppCircuit
import htnk128.scalajs.react.materialui.sample.shared.api.sample._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._

object SampleList {

  case class Props(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem], proxy: ModelProxy[Pot[SampleContext]])

  case class State(wrapper: ReactConnectProxy[Pot[SampleContext]])

  private val component = ScalaComponent
    .builder[Props]("SampleList")
    .initialStateFromProps(P => State(P.proxy.connect(m => m)))
    .renderPS((_, P, S) => S.wrapper(SampleListMain(P.selectedPage, P.ctrl, _)))
    .build

  def apply(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem]): VdomElement =
    AppCircuit.wrap(_.sample)(p => component(Props(selectedPage, ctrl, p)))
}

private[this] object SampleListMain {

  case class Props(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem], proxy: ModelProxy[Pot[SampleContext]])

  case class Backend(scope: BackendScope[Props, _]) {

    def render(P: Props): VdomElement = {

      import DataTable._

      <.div(
        <.h2(P.selectedPage.title),
        MuiRaisedButton(
          label = "作成",
          primary = true,
          onClick = { (_: ReactEvent) => P.ctrl.set(SampleModule.Submenu1.Add) }
        )(),
        P.proxy().renderPending(_ > 500, _ => <.p("Loading...")),
        P.proxy().renderFailed(_ => <.p("Failed to load")),
        P.proxy().render { m =>
          val configs = Seq(
            SimpleStringConfig[SampleData]("ID", _.id.toString, Some("10%"), sortable = true),
            SimpleStringConfig[SampleData]("名称", _.name),
            ColumnConfig[SampleData](
              "操作",
              sd => MuiFloatingActionButton(
                onClick = js.defined(_ => P.ctrl.set(SampleModule.Submenu1.Edit(sd.id))),
                mini = true
              )(Mui.SvgIcons.ContentCreate()()),
              Some("10%")
            )
          )
          DataTable(m.list.values, configs)()
        }
      )
    }
  }

  private val component = ScalaComponent
    .builder[Props]("SampleListMain")
    .renderBackend[Backend]
    .componentWillMount { scope =>
      Callback.when(scope.props.proxy.value.isEmpty) {
        scope.props.proxy.dispatchCB(SampleAction.SampleSearch(SampleSearchCondition()))
      }
    }
    .build

  def apply(selectedPage: MenuItem, ctrl: RouterCtl[MenuItem], proxy: ModelProxy[Pot[SampleContext]]): VdomElement =
    component(Props(selectedPage, ctrl, proxy))
}
