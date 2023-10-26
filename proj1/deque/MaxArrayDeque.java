package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> c;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }

    public T max() {
        if (size() == 0) {
            return null;
        }
        T max = this.get(0);
        for (int i = 1; i < size(); i++) {
            if (c.compare(max, get(i)) < 0) {
                max = get(i);
            }
        }
        return max;
    }

    public T max(Comparator<T> comparator) {
        if (size() == 0) {
            return null;
        }
        T max = this.get(0);
        for (int i = 1; i < size(); i++) {
            if (comparator.compare(max, get(i)) < 0) {
                max = get(i);
            }
        }
        return max;
    }
}
