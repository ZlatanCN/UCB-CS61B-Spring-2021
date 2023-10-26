package deque;


import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private Node sentinal;

    private class Node {
        private Node prev;
        private Node next;
        private T item;
        private Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque() {
        size = 0;
        sentinal = new Node(null, null, null);
        sentinal.next = sentinal;
        sentinal.prev = sentinal;
    }

    @Override
    public void addFirst(T item) {
        size += 1;
        Node first = new Node(sentinal, item, sentinal.next);
        sentinal.next.prev = first;
        sentinal.next = first;
    }

    @Override
    public void addLast(T item) {
        if (size == 0) {
            addFirst(item);
        } else {
            size += 1;
            Node last = new Node(sentinal.prev, item, sentinal.prev.next);
            sentinal.prev.next = last;
            sentinal.prev = last;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinal.next;
        while (p != sentinal) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    private void clearNode(Node targetNode) {
        targetNode.prev = null;
        targetNode.next = null;
    }

    @Override
    public T removeFirst() {
        Node oldFirst = sentinal.next;
        if (size == 0) {
            return null;
        } else {
            sentinal.next = oldFirst.next;
            oldFirst.next.prev = sentinal;
            clearNode(oldFirst);
            size -= 1;
            return oldFirst.item;
        }
    }

    @Override
    public T removeLast() {
        Node oldLast = sentinal.prev;
        if (size == 0) {
            return null;
        } else {
            oldLast.prev.next = sentinal;
            sentinal.prev = oldLast.prev;
            clearNode(oldLast);
            size -= 1;
            return oldLast.item;
        }
    }

    @Override
    public T get(int index) {
        if (size == 0) {
            return null;
        }
        Node p = sentinal.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    private T getRecursiveHelper(int index, Node p) {
        if (index == 0) {
            return p.item;
        } else {
            return getRecursiveHelper(index - 1, p.next);
        }
    }

    public T getRecursive(int index) {
        Node p = sentinal.next;
        if (size == 0) {
            return null;
        } else {
            return getRecursiveHelper(index, p);
        }
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Deque<T> other = (Deque<T>) o;
        if (other.size() != this.size()) {
            return false;
        }
        for (int index = 0; index < this.size(); index++) {
            if (this.get(index) != other.get(index)) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int index;

        private LinkedListDequeIterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public T next() {
            T returnItem = get(index);
            index += 1;
            return returnItem;
        }
    }
}
