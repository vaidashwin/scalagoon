package modules

import java.net.URLEncoder

import app.messaging.{ChatMessage, IrcMessage}
import modules.jsonserde.MagicCard
import play.api.libs.json.{JsArray, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaj.http.{Http, HttpRequest}

import modules.jsonserde.MagicCard._

/**
  * Created by Ashwin on 8/6/18.
  */
class ScryfallModule(callback: IrcMessage => Unit) extends AsyncModule(callback) {
  val messageRegex = "mtg (.*)$".r

  override val asyncBehavior: PartialFunction[IrcMessage, Future[Option[IrcMessage]]] = {
    case ChatMessage(user, channel, message) =>
      message match {
        case messageRegex(query: String) => Future({
          println(URLEncoder.encode(query, "UTF-8").replace("+", "%20"))
          val request = Http(url = "https://api.scryfall.com/cards/search").param(key = "q", query)
          val json = Json.parse(request.asBytes.body)
          ((json \ "data").validate[JsArray].asOpt map {
            case JsArray(seq) =>
              val cardSeq =seq.flatMap(_.validate[MagicCard].asOpt)
              cardSeq.find(_.name.toLowerCase == query.trim.toLowerCase).orElse(cardSeq.headOption)
            case _ => None
          }).getOrElse(None).map(card => ChatMessage(user, channel, card.toString)).orElse(Some(ChatMessage(user, channel, "You done goofed.")))
        })
        case _ => Future(None)
      }

  }
}