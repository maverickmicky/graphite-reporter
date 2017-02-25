package com.maverick.metrics;

import com.maverick.metrics.annotation.*;
import org.aopalliance.intercept.*;
import org.slf4j.*;

import javax.inject.*;

/**
 * Inject this interceptor in the service to record metrics for methods annotated with {@link Timed}
 */
public class GraphiteTimer implements MethodInterceptor{

	private static final Logger logger = LoggerFactory.getLogger(GraphiteTimer.class);

	private final GraphiteMetricsRegistry reporter;

	@Inject
	public GraphiteTimer(GraphiteMetricsRegistry reporter) {
		this.reporter = reporter;
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		String name = methodInvocation.getMethod().getAnnotation(Timed.class).name();
		logger.debug("Intercepting {}", name);
		reporter.timeStart(name);
		Object response = methodInvocation.proceed();
		reporter.timeStop(name);
		return response;
	}

}
