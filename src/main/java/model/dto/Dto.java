package model.dto;

import java.util.Objects;

public class Dto<K> {
    protected K key;

    protected Dto(K key) {
        if (key == null) {
            throw new IllegalArgumentException("No key " + null);
        }
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dto<?> dto = (Dto<?>) o;
        return Objects.equals(key, dto.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
