package edu.gemini.aspen.gds.obsevent.handler

import edu.gemini.aspen.giapi.data.{ObservationEvent, DataLabel}
import edu.gemini.aspen.gds.keywords.database.KeywordsDatabase
import edu.gemini.aspen.gds.actors.factory.CompositeActorsFactory
import edu.gemini.aspen.gds.actors._
import edu.gemini.aspen.gds.api._
import edu.gemini.aspen.gmp.services.PropertyHolder
import org.apache.felix.ipojo.annotations.{Provides, Requires, Instantiate, Component}
import org.apache.felix.ipojo.handlers.event.publisher.Publisher
import org.apache.felix.ipojo.handlers.event.{Publishes, Subscriber}

/**
 * Marker interface used to export GDSObseventHandlerImpl and used by the Health component */
trait GDSObseventHandler

/**
 * Simple Observation Event Handler that creates a KeywordSetComposer and launches the
 * keyword values acquisition process
 */
@Component
@Instantiate
@Provides(specifications = Array[Class[_]](classOf[GDSObseventHandler]))
// todo: reduce amount of dependencies
class GDSObseventHandlerImpl(
                          @Requires actorsFactory: CompositeActorsFactory,
                          @Requires keywordsDatabase: KeywordsDatabase,
                          @Requires errorPolicy: CompositeErrorPolicy,
                          @Requires propertyHolder: PropertyHolder) extends GDSObseventHandler {

  @Publishes(name="gdsrelay", topics = "edu/gemini/aspen/gds/gdsevent", dataKey = "gdsevent")
  var publisher:Publisher = _

  private lazy val replyHandler = new ReplyHandler(actorsFactory, keywordsDatabase, errorPolicy, propertyHolder, publisher)

  @Subscriber(name="obsend", topics="edu/gemini/aspen/gds/obsevent/handler", dataType = "scala.Tuple2", dataKey = "observationevent")
  def onObservationEvent(event: (ObservationEvent, DataLabel)) {
    replyHandler ! AcquisitionRequest(event._1, event._2)
  }

}