package htnk128.scalajs.react.materialui.sample.client
package routes

import htnk128.scalajs.react.materialui.sample.client.components.Motd
import htnk128.scalajs.react.materialui.sample.client.model.MenuItem
import htnk128.scalajs.react.materialui.sample.client.pages.MainPage
import japgolly.scalajs.react.extra.router.{ Redirect, RouterConfigDsl, StaticDsl }
import japgolly.scalajs.react.vdom.html_<^._

object RootModule {

  case object Root extends MenuItem("Root", "", (_, _) => <.div())
  case object DashBoard extends MenuItem("ダッシュボード", "#dashboard", (mi, c) => Motd(mi, c))

  val routes: StaticDsl.Rule[MenuItem] = RouterConfigDsl[MenuItem].buildRule { dsl =>
    import dsl._

    implicit val method: Redirect.Replace.type = Redirect.Replace

    (trimSlashes
      | staticRoute(root, Root) ~> redirectToPage(DashBoard)
      | staticRoute(DashBoard.routerPath, DashBoard) ~> renderR(c => MainPage(DashBoard, c))
    )
  }
}
