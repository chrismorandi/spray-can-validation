package com.example.service

import akka.actor.Actor
import spray.http.StatusCodes
import spray.routing._

import com.example.domain.Applicant
import com.example.controller.PassportValidationController


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class PassportValidationServiceActor extends Actor with PassportValidationService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait PassportValidationService extends HttpService {

  import com.example.infrastructure.JsonProtocol._
  import spray.httpx.SprayJsonSupport._

  def allGoodAtRoot = path("") { get { complete {"all good!"} } }

  def myRoute = allGoodAtRoot ~ passportValidationRoute ~ passportApplicationRoute

  def passportValidationRoute =
    path("passport" / ".*".r / "validate") { passportNumber =>
        get {
          complete { "Passport number correct" }
        }
      }

  def passportApplicationRoute =
    path("application"){
      post {
        entity(as[Applicant]) { applicant =>
          val errors = PassportValidationController.validate(applicant)
          if (errors isEmpty) complete("Creating applicant: %s".format(applicant.passportNumber))
          else complete(StatusCodes.BadRequest, errors.serializeToJson)
        }
      }
    }
}