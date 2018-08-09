package modules

import app.messaging.{ChatMessage, IrcMessage}

import scala.util.matching.Regex

/**
  * Created by Ashwin on 8/5/18.
  */
class GonnaGiveItToYaModule extends IrcModule {
  var xs: Int = 0

  override def func: PartialFunction[IrcMessage, Option[IrcMessage]] = {
    case ChatMessage(user, channel, message) if {
      user.startsWith("ChewyLSB") &&
      message.matches(".*\\bx\\b.*")
    } => Some(ChatMessage("", channel, "gon give it to ya"))
  }
}
