package htnk128.scalajs.react.materialui.sample.shared.api.sample

trait SampleApi {

  def sampleList(condition: SampleSearchCondition): Seq[SampleData]

  def sampleLoad(id: Int): SampleData

  def sampleAdd(data: SampleData): SampleData

  def sampleEdit(data: SampleData): SampleData

  def sampleDelete(data: SampleData): SampleData
}
