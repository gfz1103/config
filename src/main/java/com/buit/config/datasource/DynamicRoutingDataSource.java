package com.buit.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * @Description
 * @Author yueyu
 * @Date 2021/3/7 15:32
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private ThreadLocal<String> lookupKey = new ThreadLocal<>();
    private String defaultKey;

    @Override
    protected Object determineCurrentLookupKey() {
        return lookupKey.get()==null?defaultKey:lookupKey.get();
    }

    public void setLookupKey(String key){
        lookupKey.set(key);
    }

    public void removeLookupKey(){
        lookupKey.remove();
    }

    public boolean isNotSetKey(){
        return lookupKey.get()==null;
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }
}
