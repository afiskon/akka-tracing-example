package me.eax.examples.tracing

import akka.actor._
import kamon.Kamon
import kamon.trace.Tracer
import org.slf4j.LoggerFactory
import com.typesafe.scalalogging._

import scala.compat.Platform
import scala.concurrent._
import scala.concurrent.duration._

object Main extends App {
  Kamon.start()
  val log = Logger(LoggerFactory.getLogger(this.getClass))
  val system = ActorSystem("system")
  val traceSubscriber = system.actorOf(Props(new TraceSubscriber), "traceSubscriber")
  Kamon.tracer.subscribe(traceSubscriber)

  val secondActorRef = system.actorOf(Props(new SecondActor), "secondActor")
  val second = SecondActor.AskExt(secondActorRef)

  val firstActorRef = system.actorOf(Props(new FirstActor(second)), "firstActor")
  val first = FirstActor.AskExt(firstActorRef)

  def runTest(): Unit = {
    val testsNumber = 2048
    Tracer.withNewContext("TestContext", traceToken = Some(s"dummy-trace-token"), autoFinish = true) {
      for (i <- 1 to testsNumber) {
        val fResult = {
          Tracer.currentContext.withNewAsyncSegment("Main/sayHello", "ask", "me.eax.example") {
            first.sayHello(s"Message $i")
          }
        }

        Await.result(fResult, Duration.Inf)
      }
    }
  }

  for(_ <- 1 to 256) runTest()

  Kamon.shutdown()
  system.shutdown()
  system.awaitTermination()
}
