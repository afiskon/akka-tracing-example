package me.eax.examples.tracing

import scala.concurrent.duration._
import akka.util.Timeout

trait AskHelper {
  implicit protected val timeout = Timeout(5.seconds)
}
