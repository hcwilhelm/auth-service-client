package io.relayr.authservice.model

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

case class AuthToken(token: String, scopes: List[String], issueDate: DateTime, expiryDate: DateTime, appId: UUID, userId: UUID, mark: Option[String] = None) {
  def isExpired: Boolean = expiryDate.isBeforeNow
}

object AuthToken {
  implicit val jsonFormat = Json.format[AuthToken]
  def toJsonString(authToken: AuthToken): String = Json.stringify(Json.toJson(authToken))
}
