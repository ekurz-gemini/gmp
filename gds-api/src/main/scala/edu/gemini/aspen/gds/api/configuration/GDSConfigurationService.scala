package edu.gemini.aspen.gds.api.configuration

import edu.gemini.aspen.gds.api.GDSConfiguration
import org.apache.felix.ipojo.annotations.{Property, Provides, Component}
import java.io.{BufferedWriter, File, FileWriter}
import edu.gemini.aspen.gds.api.Predef._
import com.google.common.io.Files
import com.google.common.base.Charsets

/**
 * Reads and writes configuration files. Update operations(saveConfiguration and updateConfiguration) try to match
 * configuration elements by checking which element has the maximum number of columns identical to the new configuration
 * element. Ties are broken by prioritizing: Subsystem channel, Keyword Name and Comment. If there's still a tie, the
 * operation fails.
 */
trait GDSConfigurationService {
  /**
   * Reads from file and returns the configuration items
   */
  def getConfiguration: List[GDSConfiguration]

  /**
   *  Reads from file and returns a representation of the file including comments and empty lines.
   */
  def getFullConfiguration: List[ConfigItem[_]]

  /**
   * Indicates whether an error has been detected on the configuration */
  def hasError: Boolean

  /**
   * Returns the actual text of the configuration file */
  def textContent: String

  /**
   * Overwrites the current configuration file
   */
  def replaceConfiguration(config: List[GDSConfiguration]): Unit

  /**
   * Updates elements that already exist, adds the new ones, and deletes missing ones
   */
  def updateConfiguration(config: List[ConfigItem[_]]): Unit

  /**
   * Adds new configurations to the end of the file
   */
  def addConfiguration(config: List[GDSConfiguration]): Unit
}

@Component
@Provides(specifications = Array[Class[_]](classOf[GDSConfigurationService]))
class GDSConfigurationServiceImpl(@Property(name = "keywordsConfiguration", value = "NOVALID", mandatory = true) configurationFile: String) extends GDSConfigurationService {
  def getConfiguration: List[GDSConfiguration] = {
    GDSConfigurationFile.getConfiguration(configurationFile)
  }

  def replaceConfiguration(config: List[GDSConfiguration]) {
    val newFile = new File(configurationFile)
    use(new BufferedWriter(new FileWriter(newFile))) {
      writer: BufferedWriter => for (configLine <- config) {
        writer.write(configLine.formatForConfigFile)
        writer.newLine()
      }
    }
  }

  def hasError = GDSConfigurationFile.hasError(configurationFile)

  def textContent = {
    val originalFile = new File(configurationFile)
    Files.toString(originalFile, Charsets.UTF_8)
  }

  def getFullConfiguration: List[ConfigItem[_]] = {
    GDSConfigurationFile.getFullConfiguration(configurationFile)
  }

  def updateConfiguration(config: List[ConfigItem[_]]) {
    GDSConfigurationFile.saveConfiguration(configurationFile, config)
  }

  def addConfiguration(config: List[GDSConfiguration]) {
    val newFile = new File(configurationFile)
    use(new BufferedWriter(new FileWriter(newFile, true))) {
      writer: BufferedWriter => for (configLine <- config) {
        writer.append(configLine.formatForConfigFile)
        writer.newLine()
      }
    }
  }
}