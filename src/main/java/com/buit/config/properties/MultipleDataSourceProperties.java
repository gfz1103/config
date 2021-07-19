package com.buit.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author yueyu
 * @Date 2021/1/19 14:33
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class MultipleDataSourceProperties {
    private String defaultName;
    private List<String> relaySequence;
    private Map<String, DynamicDataSourceProperties> multiple;

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public Map<String, DynamicDataSourceProperties> getMultiple() {
        return multiple;
    }

    public void setMultiple(Map<String, DynamicDataSourceProperties> multiple) {
        this.multiple = multiple;
    }

    public List<String> getRelaySequence() {
        return relaySequence;
    }

    public void setRelaySequence(List<String> relaySequence) {
        this.relaySequence = relaySequence;
    }
}
