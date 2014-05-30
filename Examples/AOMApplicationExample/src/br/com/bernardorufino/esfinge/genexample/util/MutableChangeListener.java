package br.com.bernardorufino.esfinge.genexample.util;

public interface MutableChangeListener<E extends ObservableBean> {

    public void onBeforeMutableChange(MutableObservableList<E> list, E item);

    public void onAfterMutableChange(MutableObservableList<E> list, E item);

}
