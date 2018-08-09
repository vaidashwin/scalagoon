package modules

import app.messaging.{ServerMessage, JoinMessage, IrcMessage}

import scala.util.matching.Regex

/**
  * Created by Ashwin on 8/5/18.
  */
class JoinModule(channel: String) extends IrcModule {
  override def func: PartialFunction[IrcMessage, Option[IrcMessage]] = {
    case ServerMessage(4, _) => Some(JoinMessage(channel))
  }
}
