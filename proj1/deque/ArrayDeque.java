package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;
    private final double R = 0.25;

    private int allowSize = 8;

    public ArrayDeque() {
        items = (T[]) new Object[allowSize];
        size = 0;
        first = 3;
        last = 4;
    }

    @Override
    public void addFirst(T item) {
        if (first == 0) {
            resize((int) (size / R));
        }
        items[first] = item;
        first -= 1;
        size += 1;
    }

    private void resize(int newSize) {
        T[] temp = (T[]) new Object[newSize];
        System.arraycopy(items, first + 1, temp, (newSize - size) / 2, last - first - 1);
        for (int i = 0; i < newSize; i++) {
            if (temp[i] == items[first + 1]) {
                first = i - 1;
                break;
            }
        }
        for (int j = newSize - 1; j >= 0; j--) {
            if (temp[j] == items[last - 1]) {
                last = j + 1;
                break;
            }
        }
        allowSize = newSize;
        items = temp;
    }

    @Override
    public void addLast(T item) {
        if (last == allowSize - 1) {
            resize((int) (size / R));
        }
        items[last] = item;
        last += 1;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = first + 1; i <= last - 1; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T removed = items[first + 1];
        items[first + 1] = null;
        first += 1;
        size -= 1;
        return removed;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removed = items[last - 1];
        items[last - 1] = null;
        last -= 1;
        size -= 1;
        return removed;
    }

    @Override
    public T get(int index) {
        return items[first + 1 + index];
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (other.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int index;

        private ArrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T returnItem = get(index);
            index += 1;
            return returnItem;
        }
    }
}
