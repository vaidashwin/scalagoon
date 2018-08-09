package modules.jsonserde

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


case class MagicCard( name: String,
                      cost: Option[String],
                      cardType: String,
                      oracle: String,
                      pow: Option[String],
                      tou: Option[String],
                      usd: Option[String],
                      eur: Option[String],
                      tix: Option[String]
                    ) {
  override def toString: String = {
    val builder = new StringBuilder()
    builder.append(name + " | ")
    builder.append(cardType)
    pow.foreach(p => tou.foreach{ t =>
      builder.append(" " + p + " / " + t)
    })
    cost.foreach(c => builder.append(", " + c))
    builder.append(" | ")
    builder.append(oracle)
    val prices = usd.map(_ + " USD" + eur.map(e => " " + e + " EUR" + tix.map(t => " " + t + " TIX").getOrElse("")).getOrElse(""))
    prices.foreach(p => builder.append(" | " + p))
    builder.toString.replace("\n", " ")
  }
}

object MagicCard {
  implicit val magicCardReads: Reads[MagicCard] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "mana_cost").readNullable[String] and
    (JsPath \ "type_line").read[String] and
    (JsPath \ "oracle_text").read[String] and
    (JsPath \ "power").readNullable[String] and
    (JsPath \ "toughness").readNullable[String] and
    (JsPath \ "usd").readNullable[String] and
    (JsPath \ "eur").readNullable[String] and
    (JsPath \ "tix").readNullable[String]
  )(MagicCard.apply _)
}