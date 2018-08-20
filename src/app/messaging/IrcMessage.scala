package app.messaging

/**
  * Created by Ashwin on 8/7/18.
  */
trait IrcMessage {
  def serialize: String = throw new RuntimeException("Message of type " + this.getClass.getSimpleName + " inccorrectly serialized.")
}

// for messages from the server
case class ServerMessage(code: Int, body: String) extends IrcMessage

case class ChatMessage(user: String, channel: String, message: String) extends IrcMessage {
  override def serialize = {
    val msgTemplate = "PRIVMSG " + channel + " :" + _
    val length = 384 - msgTemplate("").length // 512 is max length, but lets be safe
    message.split(' ').foldLeft(List[String]()){ (messages, word) =>
      if ( messages.headOption.exists(_.length >= length) ) {
        word :: messages
      } else {
        messages match {
          case currLine :: tail => currLine + " " + word :: tail
          case _ => word :: Nil
        }
      }
    }.reverse.map(msgTemplate) match {
      case msg @ too :: long :: words :: _ :: _ => (too :: long :: words :: " (truncated)" :: Nil).reduce(_ + "\r\n" + _)
      case msg => msg.reduce(_ + "\r\n" + _)
    }
  }
}

case class PingMessage(body: String) extends IrcMessage

case class PongMessage(body: String) extends IrcMessage {
  override def serialize = "PONG " + body
}

case class JoinMessage(channel: String) extends IrcMessage {
  override def serialize = "JOIN " + channel
}

object IrcMessage {
  val pingMsgRegex = "PING (.*)$".r
  val chatMsgRegex = ":([^!]+).* PRIVMSG (#\\w+) :(.*)$".r
  val serverMsgRegex = ":.+ (\\d+) \\w+ (.*)$".r
  def inputMatcher: PartialFunction[String, IrcMessage] = {
    case pingMsgRegex(body: String) => PingMessage(body)
    case chatMsgRegex(user: String, channel: String, message: String) => ChatMessage(user, channel, message)
    case serverMsgRegex(code: String, body: String) => ServerMessage(code.toInt, body)
  }
}