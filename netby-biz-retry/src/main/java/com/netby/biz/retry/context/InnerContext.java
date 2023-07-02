package com.netby.biz.retry.context;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * @author: byg
 */
public class InnerContext {

    private final Map<String, Object> systemMap = Maps.newConcurrentMap();
    private final Map<String, Object> defaultMap = Maps.newConcurrentMap();
    private final Map<String, Object> remoteMap = Maps.newConcurrentMap();

    public void putSystem(String key, Object value) {
        systemMap.put(key, value);
    }

    public void putRemote(String key, Object value) {
        remoteMap.put(key, value);
    }

    public void put(String key, Object value) {
        defaultMap.put(key, value);
    }

    public void putAll(InnerContext context) {
        this.clear();
        if (context != null) {
            systemMap.putAll(context.getSystemMap());
            remoteMap.putAll(context.getRemoteMap());
            defaultMap.putAll(context.getDefaultMap());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getSystem(String key) {
        return (T) systemMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRemote(String key) {
        return (T) remoteMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) defaultMap.get(key);
    }

    public Map<String, Object> getRemoteMap() {
        return remoteMap;
    }

    public Map<String, Object> getSystemMap() {
        return systemMap;
    }

    public Map<String, Object> getDefaultMap() {
        return defaultMap;
    }

    public void removeSystem(String key) {
        systemMap.remove(key);
    }

    public void removeRemote(String key) {
        remoteMap.remove(key);
    }

    public void remove(String key) {
        defaultMap.remove(key);
    }

    public boolean hasSystem(String key) {
        return systemMap.containsKey(key);
    }

    public boolean hasRemote(String key) {
        return remoteMap.containsKey(key);
    }

    public boolean has(String key) {
        return defaultMap.containsKey(key);
    }

    public boolean isSystemEmpty() {
        return systemMap.isEmpty();
    }

    public void clear() {
        systemMap.clear();
        remoteMap.clear();
        defaultMap.clear();
    }
}
