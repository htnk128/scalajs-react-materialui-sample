package htnk128.scalajs.react.materialui.sample.server.repository

import scala.util.Try

trait Repository[E] {

  type Ctx = EntityIOContext
  type Cri = Criteria

  val defaultCtx: Ctx

  def findAll(criteria: Cri)(implicit ctx: Ctx = defaultCtx): Try[Seq[E]]

  def find(criteria: Cri)(implicit ctx: Ctx = defaultCtx): Try[Option[E]]

  def create(entity: E)(implicit ctx: Ctx = defaultCtx): Try[E]

  def save(entity: E)(implicit ctx: Ctx = defaultCtx): Try[E]

  def destroy(criteria: Cri)(implicit ctx: Ctx = defaultCtx): Try[E]
}
