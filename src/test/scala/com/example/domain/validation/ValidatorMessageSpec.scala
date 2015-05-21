package com.example.domain.validation

import com.example.domain.validation.ValidatorExtensions.ExtendedValidator
import com.example.domain.{Applicant, FieldError}
import skinny.validator.Validator
import org.scalatest.{FunSpec, Matchers}

/**
 * Created by chrism on 18/05/2015.
 */
class ApplicantValidatorSpec extends FunSpec with Matchers {

  describe("An ApplicantValidator woth a valid Applicant"){
    val av = ApplicantValidator(Applicant("12412412412", "email@yahoo.com", "email@yahoo.com"))
    it("should return an instance of a Validator when calling its apply method") {
      av shouldBe a [Validator]
    }

    it("should indicate that there are no errors with a valid applicant"){
      av.hasErrors should be (false)
    }
  }

  describe("An ApplicantValidator with an invalid passport number") {
    val iav = ApplicantValidator(Applicant("41241F24124", "email@yahoo.com", "email@yahoo.com"))

    it("should return an instance of a Validator when calling its apply method") {
      iav shouldBe a [Validator]
    }

    it("should indicate an error with an invalid applicant") {
      iav.hasErrors should be(true)
    }

    it("should include a FieldError to indicate passportNumber is not a valid number") {
      iav.fieldErrors() should contain(FieldError("passportNumber", "passportNumber is not a valid number"))
    }
  }

  describe("An ApplicantValidator with an invalid email") {
    val v = ApplicantValidator(Applicant("4124124124", "emailyahoo.com", "email@yahoo.com"))
    it("should include a FieldError to indicate email is not a valid email") {
      v.fieldErrors() should contain(FieldError("email", "email is not a valid email"))
    }
  }

  describe("An ApplicantValidator with a missing passport number ") {
    val v = ApplicantValidator(Applicant("", "emailyahoo.com", "email@yahoo.com"))
    it("should include a FieldError to indicate passportNumber is required") {
      v.fieldErrors() should contain(FieldError("passportNumber", "passportNumber is required"))
    }
  }

  describe("An ApplicantValidator with a missing passport number and an invalid email address ") {
    val v = ApplicantValidator(Applicant("", "emailyahoo.com", "email@yahoo.com"))
    it("should include FieldErrors to indicate the missing passport number and the invalid email address") {
      v.fieldErrors() should contain allOf (FieldError("passportNumber", "passportNumber is required"), FieldError("email", "email is not a valid email"))
    }
  }
}

