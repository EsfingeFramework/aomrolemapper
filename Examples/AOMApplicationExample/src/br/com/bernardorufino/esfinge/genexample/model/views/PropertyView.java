package br.com.bernardorufino.esfinge.genexample.model.views;

import br.com.bernardorufino.esfinge.genexample.util.TypeConverter;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class PropertyView {

    private Class<?> mType;
    private IProperty mProperty;
    private final ReadOnlyProperty<String> mName;
    private final Property<Object> mValue = new SimpleObjectProperty<>();
    private final Property<String> mValueString = new SimpleStringProperty();

    public PropertyView(IProperty property) throws EsfingeAOMException {
        mName = new SimpleStringProperty(property.getPropertyType().getName());
        mType = (Class<?>) property.getPropertyType().getType();
        load(property);
        mValueString.addListener(mValueStringChangeListener);
    }

    private final ChangeListener<? super String> mValueStringChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            try {
                mValue.setValue(TypeConverter.convertFromString(newValue, mType));
            } catch (Exception e) {
                mValueString.removeListener(this);
                mValueString.setValue(oldValue);
                mValueString.addListener(this);
            }
        }
    };

    public ReadOnlyProperty<String> nameProperty() {
        return mName;
    }

    public String getName() {
        return mName.getValue();
    }

    public Property<String> valueStringProperty() {
        return mValueString;
    }

    public Object getValue() {
        return mValue.getValue();
    }

    public void load(IProperty property) throws EsfingeAOMException {
        if (!property.getPropertyType().getName().equals(mName.getValue())) {
            throw new AssertionError("Trying to load property " + property.getPropertyType().getName() + " into " +
                    "property " + mName.getValue());
        }
        mProperty = property;
        mValue.setValue(mProperty.getValue());
        String value = (mValue.getValue() == null) ? "" : mValue.getValue().toString();
        mValueString.setValue(value);
    }
}
