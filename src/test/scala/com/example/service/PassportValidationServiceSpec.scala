package com.example.service

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.http._
import spray.testkit.Specs2RouteTest
import spray.json._

import com.example.infrastructure.JsonProtocol._
import com.example.domain.Applicant

class PassportValidationServiceSpec extends Specification with Specs2RouteTest with PassportValidationService {

  def actorRefFactory = system
  
  "PassportValidationService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> myRoute ~> check {
        responseAs[String] must contain("all good!")
      }
    }

    "post new applicant" in {
      Post("/application", HttpEntity(MediaTypes.`application/json`, Applicant("123456789","x@y.com","return@y.com").toJson.toString)) ~>
        myRoute ~> check {
        status shouldEqual OK //Created??
        responseAs[String] must contain("Creating applicant: 123456789")
      }
    }

    "post applicant with incorrect passportNumber" in {
      Post("/application", HttpEntity(MediaTypes.`application/json`, Applicant("12345678T9","x@y.com","return@y.com").toJson.toString)) ~>
        myRoute ~> check {
        status shouldEqual BadRequest
        responseAs[String] must contain("passportNumber is not a valid number")
      }
    }

    "post applicant with incorrect email" in {
      Post("/application", HttpEntity(MediaTypes.`application/json`, Applicant("12345678T9","xy.com","return@y.com").toJson.toString)) ~>
        myRoute ~> check {
        status shouldEqual BadRequest
        responseAs[String] must contain("email is not a valid email")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
