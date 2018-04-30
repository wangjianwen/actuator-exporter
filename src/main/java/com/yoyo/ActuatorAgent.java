package com.yoyo;

import com.yoyo.collector.BasicMetricCollector;
import com.yoyo.collector.ManagementMetricCollector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class ActuatorAgent {

    public static void premain(String agentArgument, Instrumentation instrumentation) throws Exception {
//        new BasicMetricCollector().register();
        new ManagementMetricCollector().register();
        InetSocketAddress server = new InetSocketAddress(8123);;
        try {
            new HTTPServer(server, CollectorRegistry.defaultRegistry, true);
        } catch (IOException e) {
            throw new IllegalStateException("启动prometheus agent失败", e);
        }
    }
}
