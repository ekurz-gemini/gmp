package edu.gemini.aspen.gds.web.ui.keywords

import edu.gemini.aspen.gds.web.ui.api.GDSWebModule
import edu.gemini.aspen.gds.api.configuration.GDSConfigurationService
import model.GDSKeywordsDataSource
import edu.gemini.aspen.gds.web.ui.api.VaadinUtilities._
import com.vaadin.ui.Window.Notification
import com.vaadin.ui._

/**
 * Module for the table to edit the keywords
 */
class KeywordsTableModule(configService: GDSConfigurationService) extends GDSWebModule {
  val title = "Keyword Configuration"
  val order = 0
  val dataSource = new GDSKeywordsDataSource(configService.getConfiguration)

  def buildValidateButton(mainWindow: Window): Button = {
    val button: Button = new Button("Validate")

    button.addListener((e: Button#ClickEvent) => {
      mainWindow.showNotification("Validating...", Notification.TYPE_HUMANIZED_MESSAGE)
      println(dataSource.toGDSConfiguration.head)
    })
    button
  }

  override def buildTabContent(mainWindow: Window) = {
    val layout = new VerticalLayout

    val table = new Table("Keywords", dataSource)
    //table.setSelectable(true)
    table.setNullSelectionAllowed(false)
    table.setImmediate(true)
    table.addStyleName("components-inside")
    table.setSizeFull
    //table.setPageLength(10)
    table.setCacheRate(0.2)
    table.setEditable(true)
    table.setColumnCollapsingAllowed(true)

    layout.addComponent(table)
    layout.setExpandRatio(table, 1.0f)

    val button = buildValidateButton(mainWindow)

    layout.addComponent(button)
    layout.setComponentAlignment(button, Alignment.MIDDLE_RIGHT)
    layout.setSizeFull
    layout
  }
}