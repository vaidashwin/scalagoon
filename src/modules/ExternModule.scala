package modules

import app.messaging.{IrcMessage, ChatMessage}


import scalabotlib.testmodule.IrcModuleClj


class ExternModule(A : IrcModuleClj) extends IrcModule {
  //val check = A.regex.r

  override def func: PartialFunction[IrcMessage, Option[IrcMessage]] = {
    case ChatMessage(user, channel, A.regex.r(message)) =>
      Option(A.func(user, channel, message)) match {
        case Some(i: String) => Some(ChatMessage(user, channel, i))
        case None => None
      }
  }

}
