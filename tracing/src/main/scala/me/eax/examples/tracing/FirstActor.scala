package me.eax.examples.tracing

import akka.actor._
import akka.pattern.ask
import kamon.trace.Tracer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

case object FirstActor {

  case class SayHelloFirst(msg: String)

  case class AskExt(ref: ActorRef) extends AskHelper {
    def sayHello(msg: String): Future[Unit] = {
      (ref ? SayHelloFirst(msg)).mapTo[Unit]
    }
  }
}

class FirstActor(second: SecondActor.AskExt) extends Actor with ActorLogging {
  import FirstActor._

  override def receive: Receive = {
    case r: SayHelloFirst =>
      val token = Tracer.currentContext.token
      log.debug(s"First actor: ${r.msg}, token = $token")
      val origSender = sender()
      second.sayHello(r.msg) map { _ =>
        origSender ! {}
      }
  }
}
