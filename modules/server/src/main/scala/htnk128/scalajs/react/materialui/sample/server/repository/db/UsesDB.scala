package htnk128.scalajs.react.materialui.sample.server.repository.db

import scala.util.{ Failure, Success, Try }

import scalikejdbc.TxBoundary.Try._
import scalikejdbc._

trait UsesDB {

  def runWithTx[T](f: JDBCEntityIOContext => Try[T]): T = DB localTx { session =>
    implicit val ctx: JDBCEntityIOContext = JDBCEntityIOContext(session)

    f(ctx)
  } match {
    case Success(r) => r
    case Failure(t) => throw t
  }

  def runWithReadOnly[T](f: JDBCEntityIOContext => Try[T]): T = DB readOnly { session =>
    implicit val ctx: JDBCEntityIOContext = JDBCEntityIOContext(session)

    f(ctx)
  } match {
    case Success(r) => r
    case Failure(t) => throw t
  }
}
