package htnk128.scalajs.react.materialui.sample.client.routes

import htnk128.scalajs.react.materialui.sample.client.components.sample._
import htnk128.scalajs.react.materialui.sample.client.model.{ Menu, MenuItem }
import htnk128.scalajs.react.materialui.sample.client.pages.MainPage
import japgolly.scalajs.react.extra.router.{ Redirect, RouterConfigDsl, StaticDsl }
import japgolly.scalajs.react.vdom.html_<^._

object SampleModule {

  val prefixPath: String = "#sample"

  val subMenu: Seq[Menu] = Seq(
    Menu("サブメニュー1", AppRouter.SamplePage(Submenu1.List), isBelong = {
      case AppRouter.SamplePage(_) => true
      case _ => false
    })
  )

  object Submenu1 {
    case object List extends MenuItem("サブメニュー1/一覧", "submenu1", (mi, c) => SampleList(mi, c))
    case object Add extends MenuItem("サブメニュー1/作成", "submenu1/add", (mi, c) => SampleEdit(None, mi, c))
    case class Edit(id: Int) extends MenuItem("サブメニュー1/編集", "submenu1/edit", (mi, c) => SampleEdit(Some(id), mi, c))

    val routes: StaticDsl.Rule[MenuItem] = RouterConfigDsl[MenuItem].buildRule { dsl =>
      import dsl._

      (trimSlashes
        | staticRoute(List.routerPath, List) ~> renderR(c => MainPage(List, c))
        | staticRoute(Add.routerPath, Add) ~> renderR(c => MainPage(Add, c))
        | dynamicRouteCT("submenu1" / "edit" / int.caseClass[Edit]) ~> dynRenderR((p, c) => MainPage(p, c))
      )
    }
  }

  case object Root extends MenuItem("SampleRoot", "", (_, _) => <.div())

  val routes: StaticDsl.Rule[MenuItem] = RouterConfigDsl[MenuItem].buildRule { dsl =>
    import dsl._

    implicit val method: Redirect.Replace.type = Redirect.Replace

    (trimSlashes
      | staticRoute(root, Root) ~> redirectToPage(Submenu1.List)
      | Submenu1.routes
    )
  }
}
