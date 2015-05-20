package com.example.infrastructure

import spray.json.{DefaultJsonProtocol}
import com.example.domain._
import spray.json.pimpAny

object JsonProtocol extends DefaultJsonProtocol {

  implicit class ExtendedFieldError(fieldErrors: Iterable[FieldError]){
    def serializeToJson(): String = {
      Map("error" -> fieldErrors).toJson.compactPrint
    }
  }

  implicit val applicantFormat = jsonFormat3(Applicant)
  implicit val fieldErrorFormat = jsonFormat2(FieldError)
}