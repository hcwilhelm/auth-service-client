package io.relayr.authservice

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import io.relayr.authservice.error.ErrorLike
import io.relayr.authservice.model._
import play.api.http.Status
import play.api.libs.json.Json
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaz.{-\/, \/, \/-}

/**
  * The AuthService Client used to call the endpoints on the
  * AuthService microservice. You need to inject the baseUrl
  *
  * Example : "http://localhost:8080"
  *
  */
class Client(val baseURL: String) {

  /**
    * The Actor system needed by the materializer
    */
  implicit val system = ActorSystem()

  /**
    * The akka stream materializer needed by the AhcWs http client
    */
  implicit val materializer = ActorMaterializer()

  /**
    * The WSClient implementation used to handle requests and
    * responses
    */
  private val ws = AhcWSClient()

  /**
    * Route endpoints for the AuthService
    */
  private val AUTHTOKEN_ROUTE = "/authtoken"
  private val DEVTOKEN_ROUTE = "/devtoken"
  private val AUTHORIZED_ROUTE = "/authorized"


  /**
    * Get an AuthToken by token
    *
    */
  def getAuthToken(token: String): Future[ErrorLike \/ AuthToken] = {
    val url = baseURL + AUTHTOKEN_ROUTE + "/" + token

    ws.url(url).get() map { response =>
      response.status match {
        case Status.OK => \/-(response.json.validate[AuthToken].get)
        case errorCode => -\/(ErrorLike(errorCode, response.body))
      }
    }
  }

  /**
    * Create a new AuthToken
    */
  def createAuthToken(createAuthToken: CreateAuthToken): Future[ErrorLike \/ AuthToken] = {
    val url = baseURL + AUTHTOKEN_ROUTE

    ws.url(url).post(Json.toJson(createAuthToken)) map { response =>
      response.status match {
        case Status.OK => \/-(response.json.validate[AuthToken].get)
        case errorCode => -\/(ErrorLike(errorCode, response.body))
      }
    }
  }

  /**
    * Delete an AuthToken by token
    */
  def deleteAuthToken(token: String): Future[ErrorLike \/ Unit] = {
    val url = baseURL + AUTHTOKEN_ROUTE + "/" + token

    ws.url(url).delete() map { response =>
      response.status match {
        case Status.NO_CONTENT => \/-(())
        case errorCode => -\/(ErrorLike(errorCode, response.body))
      }
    }
  }

  /**
    * Get an DevToken by appId
    *
    */
  def getDevToken(appId: UUID): Future[ErrorLike \/ AuthToken] = {
    val url = baseURL + DEVTOKEN_ROUTE + "/" + appId

    ws.url(url).get() map { response =>
      response.status match {
        case Status.OK => \/-(response.json.validate[AuthToken].get)
        case errorCode => -\/(ErrorLike(errorCode, response.body))
      }
    }
  }

  /**
    * Delete an AuthToken by token
    */
  def deleteDevToken(appId: UUID): Future[ErrorLike \/ Unit] = {
    val url = baseURL + DEVTOKEN_ROUTE + "/" + appId

    ws.url(url).delete() map { response =>
      response.status match {
        case Status.NO_CONTENT => \/-(())
        case errorCode => -\/(ErrorLike(errorCode, response.body))
      }
    }
  }

  def authorized(token: String, action: Action, deviceId: UUID): Future[ErrorLike \/ Boolean] = {

    val urlBuilder = new StringBuilder()
    urlBuilder.append(baseURL)
    urlBuilder.append(AUTHORIZED_ROUTE).append("/")
    urlBuilder.append(token).append("/")
    urlBuilder.append(action).append("/")
    urlBuilder.append(deviceId)

    ws.url(urlBuilder.toString()).get() map { response =>
      response.status match {
        case Status.NO_CONTENT => \/-(true)
        case Status.FORBIDDEN => \/-(false)
        case errorCode => -\/(ErrorLike(errorCode, response.body))
      }
    }
  }
}
