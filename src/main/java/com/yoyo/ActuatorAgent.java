package com.yoyo;

import com.yoyo.collector.ActuatorCollector;
import com.yoyo.processor.*;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;

public class ActuatorAgent {

    public static void premain(String agentArgument, Instrumentation instrumentation) throws Exception {

        String[] args = agentArgument.split(":");
        if (args.length != 2 ) {
            System.err.println("Usage: -javaagent:/path/to/JavaAgent.jar=<agentPort>:<httpPort>");
            System.exit(1);
        }

        final ActuatorScraper scraper = new ActuatorScraper(Integer.valueOf(args[1]));
        final ActuatorCollector collector = new ActuatorCollector(scraper);
        collector.registerMetricProcessors(new BasicMetricProcessor());
        collector.registerMetricProcessors(new ManagementMetricProcessor());
        collector.registerMetricProcessors(new ClassLoadingMetricProcessor());
        collector.registerMetricProcessors(new ThreadMetricProcessor());
        collector.registerMetricProcessors(new GarbageCollectionMetricProcessor());
        collector.register();

        InetSocketAddress server = new InetSocketAddress(Integer.valueOf(args[0]));;
        try {
            new HTTPServer(server, CollectorRegistry.defaultRegistry, true);
        } catch (IOException e) {
            throw new IllegalStateException("启动prometheus agent失败", e);
        }
    }
}
