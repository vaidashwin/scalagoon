package app

import java.io._
import java.net.Socket
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.{XPathConstants, XPathFactory}

import app.messaging.{ChatMessage, IrcMessage}
import modules._
import org.w3c.dom.NodeList

import scalabotlib.testmodule.TestModuleClj

import scalabotlib.db.initStore
import scalabotlib.initfile.getValue

/**
  * Created by Ashwin on 3/16/17.
  */
object Robot extends App {

  val documentBuilderFactory = DocumentBuilderFactory.newInstance()
  val xpathFactory = XPathFactory.newInstance()

  override def main(args: Array[String]): Unit = {
    val nick, login = Option(getValue("nickname")).getOrElse("gangsinesjr")
    val channel = Option(getValue("channel")).getOrElse("#mtgoon")
    val endLine = "\r\n"

    val serverUrl = Option(getValue("server")).getOrElse("irc.synirc.net")
    val socket = new Socket(serverUrl, 6667)
    val botWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))
    val botReader = new BufferedReader(new InputStreamReader(socket.getInputStream))

    // initStore is so the fs-store is setup before the bot comes online
    initStore
    botWriter.write(s"NICK $nick $endLine")
    botWriter.write(s"USER $login 8 * : I AM GANGSINES $endLine")
    botWriter.flush()

    def respond(message: IrcMessage) = {
      val msgString = message.serialize
      println(msgString)
      botWriter.write(s"$msgString$endLine")
      botWriter.flush()
    }

    val modules: List[IrcModule] = PongModule ::
      new JoinModule(channel) ::
      new GonnaGiveItToYaModule ::
      new ScryfallModule(respond) ::
      new MerriamWebsterModule(respond) ::
//      Clojure modules are added like so:
//      new ExternModule(new TestModuleClj) ::
      Nil

    val helpFunc: PartialFunction[IrcMessage, Option[IrcMessage]] = {
      case ChatMessage(user, ch, s) if s == s"hi $nick" =>
        val moduleMessage = modules.flatMap(_.helpBlurb).mkString("; ")
        Some(ChatMessage(nick, ch, s"hi i'm $nick, you can say i'm broken or ask me at https://github.com/vaidashwin/scalagoon/  My modules are: $moduleMessage"))
    }

    val moduleFunc: PartialFunction[IrcMessage, Option[IrcMessage]] = helpFunc orElse modules.map(_.func).reduce(_ orElse _)


    var line = ""
    while ( {
      line = botReader.readLine()
      line != null
    }) {
      println(line)
      Some(line).collect(IrcMessage.inputMatcher).collect(moduleFunc).foreach(_.foreach(ircMessage => respond(ircMessage)))
    }
  }

}
