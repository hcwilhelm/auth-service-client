package io.relayr.authservice.model

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json


case class CreateAuthToken(scopes: List[String], expiryDate: DateTime, appId: UUID, userId: UUID, mark: Option[String])

object CreateAuthToken {
  implicit val jsonFormat = Json.format[CreateAuthToken]
}
