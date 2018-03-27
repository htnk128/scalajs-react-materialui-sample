package htnk128.scalajs.react.materialui.sample.client.components.validation

abstract class FormLayout[T](private var dataModel: T) {

  private val fields = Map.newBuilder[String, FormField[_]]

  def setDataModel(dataModel: T): Unit = this.dataModel = dataModel

  def currentDataModel: T = dataModel

  def createField[V](
    getValue: T => V,
    validator: V => ValidationResult = (_: V) => ValidationResult.Success
  ): String = {
    val uuid = java.util.UUID.randomUUID.toString
    fields += uuid -> DefaultFormField[V](() => getValue(currentDataModel), validator)
    uuid
  }

  def getField(key: String): FormField[_] =
    fields
      .result
      .find(_._1 == key)
      .map(_._2)
      .getOrElse(throw new Exception(s"'$key' field can not be found."))

  def isValid: Boolean = {
    val _fields = fields.result
    val result = _fields
      .map {
        case (_, f) =>
          f.validate()
      }
      .count(!_.isValid) == 0
    fields.clear
    fields ++= _fields
    result
  }

  def validatedDataModel: Option[T] =
    if (!isValid) None else Some(currentDataModel)
}
