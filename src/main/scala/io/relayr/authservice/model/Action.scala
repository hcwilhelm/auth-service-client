package io.relayr.authservice.model

sealed trait Action
case object Read extends Action
case object Write extends Action
case object Admin extends Action
case class UnsupportedAction(value: String) extends Action

object Action {
  def apply(s: String): Action = s.toLowerCase match {
    case "read" => Read
    case "write" => Write
    case "admin" => Admin
    case _ => UnsupportedAction(s)
  }

  def unapply(o: Action): Option[String] = o match {
    case Read => Some("read")
    case Write => Some("write")
    case Admin => Some("admin")
    case action: UnsupportedAction => Some(action.value)
  }
}
