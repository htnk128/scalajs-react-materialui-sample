package htnk128.scalajs.react.materialui.sample.server.repository.db

import scala.util.Try

import htnk128.scalajs.react.materialui.sample.server.repository.{ EntityIOContext, Repository }
import scalikejdbc.DBSession

abstract class JDBCRepository[E] extends Repository[E] {

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): Try[A] = Try {
    ctx match {
      case JDBCEntityIOContext(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }
}
