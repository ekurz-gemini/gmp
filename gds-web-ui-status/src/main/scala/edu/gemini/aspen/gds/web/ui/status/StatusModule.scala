package edu.gemini.aspen.gds.web.ui.status

import com.vaadin.Application
import com.vaadin.ui.{Accordion, Component}
import com.vaadin.terminal.ThemeResource
import edu.gemini.aspen.gds.web.ui.api.GDSWebModule
import edu.gemini.aspen.gds.observationstate.ObservationStateProvider
import edu.gemini.aspen.giapi.status.StatusDatabaseService
import edu.gemini.aspen.gds.api.Conversions._
import edu.gemini.aspen.giapi.web.ui.vaadin.components._
import edu.gemini.aspen.giapi.web.ui.vaadin.containers.Panel
import edu.gemini.aspen.giapi.web.ui.vaadin.layouts._
import model.{ObservationsSource, InMemoryObservationsSource, ObservationSourceQueryDefinition, ObservationsBeanQuery}
import StatusModule._
import edu.gemini.aspen.gmp.top.Top
import edu.gemini.aspen.giapi.web.ui.vaadin.data.Property
import org.vaadin.addons.lazyquerycontainer.{LazyQueryContainer, BeanQueryFactory}
import edu.gemini.aspen.giapi.web.ui.vaadin.selects.Table
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import scala.collection.JavaConversions._

class StatusModule(observationSource:ObservationsSource) extends GDSWebModule {
  val title: String = "Status"
  val order: Int = 0
  val topGrid = new GridLayout(columns = 2, rows = 3, margin = true, spacing = true)
  val nLast = 10
  val accordion = new Accordion()

  observationSource.registerListener(() => {
    refresh()
  })

  //properties
  val statusProp = Property(defaultStatus)
  val processingProp = Property(defaultProcessing)
  val lastDataLabelProp = Property(defaultLastDataLabel)

  //labels
  val status = new Label(style = "gds-green", property = statusProp)
  val processing = new Label(property = processingProp)
  val lastDataLabel = new Label(property = lastDataLabelProp)

  val dataContainer = buildDataContainer()
  val statusTable = new Table(dataSource = dataContainer,
    selectable = true,
    style = "logs",
    sizeFull = true,
    sortAscending = true,
    sortPropertyId = "timeStamp",
    cellStyleGenerator = styleGenerator)
  val statusProperty = "status"
  statusTable.addGeneratedColumn(statusProperty, (itemId: AnyRef, columnId: AnyRef) => {
    val result = dataContainer.getItem(itemId).getItemProperty("result").getValue
    result match {
      case Successful => new Embedded(objectType = com.vaadin.ui.Embedded.TYPE_IMAGE, source = new ThemeResource("../runo/icons/16/ok.png"))
      case MissingKeywords => new Embedded(objectType = com.vaadin.ui.Embedded.TYPE_IMAGE, source = new ThemeResource("../gds/warning.png"))
      case ErrorKeywords => new Embedded(objectType = com.vaadin.ui.Embedded.TYPE_IMAGE, source = new ThemeResource("../gds/warning.png"))
      case Error => new Embedded(objectType = com.vaadin.ui.Embedded.TYPE_IMAGE, source = new ThemeResource("../gds/failed.png"))
      case _ => new Embedded(objectType = com.vaadin.ui.Embedded.TYPE_IMAGE, source = new ThemeResource("../runo/icons/16/ok.png"))
    }
  })

  statusTable.setColumnHeader(statusProperty, "")
  statusTable.setColumnAlignment(statusProperty, com.vaadin.ui.Table.ALIGN_CENTER)
  statusTable.setColumnWidth(statusProperty, 20)

  val columns = Array[AnyRef]("status", "timeStamp", "dataLabel")
  statusTable.setVisibleColumns(columns)

  val bottomPanel = new Panel("Last " + nLast + " Observations", sizeFull = true) {
    add(statusTable)
    add(accordion)
  }

  case class Entry(dataLabel: String = "", times: String = "", missing: String = "", errors: String = "")

  override def buildTabContent(app: Application): Component = {
    /*statusProp.setValue(propertySources.getStatus)
    processingProp.setValue(propertySources.getProcessing)
    lastDataLabelProp.setValue(propertySources.getLastDataLabel)*/

    topGrid.setSizeFull()
    topGrid.setColumnExpandRatio(0, 1.0f)
    topGrid.setColumnExpandRatio(1, 3.0f)

    topGrid.add(buildLabel("Current Health:"))
    topGrid.add(status)
    topGrid.add(buildLabel("DataSets in Process:"))
    topGrid.add(processing)
    topGrid.add(buildLabel("Last DataSet:"))
    topGrid.add(lastDataLabel)
    val indicator = new ProgressIndicator(0f)
    topGrid.addComponent(indicator)

    // Set polling frequency to 0.5 seconds.
    indicator.setPollingInterval(500)

    accordion.setSizeFull()
    //generateAccordion(app, getLastEntries(propertySources.getLastDataLabels(nLast)))

    new Thread(new Runnable() {
      def run() {
        while (true) {
          // Do some "heavy work"
            TimeUnit.MILLISECONDS.sleep(500) // Sleep for 50 milliseconds
          //println("onEndObservation")
          //observationSource.onEndObservation()
          if (observationSource.observations.nonEmpty) {
          /*val tableItem = statusTable.getItem(0)
          tableItem.getItemPropertyIds() foreach {
            pid => tableItem.getItemProperty(pid).setValue(tableItem.getItemProperty(pid).getValue())
          }*/
          statusTable.refreshRowCache()
          statusTable.requestRepaint()
            println("UPDAT ETAB")
          statusTable.setContainerDataSource(statusTable.getContainerDataSource)
        }
        }
      }
    })

    new Panel(sizeFull = true) {
      add(topGrid)
      add(bottomPanel)
    }
  }

  private def generateAccordion(app: Application, lastEntries: List[Entry]) {
    for (entry: Entry <- lastEntries) {
      val grid = new GridLayout(columns = 2, rows = 3, margin = true, spacing = true) {
        setSizeFull()
        setColumnExpandRatio(0, 1.0f)
        setColumnExpandRatio(1, 3.0f)
        add(buildLabel("Time to update FITS"))
        add(new Label(entry.times))
        if (entry.missing.length() > 0) {
          add(buildLabel("Missing Keywords"))
          add(new Label(entry.missing))
        }
        if (entry.errors.length() > 0) {
          add(buildLabel("Found errors collecting Keywords:"))
          add(new Label(entry.errors))
        }
      }
      /*val resource = if (propertySources.isInError(entry.dataLabel).isDefined) {
        new ThemeResource("../gds/failed.png")
      } else {
        new ThemeResource("../runo/icons/16/ok.png")
      }*/
      //accordion.addTab(grid, entry.dataLabel, resource)
    }
    accordion.setVisible(accordion.getComponentCount != 0)
  }

  override def refresh(app: Application) {
    /*statusProp.setValue(propertySources.getStatus)
    status.setStyleName(propertySources.getStatusStyle)
    processingProp.setValue(propertySources.getProcessing)

    accordion.removeAllComponents()
    generateAccordion(app, getLastEntries(propertySources.getLastDataLabels(nLast)))*/
    refresh()
  }

  private def refresh() {
    println("Refreshed " + observationSource.pending.mkString(", "))
    observationSource.observations.headOption foreach {
      o => lastDataLabelProp.setValue(o.getDataLabel())
    }
    processingProp.setValue(observationSource.pending.mkString(", "))
  }

  private def buildDataContainer() = {
    val queryFactory = new BeanQueryFactory[ObservationsBeanQuery](classOf[ObservationsBeanQuery])
    val definition = new ObservationSourceQueryDefinition(observationSource, false, 300)

    definition.addProperty("result", classOf[ObservationStatus], Successful, true, true)
    definition.addProperty("timeStamp", classOf[java.lang.Long], 0L, true, true)
    definition.addProperty("dataLabel", classOf[String], "", true, true)
    queryFactory.setQueryDefinition(definition)

    new LazyQueryContainer(definition, queryFactory)
  }

  private def getLastEntries(dataLabels: Traversable[String]): List[Entry] = {
    /*dataLabels map {
      l => if (propertySources.isInError(l).isDefined) {
        new Entry(l, propertySources.getTimes(l), propertySources.getMissingKeywords(l), propertySources.getKeywordsInError(l))
      } else {
        new Entry(l, propertySources.getTimes(l))
      }
    } take (nLast) toList*/

    Nil
  }

  /**
   * Define a custom cell style based on the content of the cell */
  private def styleGenerator(itemId: AnyRef, propertyId: AnyRef): String = {
    STYLES.getOrElse(dataContainer.getItem(itemId).getItemProperty("result").getValue.asInstanceOf[ObservationStatus], "")
  }

  val STYLES = Map[ObservationStatus, String](MissingKeywords -> "warn", Error -> "error", ErrorKeywords -> "warn")

}

protected object StatusModule {
  //default values
  val defaultStatus = "UNKNOWN"
  val defaultProcessing = ""
  val defaultLastDataLabel = ""
  val defaultTimes = ""
  val defaultMissing = ""
  val defaultErrors = ""

  def buildLabel(label: String) = new Label(caption = label, style = "gds-bold")

}