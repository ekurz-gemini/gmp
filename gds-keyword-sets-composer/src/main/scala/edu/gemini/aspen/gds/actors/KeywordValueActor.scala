package edu.gemini.aspen.gds.actors

import actors.Actor
import edu.gemini.aspen.gds.api.CollectedValue

/**
 * Message indicating that a value should be collected
 */
case class Collect()

/**
 * Trait for an actor that retrieve a specific value
 *
 * It is expected that the reply will be a List[CollectedValues]
 */
trait KeywordValueActor extends Actor {
    start

    override def act() {
        loop {
            react {
                case Collect => reply(collectValues)
            }
        }
    }

    def collectValues: List[CollectedValue]
}