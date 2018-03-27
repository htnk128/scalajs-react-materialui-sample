package htnk128.scalajs.react.materialui.sample.server.model.scalikejdbc

import scalikejdbc._

case class Sample(
  id: Int,
  name: String) {

  def save()(implicit session: DBSession = Sample.autoSession): Sample = Sample.save(this)(session)

  def destroy()(implicit session: DBSession = Sample.autoSession): Int = Sample.destroy(this)(session)

}

object Sample extends SQLSyntaxSupport[Sample] {

  override val tableName = "sample"

  override val columns = Seq("id", "name")

  def apply(s: SyntaxProvider[Sample])(rs: WrappedResultSet): Sample = apply(s.resultName)(rs)
  def apply(s: ResultName[Sample])(rs: WrappedResultSet): Sample = new Sample(
    id = rs.get(s.id),
    name = rs.get(s.name)
  )

  val s = Sample.syntax("s")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Sample] = {
    withSQL {
      select.from(Sample as s).where.eq(s.id, id)
    }.map(Sample(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Sample] = {
    withSQL(select.from(Sample as s)).map(Sample(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Sample as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Sample] = {
    withSQL {
      select.from(Sample as s).where.append(where)
    }.map(Sample(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Sample] = {
    withSQL {
      select.from(Sample as s).where.append(where)
    }.map(Sample(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Sample as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String)(implicit session: DBSession = autoSession): Sample = {
    val generatedKey = withSQL {
      insert.into(Sample).namedValues(
        column.name -> name
      )
    }.updateAndReturnGeneratedKey.apply()

    Sample(
      id = generatedKey.toInt,
      name = name)
  }

  def batchInsert(entities: Seq[Sample])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name))
    SQL("""insert into sample(
      name
    ) values (
      {name}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Sample)(implicit session: DBSession = autoSession): Sample = {
    withSQL {
      update(Sample).set(
        column.id -> entity.id,
        column.name -> entity.name
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Sample)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Sample).where.eq(column.id, entity.id) }.update.apply()
  }

}
