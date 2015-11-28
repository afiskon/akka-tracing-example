package me.eax.examples.tracing

import akka.actor._
import akka.pattern.ask
import kamon.trace.Tracer

import scala.concurrent.Future

case object SecondActor {

  case class SayHelloSecond(msg: String)

  case class AskExt(ref: ActorRef) extends AskHelper {
    def sayHello(msg: String): Future[Unit] = {
      (ref ? SayHelloSecond(msg)).mapTo[Unit]
    }
  }
}

class SecondActor extends Actor with ActorLogging {
  import SecondActor._

  override def receive: Receive = {
    case r: SayHelloSecond =>
      val token = Tracer.currentContext.token
      log.debug(s"Second actor: ${r.msg}, token = $token")

      sender() ! {}
  }
}
