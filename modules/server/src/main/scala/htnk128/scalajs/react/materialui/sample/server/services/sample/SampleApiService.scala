package htnk128.scalajs.react.materialui.sample.server.services.sample

import com.typesafe.scalalogging.LazyLogging
import htnk128.scalajs.react.materialui.sample.server.exception.DataNotFoundException
import htnk128.scalajs.react.materialui.sample.server.model.scalikejdbc.Sample
import htnk128.scalajs.react.materialui.sample.server.repository.SampleRepository
import htnk128.scalajs.react.materialui.sample.server.repository.db.{ PkCriteria, SampleSearchCriteria, UsesDB }
import htnk128.scalajs.react.materialui.sample.server.system.DIProvider
import htnk128.scalajs.react.materialui.sample.shared.api.sample._

trait SampleApiService extends SampleApi with LazyLogging with UsesDB {

  lazy private val sampleRepository = DIProvider.get(classOf[SampleRepository[Sample]])

  override def sampleList(condition: SampleSearchCondition): Seq[SampleData] =
    runWithReadOnly { implicit ctx =>
      sampleRepository.findAll(SampleSearchCriteria(condition.name))
    }
      .map(getAsSampleData)

  def sampleLoad(id: Int): SampleData =
    runWithReadOnly { implicit ctx =>
      sampleRepository.find(PkCriteria(id))
    }
      .map(getAsSampleData)
      .getOrElse(throw new DataNotFoundException(s"sample not found.(id: $id)"))

  override def sampleAdd(data: SampleData): SampleData = {
    val sample = runWithTx { implicit ctx =>
      sampleRepository.create(getAsSample(data))
    }
    getAsSampleData(sample)
  }

  override def sampleEdit(data: SampleData): SampleData = {
    val sample = runWithTx { implicit ctx =>
      sampleRepository.save(getAsSample(data))
    }
    getAsSampleData(sample)
  }

  override def sampleDelete(data: SampleData): SampleData = {
    val sample = runWithTx { implicit ctx =>
      sampleRepository.destroy(PkCriteria(data.id))
    }
    getAsSampleData(sample)
  }

  private def getAsSampleData(s: Sample) = SampleData(s.id, s.name)

  private def getAsSample(sd: SampleData) = Sample(sd.id, sd.name)
}
