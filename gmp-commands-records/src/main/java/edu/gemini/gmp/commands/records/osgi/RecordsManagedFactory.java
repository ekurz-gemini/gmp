package edu.gemini.gmp.commands.records.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedServiceFactory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

public class RecordsManagedFactory implements ManagedServiceFactory {
    private static final Logger LOG = Logger.getLogger(RecordsManagedFactory.class.getName());

    /*private final Map<String, EpicsServices> existingServices = Maps.newHashMap();
    private final BundleContext context;

    private class ServiceRef<T> {
        private final ServiceRegistration<?> serviceRegistration;
        private final T service;

        private ServiceRef(ServiceRegistration<?> serviceRegistration, T service) {
            this.serviceRegistration = serviceRegistration;
            this.service = service;
        }
    }

    private class EpicsServices {
        private final ServiceRef<EpicsService> epicsService;
        private final ServiceRef<EpicsWriter> epicsWriter;
        private final ServiceRef<EpicsReader> epicsReader;
        private final ServiceRef<EpicsObserverImpl> epicsObserver;

        private EpicsServices(ServiceRef<EpicsService> epicsService, ServiceRef<EpicsWriter> epicsWriter, ServiceRef<EpicsReader> epicsReader, ServiceRef<EpicsObserverImpl> epicsObserver) {
            this.epicsService = epicsService;
            this.epicsWriter = epicsWriter;
            this.epicsReader = epicsReader;
            this.epicsObserver = epicsObserver;
        }
    }*/

    public RecordsManagedFactory(BundleContext context) {
        //this.context = context;
    }

    public String getName() {
        return "EpicsService factory";
    }

    @Override
    public void updated(String pid, Dictionary<String, ?> properties) {
        /*if (existingServices.containsKey(pid)) {
            existingServices.get(pid).epicsService.service.changedAddress(properties);
        } else {
            if (checkProperties(properties)) {
                EpicsService epicsService = createService(properties);
                epicsService.startService();
                ServiceRegistration<?> esRegistration = context.registerService(JCAContextController.class, epicsService, new Hashtable<String, Object>());

                EpicsWriter epicsWriter = new EpicsWriterImpl(epicsService);
                ServiceRegistration<?> ewRegistration = context.registerService(EpicsWriter.class, epicsWriter, new Hashtable<String, Object>());

                EpicsReader epicsReader = new EpicsReaderImpl(epicsService);
                ServiceRegistration<?> erRegistration = context.registerService(EpicsReader.class, epicsReader, new Hashtable<String, Object>());

                EpicsObserverImpl epicsObserver = new EpicsObserverImpl(epicsService);
                epicsObserver.startObserver();
                ServiceRegistration<?> eoRegistration = context.registerService(EpicsObserver.class, epicsObserver, new Hashtable<String, Object>());

                final EpicsClientSubscriber epicsClientSubscriber = new EpicsClientSubscriber(epicsObserver);

                new ServiceTracker<EpicsClient, EpicsClient>(context, EpicsClient.class, new ServiceTrackerCustomizer<EpicsClient, EpicsClient>() {
                    @Override
                    public EpicsClient addingService(ServiceReference<EpicsClient> reference) {
                        EpicsClient epicsClient = context.getService(reference);
                        HashMap<String, Object> properties = Maps.newHashMap();
                        for (String key: reference.getPropertyKeys()) {
                            properties.put(key, reference.getProperty(key));
                        }
                        epicsClientSubscriber.bindEpicsClient(epicsClient, ImmutableMap.copyOf(properties));
                        return epicsClient;
                    }

                    @Override
                    public void modifiedService(ServiceReference<EpicsClient> epicsClientServiceReference, EpicsClient epicsClient) {

                    }

                    @Override
                    public void removedService(ServiceReference<EpicsClient> epicsClientServiceReference, EpicsClient epicsClient) {
                        epicsClientSubscriber.unbindEpicsClient(epicsClient);
                    }
                });

                existingServices.put(pid, new EpicsServices(new ServiceRef<EpicsService>(esRegistration, epicsService), new ServiceRef<EpicsWriter>(ewRegistration, epicsWriter), new ServiceRef<EpicsReader>(erRegistration, epicsReader), new ServiceRef<EpicsObserverImpl>(eoRegistration, epicsObserver)));
            } else {
                LOG.warning("Cannot build " + EpicsService.class.getName() + " without the required properties");
            }
        }*/
    }

    /*private EpicsService createService(Dictionary<String, ?> properties) {
        String addressList = properties.get(EpicsService.PROPERTY_ADDRESS_LIST).toString();
        return new EpicsService(addressList);
    }

    private boolean checkProperties(Dictionary<String, ?> properties) {
        return properties.get(EpicsService.PROPERTY_ADDRESS_LIST) != null;
    }*/

    @Override
    public void deleted(String pid) {
        /*if (existingServices.containsKey(pid)) {
                    ServiceRef<EpicsObserverImpl> eoReference = existingServices.get(pid).epicsObserver;
                    eoReference.serviceRegistration.unregister();
                    eoReference.service.stopObserver();

                    ServiceRef<EpicsService> reference = existingServices.get(pid).epicsService;
                    reference.serviceRegistration.unregister();
                    reference.service.stopService();

                    ServiceRef<EpicsWriter> ewReference = existingServices.get(pid).epicsWriter;
                    ewReference.serviceRegistration.unregister();

                    ServiceRef<EpicsReader> erReference = existingServices.get(pid).epicsReader;
                    erReference.serviceRegistration.unregister();

                    existingServices.remove(pid);
                }*/
    }

    /*public void stopServices() {
        for (String pid: existingServices.keySet()) {
            deleted(pid);
        }
    }*/

}
