package br.com.bernardorufino.esfinge.genexample.model.views;

import br.com.bernardorufino.esfinge.genexample.util.ObservableBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EntityView implements ObservableBean {

    private static final String DEFAULT_NAME = "Unnamed";

    private IEntity mEntity;
    private final ObservableList<PropertyView> mPropertyViews;

    private Property<Boolean> mUnsaved = new SimpleBooleanProperty(false);
    private Property<String> mName = new SimpleStringProperty(DEFAULT_NAME);


    public EntityView(IEntity entity) {
        mEntity = entity;
        mPropertyViews = loadProperties();
    }

    public IEntity getEntity() {
        return mEntity;
    }

    public void setUnsaved(boolean unsaved) {
        mUnsaved.setValue(unsaved);
    }

    public ObservableList<PropertyView> getPropertyViews() {
        return mPropertyViews;
    }

    @Override
    public Collection<? extends Property<?>> getProperties() {
        return Arrays.asList(mUnsaved, mName);
    }

    @Override
    public String toString() {
        String name = mName.getValue();
        if (mUnsaved.getValue()) name = "* " + name;
        return name;
    }

    public void reload() {
        try {
            mEntity = ModelManager.getInstance().getFreshEntity(mEntity);
            mPropertyViews.clear();
            mPropertyViews.addAll(loadProperties());
            mUnsaved.setValue(false);
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    private ChangeListener<? super String> mPropertyChangedListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            mUnsaved.setValue(true);
        }
    };

    private ObservableList<PropertyView> loadProperties() {
        try {
            ObservableList<PropertyView> propertyViews = FXCollections.observableArrayList();
            List<IProperty> properties = mEntity.getProperties();
            for (IProperty property : properties) {
                PropertyView propertyView = new PropertyView(property);
                propertyViews.add(propertyView);
                if ("name".equals(propertyView.getName())) bindName(propertyView);
                propertyView.valueStringProperty().addListener(mPropertyChangedListener);
            }
            return propertyViews;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    private void bindName(PropertyView propertyView) {
        // TODO: Check property type, case not string skip
        String name = (String) propertyView.getValue();
        if (name == null || "".equals(name)) {
            propertyView.valueStringProperty().setValue(DEFAULT_NAME);
        }
        mName.bindBidirectional(propertyView.valueStringProperty());
    }

}
