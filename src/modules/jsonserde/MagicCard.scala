package modules.jsonserde

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Face(
                 cost: Option[String],
                 cardType: String,
                 oracle: String,
                 pow: Option[String],
                 tou: Option[String]
               ) {
  override def toString: String = {
    val body = pow.map(_ + "/" + tou.getOrElse("")).map(b => s" $b").getOrElse("")
    s"$cardType$body${cost.map(c => s" $c").getOrElse("")} | $oracle"
  }
}

class MagicCard( val name: String,
                 val faces: List[Face],
                 val usd: Option[String],
                 val eur: Option[String],
                 val tix: Option[String]
               ) {

  override def toString: String = {
    val prices = usd.map(u => s" $u USD").getOrElse("") + eur.map(e => s" $e EUR").getOrElse("") + tix.map(t => s" $t TIX").getOrElse("")
    "[\\r\\n]".r.replaceAllIn(faces match {
      case face :: Nil => s"$name | $face |$prices "
      case _ =>
        val faceString = faces.map(face => s"${face.toString}").mkString(" // ")
        s"$name || $faceString ||$prices"
    }, " ")
  }
}

object MagicCard {
  def apply(name: String,
            faces: Option[List[Face]],
            cost: Option[String],
            cardType: Option[String],
            oracle: Option[String],
            pow: Option[String],
            tou: Option[String],
            usd: Option[String],
            eur: Option[String],
            tix: Option[String]
           ): MagicCard = {
    faces.getOrElse(Nil) match {
      case Nil => new MagicCard(name, Face(cost, cardType.getOrElse(""), oracle.getOrElse(""), pow, tou) :: Nil, usd, eur, tix)
      case f => new MagicCard(name, f, usd, eur, tix)
    }
  }

  implicit val faceRead: Reads[Face] = (
    (JsPath \ "mana_cost").readNullable[String] and
    (JsPath \ "type_line").read[String] and
    (JsPath \ "oracle_text").read[String] and
    (JsPath \ "power").readNullable[String] and
    (JsPath \ "toughness").readNullable[String]
  )(Face.apply _)

  implicit val magicCardReads: Reads[MagicCard] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "card_faces").readNullable[List[Face]] and
    (JsPath \ "mana_cost").readNullable[String] and
    (JsPath \ "type_line").readNullable[String] and
    (JsPath \ "oracle_text").readNullable[String] and
    (JsPath \ "power").readNullable[String] and
    (JsPath \ "toughness").readNullable[String] and
    (JsPath \ "usd").readNullable[String] and
    (JsPath \ "eur").readNullable[String] and
    (JsPath \ "tix").readNullable[String]
  )(MagicCard.apply _)
}