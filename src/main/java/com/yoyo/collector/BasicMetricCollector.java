package com.yoyo.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoyo.utils.OkHttpClients;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

import java.io.IOException;
import java.util.*;


/**
 * jvm基础的指标收集
 */
public class BasicMetricCollector extends Collector {

    private Map<String, Number> metrics;
    private static final String ACTAUTOR = "http://localhost:9001/metrics";
    private ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean first = true;

    public BasicMetricCollector(){
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> sampleFamilies = new ArrayList<MetricFamilySamples>();
        metrics = new LinkedHashMap<>();
        if(first){
            metrics.put("mem", 309094.0);
            metrics.put("mem.free", 209565.0);
            metrics.put("processors", 8.0);
            metrics.put("instance.uptime", 11256785.0);
            first = false;
        } else {
            final String response = OkHttpClients.doGet(ACTAUTOR);
            try {
                metrics = objectMapper.readValue(response, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        GaugeMetricFamily mem = new GaugeMetricFamily("jvm_total_mem", "JVM total memory.",
                metrics.get("mem").doubleValue());
        sampleFamilies.add(mem);
        GaugeMetricFamily memFree = new GaugeMetricFamily("jvm_free_mem", "JVM free memory.",
                 metrics.get("mem.free").doubleValue());
        sampleFamilies.add(memFree);
        GaugeMetricFamily processors = new GaugeMetricFamily("jvm_processors",
                "The number of JVM processors.", metrics.get("processors").doubleValue());
        sampleFamilies.add(processors);

        GaugeMetricFamily instanceUpTime = new GaugeMetricFamily("intstance_up_time",
                "Time of the instance up.", metrics.get("instance.uptime").doubleValue());
        sampleFamilies.add(instanceUpTime);
        return sampleFamilies;
    }
}
