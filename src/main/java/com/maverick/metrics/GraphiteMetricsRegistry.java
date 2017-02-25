package com.maverick.metrics;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.*;
import org.slf4j.*;

import javax.inject.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * Inject this class to spin up a graphite reporter in the background.
 */
public class GraphiteMetricsRegistry extends MetricRegistry{

	private static final String DEFAULT_GRAPHITE_PREFIX = "metrics.";
	private static final Logger logger = LoggerFactory.getLogger(GraphiteMetricsRegistry.class);
	private boolean enabled;
	private String envHostName;

	@Inject
	public GraphiteMetricsRegistry() {
		this.enabled = true; //fetch property from configuration
		String graphiteHost = "graphite.example.com"; //fetch property from configuration
		Integer graphitePort = 2003; //fetch property from configuration

		try {
			this.envHostName = InetAddress.getLocalHost().getHostName().replace(".","-");
		} catch (UnknownHostException e) {
			logger.debug("Unable to get the hostname for the env");
		}
		Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
		GraphiteReporter reporter = GraphiteReporter.forRegistry(this)
		                                            .prefixedWith(DEFAULT_GRAPHITE_PREFIX + this.envHostName)
		                                            .convertRatesTo(TimeUnit.SECONDS)
		                                            .convertDurationsTo(TimeUnit.MILLISECONDS)
		                                            .filter(MetricFilter.ALL)
		                                            .build(graphite);
		reporter.start(1, TimeUnit.MINUTES);
		logger.debug("Initialized graphite reporter with prefix " + DEFAULT_GRAPHITE_PREFIX + "{}", this.envHostName);
	}

	void timeStart(String name) {
		if (isEnabled()) {
			this.timer(name).time();
			logger.debug("Start timer for " + DEFAULT_GRAPHITE_PREFIX + "{}{}", this.envHostName, name);
		}
	}

	void timeStop(String name) {
		if (isEnabled()) {
			this.timer(name).time().stop();
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

	public boolean isEnabled() {
		return this.enabled;
	}
}
