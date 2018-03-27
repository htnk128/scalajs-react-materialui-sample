package htnk128.scalajs.react.materialui.sample.server.model.scalikejdbc

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import scalikejdbc.config.DBs

class SampleSpec extends fixture.FlatSpec with Matchers with AutoRollback with BeforeAndAfterAll {
  val s = Sample.syntax("s")

  behavior of "Sample"

  private var inserted: Sample = _

  override def beforeAll(): Unit = {
    DBs.setupAll()
    inserted = Sample.create("SampleSpec")
  }

  override def afterAll(): Unit = {
    inserted.destroy()
    DBs.closeAll()
  }

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Sample.find(inserted.id)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Sample.findBy(sqls.eq(s.id, inserted.id))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Sample.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = Sample.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Sample.findAllBy(sqls.eq(s.id, inserted.id))
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Sample.countBy(sqls.eq(s.id, inserted.id))
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = Sample.create(name = "MyString")
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = Sample.find(inserted.id).get
    val modified = entity.copy(name = "SampleSpeca")
    val updated = Sample.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Sample.find(inserted.id).get
    val deleted = Sample.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Sample.find(inserted.id)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Sample.findAll()
    entities.foreach(e => Sample.destroy(e))
    val batchInserted = Sample.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
