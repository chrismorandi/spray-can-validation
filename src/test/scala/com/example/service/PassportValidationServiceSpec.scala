package com.example.service

import spray.http._
import spray.json._
import spray.testkit.ScalatestRouteTest
import spray.http.StatusCodes._
import org.scalatest._
import org.scalatest.Matchers

import com.example.infrastructure.JsonProtocol._
import com.example.domain.Applicant


class PassportValidationServiceSpec extends FreeSpec with PassportValidationService with ScalatestRouteTest with Matchers {

  def actorRefFactory = system
  
  "The PassportValidationService" - {
    "when calling GET /" - {
      "should return a greeting" in {
        Get() ~> myRoute ~> check {
          status shouldEqual (OK)
        }
      }
    }

    "when posting a valid applicant" - {
      "should return the passport number and status OK" - {
        Post("/application", HttpEntity(MediaTypes.`application/json`, Applicant("123456789", "x@y.com", "return@y.com").toJson.toString)) ~>
          myRoute ~> check {
          status shouldEqual (OK)
          responseAs[String] should equal("Creating applicant: 123456789")
        }
      }
    }

    "when posting applicant with incorrect passportNumber" - {
      "should return a BadRequest with invalid passport indication in payload" - {
        Post("/application", HttpEntity(MediaTypes.`application/json`, Applicant("12345678T9", "x@y.com", "return@y.com").toJson.toString)) ~>
          myRoute ~> check {
          status shouldEqual BadRequest
          responseAs[String] should equal("{\"error\":[{\"fieldName\":\"passportNumber\",\"errorMessage\":\"passportNumber is not a valid number\"}]}")
        }
      }
    }

    "when posting an applicant with incorrect email" - {
      "should return a BadRequest with bad email indactor in the payload"
      Post("/application", HttpEntity(MediaTypes.`application/json`, Applicant("12345678T9", "xy.com", "return@y.com").toJson.toString)) ~>
        myRoute ~> check {
        status shouldEqual BadRequest
        responseAs[String] should equal("{\"error\":[{\"fieldName\":\"passportNumber\",\"errorMessage\":\"passportNumber is not a valid number\"},{\"fieldName\":\"email\",\"errorMessage\":\"email is not a valid email\"}]}")
      }
    }
  }


}
