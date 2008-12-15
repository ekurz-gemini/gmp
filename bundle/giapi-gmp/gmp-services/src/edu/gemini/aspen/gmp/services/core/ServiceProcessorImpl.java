package edu.gemini.aspen.gmp.services.core;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * A very simple implementation of the ServiceProcessor
 */
public class ServiceProcessorImpl implements ServiceProcessor {

    private static final Logger LOG = Logger.getLogger(ServiceProcessorImpl.class.getName());

    private Map<ServiceType, Service> _services;

    public ServiceProcessorImpl() {
        _services = new HashMap<ServiceType, Service>();
    }

    public void registerService(Service service) {
        if (service != null) {
            _services.put(service.getType(), service);
        }
    }

    public void process(ServiceType type, ServiceRequest request) throws ServiceException {
        Service service = _services.get(type);
        if (service != null) {
            service.process(request);
        } else {
            LOG.warning("No registered service to answer for properties");
        }
    }
}
