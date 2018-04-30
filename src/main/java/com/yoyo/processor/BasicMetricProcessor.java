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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * jvm基础的指标收集
 */
public class BasicMetricProcessor implements MetricProcessor {

    @Override
    public List<MetricFamilySamples> collect(ActuatorEndPointsInfo actuatorEndPointsInfo) {
        List<MetricFamilySamples> sampleFamilies = new ArrayList<MetricFamilySamples>();
        Map<String, Number> metrics = actuatorEndPointsInfo.getMetrics();

        GaugeMetricFamily mem = new GaugeMetricFamily("jvm_total_mem", "JVM total memory.",
               Maps.safedGetValue(metrics, "mem"));
        sampleFamilies.add(mem);
        GaugeMetricFamily memFree = new GaugeMetricFamily("jvm_free_mem", "JVM free memory.",
                Maps.safedGetValue(metrics, "mem.free"));
        sampleFamilies.add(memFree);
        GaugeMetricFamily processors = new GaugeMetricFamily("jvm_processors",
                "The number of JVM processors.",Maps.safedGetValue(metrics, "processors"));
        sampleFamilies.add(processors);

        GaugeMetricFamily instanceUpTime = new GaugeMetricFamily("intstance_up_time",
                "Time of the instance up.",Maps.safedGetValue(metrics, "instance.uptime"));
        sampleFamilies.add(instanceUpTime);
        return sampleFamilies;
    }
}
