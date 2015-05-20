package com.example.domain.validation

import skinny.validator._

import com.example.domain._

/**
 * Created by chrism on 18/05/2015.
 */

object ValidatorExtensions {

  val messages = Messages.loadFromConfig("errorfield-messages")

  implicit class ExtendedValidator(validator: Validator){

    private def formatErrorMessage(errorMessageType: String, parameterName: String, messageParameters: Seq[Any]): String =
      messages.get(errorMessageType, Seq(parameterName) ++ messageParameters).getOrElse("")

    def fieldErrors(): Iterable[FieldError] =
      for (errorParameter <- validator.errors.errors;
           individualErrors <- errorParameter._2;
           paramName = errorParameter._1;
           messageParams = individualErrors.messageParams)
        yield FieldError(paramName, formatErrorMessage(individualErrors.name, paramName, messageParams))
    }
}

object ApplicantValidator extends Validator {

  def apply(applicant: Applicant): Validator = {
    Validator()(
      param("passportNumber" -> applicant.passportNumber) is required & numeric,
      param("email" -> applicant.email) is required & email,
      param("emailConfirmation" -> applicant.emailConfirmation) is required & email
    )
  }
}