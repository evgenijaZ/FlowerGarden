package com.flowergarden.concurrency;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * @author Yevheniia Zubrych on 24.04.2018.
 */
public class SynchronizedArrayList<E> extends AbstractList <E> {
    private final Object mutex;
    private ArrayList <E> arrayList;

    public SynchronizedArrayList() {
        this.mutex = this;
        arrayList = new ArrayList <E>();
    }

    public SynchronizedArrayList(int initialCapacity, Object mutex) {
        this.mutex = Objects.requireNonNull(mutex);
        arrayList = new ArrayList <E>(initialCapacity);
    }

    public E get(int index) {
        synchronized (mutex) {
            return arrayList.get(index);
        }
    }

    public int size() {
        synchronized (mutex) {
            return arrayList.size();
        }
    }

    public boolean isEmpty() {
        synchronized (mutex) {
            return arrayList.isEmpty();
        }
    }

    public boolean contains(Object o) {
        synchronized (mutex) {
            return arrayList.contains(o);
        }
    }

    public Object[] toArray() {
        synchronized (mutex) {
            return arrayList.toArray();
        }
    }

    public boolean add(E e) {
        synchronized (mutex) {
            return arrayList.add(e);
        }
    }

    public int indexOf(Object o) {
        synchronized (mutex) {
            return arrayList.indexOf(o);
        }
    }

    public int lastIndexOf(Object o) {
        synchronized (mutex) {
            return arrayList.lastIndexOf(o);
        }
    }

    public boolean addAll(int index, Collection <? extends E> collection) {
        synchronized (mutex) {
            return arrayList.addAll(index, collection);
        }
    }

    public boolean remove(Object o) {
        synchronized (mutex) {
            return arrayList.remove(o);
        }
    }

    public boolean containsAll(Collection <?> coll) {
        synchronized (mutex) {
            return arrayList.containsAll(coll);
        }
    }

    public boolean addAll(Collection <? extends E> coll) {
        synchronized (mutex) {
            return arrayList.addAll(coll);
        }
    }

    public boolean removeAll(Collection <?> coll) {
        synchronized (mutex) {
            return arrayList.removeAll(coll);
        }
    }

    public boolean retainAll(Collection <?> coll) {
        synchronized (mutex) {
            return arrayList.retainAll(coll);
        }
    }

    public void replaceAll(UnaryOperator <E> operator) {
        synchronized (mutex) {
            arrayList.replaceAll(operator);
        }
    }

    public void sort(Comparator <? super E> comparator) {
        synchronized (mutex) {
            arrayList.sort(comparator);
        }
    }

    public void clear() {
        synchronized (mutex) {
            arrayList.clear();
        }
    }

    public ListIterator <E> listIterator() {
        return new SynchronizedListIterator(0);
    }

    public ListIterator <E> listIterator(int index) {
        if (index < 0 || index > arrayList.size())
            throw new IndexOutOfBoundsException("Index: " + index);
        return new SynchronizedListIterator(index);
    }

    public String toString() {
        synchronized (mutex) {
            return arrayList.toString();
        }
    }


    class SynchronizedListIterator implements ListIterator <E> {
        private final Object mutex;
        private int index;
        private int lastRet = -1;

        SynchronizedListIterator(int index, Object mutex) {
            this.index = index;
            this.mutex = mutex;
        }

        SynchronizedListIterator(int index) {
            this.index = index;
            this.mutex = SynchronizedArrayList.this;
        }

        @Override
        public boolean hasNext() {
            synchronized (mutex) {
                return index != arrayList.size();
            }
        }

        @Override
        public E next() {
            int i = index;

            if (i >= size())
                throw new NoSuchElementException();
            i++;
            synchronized (mutex) {
                return arrayList.get(lastRet = i);
            }
        }

        @Override
        public boolean hasPrevious() {
            synchronized (mutex) {
                return index != 0;
            }
        }

        @Override
        public E previous() {
            try {
                synchronized (mutex) {
                    int i = index - 1;
                    E previous = arrayList.get(i);
                    lastRet = index = i;
                    return previous;
                }
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            synchronized (mutex) {
                return index;
            }
        }

        @Override
        public int previousIndex() {
            synchronized (mutex) {
                return index - 1;
            }
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            if (index > 0 && index < size())
                try {
                    synchronized (mutex) {
                        arrayList.remove(lastRet);
                        index = lastRet;
                        lastRet = -1;
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new ConcurrentModificationException();
                }
        }

        @Override
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                synchronized (mutex) {
                    arrayList.set(lastRet, e);
                }
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                synchronized (mutex) {
                    arrayList.set(lastRet, e);
                }
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
