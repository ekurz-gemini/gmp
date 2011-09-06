package edu.gemini.aspen.gds.web.ui.keywords.model

import com.vaadin.data.Item
import com.vaadin.ui.TextField
import com.vaadin.data.validator.AbstractStringValidator
import edu.gemini.aspen.gds.api.{Channel, GDSConfiguration}

/**
 * PropertyItemWrapperFactory for Channel that uses a TextField to enter the channel name
 */
class ChannelPropertyFactory extends PropertyItemWrapperFactory(classOf[Channel], classOf[TextField]) {
  override val width = 150

  override def buildPropertyControlAndWrapper(config: GDSConfiguration) = {
    val textField = new TextField("", config.channel.name)
    textField.addValidator(ChannelPropertyFactory.validator(textField))
    textField.setCaption("Channel")
    textField.setRequired(true)
    textField.setImmediate(true)
    textField.setRequired(true)
    textField.setValidationVisible(true)
    textField.setMaxLength(80)
    textField.setInvalidAllowed(false)

    def updateFunction(config: GDSConfiguration) = {
      config.copy(channel = Channel(textField.getValue.toString))
    }

    (textField, updateFunction)
  }
}

object ChannelPropertyFactory {
  def validator(textField: TextField) = new AbstractStringValidator("Value {0} cannot be empty, enter NONE if not used") {
    def isValidString(value: String) = value.nonEmpty

    override def validate(value: AnyRef) {
      if (!isValid(value)) {
        textField.addStyleName("validation-error")
      } else {
        textField.removeStyleName("validation-error")
      }
      super.validate(value)
    }
  }

}











