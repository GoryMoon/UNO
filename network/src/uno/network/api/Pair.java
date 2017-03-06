package uno.network.api;

import java.io.Serializable;

/**
 * A pair of two objects, a key and value
 * @param <T> First type to store
 * @param <V> Second type to store
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class Pair<T, V> implements Serializable {

    private T key;
    private V value;

    /**
     * Sets the values
     * @param key First value to set
     * @param value Second value to set
     */
    public Pair(T key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return Gets the first value
     */
    public T getKey() {
        return key;
    }

    /**
     * @return Gets the second value
     */
    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            return (key != null ? key.equals(pair.key) : pair.key == null) && (value != null ? value.equals(pair.value) : pair.value == null);
        }
        return false;
    }
}
