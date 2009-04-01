package edu.gemini.epics.test;

import java.util.Map;
import java.util.TreeMap;

import edu.gemini.epics.IEpicsClient;
import edu.gemini.epics.impl.ChannelBindingSupport;
import gov.aps.jca.CAException;

/**
 * Trivial test that does not use the OSGi activator.
 */
public class WeatherTest implements IEpicsClient {

	/** Map the channel names to friendly text names. */
	private static final Map<String, String> CHANNELS = new TreeMap<String, String>();
	static {
		CHANNELS.put("ws:wsFilter.VALP", "Humidity");
		CHANNELS.put("ws:wsFilter.VALO", "Pressure");
		CHANNELS.put("ws:wsFilter.VALL", "Temperature");
		CHANNELS.put("ws:wsFilter.VALN", "Wind Direction");
		CHANNELS.put("ws:wsFilter.VALM", "Wind Speed");
	}

	private final ChannelBindingSupport cbs;

	public WeatherTest() throws CAException {
		 cbs = new ChannelBindingSupport(this);
        for (String s : CHANNELS.keySet()) {
            cbs.bindChannel(s);
        }
	}
	
	public void channelChanged(String channel, Object value) {
		System.out.println(CHANNELS.get(channel) + ": " + value);
	}

	public void connected() {
		// This will not be called
	}

	public void disconnected() {
		// This will not be called
	}

	@Override
	protected void finalize() throws Throwable {
		cbs.close();
		super.finalize();
	}
	
	public static void main(String[] args) throws InterruptedException, CAException {
	
		System.setProperty("com.cosylab.epics.caj.CAJContext.addr_list", "172.17.2.255");
		System.setProperty("com.cosylab.epics.caj.CAJContext.auto_addr_list", "false");		

		new WeatherTest();
		Thread.sleep(Long.MAX_VALUE);

	}

}
