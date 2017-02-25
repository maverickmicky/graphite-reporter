package com.maverick.metrics.resource;

import com.maverick.metrics.*;
import org.slf4j.*;

import javax.inject.*;
import javax.ws.rs.*;

/**
 * Include this resource if you want to dynamically control the graphite reporting.
 *
 */
@Path("/graphite")
public class GraphiteReportingResource {

	private static final Logger logger = LoggerFactory.getLogger(GraphiteReportingResource.class);
	private final GraphiteMetricsRegistry metricsReporter;

	@Inject
	public GraphiteReportingResource(GraphiteMetricsRegistry metricsReporter){
		this.metricsReporter = metricsReporter;
	}

	@Path("/enable")
	@POST
	public void enableGraphiteReporting(){
		logger.debug("Request to enable graphite reporting");
		metricsReporter.enableReporting();
	}

	@Path("/disable")
	@POST
	public void disableGraphiteReporting(){
		logger.debug("Request to disable graphite reporting");
		metricsReporter.disableReporting();
	}

	@Path("/status")
	@GET
	public String graphiteStatus() {
		logger.debug("Request to check status of graphite reporting");
		if (metricsReporter.isEnabled()) {
			return "ENABLED";
		} else {
			return "DISABLED";
		}
	}
}
