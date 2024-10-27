package com.pilzbros.saw.structs;

public class DLL<T> {
    private DLLNode<T> head;
    private DLLNode<T> tail;

    public DLL() {
        this.head = this.tail = null;
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public void clear() {
        this.head = this.tail = null;
    }

    public T firstEl() {
        return this.head != null ? this.head.info : null;
    }

    public void addToHead(T el) {
        if (this.head != null) {
            this.head = new DLLNode(el, this.head, (DLLNode)null);
            this.head.next.prev = this.head;
        } else {
            this.head = this.tail = new DLLNode(el);
        }

    }

    public void addToTail(T el) {
        if (this.tail != null) {
            this.tail = new DLLNode(el, (DLLNode)null, this.tail);
            this.tail.prev.next = this.tail;
        } else {
            this.head = this.tail = new DLLNode(el);
        }

    }

    public T deleteFromHead() {
        if (this.isEmpty()) {
            return null;
        } else {
            T el = this.head.info;
            if (this.head == this.tail) {
                this.head = this.tail = null;
            } else {
                this.head = this.head.next;
                this.head.prev = null;
            }

            return el;
        }
    }

    public T deleteFromTail() {
        if (this.isEmpty()) {
            return null;
        } else {
            T el = this.tail.info;
            if (this.head == this.tail) {
                this.head = this.tail = null;
            } else {
                this.tail = this.tail.prev;
                this.tail.next = null;
            }

            return el;
        }
    }

    public void printAll() {
        for(DLLNode tmp = this.head; tmp != null; tmp = tmp.next) {
            System.out.print(tmp.info + " ");
        }

    }

    public T find(T el) {
        DLLNode tmp;
        for(tmp = this.head; tmp != null && tmp.info != el; tmp = tmp.next) {
        }

        return tmp == null ? null : (T)tmp.info;
    }

    public T dequeue() {
        return this.deleteFromTail();
    }

    public int size() {
        if (this.isEmpty()) {
            return 0;
        } else if (this.head == this.tail) {
            return 1;
        } else {
            int counter = 0;

            for(DLLNode tmp = this.head; tmp.next != null; ++counter) {
                tmp = tmp.next;
            }

            return counter;
        }
    }

    public void enqueue(T el) {
        this.addToTail(el);
    }

    public boolean inQueue(T el) {
        return this.find(el) != null;
    }

    public void remove(T el) {
        if (this.find(el) != null) {
            DLLNode<T> tmp = this.head;
            if (this.head.info == el) {
                tmp = this.head;
                this.head = this.head.next;
            } else if (this.tail.info == el) {
                this.tail = this.tail.prev;
            } else {
                while(tmp.info != el && tmp.next != null) {
                    tmp = tmp.next;
                }

                if (tmp.info == el) {
                    tmp.prev.next = tmp.next;
                    tmp.next.prev = tmp;
                }
            }
        }

    }

    public T get(int i) {
        DLLNode<T> tmp = this.head;

        for(int x = 0; x < i; ++x) {
            tmp = tmp.next;
        }

        return tmp.info;
    }
}