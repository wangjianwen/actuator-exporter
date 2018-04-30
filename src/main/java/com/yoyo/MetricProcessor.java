package com.yoyo;

import io.prometheus.client.Collector;

import java.util.List;

public interface MetricProcessor {
    List<Collector.MetricFamilySamples> collect(ActuatorEndPointsInfo actuatorEndPointsInfo);
}
