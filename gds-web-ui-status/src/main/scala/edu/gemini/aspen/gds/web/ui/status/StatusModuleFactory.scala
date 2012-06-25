package edu.gemini.aspen.gds.web.ui.status

import edu.gemini.aspen.gds.web.ui.api.GDSWebModuleFactory
import edu.gemini.aspen.giapi.status.StatusDatabaseService
import edu.gemini.aspen.gds.observationstate.ObservationStateProvider
import model.ObservationsSource
import org.apache.felix.ipojo.annotations.{Requires, Provides, Instantiate, Component}
import edu.gemini.aspen.gmp.top.Top

/**
 * Factory class for StatusModule classes, one is created per user
 */
@Component
@Instantiate
@Provides(specifications = Array[Class[_]](classOf[GDSWebModuleFactory]))
class StatusModuleFactory(@Requires observationSource:ObservationsSource) extends GDSWebModuleFactory {
  override def buildWebModule = new StatusModule(observationSource)

  override protected def canEqual(other: Any): Boolean = other match {
    case _: StatusModuleFactory => true
    case _ => false
  }
}