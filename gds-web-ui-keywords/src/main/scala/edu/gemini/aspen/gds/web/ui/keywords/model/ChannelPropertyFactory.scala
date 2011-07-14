package edu.gemini.aspen.gds.web.ui.keywords.model

import com.vaadin.data.Item
import com.vaadin.ui.TextField
import com.vaadin.data.validator.AbstractStringValidator
import edu.gemini.aspen.gds.api.{Channel, GDSConfiguration}

/**
 * PropertyItemWrapperFactory for Channel that uses a TextField to enter the channel name
 */
class ChannelPropertyFactory extends PropertyItemWrapperFactory(classOf[Channel], classOf[TextField]) {
  val validator = new AbstractStringValidator("Value {0} must be less than 8 characters") {
    def isValidString(value: String) = !value.isEmpty
  }

  override def createItemAndWrapper(config: GDSConfiguration, item: Item) = {
    val textField = new TextField("", config.channel.name)
    textField.addValidator(validator)
    textField.setImmediate(true)
    textField.setRequired(true)
    textField.setInvalidAllowed(false)

    def wrapper(config: GDSConfiguration) = {
      config.copy(channel = Channel(textField.getValue.toString))
    }

    (textField, wrapper)
  }
}













