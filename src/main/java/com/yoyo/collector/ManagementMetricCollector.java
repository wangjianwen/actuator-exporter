package com.yoyo.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoyo.utils.OkHttpClients;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.*;

public class ManagementMetricCollector extends Collector {

    private Map<String, Number> metrics;
    private static final String ACTAUTOR = "http://localhost:9001/metrics";
    private ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean first = true;

    public ManagementMetricCollector() {

    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> sampleFamilies = new ArrayList<MetricFamilySamples>();


        if(first){
            metrics = new LinkedHashMap<>();
            metrics.put("systemload.average", -1.0);
            metrics.put("uptime", 249344.0);
            metrics.put("heap.committed", 249344.0);
            metrics.put("heap.init", 129024.0);
            metrics.put("heap.used", 309094.0);
            metrics.put("heap", 1833472.0);
            metrics.put("nonheap.committed", 61096.0);
            metrics.put("nonheap.init", 2496.0);
            metrics.put("nonheap.used", 59750.0);
            metrics.put("nonheap", 0.0);
            first = false;
        } else {
            final String response = OkHttpClients.doGet(ACTAUTOR);
            System.out.println("ManagementMetricCollector:" + response);
            try {
                metrics = objectMapper.readValue(response, Map.class);
                System.out.println("ManagementMetricCollector:readValue" + metrics);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GaugeMetricFamily mem = new GaugeMetricFamily("uptime", "The time of JVM.",
                 metrics.get("uptime").doubleValue());
        sampleFamilies.add(mem);


        GaugeMetricFamily systemloadAverage = new GaugeMetricFamily("system_load_average", "The time of JVM.",
                 metrics.get("systemload.average").doubleValue());
        sampleFamilies.add(systemloadAverage);

        GaugeMetricFamily used = new GaugeMetricFamily("jvm_memory_bytes_used", "Used bytes of a given JVM memory area.", Collections.singletonList("area"));
        used.addMetric(Collections.singletonList("heap"),  metrics.get("heap.used").doubleValue());
        used.addMetric(Collections.singletonList("nonheap"),  metrics.get("nonheap.used").doubleValue());
        sampleFamilies.add(used);

        GaugeMetricFamily committed = new GaugeMetricFamily("jvm_memory_bytes_committed", "Committed (bytes) of a given JVM memory area.", Collections.singletonList("area"));
        committed.addMetric(Collections.singletonList("heap"),  metrics.get("heap.committed").doubleValue());
        committed.addMetric(Collections.singletonList("nonheap"),  metrics.get("nonheap.committed").doubleValue());
        sampleFamilies.add(committed);

        GaugeMetricFamily max = new GaugeMetricFamily("jvm_memory_bytes_max", "Max (bytes) of a given JVM memory area.", Collections.singletonList("area"));
        max.addMetric(Collections.singletonList("heap"),  metrics.get("heap").doubleValue());
        max.addMetric(Collections.singletonList("nonheap"),  metrics.get("nonheap").doubleValue());
        sampleFamilies.add(max);

        GaugeMetricFamily init = new GaugeMetricFamily("jvm_memory_bytes_init", "Initial bytes of a given JVM memory area.", Collections.singletonList("area"));
        init.addMetric(Collections.singletonList("heap"),  metrics.get("heap.init").doubleValue());
        init.addMetric(Collections.singletonList("nonheap"),  metrics.get("nonheap.init").doubleValue());


        return sampleFamilies;
    }
}
