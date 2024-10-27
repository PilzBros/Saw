package com.pilzbros.saw.structs;

import java.util.LinkedList;

public class Queue<T> {
    private LinkedList<T> list = new LinkedList();

    public void clear() {
        this.list.clear();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public T firstEl() {
        return this.list.getFirst();
    }

    public T dequeue() {
        return this.list.removeFirst();
    }

    public void enqueue(T el) {
        this.list.addLast(el);
    }

    public String toString() {
        return this.list.toString();
    }

    public int size() {
        return this.list.size();
    }

    public boolean inQueue(T el) {
        return this.list.contains(el);
    }
}