package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }

        private V get(K key) {
            return value;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int tableSize;
    private double loadFactor;
    private HashSet<K> keySet;

    /** Constructors */
    public MyHashMap() {
        tableSize = 16;
        loadFactor = 0.75;
        keySet = new HashSet<>();
        buckets = createTable(tableSize);
        for (int i = 0; i < tableSize; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        tableSize = initialSize;
        loadFactor = 0.75;
        keySet = new HashSet<>();
        buckets = createTable(tableSize);
        for (int i = 0; i < tableSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        tableSize = initialSize;
        loadFactor = maxLoad;
        keySet = new HashSet<>();
        buckets = createTable(tableSize);
        for (int i = 0; i < tableSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Your code won't compile until you do so!
    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        keySet.clear();
        buckets = createTable(tableSize);
        for (int i = 0; i < tableSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        V value = null;
        if (!containsKey(key)) {
            return null;
        }
        int index = hash(key, tableSize);
        for (Node node : buckets[index]) {
            if (isKeyEqual(node.key, key)) {
                value = node.value;
                break;
            }
        }
        return value;
    }

    private int hash(K key, int tableSize) {
        int h = Math.abs(key.hashCode());
        h ^= (h >>> 20) ^ (h >>> 12);
        h = h ^ (h >>> 7) ^ (h >>> 4);
        return h % tableSize;
    }

    private boolean isKeyEqual(K key1, K key2) {
        return key1.equals(key2);
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return keySet.size();
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        int index = hash(key, tableSize);
        Node newNode = createNode(key, value);
        if (!keySet.contains(key)) {
            buckets[index].add(newNode);
            keySet.add(key);
        } else {
            for (Node node : buckets[index]) {
                if (isKeyEqual(node.key, key)) {
                    node.value = value;
                    break;
                }
            }
        }

        loadFactor = (double) size() / tableSize;
        if (loadFactor > 0.75) {
            resize();
        }
    }

    private void resize() {
        tableSize *= 2;
        Collection<Node>[] newBuckets = createTable(tableSize);
        for (int i = 0; i < tableSize; i++) {
            newBuckets[i] = createBucket();
        }
        for (K key : keySet) {
            int oldIndex = hash(key, tableSize / 2);
            int newIndex = hash(key, tableSize);
            for (Node node : buckets[oldIndex]) {
                newBuckets[newIndex].add(node);
            }
        }
        this.buckets = newBuckets;
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        V value = null;
        if (!containsKey(key)) {
            return null;
        }
        int index = hash(key, tableSize);
        for (Node node : buckets[index]) {
            if (isKeyEqual(node.key, key)) {
                value = node.value;
                buckets[index].remove(node);
                keySet.remove(key);
                break;
            }
        }
        return value;
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        return remove(key);
    }


    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
