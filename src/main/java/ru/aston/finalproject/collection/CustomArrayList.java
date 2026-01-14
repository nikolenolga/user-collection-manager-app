package ru.aston.finalproject.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class CustomArrayList<E> extends AbstractList<E>
        implements List<E>, Cloneable, Serializable {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROWTH_THRESHOLD = 1000;
    private static final double HIGH_GROWTH_FACTOR = 2.0;
    private static final double NORMAL_GROWTH_FACTOR = 1.5;
    private static final Object[] EMPTY_ELEMENT_DATA = {};
    protected transient int modCount = 0;
    private transient Object[] elementData;
    private int size;

    public CustomArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    public CustomArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            int capacity = Math.max(initialCapacity, DEFAULT_CAPACITY);
            this.elementData = new Object[capacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENT_DATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    public CustomArrayList(Collection<? extends E> c) {

        Objects.requireNonNull(c, "Collection cannot be null");
        int collectionSize = c.size();
        int initialCapacity = Math.max(collectionSize, DEFAULT_CAPACITY);
        this.elementData = new Object[initialCapacity];
        for (E element : c) {
            elementData[size++] = element;
        }
        if (size >= elementData.length * LOAD_FACTOR) {
            ensureCapacity(size + 1);
        }
    }

    public CustomArrayList(CustomArrayList<E> other) {
        this.elementData = Arrays.copyOf(other.elementData, other.size);
        this.size = other.size;
    }

    private int calculateNewCapacity(int oldCapacity, int minCapacity) {

        int newCapacity;
        if (oldCapacity < GROWTH_THRESHOLD) {
            newCapacity = (int) (oldCapacity * HIGH_GROWTH_FACTOR);
        } else {
            newCapacity = (int) (oldCapacity * NORMAL_GROWTH_FACTOR);
        }

        if (newCapacity < 0) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            newCapacity = Integer.MAX_VALUE;
        }

        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }

        return newCapacity;
    }

    public void ensureCapacity(int minCapacity) {
        if (elementData == EMPTY_ELEMENT_DATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        if (minCapacity > elementData.length) {
            int newCapacity = calculateNewCapacity(elementData.length, minCapacity);
            grow(newCapacity);
        }
    }

    private void grow(int newCapacity) {
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    @Override
    public boolean add(E element) {
        modCount++;
        ensureCapacity(size + 1);
        elementData[size] = element;
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        modCount++;
        ensureCapacity(size + 1);
        if (index < size) {
            System.arraycopy(elementData, index, elementData, index + 1, size - index);
        }
        elementData[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {

        rangeCheckForAdd(index);
        Objects.requireNonNull(c, "Collection cannot be null");
        if (c.isEmpty()) {
            return false;
        }

        int numNew = c.size();
        ensureCapacity(size + numNew);
        modCount++;
        int numMoved = size - index;

        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }

        copyCollectionElements(c, elementData, index);
        size += numNew;
        return true;
    }

    private void copyCollectionElements(Collection<? extends E> c, Object[] destination, int destPos) {
        if (c instanceof CustomArrayList) {
            CustomArrayList<?> sourceList = (CustomArrayList<?>) c;
            System.arraycopy(sourceList.elementData, 0, destination, destPos, sourceList.size);
        } else {
            int pos = destPos;
            for (E element : c) {
                destination[pos++] = element;
            }
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c, "Collection cannot be null");
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        Objects.requireNonNull(c, "Collection cannot be null");
        if (c.isEmpty()) {
            return false;
        }

        boolean modified = false;
        int i = 0;
        while (i < size) {
            if (c.contains(elementData[i])) {
                fastRemove(i);
                modified = true;
            } else {
                i++;
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        Objects.requireNonNull(c, "Collection cannot be null");
        boolean modified = false;

        int i = 0;
        while (i < size) {
            if (!c.contains(elementData[i])) {
                fastRemove(i);
                modified = true;
            } else {
                i++;
            }
        }

        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {

        rangeCheck(index);
        modCount++;
        E oldValue = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;

        return oldValue;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elementData[i])) {
                fastRemove(i);
                return true;
            }
        }
        return false;
    }

    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
    }

    @Override
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    public int capacity() {
        return elementData.length;
    }

    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            CustomArrayList<E> clone = (CustomArrayList<E>) super.clone();
            clone.elementData = Arrays.copyOf(elementData, size);
            clone.modCount = 0;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, elementData[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(o, elementData[i])) {
                return i;
            }
        }
        return -1;
    }

    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENT_DATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    public String getGrowthStrategyInfo() {
        int capacity = elementData.length;
        if (capacity < GROWTH_THRESHOLD) {
            return String.format("Current capacity: %d (fast growth: x%.1f)",
                    capacity, HIGH_GROWTH_FACTOR);
        } else {
            return String.format("Current capacity: %d (normal growth: x%.1f)",
                    capacity, NORMAL_GROWTH_FACTOR);
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elementData[i]);
        }

        sb.append(']');
        return sb.toString();
    }

    private class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;
        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {

            checkForComodification();
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            Object[] elementData = CustomArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            lastRet = i;

            return (E) elementData[i];
        }

        @Override
        public void remove() {

            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                CustomArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
