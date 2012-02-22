package edu.gemini.aspen.gds.fits

import org.scalatest.matchers.ShouldMatchers
import java.io.File
import org.junit.Assert._
import edu.gemini.aspen.giapi.data.DataLabel
import edu.gemini.fits.{Header, DefaultHeaderItem, DefaultHeader}
import org.scalatest.{BeforeAndAfterEach, FunSuite}

abstract class FitsBaseTest extends FunSuite with ShouldMatchers with BeforeAndAfterEach {
  val originalFile: File
  val destinationFile: File
  val dataLabel: DataLabel

  def createHeadersWithAirMass(headerIndex: Int): List[Header] = {
    val primaryHeader = new DefaultHeader(headerIndex)
    primaryHeader.add(DefaultHeaderItem.create("AIRMASS", 1.0, "Mass of airmass"))

    primaryHeader :: Nil
  }

  def updateFitsFile(headers: List[Header]) {
    val fitsUpdater = new FitsUpdater(originalFile.getParentFile, destinationFile.getParentFile, dataLabel, headers)
    fitsUpdater.updateFitsHeaders({
      label => destinationFile.getName
    })
  }

  def verifyKeywordInHeader(header: Header, keyword: String) {
    assertNotNull(header.get(keyword))
  }

  def verifyKeywordNotInHeader(header: Header, keyword: String) {
    assertNull(header.get(keyword))
  }

  override def afterEach() = if (destinationFile.exists) {
    destinationFile.delete
  }
}