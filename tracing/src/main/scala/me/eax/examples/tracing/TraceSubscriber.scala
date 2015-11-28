package me.eax.examples.tracing

import akka.actor.{ActorLogging, Actor}
import kamon.trace.TraceInfo

class TraceSubscriber extends Actor with ActorLogging {
  var traceNumber = 0
  override def receive: Receive = {
    case inf: TraceInfo =>
      traceNumber = traceNumber + 1
      val segmentsNumber = inf.segments.size
      log.info(
        s"""|Trace number $traceNumber ---
            |Trace Name: ${inf.name},
            |timestamp: ${inf.timestamp},
            |elapsedTime: ${inf.elapsedTime},
            |segmentsNumber: $segmentsNumber
            |""".stripMargin.replace('\n', ' ')
      )

  }
}
