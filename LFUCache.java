import java.util.*;

class LFUCache {
    private final int capacity;
    private int minFreq;
    private Map<Integer, Integer> valueMap;
    private Map<Integer, Integer> freqMap;
    private Map<Integer, LinkedHashSet<Integer>> freqListMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        valueMap = new HashMap<>();
        freqMap = new HashMap<>();
        freqListMap = new HashMap<>();
    }

    public int get(int key) {
        if (!valueMap.containsKey(key)) return -1;
        increaseFreq(key);
        return valueMap.get(key);
    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (valueMap.containsKey(key)) {
            valueMap.put(key, value);
            increaseFreq(key);
            return;
        }

        if (valueMap.size() >= capacity) {
            removeLeastFreq();
        }

        valueMap.put(key, value);
        freqMap.put(key, 1);
        freqListMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    private void increaseFreq(int key) {
        int freq = freqMap.get(key);
        freqMap.put(key, freq + 1);
        freqListMap.get(freq).remove(key);

        if (freq == minFreq && freqListMap.get(freq).isEmpty()) minFreq++;

        freqListMap.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    private void removeLeastFreq() {
        int keyToRemove = freqListMap.get(minFreq).iterator().next();
        freqListMap.get(minFreq).remove(keyToRemove);
        valueMap.remove(keyToRemove);
        freqMap.remove(keyToRemove);
    }
}
