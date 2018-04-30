package com.yoyo.processor;

import com.yoyo.ActuatorEndPointsInfo;
import com.yoyo.MetricProcessor;
import com.yoyo.utils.Maps;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GarbageCollectionMetricProcessor implements MetricProcessor{

    @Override
    public List<Collector.MetricFamilySamples> collect(ActuatorEndPointsInfo actuatorEndPointsInfo) {
        List<Collector.MetricFamilySamples> sampleFamilies = new ArrayList<Collector.MetricFamilySamples>();
        Map<String, Number> metrics = actuatorEndPointsInfo.getMetrics();
        List<GarbageCollectorMXBean> garbageCollectorMxBeans = ManagementFactory
                .getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMxBeans) {
            String name = beautifyGcName(garbageCollectorMXBean.getName());
            sampleFamilies.add(new CounterMetricFamily("gc_"+name+"_count", "The gc count of " + name, Maps.safedGetValue(metrics, "gc."+name+".count")));
            sampleFamilies.add(new CounterMetricFamily("gc_"+name+"_sum", "The total gc time of " + name, Maps.safedGetValue(metrics, "gc."+name+".sum")));
        }

        return sampleFamilies;
    }

    private String beautifyGcName(String name) {
        return name.replace(" ", "_").toLowerCase();
    }
}
