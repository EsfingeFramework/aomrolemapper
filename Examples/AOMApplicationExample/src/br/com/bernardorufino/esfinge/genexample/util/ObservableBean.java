package br.com.bernardorufino.esfinge.genexample.util;

import javafx.beans.property.Property;

import java.util.Collection;

public interface ObservableBean {

    public Collection<? extends Property<?>> getProperties();

}
