package br.com.bernardorufino.esfinge.genexample.util;

public abstract class AbstractMutableChangeListener<E extends ObservableBean> implements MutableChangeListener<E> {

    @Override
    public void onBeforeMutableChange(MutableObservableList<E> list, E item) { /* No-op */ }

    @Override
    public void onAfterMutableChange(MutableObservableList<E> list, E item) { /* No-op */ }

}
