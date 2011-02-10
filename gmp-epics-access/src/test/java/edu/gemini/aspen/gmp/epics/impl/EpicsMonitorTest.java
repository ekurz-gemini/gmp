package edu.gemini.aspen.gmp.epics.impl;

import edu.gemini.aspen.gmp.epics.EpicsRegistrar;
import edu.gemini.aspen.gmp.epics.EpicsUpdateImpl;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EpicsMonitorTest {
    @Test
    public void testConnected() {
        EpicsMonitor epicsMonitor = new EpicsMonitor(null);
        assertFalse(epicsMonitor.isConnected());
        epicsMonitor.connected();
        assertTrue(epicsMonitor.isConnected());
    }

    @Test
    public void testDisconnected() {
        EpicsMonitor epicsMonitor = new EpicsMonitor(null);
        assertFalse(epicsMonitor.isConnected());
        epicsMonitor.connected();
        epicsMonitor.disconnected();
        assertFalse(epicsMonitor.isConnected());
    }

    @Test
    public void testChannelChanged() {
        EpicsRegistrar registrar = mock(EpicsRegistrar.class);
        EpicsMonitor epicsMonitor = new EpicsMonitor(registrar);

        epicsMonitor.channelChanged("X.val1", Integer.valueOf(1));
        verify(registrar).processEpicsUpdate(new EpicsUpdateImpl("X.val1", Integer.valueOf(1)));
    }
}
