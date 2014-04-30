package org.simple.workflow.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUMap<K, V>
    extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private int max_cap;

    public LRUMap(int initial_cap, int max_cap) {
        super(initial_cap, 0.75f, true);
        this.max_cap = max_cap;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.max_cap;
    }
}
