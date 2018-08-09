package modules

import app.messaging.{PingMessage, PongMessage, IrcMessage}

/**
  * Created by Ashwin on 8/5/18.
  */
object PongModule extends IrcModule {
  override def func: PartialFunction[IrcMessage, Option[IrcMessage]] = {
    case PingMessage(body) => Some(PongMessage(body))
  }
}
