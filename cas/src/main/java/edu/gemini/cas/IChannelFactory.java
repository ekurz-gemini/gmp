package edu.gemini.cas;

import gov.aps.jca.CAException;

import java.util.List;

/**
 * Interface IChannelFactory
 *
 * @author Nicolas A. Barriga
 *         Date: Dec 2, 2010
 */
public interface IChannelFactory {
    /**
     * Creates a new channel, with a simulated EPICS process variable(PV) of the type of the initial value
     *
     * @param name name of the PV in EPICS
     * @param value the initial value of the Channel. The type will be extracted from this parameter.
     * @param <T> type must be one of Integer, Float, Double or String
     * @return the new channel
     * @throws CAException
     */
    <T> IChannel<T> createChannel(String name, T value) throws CAException;
    <T> IChannel<T> createChannel(String name, List<T> value) throws CAException;

    /**
     * Creates a new channel, with a simulated EPICS process variable(PV) of the type of the initial value,
     * that is able to raise an alarm.
     *
     * @param name name of the PV in EPICS
     * @param value the initial value of the Channel. The type will be extracted from this parameter.
     * @param <T> type must be one of Integer, Float, Double or String
     * @return the new channel
     * @throws CAException
     */
    <T> IAlarmChannel<T> createAlarmChannel(String name, T value) throws CAException;
    <T> IAlarmChannel<T> createAlarmChannel(String name, List<T> value) throws CAException;

}
