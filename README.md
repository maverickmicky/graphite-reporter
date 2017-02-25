#Graphite Metric Reporter

This library provides support to record metrics in a **Java** application and export them to **Graphite**.

#####Metrics library used
[metrics-graphite](https://github.com/dropwizard/metrics/tree/3.2-development/metrics-graphite)

#####Usage
Steps to use this library:
1. Add maven dependency to this library
2. Inject the _GraphiteTimer_ in your application
3. Annotate the required methods with _@Timed_
4. (Optional) Include REST resource _GraphiteReportingResource_

e.g. maven dependency:

    <dependency>
        <groupId>com.maverick.libraries</groupId>
        <artifactId>guice-graphite-reporter</artifactId>
        <version>0-SNAPSHOT</version>
    </dependency>

If you are using **Guice** in your application then in your module configuration:

    GraphiteTimer timer = Guice.createInjector(new InterceptorModule()).getInstance(GraphiteTimer.class);
	bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timed.class), timer);

annotate method:

    @com.maverick.metrics.annotation.Timed(name = "meterName")
    public Response someMethod(Request request) {
    
bind resource in your ServletModule:

    @Override
    protected void configureServlets() {
        ....
        ....
        bind(GraphiteReportingResource.class);

#####Metrics
You can see the metrics in **Graphite** browser under group _**metrics**_. Look for your env/machine name and then navigate to the metric name you provided in 
the annotation.

If you have **Grafana** hooked up to **Graphite** server then you can view the metrics in more fancy way.

#####Reporting control
You can enable/disable the reporting on the fly using the _GraphiteReportingResource_. It provides three API:

- **POST** /graphite/enable
- **POST** /graphite/disable
- **GET**  /graphite/status
