package htnk128.scalajs.react.materialui.sample.shared.api.sample

case class SampleContext(
  list: SampleListData,
  searchCondition: SampleSearchCondition = SampleSearchCondition(),
  edit: SampleData
)

case class SampleData(id: Int, name: String)

object SampleData {

  def apply(): SampleData = {
    SampleData(id = 0, name = "")
  }
}

case class SampleListData(values: Seq[SampleData] = Seq.empty)

case class SampleSearchCondition(name: String = "")
