package com.yoyo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoyo.utils.OkHttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActuatorScraper {
    private static final String SCRAPER_URL = "http://localhost:%d/%s";
    private final int port;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ActuatorScraper(int port){
        this.port = port;
    }

    public ActuatorEndPointsInfo doScraper(){
        ActuatorEndPointsInfo actuatorEndPointsInfo = new ActuatorEndPointsInfo();
        actuatorEndPointsInfo.setMetrics(metrics());
        return actuatorEndPointsInfo;
    }


    private Map<String, Number> metrics(){
        final String response = OkHttpClients.doGet(metricsEndPoint());
        if(response == null){
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private String metricsEndPoint(){
        return String.format(SCRAPER_URL, port, "metrics");
    }
}
