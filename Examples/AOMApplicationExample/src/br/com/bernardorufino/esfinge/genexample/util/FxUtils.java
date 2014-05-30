package br.com.bernardorufino.esfinge.genexample.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;

public class FxUtils {

    public static <T> void updateList(ListView<T> list) {
        ObservableList<T> items = list.getItems();
        list.setItems(null);
        list.setItems(items);
    }

    public static <T> void reselectCurrent(ListView<T> list) {
        SelectionModel<T> model = list.getSelectionModel();
        int i = model.getSelectedIndex();
        model.clearSelection();
        System.out.println("selecting i = " + i);
        model.select(i);
    }

    // REFACTOR: Change idiom to use selection model
    public static <T> void forceSelect(ListView<T> list, T item) {
        SelectionModel<T> model = list.getSelectionModel();
        model.clearSelection();
        model.select(item);
    }

    public static <T> void forceSelect(ChoiceBox<T> list, T item) {
        SelectionModel<T> model = list.getSelectionModel();
        model.clearSelection();
        model.select(item);
    }    

    public static <T> void forceSelectFirst(ListView<T> list) {
        SelectionModel<T> model = list.getSelectionModel();
        model.clearSelection();
        model.selectFirst();
    }

    public static <T> void selectNearby(ListView<T> list, T item) {
        SelectionModel<T> model = list.getSelectionModel();
        int i = model.getSelectedIndex();
        int n = list.getItems().size();
        if (i + 1 < n) model.select(i + 1);
        else if (i - 1 >= 0) model.select(i - 1);
        else model.clearSelection();
    }

    public static <T> void selectOrNearby(ListView<T> listView, int i) {
        int n = listView.getItems().size();
        listView.getSelectionModel().select((i < n) ? i : i - 1);
    }

    public static <T> void remove(ListView<T> listView, T item) {
        int i = listView.getSelectionModel().getSelectedIndex();
        listView.getItems().remove(item);
        selectOrNearby(listView, i);
    }
}
