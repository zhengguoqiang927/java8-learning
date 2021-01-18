package com.zhengguoqiang.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LRUCache {
    static class DoubleLinkedNode {
        int key;
        int value;
        DoubleLinkedNode prev;
        DoubleLinkedNode next;

        public DoubleLinkedNode() {
        }

        public DoubleLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<Integer, DoubleLinkedNode> cache = new HashMap<>();
    private int size;
    private final int capacity;
    private final DoubleLinkedNode head, tail;

    public LRUCache(int capacity) {
        if (capacity <= 0) throw new RuntimeException("capacity must to be a positive number.");
        this.size = 0;
        this.capacity = capacity;
        head = new DoubleLinkedNode();
        tail = new DoubleLinkedNode();
        head.next = tail;
        tail.prev = head;
    }

    public void put(int key, int value) {
        DoubleLinkedNode node = cache.get(key);
        if (node == null) {
            DoubleLinkedNode newNode = new DoubleLinkedNode(key, value);
            cache.put(key, newNode);
            addToTail(newNode);
            size++;
            if (size > capacity) {
                //删除头结点
                removeNode(head.next);
                cache.remove(head.next.key);
                size--;
            }
        } else {
            node.value = value;
            moveToTail(node);
        }
    }

    public int get(int key) {
        DoubleLinkedNode node = cache.get(key);
        if (node == null) return -1;
        moveToTail(node);
        return node.value;
    }

    private void addToTail(DoubleLinkedNode node) {
        //指定当前节点的前驱结点和后继结点分别为tail的前驱和tail本身
        node.prev = tail.prev;
        node.next = tail;
        //使tail的前驱结点的后继结点为当前结点
        tail.prev.next = node;
        tail.prev = node;
    }

    private void removeNode(DoubleLinkedNode node) {
        if (node == tail) return;
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToTail(DoubleLinkedNode node) {
        removeNode(node);
        addToTail(node);
    }

    public int size() {
        return size;
    }

    public void forEach(Consumer<Integer> action) {
        if (action == null)
            throw new NullPointerException();
        for (DoubleLinkedNode e = head.next; e != tail; e = e.next) {
            action.accept(e.value);
        }
    }


    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(-1);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        lruCache.put(4, 4);
        lruCache.put(5, 5);
        lruCache.put(1, 11);
        lruCache.get(5);
        lruCache.get(3);

        lruCache.forEach(System.out::println);
    }
}
