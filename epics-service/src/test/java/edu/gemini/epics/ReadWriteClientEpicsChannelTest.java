package edu.gemini.epics;

import edu.gemini.cas.impl.ChannelAccessServerImpl;
import edu.gemini.epics.api.Channel;
import edu.gemini.epics.impl.EpicsWriterImpl;
import gov.aps.jca.CAException;
import gov.aps.jca.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jluhrs on 10/30/14.
 */
public class ReadWriteClientEpicsChannelTest {

    private static String CHANNEL_NAME="dummy:test";
    private ChannelAccessServerImpl giapicas;
    private EpicsService epicsService;
    private EpicsWriter epicsWriter;

    @Before
    public void setUp() throws Exception {
        giapicas = new ChannelAccessServerImpl();
        giapicas.start();

        epicsService = new EpicsService("127.0.0.1");
        epicsService.startService();
        epicsWriter = new EpicsWriterImpl(epicsService);
    }

    @After
    public void tearDown() throws Exception {
        epicsService.stopService();
		epicsService = null;

        Thread.sleep(20);//can't start and stop immediately, and our tests are too short
        giapicas.stop();
    }

    @Test
    public void testWriteInteger() throws CAException, TimeoutException {
        int testValue = 1;
        Channel ch = giapicas.createChannel(CHANNEL_NAME, 0);
        
        ReadWriteClientEpicsChannel<Integer> rwChannel = epicsWriter.getIntegerChannel(CHANNEL_NAME);
        
        rwChannel.setValue(testValue);

        int readValue = rwChannel.getFirst();

        assertTrue("Failed to write Integer channel.", readValue==testValue);

    }

    @Test
    public void testWriteDouble() throws CAException, TimeoutException {
        double testValue = 2.0;
        Channel ch = giapicas.createChannel(CHANNEL_NAME, 0.0);

        ReadWriteClientEpicsChannel<Double> rwChannel = epicsWriter.getDoubleChannel(CHANNEL_NAME);

        rwChannel.setValue(testValue);

        double readValue = rwChannel.getFirst();

        assertTrue("Failed to write Double channel.", readValue==testValue);
    }

    @Test
    public void testWriteFloat() throws CAException, TimeoutException {
        float testValue = 3.0f;
        Channel ch = giapicas.createChannel(CHANNEL_NAME, 0.0f);

        ReadWriteClientEpicsChannel<Float> rwChannel = epicsWriter.getFloatChannel(CHANNEL_NAME);

        rwChannel.setValue(testValue);

        float readValue = rwChannel.getFirst();

        assertTrue("Failed to write Float channel.", readValue==testValue);
    }

    @Test
    public void testWriteString() throws CAException, TimeoutException {
        String testValue = "dummy string";
        Channel ch = giapicas.createChannel(CHANNEL_NAME, "");

        ReadWriteClientEpicsChannel<String> rwChannel = epicsWriter.getStringChannel(CHANNEL_NAME);

        rwChannel.setValue(testValue);

        String readValue = rwChannel.getFirst();

        assertEquals("Failed to write String channel.", readValue, testValue);
    }

    enum DummyEnum {
        VAL1, VAL2, VAL3
    }

    @Test
    public void testWriteEnum() throws CAException, TimeoutException {
        DummyEnum testValue = DummyEnum.VAL2;
        Channel ch = giapicas.createChannel(CHANNEL_NAME, DummyEnum.VAL1);

        ReadWriteClientEpicsChannel<DummyEnum> rwChannel = epicsWriter.getEnumChannel(CHANNEL_NAME, DummyEnum.class);

        rwChannel.setValue(testValue);

        DummyEnum readValue = rwChannel.getFirst();

        assertEquals("Failed to write Enum channel.", readValue, testValue);
    }

}
