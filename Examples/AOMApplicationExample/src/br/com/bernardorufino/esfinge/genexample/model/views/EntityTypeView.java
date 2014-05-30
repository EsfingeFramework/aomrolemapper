package br.com.bernardorufino.esfinge.genexample.model.views;

import br.com.bernardorufino.esfinge.genexample.util.ObservableBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EntityTypeView implements ObservableBean {

    private IEntityType mEntityType;

    private Property<String> mStrName = new SimpleStringProperty();
    private Property<Boolean> mUnsaved = new SimpleBooleanProperty(false);
    private ObservableList<PropertyTypeView> mPropertyTypeViews = FXCollections.observableArrayList();

    public EntityTypeView() {

    }

    public EntityTypeView(IEntityType entityType) throws EsfingeAOMException {
        mEntityType = entityType;
        mStrName.setValue(mEntityType.getName());
        mPropertyTypeViews = loadPropertyTypes();
    }

    public IEntityType getEntityType() {
        return mEntityType;
    }

    public String getStrName() {
        return mStrName.getValue();
    }

    public Property<String> nameProperty() {
        return mStrName;
    }

    public void setUnsaved(boolean unsaved) {
        mUnsaved.setValue(unsaved);
    }

    public ObservableList<PropertyTypeView> getPropertyTypeViews() {
        return mPropertyTypeViews;
    }

    public void removePropertyTypeView(PropertyTypeView propertyTypeView) {
        mPropertyTypeViews.remove(propertyTypeView);
    }

    public void addPropertyTypeView(PropertyTypeView propertyTypeView) {
        mPropertyTypeViews.add(propertyTypeView);
    }

    @Override
    public Collection<? extends Property<?>> getProperties() {
        return Arrays.asList(mUnsaved, mStrName);
    }

    @Override
    public String toString() {
        try {
            String name = mEntityType.getName();
            if (mUnsaved.getValue()) name = "* " + name;
            return name;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    private ChangeListener<? super String> mPropertyTypeChangedListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            mUnsaved.setValue(true);
        }
    };

    private ListChangeListener<? super PropertyTypeView> mPropertyTypeListChanged = new ListChangeListener<PropertyTypeView>() {
        @Override
        public void onChanged(Change<? extends PropertyTypeView> change) {
            mUnsaved.setValue(true);
        }
    };

    private ObservableList<PropertyTypeView> loadPropertyTypes() {
        try {
            ObservableList<PropertyTypeView> propertyTypeViews = FXCollections.observableArrayList();
            List<IPropertyType> propertyTypes = mEntityType.getPropertyTypes();
            for (IPropertyType propertyType : propertyTypes) {
                PropertyTypeView propertyTypeView = new PropertyTypeView(propertyType);
                propertyTypeViews.add(propertyTypeView);
                propertyTypeView.nameProperty().addListener(mPropertyTypeChangedListener);
                propertyTypeView.typeStringProperty().addListener(mPropertyTypeChangedListener);
            }
            propertyTypeViews.addListener(mPropertyTypeListChanged);
            return propertyTypeViews;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }
}
