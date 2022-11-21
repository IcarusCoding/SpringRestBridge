package de.intelligence.restbridge.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MultiValueMap<K, V> {

    private final Map<K, List<V>> delegateMap;

    public MultiValueMap() {
        this.delegateMap = new HashMap<>();
    }

    public MultiValueMap(MultiValueMap<K, V> map) {
        this.delegateMap = new HashMap<>(map.delegateMap);
    }

    public int size() {
        return this.delegateMap.size();
    }

    public boolean isEmpty() {
        return this.delegateMap.isEmpty();
    }

    public boolean containsKey(K key) {
        return this.delegateMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.delegateMap.containsValue(value);
    }

    public List<V> get(K key) {
        return this.delegateMap.getOrDefault(key, Collections.emptyList());
    }

    public List<V> put(K key, List<V> value) {
        return this.delegateMap.put(key, value);
    }

    public boolean put(K key, V value) {
        return this.delegateMap.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public List<V> remove(K key) {
        return this.delegateMap.remove(key);
    }

    public void remove(K key, V value) {
        this.delegateMap.computeIfPresent(key, (k, v) -> v.remove(value) && v.isEmpty() ? null : v);
    }

    public void putAll(Map<? extends K, ? extends List<V>> m) {
        this.delegateMap.putAll(m);
    }

    public void clear() {
        this.delegateMap.clear();
    }

    public Set<K> keySet() {
        return this.delegateMap.keySet();
    }

    public Collection<List<V>> values() {
        return this.delegateMap.values();
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return this.delegateMap.entrySet();
    }

    @Override
    public String toString() {
        return this.delegateMap.toString();
    }

}
