package com.yoyo.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoyo.ActuatorEndPointsInfo;
import com.yoyo.MetricProcessor;
import com.yoyo.utils.Maps;
import com.yoyo.utils.OkHttpClients;
import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.GaugeMetricFamily;

import java.io.IOException;
import java.util.*;

public class ManagementMetricProcessor implements MetricProcessor {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<MetricFamilySamples> collect(ActuatorEndPointsInfo actuatorEndPointsInfo) {
        List<MetricFamilySamples> sampleFamilies = new ArrayList<MetricFamilySamples>();
        Map<String, Number> metrics = actuatorEndPointsInfo.getMetrics();
        GaugeMetricFamily mem = new GaugeMetricFamily("uptime", "The time of JVM.",
                Maps.safedGetValue(metrics, "uptime"));
        sampleFamilies.add(mem);

        GaugeMetricFamily systemloadAverage = new GaugeMetricFamily("system_load_average", "The time of JVM.",
                Maps.safedGetValue(metrics, "systemload.average"));
        sampleFamilies.add(systemloadAverage);

        GaugeMetricFamily used = new GaugeMetricFamily("jvm_memory_bytes_used", "Used bytes of a given JVM memory area.", Collections.singletonList("area"));
        used.addMetric(Collections.singletonList("heap"), Maps.safedGetValue(metrics, "heap.used"));
        used.addMetric(Collections.singletonList("nonheap"),  Maps.safedGetValue(metrics, "nonheap.used"));
        sampleFamilies.add(used);

        GaugeMetricFamily committed = new GaugeMetricFamily("jvm_memory_bytes_committed", "Committed (bytes) of a given JVM memory area.", Collections.singletonList("area"));
        committed.addMetric(Collections.singletonList("heap"),  Maps.safedGetValue(metrics, "heap.committed"));
        committed.addMetric(Collections.singletonList("nonheap"),  Maps.safedGetValue(metrics, "nonheap.committed"));
        sampleFamilies.add(committed);

        GaugeMetricFamily max = new GaugeMetricFamily("jvm_memory_bytes_max", "Max (bytes) of a given JVM memory area.", Collections.singletonList("area"));
        max.addMetric(Collections.singletonList("heap"),  Maps.safedGetValue(metrics, "heap"));
        max.addMetric(Collections.singletonList("nonheap"),  Maps.safedGetValue(metrics, "nonheap"));
        sampleFamilies.add(max);

        GaugeMetricFamily init = new GaugeMetricFamily("jvm_memory_bytes_init", "Initial bytes of a given JVM memory area.", Collections.singletonList("area"));
        init.addMetric(Collections.singletonList("heap"),  Maps.safedGetValue(metrics, "heap.init"));
        init.addMetric(Collections.singletonList("nonheap"), Maps.safedGetValue(metrics, "nonheap.init"));

        return sampleFamilies;
    }
}
