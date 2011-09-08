package edu.gemini.aspen.gds.web.ui.modules

import org.apache.felix.ipojo.annotations.{Provides, Instantiate}
import edu.gemini.aspen.gds.web.ui.api.StatusPanelModule
import com.vaadin.data.util.ObjectProperty

@org.apache.felix.ipojo.annotations.Component
@Instantiate
@Provides(specifications = Array(classOf[StatusPanelModule]))
class CurrentStatusPanel extends AbstractStatusPanelModule {
  val order = 0
  val label = "Status:"
  val item = "Running"
  val property = new ObjectProperty[String]("Running")
}