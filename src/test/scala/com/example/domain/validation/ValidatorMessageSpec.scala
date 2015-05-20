package com.example.domain.validation

import com.example.domain.validation.ValidatorExtensions.ExtendedValidator
import com.example.domain.{Applicant, FieldError}
import org.specs2.mutable.Specification
import skinny.validator.Validator

/**
 * Created by chrism on 18/05/2015.
 */
class ApplicantValidatorSpec extends Specification  {
  "This is a spec for a Applicant Based Validator".txt

  "The apply function of the singleton ApplicantBasedValidator should return a new Instance" >> {
    ApplicantValidator(Applicant("12412412412", "email@yahoo.com", "email@yahoo.com")) must beAnInstanceOf[Validator]
  }

  "A valid Applicant should not indicate any errors" >> {
    ApplicantValidator(Applicant("4124124124", "email@yahoo.com", "email@yahoo.com")).hasErrors must beFalse
  }

  "A none numeric password should produce a fieldError"  >> {
    val v = ApplicantValidator(Applicant("41241F24124", "email@yahoo.com", "email@yahoo.com"))
    v.hasErrors must beTrue
    v.fieldErrors() must contain(FieldError("passportNumber", "passportNumber is not a valid number"))
  }

  "A none valid email should produce a fieldError"  >> {
    val v = ApplicantValidator(Applicant("4124124124", "emailyahoo.com", "email@yahoo.com"))
    v.hasErrors must beTrue
    v.fieldErrors() must contain(FieldError("email", "email is not a valid email"))
  }

  "A missing passport number should produce a fieldError"  >> {
    val v = ApplicantValidator(Applicant("", "email@yahoo.com", "email@yahoo.com"))
    v.hasErrors must beTrue
    v.fieldErrors() must contain(FieldError("passportNumber", "passportNumber is required"))
  }

  "multiple field errors can be produced"  >> {
    val v = ApplicantValidator(Applicant("", "emailyahoo.com", "email@yahoo.com"))
    v.hasErrors must beTrue
    v.fieldErrors() must contain(allOf(FieldError("passportNumber", "passportNumber is required"), FieldError("email", "email is not a valid email")))
  }

}

