package com.yoyo.processor;

import com.yoyo.ActuatorEndPointsInfo;
import com.yoyo.MetricProcessor;
import com.yoyo.utils.Maps;
import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassLoadingMetricProcessor implements MetricProcessor{

    @Override
    public List<MetricFamilySamples> collect(ActuatorEndPointsInfo actuatorEndPointsInfo) {
        List<MetricFamilySamples> sampleFamilies = new ArrayList<MetricFamilySamples>();
        Map<String, Number> metrics = actuatorEndPointsInfo.getMetrics();

        sampleFamilies.add(new GaugeMetricFamily("jvm_classes_loaded", "The number of classes that are currently loaded in the JVM", Maps.safedGetValue(metrics,"classes")));
        sampleFamilies.add(new CounterMetricFamily("jvm_classes_loaded_total", "The total number of classes that have been loaded since the JVM has started execution", Maps.safedGetValue(metrics, "classes.loaded")));
        sampleFamilies.add(new CounterMetricFamily("jvm_classes_unloaded_total", "The total number of classes that have been unloaded since the JVM has started execution", Maps.safedGetValue(metrics, "classes.unloaded")));
        return sampleFamilies;
    }
}
