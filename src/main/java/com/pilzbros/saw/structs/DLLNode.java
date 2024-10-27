package com.pilzbros.saw.structs;

public class DLLNode<T> {
    public T info;
    public DLLNode<T> next;
    public DLLNode<T> prev;

    public DLLNode() {
        this.next = null;
        this.prev = null;
    }

    public DLLNode(T el) {
        this.info = el;
        this.next = null;
        this.prev = null;
    }

    public DLLNode(T el, DLLNode<T> n, DLLNode<T> p) {
        this.info = el;
        this.next = n;
        this.prev = p;
    }
}