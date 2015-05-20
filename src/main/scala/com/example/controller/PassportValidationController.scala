package com.example.controller

import com.example.domain.{FieldError, Applicant}
import com.example.domain.validation.ApplicantValidator
import com.example.domain.validation.ValidatorExtensions.ExtendedValidator

/**
 * Created by chrism on 20/05/2015.
 */
object PassportValidationController {
  def validate(applicant : Applicant): Iterable[FieldError] = {
    ApplicantValidator(applicant).fieldErrors()
  }
}
