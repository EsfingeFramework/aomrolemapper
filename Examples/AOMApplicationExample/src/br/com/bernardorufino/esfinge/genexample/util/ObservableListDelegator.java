package br.com.bernardorufino.esfinge.genexample.util;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ObservableListDelegator<E> implements ObservableList<E> {

    private final ObservableList<E> mObservableList;

    public ObservableListDelegator(ObservableList<E> observableList) {
        mObservableList = observableList;
    }

    public void addListener(ListChangeListener<? super E> listChangeListener) {
        mObservableList.addListener(listChangeListener);
    }

    public void removeListener(ListChangeListener<? super E> listChangeListener) {
        mObservableList.removeListener(listChangeListener);
    }

    public boolean addAll(E... es) {
        return mObservableList.addAll(es);
    }

    public boolean setAll(E... es) {
        return mObservableList.setAll(es);
    }

    public boolean setAll(Collection<? extends E> es) {
        return mObservableList.setAll(es);
    }

    public boolean removeAll(E... es) {
        return mObservableList.removeAll(es);
    }

    public boolean retainAll(E... es) {
        return mObservableList.retainAll(es);
    }

    @Override
    public void remove(int i, int i2) {
        mObservableList.remove(i, i2);
    }

    @Override
    public int size() {
        return mObservableList.size();
    }

    @Override
    public boolean isEmpty() {
        return mObservableList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mObservableList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return mObservableList.iterator();
    }

    @Override
    public Object[] toArray() {
        return mObservableList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return mObservableList.toArray(a);
    }

    public boolean add(E e) {
        return mObservableList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return mObservableList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mObservableList.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return mObservableList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return mObservableList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return mObservableList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return mObservableList.retainAll(c);
    }

    @Override
    public void clear() {
        mObservableList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return mObservableList.equals(o);
    }

    @Override
    public int hashCode() {
        return mObservableList.hashCode();
    }

    @Override
    public E get(int index) {
        return mObservableList.get(index);
    }

    public E set(int index, E element) {
        return mObservableList.set(index, element);
    }

    public void add(int index, E element) {
        mObservableList.add(index, element);
    }

    @Override
    public E remove(int index) {
        return mObservableList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return mObservableList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mObservableList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return mObservableList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return mObservableList.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return mObservableList.subList(fromIndex, toIndex);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        mObservableList.addListener(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        mObservableList.removeListener(invalidationListener);
    }

    

}