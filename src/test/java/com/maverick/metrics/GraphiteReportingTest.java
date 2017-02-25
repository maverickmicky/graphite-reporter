package com.maverick.metrics;

import com.google.inject.*;
import com.google.inject.matcher.*;
import com.maverick.metrics.annotation.*;
import com.maverick.metrics.resource.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.junit.*;

import javax.inject.Inject;
import java.net.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GraphiteReportingTest {

	private GraphiteMetricsRegistry reporter;
	private GraphiteReportingResource graphiteResource;

	@Inject
	private AwesomeResource awesomeResource;

	@Before
	public void before() throws UnknownHostException {
		reporter = spy(new GraphiteMetricsRegistry());
		graphiteResource = spy(new GraphiteReportingResource(reporter));
		Guice.createInjector(getTestModule()).injectMembers(this);
	}

	@Test
	public void testAnnotatedMethodInterceptionAndReporting() {
		awesomeResource.awesomeMethod("Awesomeness");
		verify(reporter,times(1)).timeStart("Awesomeness");
		verify(reporter,times(1)).timeStop("Awesomeness");
	}

	@Test
	public void testMethodInterceptionAndReporting() {
		awesomeResource.awefulMethod("Awfulness");
		verify(reporter,times(0)).timeStart("Awfulness");
		verify(reporter,times(0)).timeStop("Awfulness");
	}

	@Test
	public void testEnableReporting() {
		graphiteResource.enableGraphiteReporting();
		verify(reporter,times(1)).enableReporting();
		verify(reporter,times(0)).disableReporting();
		Assert.assertTrue(reporter.isEnabled());
		Assert.assertEquals(graphiteResource.graphiteStatus(), "ENABLED");
	}

	@Test
	public void testDisableReporting() {
		graphiteResource.disableGraphiteReporting();
		verify(reporter,times(0)).enableReporting();
		verify(reporter,times(1)).disableReporting();
		Assert.assertFalse(reporter.isEnabled());
		Assert.assertEquals(graphiteResource.graphiteStatus(), "DISABLED");
	}

	private Module getTestModule() {
		return new AbstractModule() {
			@Override protected void configure() {
				bind(GraphiteMetricsRegistry.class).toInstance(reporter);
				GraphiteTimer timer = new GraphiteTimer(reporter);
				bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timed.class), timer);
			}
		};
	}
}
