package edu.gemini.aspen.gds.constant

import edu.gemini.aspen.gds.api.Conversions._
import org.junit.Test
import org.junit.Assert._
import edu.gemini.aspen.giapi.data.ObservationEvent
import edu.gemini.aspen.gds.api.{ErrorCollectedValue, CollectionError, CollectedValue, GDSConfiguration}

class ConstantActorTest {

    @Test
    def testActor() {
        val constActor = new ConstantActor(buildConfiguration("key1", "val1") :: buildConfiguration("key2", "val2") :: Nil)
        assertEquals(CollectedValue("key1", "val1", "", 0) :: CollectedValue("key2", "val2", "", 0) :: Nil, constActor.collectValues())
    }

    @Test
    def testActorWrongType() {
        val constActor = new ConstantActor(GDSConfiguration("GPI",
            "OBS_START_ACQ",
            "key1",
            0,
            "INT",
            false,
            "val1",
            "CONSTANT",
            "",
            0,
            "") :: Nil)
        assertEquals(ErrorCollectedValue("key1", CollectionError.TypeMismatch, "", 0) :: Nil, constActor.collectValues())
    }

    @Test
    def testActorFactory() {
        val factory = new ConstantActorsFactory
        factory.configure(List(GDSConfiguration("GPI", "OBS_PREP", "TEST", 0, "DOUBLE", false, "1.0", "CONSTANT", "ws:massAirmass", 0, "my comment")))

        assertEquals(1, factory.buildActors(ObservationEvent.OBS_START_ACQ, "label").length)

        val actors = factory.buildActors(ObservationEvent.OBS_PREP, "label")
        assertEquals(1, actors.length)

        val values = actors.head.collectValues()
        values.head match {
            case CollectedValue(fits, value, comment, 0) => {
                assertEquals(stringToFitsKeyword("TEST"), fits)
                assertEquals(1.0, value)
                assertEquals("my comment", comment)
            }
            case _ => fail("Wrong answer")
        }
    }

    private def buildConfiguration(keyword: String, value: String): GDSConfiguration = {
        GDSConfiguration("GPI",
            "OBS_START_ACQ",
            keyword,
            0,
            "STRING",
            false,
            value,
            "CONSTANT",
            "",
            0,
            "")
    }
}