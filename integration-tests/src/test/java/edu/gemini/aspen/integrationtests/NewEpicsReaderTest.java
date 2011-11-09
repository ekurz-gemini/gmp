package edu.gemini.aspen.integrationtests;

import edu.gemini.epics.EpicsService;
import edu.gemini.epics.NewEpicsReader;
import edu.gemini.epics.ReadOnlyClientEpicsChannel;
import edu.gemini.epics.impl.NewEpicsReaderImpl;
import gov.aps.jca.CAException;
import gov.aps.jca.TimeoutException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewEpicsReaderTest extends NewEpicsTestBase {
    private NewEpicsReader epicsReader;

    @Before
    public void setup() throws CAException {
        super.setup();
        epicsReader = new NewEpicsReaderImpl(new EpicsService(context));
    }

    @Test
    public void testGetWrongUnderlyingType() throws CAException, InterruptedException {
        try {
            ReadOnlyClientEpicsChannel<Integer> channel = epicsReader.getIntegerChannel(doubleName);
        } catch (IllegalArgumentException ex) {
            assertEquals("Channel " + doubleName + " can be connected to, but is of incorrect type.", ex.getMessage());
        }
    }


    @Test
    public void testGetWrongType() throws CAException, InterruptedException {

        ReadOnlyClientEpicsChannel<Double> channel = epicsReader.getDoubleChannel(doubleName);
        assertTrue(channel.isValid());
        try {
            ReadOnlyClientEpicsChannel<Integer> channel2 = epicsReader.getIntegerChannel(doubleName);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Channel " + doubleName + " can be connected to, but is of incorrect type.", ex.getMessage());
        }
        epicsReader.destroyChannel(channel);
    }

    @Test
    public void testGetWrongTypeAsync() throws CAException, InterruptedException {

        ReadOnlyClientEpicsChannel<?> channel = epicsReader.getChannelAsync(doubleName);
        Thread.sleep(500);
        assertTrue(channel.isValid());
        try {
            ReadOnlyClientEpicsChannel<Integer> channel2 = epicsReader.getIntegerChannel(doubleName);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Channel " + doubleName + " can be connected to, but is of incorrect type.", ex.getMessage());
        }
        epicsReader.destroyChannel(channel);
    }

    @Test
    public void testGetValues() throws CAException, InterruptedException, TimeoutException {
        ReadOnlyClientEpicsChannel<Double> dChannel = epicsReader.getDoubleChannel(doubleName);
        ReadOnlyClientEpicsChannel<Integer> iChannel = epicsReader.getIntegerChannel(intName);
        ReadOnlyClientEpicsChannel<Float> fChannel = epicsReader.getFloatChannel(floatName);
        ReadOnlyClientEpicsChannel<String> sChannel = epicsReader.getStringChannel(stringName);
        assertTrue(dChannel.isValid());
        assertEquals((Double) 1.0, dChannel.getFirst());
        dChannel.destroy();
        assertTrue(iChannel.isValid());
        assertEquals((Integer) 1, iChannel.getFirst());
        iChannel.destroy();
        assertTrue(fChannel.isValid());
        assertEquals((Float) 1.0f, fChannel.getFirst());
        fChannel.destroy();
        assertTrue(sChannel.isValid());
        assertEquals("1", sChannel.getFirst());
        sChannel.destroy();
    }

    @Test
    public void testGetValueAsync() throws CAException, InterruptedException, TimeoutException {
        ReadOnlyClientEpicsChannel<?> dChannel = epicsReader.getChannelAsync(doubleName);
        Thread.sleep(500);
        assertTrue(dChannel.isValid());
        assertEquals((Double) 1.0, dChannel.getFirst());
        dChannel.destroy();
    }
}
