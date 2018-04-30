package com.yoyo.collector;

import com.yoyo.ActuatorEndPointsInfo;
import com.yoyo.ActuatorScraper;
import com.yoyo.MetricProcessor;
import io.prometheus.client.Collector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ActuatorCollector extends Collector{
    private ActuatorScraper scraper;
    private List<MetricProcessor> metricProcessors = new LinkedList<>();

    public ActuatorCollector(ActuatorScraper scraper){
        this.scraper = scraper;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        final ActuatorEndPointsInfo endPointsInfo = scraper.doScraper();
        List<MetricFamilySamples> metricFamilySamples = new ArrayList<>();
        metricProcessors.stream().forEach(metricProcessor -> {
            metricFamilySamples.addAll(metricProcessor.collect(endPointsInfo));
        });
        return metricFamilySamples;
    }

    public void registerMetricProcessors(MetricProcessor metricProcessor){
        metricProcessors.add(metricProcessor);
    }

}
