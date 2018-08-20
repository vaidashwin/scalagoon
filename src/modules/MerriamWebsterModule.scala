package modules

import app.messaging.{ChatMessage, IrcMessage}

import scala.concurrent.Future
import scala.xml.{Elem, XML}
import scalaj.http.Http

import scala.concurrent.ExecutionContext.Implicits.global

case class DictionaryEntry(partOfSpeech: String, definition: String) {
  override def toString = s"[$partOfSpeech] $definition"
}

/**
  * Created by Ashwin on 8/16/18.
  */
class MerriamWebsterModule(callback: IrcMessage => Unit) extends AsyncModule(callback) {
  override val helpBlurb = Some("merriam-webster (define <word>, more for more results)")
  val dictKey = "a9fad686-8bd7-4754-a4a6-839c68e94cbf"
  val thesKey = "a71ed6d4-9353-4fc8-8d14-f85aff56ebab"
  var results: List[DictionaryEntry] = Nil

  val dictCommand = "define (.*)".r
  val thesuarusCommand = "synonyms (.*)".r
  override val asyncBehavior: PartialFunction[IrcMessage, Future[Option[IrcMessage]]] = {
    case msg @ ChatMessage(user, channel, "more") =>
      Future(Some(results match {
        case result :: resultTail =>
          results = resultTail
          makeMessage(user, channel, result)
        case _ =>
          ChatMessage(user, channel, "No more results!!!!!!")
      }))
    case msg @ ChatMessage(user, channel, dictCommand(query: String)) =>
      Future({
        val request = Http(s"https://www.dictionaryapi.com/api/v1/references/collegiate/xml/$query").param("key", dictKey)
        val response: Elem = XML.loadString(request.asString.body)
        val newResults = (response \\ "entry").map { entry =>
          val pos = entry \ "fl"
          DictionaryEntry(
            pos.text,
            (entry \\ "dt").text.split(":").collect {
              case defn if defn.trim.nonEmpty => defn
            }.zipWithIndex.map {
              case (defn, idx) => s"${idx + 1}) $defn"
            }.mkString(" ")
          )
        }
        Some(newResults match {
          case result :: resultTail =>
            results = resultTail
            makeMessage(user, channel, result)
          case _ =>
            ChatMessage(user, channel, "Stop making up words!!!!!!")
        })
      })
    //case ChatMessage(user, channel, thesuarusCommand(query: String)) =>
  }

  def makeMessage(user: String, channel: String, result: DictionaryEntry) = ChatMessage(user, channel, s"$user: ${result.toString}")
}
