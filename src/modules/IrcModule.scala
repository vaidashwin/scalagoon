package modules

import app.messaging.IrcMessage

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.matching.Regex

/**
  * Created by Ashwin on 8/5/18.
  */
trait IrcModule {
  def func: PartialFunction[IrcMessage, Option[IrcMessage]]

}

abstract class AsyncModule(callback: IrcMessage => Unit) extends IrcModule {
  val asyncBehavior: PartialFunction[IrcMessage, Future[Option[IrcMessage]]]
  override def func: PartialFunction[IrcMessage, Option[IrcMessage]] = asyncBehavior.andThen { future =>
    future.onComplete {
      case Success(result) => result.foreach(callback)
      case Failure(result) =>
        println(s"asynchBehavior for module ${this.getClass.getSimpleName} failed: $result $result")
        result.printStackTrace()
    }
    None
  }
}