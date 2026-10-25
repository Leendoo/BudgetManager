package com.Leonardo.java26.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Repository<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("item får inte vara null");
        }
        items.add(item);
    }

    public List<T> findAll() {
        return List.copyOf(items);
    }

    public List<T> findWhere(Predicate<T> condition) {
        return items.stream()
                .filter(condition)
                .toList();
    }

    public int size() {
        return items.size();
    }
}


