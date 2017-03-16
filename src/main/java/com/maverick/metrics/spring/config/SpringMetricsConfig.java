package com.maverick.metrics.spring.config;

import com.codahale.metrics.*;
import com.maverick.metrics.*;
import com.ryantenney.metrics.spring.config.annotation.*;

/**
 * This will enable metrics in Spring based application
 */
@EnableMetrics
public abstract class SpringMetricsConfig extends MetricsConfigurerAdapter {

	@Override
	public MetricRegistry getMetricRegistry() {
		return new GraphiteMetricsRegistry();
	}

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {

		registerReporter(((GraphiteMetricsRegistry) metricRegistry).getReporter());

	}

}
