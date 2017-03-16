#Graphite Metric Reporter

This library provides support to record metrics in a **Java** application and export them to **Graphite**.

#####Metrics library used

[metrics-graphite](https://github.com/dropwizard/metrics/tree/3.2-development/metrics-graphite)

[metrics-spring](https://github.com/ryantenney/metrics-spring) - only for spring based applications

#####Guice usage

Add maven dependency:

    <dependency>
        <groupId>com.maverick.libraries</groupId>
        <artifactId>guice-graphite-reporter</artifactId>
        <version>0-SNAPSHOT</version>
    </dependency>

If you are using **Guice** in your application then in your module configuration:

    GraphiteTimer timer = Guice.createInjector(new InterceptorModule()).getInstance(GraphiteTimer.class);
	bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timed.class), timer);
If possible then use a direct class or package matcher instead of any() matcher.

annotate method:

    @com.maverick.metrics.annotation.Timed(name = "meterName")
    public Response someMethod(Request request) {
    
bind resource in your ServletModule(Optional):

    @Override
    protected void configureServlets() {
        ....
        ....
        bind(GraphiteReportingResource.class);

#####Spring usage

Included maven dependency:
    
    <dependency>
        <groupId>com.maverick.libraries</groupId>
        <artifactId>guice-graphite-reporter</artifactId>
        <version>0-SNAPSHOT</version>
    </dependency>
    
    <dependency>
        <groupId>com.ryantenney.metrics</groupId>
        <artifactId>metrics-spring</artifactId>
        <version>${version}</version>
    </dependency>

If you are using **Spring** in your application then extend your configuration class with SpringMetricsConfig:

    @Configuration
    public class AppConfig extends SpringMetricsConfig {
    ....
    ....
    ....
    }

Annotate method:

    @com.codahale.metrics.annotation.Timed(name = "metric-name", absolute = true)
    public Response getAccount(


#####Metrics
You can see the metrics in **Graphite** browser under group _**metrics**_. Look for your env/machine name and then navigate to the metric name you provided in 
the annotation.

If you have **Grafana** hooked up to **Graphite** server then you can view the metrics in more fancy way.

#####Reporting control
You can enable/disable the reporting on the fly using the _GraphiteReportingResource_. It provides three API:

- **POST** /graphite/enable
- **POST** /graphite/disable
- **GET**  /graphite/status
