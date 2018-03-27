package htnk128.scalajs.react.materialui.sample.client.actions

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import autowire._
import boopickle.Default._
import diode._
import diode.data._
import diode.util._
import htnk128.scalajs.react.materialui.sample.client.services.AjaxClient
import htnk128.scalajs.react.materialui.sample.shared.api.Api
import htnk128.scalajs.react.materialui.sample.shared.api.sample._

object SampleAction {

  case class SampleSearch(
    condition: SampleSearchCondition,
    potResult: Pot[SampleContext] = Empty
  ) extends PotAction[SampleContext, SampleSearch] {
    override def next(value: Pot[SampleContext]) = SampleSearch(condition, value)
  }

  case class SampleLoad(id: Int) extends Action

  case class SampleAdd(data: SampleData) extends Action

  case class SampleEdit(data: SampleData) extends Action

  case class SampleDelete(data: SampleData) extends Action

  case class Update(
    list: SampleListData,
    searchCondition: SampleSearchCondition,
    edit: SampleData
  ) extends Action
}

class SampleHandler[M](modelRW: ModelRW[M, Pot[SampleContext]]) extends ActionHandler(modelRW) {
  implicit val runner: RunAfterJS = new RunAfterJS

  import SampleAction._

  private def contextValue: SampleContext = value.toOption match {
    case Some(mc) => mc
    case _ =>
      SampleContext(SampleListData(), SampleSearchCondition(), SampleData())
  }

  override def handle: PartialFunction[Any, ActionResult[M]] = {
    case action: SampleSearch =>
      action.handleWith(
        this,
        action.effect(AjaxClient[Api].sampleList(action.condition).call()) { uds =>
          SampleContext(SampleListData(uds), action.condition, SampleData())
        }
      )(PotAction.handler())
    case SampleLoad(id) =>
      val ctx = contextValue
      val f = AjaxClient[Api].sampleLoad(id).call()

      effectOnly(Effect(f.map(edit => Update(ctx.list, ctx.searchCondition, edit))))
    case SampleAdd(data) =>
      val ctx = contextValue
      val f = AjaxClient[Api].sampleAdd(data).call()

      effectOnly(Effect(f.map(_ => SampleSearch(ctx.searchCondition))))
    case SampleEdit(data) =>
      val ctx = contextValue
      val f = AjaxClient[Api].sampleEdit(data).call()

      effectOnly(Effect(f.map(_ => SampleSearch(ctx.searchCondition))))
    case SampleDelete(data) =>
      val ctx = contextValue
      val f = AjaxClient[Api].sampleDelete(data).call()

      effectOnly(Effect(f.map(_ => SampleSearch(ctx.searchCondition))))
    case Update(list, searchCondition, edit) =>
      if (value.nonEmpty) {
        val newValue = value
          .map(_.copy(
            list = list,
            searchCondition = searchCondition,
            edit = edit
          ))

        updated(newValue)
      }
      else updated(Ready(SampleContext(SampleListData(), SampleSearchCondition(), SampleData())))
  }
}
