package fr.ulity.socketexchange.utils;

import java.util.HashMap;
import java.util.Map;

public class HashMapReversed<K, V> extends HashMap<K, V> {
    HashMap<V, K> reverseMap = new HashMap<>();

    public V put(K key, V value) {
        this.reverseMap.put(value, key);
        return super.put(key, value);
    }

    public void removeFromValue(V value) {
        K k = this.reverseMap.remove(value);
        remove(k, value);
    }

    public K getReversed(V value) {
        return this.reverseMap.get(value);
    }

    public K getKey(V value) {
        return this.reverseMap.get(value);
    }
}
