package edu.gemini.aspen.integrationtests;

import com.cosylab.epics.caj.CAJContext;
import edu.gemini.cas.impl.ChannelAccessServerImpl;
import edu.gemini.epics.api.Channel;
import gov.aps.jca.CAException;
import gov.aps.jca.JCALibrary;
import org.junit.After;
import org.junit.Before;

/**
 * Class NewEpicsTestBase
 *
 * @author Nicolas A. Barriga
 *         Date: 11/9/11
 */
public class NewEpicsTestBase {
    protected JCALibrary jca;
    protected CAJContext context;
    protected ChannelAccessServerImpl cas;
    protected final String doubleName = "giapitest:double";
    protected final String intName = "giapitest:int";
    protected final String floatName = "giapitest:float";
    protected final String stringName = "giapitest:string";
    protected Channel<Double> doubleChannel;
    protected Channel<Integer> intChannel;
    protected Channel<Float> floatChannel;
    protected Channel<String> stringChannel;

    @Before
    public void setup() throws CAException {
        jca = JCALibrary.getInstance();
        context = (CAJContext) jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
        cas = new ChannelAccessServerImpl();
        cas.start();
        doubleChannel = cas.createChannel(doubleName, 1.0);
        intChannel = cas.createChannel(intName, 1);
        floatChannel = cas.createChannel(floatName, 1.0f);
        stringChannel = cas.createChannel(stringName, "1");
    }

    @After
    public void tearDown() throws CAException {
        cas.destroyChannel(doubleChannel);
        cas.destroyChannel(intChannel);
        cas.destroyChannel(floatChannel);
        cas.destroyChannel(stringChannel);
        cas.stop();
        context.destroy();
    }
}