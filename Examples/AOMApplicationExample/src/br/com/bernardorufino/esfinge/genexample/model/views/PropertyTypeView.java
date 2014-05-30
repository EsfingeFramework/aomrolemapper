package br.com.bernardorufino.esfinge.genexample.model.views;

import br.com.bernardorufino.esfinge.genexample.util.TypeConverter;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class PropertyTypeView {

    private final IPropertyType mPropertyType;
    private Property<String> mName = new SimpleStringProperty();
    private Property<Class<?>> mType = new SimpleObjectProperty<>();
    private Property<String> mTypeString = new SimpleStringProperty();

    public PropertyTypeView(IPropertyType propertyType) {
        try {
            mPropertyType = propertyType;
            mName.setValue(propertyType.getName());
            mType.setValue((Class<?>) propertyType.getType());
            mTypeString.setValue(mType.getValue().getSimpleName());
            mTypeString.addListener(mTypeStringChangeListener);
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    public IPropertyType getPropertyType() {
        return mPropertyType;
    }

    private final ChangeListener<? super String> mTypeStringChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldType, String newType) {
            Class<?> type = TypeConverter.getType(newType);
            if (type != null) {
                mType.setValue(type);
            } else {
                mTypeString.removeListener(this);
                mTypeString.setValue(oldType);
                mTypeString.addListener(this);
            }
        }
    };

    public Property<String> nameProperty() {
        return mName;
    }

    public String getName() {
        return mName.getValue();
    }

    public Property<String> typeStringProperty() {
        return mTypeString;
    }

    public Class<?> getType() {
        return mType.getValue();
    }
}
