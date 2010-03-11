package edu.gemini.aspen.gmp.statusservice;

import edu.gemini.aspen.gmp.status.StatusHandler;

/**
 * Interface for services that will keep track of status handlers
 */
public interface StatusHandlerRegister {

    /**
     * Register a new Status Handler in the system
     * @param handler the StatusHandler to register
     */
    void addStatusHandler(StatusHandler handler);

    /**
     * Removes the status handler from the system
     * @param handler the StatusHandler to be removed
     */
    void removeStatusHandler(StatusHandler handler);
}
