package br.com.bernardorufino.esfinge.genexample.util;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class MutableObservableList<E extends ObservableBean> extends ObservableListDelegator<E> {



    private boolean mUpdateEnable = true;
    private Collection<MutableChangeListener<E>> mListeners = new ArrayList<>();

    public MutableObservableList() {
        this(new ArrayList<E>());
    }

    public MutableObservableList(List<E> list) {
        super(FXCollections.observableList(list));
        addListener(mChangeListener);
    }

    public void addMutableChangeListener(MutableChangeListener<E> listener) {
        mListeners.add(listener);
    }

    private ListChangeListener<? super E> mChangeListener = new ListChangeListener<E>() {
        @Override
        public void onChanged(Change<? extends E> change) {
            if (!mUpdateEnable) return;
            while (change.next()) {
                if (change.wasPermutated()) {
                    /* No-op */
                } else if (change.wasUpdated()) {
                    observe(get(change.getFrom()));
                } else {
                    for (E item : change.getRemoved()) unobserve(item);
                    for (E item : change.getAddedSubList()) observe(item);
                }
            }
        }
    };

    private Map<ObservableValue<?>, E> itemPropertiesMap = new HashMap<>();

    private void observe(E item) {
        for (Property<?> property : item.getProperties()) {
            itemPropertiesMap.put(property, item);
            property.addListener(mPropertyChangedListener);
        }

    }

    private void unobserve(E item) {
        for (Property<?> property : item.getProperties()) {
            itemPropertiesMap.remove(property);
            property.removeListener(mPropertyChangedListener);
        }
    }

    private ChangeListener<Object> mPropertyChangedListener = new ChangeListener<Object>() {
        @Override
        public void changed(ObservableValue<?> property, Object oldValue, Object newValue) {
            fireMutableChange(property);
        }
    };

    private void fireMutableChange(ObservableValue<?> triggerProperty) {
        E item = itemPropertiesMap.get(triggerProperty);
        int i = indexOf(item);
        mUpdateEnable = false;
        for (MutableChangeListener<E> listener : mListeners) listener.onBeforeMutableChange(this, item);
        remove(item);
        add(i, item);
        for (MutableChangeListener<E> listener : mListeners) listener.onAfterMutableChange(this, item);
        mUpdateEnable = true;
    }

    private static class MutableItemChange<E> extends ListChangeListener.Change<E> {

        private final int mIndex;

        public MutableItemChange(ObservableList<E> observableList, int index) {
            super(observableList);
            mIndex = index;
        }



        @Override
        public boolean next() {
            return true;
        }

        @Override
        public void reset() {
        }

        @Override
        public int getFrom() {
            return 0;
        }

        @Override
        public int getTo() {
            return 2;
        }

        @Override
        public List<E> getRemoved() {
            return null;
        }

        @Override
        protected int[] getPermutation() {
            return new int[0];
        }

    }

}
