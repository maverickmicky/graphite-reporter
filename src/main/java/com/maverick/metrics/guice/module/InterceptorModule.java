package com.maverick.metrics.guice.module;

import com.google.inject.*;
import com.maverick.metrics.*;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use this module when you want to inject the interceptor using Guice.
 */
public class InterceptorModule extends AbstractModule {

	@Override
	protected void configure() {}

	@Provides
	@Singleton
	@Inject
	GraphiteMetricsRegistry getMetricsReporter() {
		return new GraphiteMetricsRegistry();
	}

	@Provides
	@Singleton
	@Inject
	GraphiteTimer getGraphiteTimer(GraphiteMetricsRegistry reporter) {
		return new GraphiteTimer(reporter);
	}
}
