package br.com.bernardorufino.esfinge.genexample.view.data;

import br.com.bernardorufino.esfinge.genexample.model.views.PropertyView;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;

import static javafx.scene.control.TableColumn.CellDataFeatures;

public class PropertyValueCellFactory implements Callback<CellDataFeatures<PropertyView,String>,ObservableValue<String>> {
    @Override
    public ObservableValue<String> call(CellDataFeatures<PropertyView, String> p) {

        return null;
    }
}
