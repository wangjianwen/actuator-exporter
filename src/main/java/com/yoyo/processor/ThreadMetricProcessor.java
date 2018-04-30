package com.yoyo.processor;

import com.yoyo.ActuatorEndPointsInfo;
import com.yoyo.MetricProcessor;
import com.yoyo.utils.Maps;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThreadMetricProcessor implements MetricProcessor{

    @Override
    public List<Collector.MetricFamilySamples> collect(ActuatorEndPointsInfo actuatorEndPointsInfo) {
        List<Collector.MetricFamilySamples> sampleFamilies = new ArrayList<Collector.MetricFamilySamples>();
        Map<String, Number> metrics = actuatorEndPointsInfo.getMetrics();

        sampleFamilies.add(new GaugeMetricFamily("jvm_threads_current", "Current thread count of a JVM", Maps.safedGetValue(metrics, "threads")));
        sampleFamilies.add(new GaugeMetricFamily("jvm_threads_daemon", "Daemon thread count of a JVM", Maps.safedGetValue(metrics, "threads.daemon")));
        sampleFamilies.add(new GaugeMetricFamily("jvm_threads_peak", "Peak thread count of a JVM", Maps.safedGetValue(metrics, "threads.peak")));
        sampleFamilies.add(new CounterMetricFamily("jvm_threads_started_total", "Started thread count of a JVM", Maps.safedGetValue(metrics, "threads.totalStarted")));
        return sampleFamilies;
    }
}
