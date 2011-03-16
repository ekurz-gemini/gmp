package edu.gemini.aspen.gmp.tcs.model;

import edu.gemini.aspen.gmp.tcs.jms.JmsTcsContextDispatcher;
import edu.gemini.aspen.gmp.tcs.jms.TcsContextRequestListener;
import edu.gemini.epics.EpicsReader;
import edu.gemini.jms.api.BaseMessageConsumer;
import edu.gemini.jms.api.DestinationData;
import edu.gemini.jms.api.DestinationType;
import edu.gemini.jms.api.JmsProvider;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Modified;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;

import javax.jms.JMSException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface to define a composite of several TCS Context objects
 */
@Component
//@Instantiate(name = "tcsContext")
public class TcsContextComponent {
    private static final Logger LOG = Logger.getLogger(TcsContextComponent.class.getName());

    @Property(name = "simulation", value = "true", mandatory = true)
    private Boolean simulation;

    @Property(name = "tcsChannel", value = "NOVALID", mandatory = true)
    private String tcsChannel;

    @Property(name = "simulationData", value = "NOVALID", mandatory = true)
    private String simulationData;

    /**
     * The JMS Context Dispatcher is a JMS Producer message
     * that will send the TCS Context to the requester
     */
    private JmsTcsContextDispatcher _dispatcher;

    /**
     * JMS Listener to process the TCS Context requests.
     */
    private TcsContextRequestListener _listener;

    /**
     * Message consumer used to receive TCS Context requests
     */
    private BaseMessageConsumer _messageConsumer;

    @Requires
    private JmsProvider _provider;

    @Requires(id = "epicsReader")
    private EpicsReader _epicsReader;

    private TcsContextFetcher fetcher;

    private TcsContextComponent() {
        _dispatcher = new JmsTcsContextDispatcher("TCS Context Replier");

        _listener = new TcsContextRequestListener(_dispatcher);
        //Creates the TCS Context Request Consumer
        _messageConsumer = new BaseMessageConsumer(
                "JMS TCS Context Request Consumer",
                new DestinationData(TcsContextRequestListener.DESTINATION_NAME,
                        DestinationType.TOPIC),
                _listener
        );
    }

    protected TcsContextComponent(JmsProvider provider, EpicsReader reader, String tcsChannel) {
        this();
        this._provider = provider;
        this._epicsReader = reader;
        this.tcsChannel = tcsChannel;
        this.simulation = false;
        this.simulationData = null;
    }

    @Validate
    public void validated() throws JMSException {
        LOG.info("TcsContext validated, starting... ");
        startJMSConsumers();
        if (simulation) {
            addSimulatedTcsContextFetcher();
        }
        LOG.info("TCS Context Service started");
    }

    private void startJMSConsumers() {
        try {
            _dispatcher.startJms(_provider);
            _messageConsumer.startJms(_provider);
        } catch (JMSException e) {
            LOG.log(Level.SEVERE, "Exception starting jms consumers", e);
        }
    }

    private void addSimulatedTcsContextFetcher() {
        try {
            LOG.info("Simulating data for TCS Context from " + simulationData);
            _listener.registerTcsContextFetcher(new SimTcsContextFetcher(new FileInputStream(simulationData)));
        } catch (FileNotFoundException e) {
            LOG.log(Level.SEVERE, "Simulation file not found " + simulationData, e);
        }
    }

    @Bind(id = "epicsReader")
    public void registerEpicsReader() {
        LOG.info("Got instance of epics reader " + _epicsReader);
        if (!simulation) {
            addNewTcsContextFetcher();
        }
    }

    private void addNewTcsContextFetcher() {
        try {
            LOG.info("New instance of EPICS reader registered, get tcsContext from " + tcsChannel);
            fetcher = new EpicsTcsContextFetcher(_epicsReader, tcsChannel);
            _listener.registerTcsContextFetcher(fetcher);
        } catch (TcsContextException e) {
            LOG.log(Level.WARNING, "Can't initialize EPICS channels", e);
        }
    }

    @Unbind(id = "epicsReader")
    public void unRegisterEpicsReader() {
        if (!simulation) {
            removeOldTcsContextFetcher();
        }
    }

    private void removeOldTcsContextFetcher() {
        if (fetcher != null) {
            LOG.info("Removed old instance of EPICS writer");
            _listener.registerTcsContextFetcher(null);
        }
    }

    @Modified(id = "epicsReader")
    public void modifiedEpicsReader() {
        if (!simulation) {
            removeOldTcsContextFetcher();
            addNewTcsContextFetcher();
        }
    }
}
