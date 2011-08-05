package edu.gemini.aspen.gds.keywords.database.impl

import org.scalatest.junit.AssertionsForJUnit
import _root_.edu.gemini.aspen.gds.api.CollectedValue
import org.junit.{Before, Test}
import org.junit.Assert._
import _root_.edu.gemini.aspen.gds.api.Conversions._
import _root_.edu.gemini.aspen.giapi.data.DataLabel
import _root_.edu.gemini.aspen.gds.keywords.database._
import scala._

class KeywordsDatabaseTest extends AssertionsForJUnit {
  var db: KeywordsDatabaseImpl = null
  val key = "keyword"
  val key2 = "keyword2"
  val value = 0.1
  val colVal = CollectedValue(key, value, "my comment", 0)
  val colVal2 = CollectedValue(key2, value, "my comment", 1)
  val dataLabel = "GS-2011B"
  val dataLabel2 = "GS-2011A"

  @Before
  def setup() {
    db = new KeywordsDatabaseImpl
  }

  def check(label: DataLabel, item: CollectedValue[_]) {
    val ret = db !? (1000, Retrieve(label))
    assertFalse(ret.isEmpty) //we didn't get a timeout
    ret foreach {
      _ match {
        case cv: List[_] => assertTrue(cv.contains(item))
        case a: Any => fail("Not valid value" + a)
      }
    }
  }

  @Test
  def testRetrieveAll() {
    db ! Store(dataLabel, colVal)
    check(dataLabel, colVal)
  }


  def checkEmpty(ret: Option[Any]) {
    assertFalse(ret.isEmpty)
    ret map {
      case c: List[_] => assertTrue(c.isEmpty)
      case a: Any => error("Not a valid value " + a)
    }
  }

  @Test
  def retrieveEmpty() {
    val ret = db !? (1000, Retrieve(dataLabel))
    checkEmpty(ret)
  }

  @Test
  def retrieveWrongDataLabel() {
    db ! Store(dataLabel, colVal)
    val ret = db !? (1000, Retrieve("wrong"))
    checkEmpty(ret)
  }

  @Test
  def multipleDatasets() {
    db ! Store(dataLabel, colVal)
    db ! Store(dataLabel2, colVal2)

    check(dataLabel, colVal)

    check(dataLabel2, colVal2)
  }

  @Test
  def multipleDatasetsInList() {
    db ! StoreList(dataLabel, List(colVal, colVal2))

    check(dataLabel, colVal)

    check(dataLabel, colVal2)
  }

  @Test
  def testClean() {
    db !? (1000, Retrieve(dataLabel))
    db ! Clean(dataLabel)
    retrieveEmpty()
  }
}