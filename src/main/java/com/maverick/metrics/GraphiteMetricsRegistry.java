package com.maverick.metrics;

import com.codahale.metrics.*;
import com.codahale.metrics.Timer;
import com.codahale.metrics.graphite.*;
import org.slf4j.*;

import javax.inject.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Inject this class to spin up a graphite reporter in the background.
 */
public class GraphiteMetricsRegistry extends MetricRegistry{

	private static final String DEFAULT_GRAPHITE_PREFIX = "metrics.";
	private static final Logger logger = LoggerFactory.getLogger(GraphiteMetricsRegistry.class);
	private boolean enabled;
	private String envHostName;
	private Map<String, Timer.Context> timerContextMap;
	private GraphiteReporter reporter;

	@Inject
	public GraphiteMetricsRegistry() {
		timerContextMap = new HashMap<>();
		enabled = true; //fetch property from configuration
		startGraphiteReporter();
	}

	private void startGraphiteReporter() {
		if (enabled) {
			String graphiteHost = "graphite.example.com"; //fetch property from configuration
			Integer graphitePort = 2003; //fetch property from configuration
			Graphite graphite;
			try {
				this.envHostName = InetAddress.getLocalHost().getHostName().replace(".", "-");
				graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
				graphite.connect();
			} catch (UnknownHostException e) {
				logger.debug("Unable to get the hostname for the env");
			} catch (IOException e) {
				logger.error("Could not connect to graphite server", e);
			}

			graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
			GraphiteReporter reporter = GraphiteReporter.forRegistry(this)
			                                            .prefixedWith(DEFAULT_GRAPHITE_PREFIX + this.envHostName)
			                                            .convertRatesTo(TimeUnit.SECONDS)
			                                            .convertDurationsTo(TimeUnit.MILLISECONDS)
			                                            .filter(MetricFilter.ALL)
			                                            .build(graphite);
			reporter.start(1, TimeUnit.MINUTES);
			logger.debug("Initialized graphite reporter with prefix " + DEFAULT_GRAPHITE_PREFIX + "{}", this.envHostName);
		} else {
			logger.debug("Graphite reporting is disabled");
		}
	}

	void timeStart(String name) {
		if (isEnabled()) {
			timerContextMap.put(name, this.timer(name).time());
			logger.debug("Start timer for " + DEFAULT_GRAPHITE_PREFIX + "{}{}", this.envHostName, name);
		}
	}

	void timeStop(String name) {
		if (isEnabled()) {
			timerContextMap.get(name).stop();
			logger.debug("Stop timer for " + DEFAULT_GRAPHITE_PREFIX + "{}{}", this.envHostName, name);
		}
	}

	public void enableReporting() {
		this.enabled = true;
		logger.debug("Enabled graphite reporting for " + DEFAULT_GRAPHITE_PREFIX + "{}", this.envHostName);
	}

	public void disableReporting() {
		this.enabled = false;
		this.getTimers().values().forEach(timer -> timer.time().stop());
		logger.debug("Disabled graphite reporting for " + DEFAULT_GRAPHITE_PREFIX + "{}", this.envHostName);
	}

	public GraphiteReporter getReporter() {
		return reporter;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
}
