package htnk128.scalajs.react.materialui.sample.server.repository.db

import scala.util.{ Failure, Success, Try }

import htnk128.scalajs.react.materialui.sample.server.exception.{ DataNotFoundException, ServerException }
import htnk128.scalajs.react.materialui.sample.server.model.scalikejdbc.Sample
import htnk128.scalajs.react.materialui.sample.server.model.scalikejdbc.Sample.s
import htnk128.scalajs.react.materialui.sample.server.repository.{ SampleRepository, _ }
import scalikejdbc._

class SampleJDBCRepository extends JDBCRepository[Sample] with SampleRepository[Sample] {

  lazy val defaultCtx = JDBCEntityIOContext(Sample.autoSession)

  override def findAll(criteria: Cri)(implicit ctx: Ctx): Try[Seq[Sample]] = withDBSession(ctx) { implicit ses =>
    criteria match {
      case SampleSearchCriteria(name) =>
        withSQL {
          select.from(Sample as s).where.like(s.name, s"$name%").orderBy(s.id).desc
        }.map(Sample(s.resultName)).list.apply()
      case _ =>
        throw new IllegalStateException(s"Unexpected criteria is bound (criteria: $criteria)")
    }
  }

  override def find(criteria: Cri)(implicit ctx: Ctx): Try[Option[Sample]] = withDBSession(ctx) { implicit s =>
    criteria match {
      case PkCriteria(pk) => Sample.find(pk)
      case _ => throw new IllegalStateException(s"Unexpected criteria is bound (criteria: $criteria)")
    }
  }

  override def create(entity: Sample)(implicit ctx: Ctx): Try[Sample] = withDBSession(ctx) { implicit s =>
    Sample.create(entity.name)
  }

  override def save(entity: Sample)(implicit ctx: Ctx): Try[Sample] = withDBSession(ctx) { implicit s =>
    find(PkCriteria(entity.id)) match {
      case Success(maybeT) =>
        maybeT
          .map(_ => Sample.save(entity))
          .getOrElse(throw new DataNotFoundException("Sample data not found."))
      case Failure(t) =>
        throw new ServerException(t.getMessage, t)
    }
  }

  override def destroy(criteria: Cri)(implicit ctx: Ctx): Try[Sample] = withDBSession(ctx) { implicit s =>
    find(criteria) match {
      case Success(maybeT) =>
        maybeT
          .map { t =>
            Sample.destroy(t)
            t
          }
          .getOrElse(throw new DataNotFoundException("Sample data not found."))
      case Failure(t) =>
        throw new ServerException(t.getMessage, t)
    }
  }
}

case class SampleSearchCriteria(name: String = "") extends Criteria
