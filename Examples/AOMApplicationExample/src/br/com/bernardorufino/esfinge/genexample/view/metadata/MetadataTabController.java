package br.com.bernardorufino.esfinge.genexample.view.metadata;

import br.com.bernardorufino.esfinge.genexample.model.MetadataManager;
import br.com.bernardorufino.esfinge.genexample.model.views.EntityTypeView;
import br.com.bernardorufino.esfinge.genexample.model.views.PropertyTypeView;
import br.com.bernardorufino.esfinge.genexample.util.AbstractMutableChangeListener;
import br.com.bernardorufino.esfinge.genexample.util.FxUtils;
import br.com.bernardorufino.esfinge.genexample.util.MutableChangeListener;
import br.com.bernardorufino.esfinge.genexample.util.MutableObservableList;
import br.com.bernardorufino.esfinge.genexample.view.base.TabController;
import br.com.bernardorufino.esfinge.genexample.view.main.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

// FIXME: error while modifying properties on-the-fly, gotta update data part
public class MetadataTabController extends TabController implements Initializable {

    @FXML private TextField vTextFieldName;
    @FXML private TextField vNewPropertyTypeName;
    @FXML private TextField vNewPropertyTypeType;
    @FXML private ListView<EntityTypeView> vListEntityTypes;
    @FXML private TableView<PropertyTypeView> vTablePropertyTypes;
    @FXML private TableColumn<PropertyTypeView, String> vColPropertyTypeName;
    @FXML private TableColumn<PropertyTypeView, String> vColPropertyTypeType;

    private MainController mMainController;
    private MetadataManager mMetadataManager = MetadataManager.getInstance();

    private final ObservableList<PropertyTypeView> mPropertyTypes = FXCollections.observableArrayList();
    private MutableObservableList<EntityTypeView> mEntityTypes;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTypesList();
        setupPropertyTypesTable();
    }

    @Override
    public void onTabSelected() {
//        FxUtils.forceSelectFirst(vListEntityTypes);
    }

    private void setupPropertyTypesTable() {
        vTablePropertyTypes.setItems(mPropertyTypes);
        vColPropertyTypeName.setCellValueFactory(new PropertyValueFactory<PropertyTypeView, String>("name"));
        vColPropertyTypeName.setCellFactory(TextFieldTableCell.<PropertyTypeView>forTableColumn());
        // Use selection instead of textfield or allow multiple classes
        vColPropertyTypeType.setCellValueFactory(new PropertyValueFactory<PropertyTypeView, String>("typeString"));
        vColPropertyTypeType.setCellFactory(TextFieldTableCell.<PropertyTypeView>forTableColumn());
    }

    // Dirty Hack
    private MutableChangeListener<EntityTypeView> mTypeUpdateListener = new
            AbstractMutableChangeListener<EntityTypeView>() {
        @Override
        public void onAfterMutableChange(MutableObservableList<EntityTypeView> list, EntityTypeView item) {
            FxUtils.forceSelect(vListEntityTypes, item);
        }
    };

    private void setupTypesList() {
        SelectionModel<EntityTypeView> model = vListEntityTypes.getSelectionModel();
        model.selectedItemProperty().addListener(mEntityTypeSelectedListener);
        mEntityTypes = mMetadataManager.getEntityTypes();
        mEntityTypes.addMutableChangeListener(mTypeUpdateListener);
        vListEntityTypes.setItems(mEntityTypes);
        model.selectFirst();
    }

    private ChangeListener<? super EntityTypeView> mEntityTypeSelectedListener = new ChangeListener<EntityTypeView>() {
        @Override
        public void changed(ObservableValue<? extends EntityTypeView> observable,
                            EntityTypeView oldType, EntityTypeView newType) {
            if (oldType != null) unbindEntityType(oldType);
            if (newType != null) bindEntityType(newType);
        }
    };

    private void unbindEntityType(EntityTypeView typeView) {
        vTextFieldName.textProperty().unbindBidirectional(typeView.nameProperty());
        vTablePropertyTypes.setItems(null);
    }

    private void bindEntityType(EntityTypeView typeView) {
        vTextFieldName.textProperty().bindBidirectional(typeView.nameProperty());
        vTablePropertyTypes.setItems(typeView.getPropertyTypeViews());
    }

    public void setMainController(MainController mainController) {
        mMainController = mainController;
    }

    @FXML
    public void saveUserClick(ActionEvent actionEvent) {
        EntityTypeView entityTypeView = vListEntityTypes.getSelectionModel().getSelectedItem();
        if (mMetadataManager.save(entityTypeView)) {
            mMainController.addEntityTypeToReload(entityTypeView);
            mMainController.deliverUserMessage("Tipo salvo com sucesso.");
        } else {
            mMainController.deliverUserMessage("Houve um erro ao salvar tipo.");
        }
    }

    @FXML
    public void newEntityTypeClick(ActionEvent actionEvent) {
        EntityTypeView entityTypeView = mMetadataManager.create();
        mEntityTypes.add(entityTypeView);
        FxUtils.forceSelect(vListEntityTypes, entityTypeView);
        mMainController.deliverUserMessage("Novo tipo de entidade criado, nao esqueca de salva-lo.");
    }

    @FXML
    public void deleteEntityTypeClick(ActionEvent actionEvent) {
        EntityTypeView entityTypeView = vListEntityTypes.getSelectionModel().getSelectedItem();
        if (mMetadataManager.delete(entityTypeView)) {
            FxUtils.remove(vListEntityTypes, entityTypeView);
            mMainController.deliverUserMessage("Tipo deletado com sucesso.");
        } else {
            mMainController.deliverUserMessage("Houve um erro ao deletar tipo.");
        }
    }

    @FXML
    public void addPropertyType(ActionEvent actionEvent) {
        EntityTypeView entityTypeView = vListEntityTypes.getSelectionModel().getSelectedItem();
        String name = vNewPropertyTypeName.getText();
        String type = vNewPropertyTypeType.getText();
        if (mMetadataManager.createPropertyType(entityTypeView, name, type)) {
            mMainController.deliverUserMessage("Propriedade adicionada, nao esqueca de salvar o tipo.");
        } else {
            mMainController.deliverUserMessage("Propriedade nao pode ser adicionado, talvez o tipo nao exista.");
        }

    }

    public void deletePropertyTypeClick(ActionEvent actionEvent) {
        PropertyTypeView propertyTypeView = vTablePropertyTypes.getSelectionModel().getSelectedItem();
        if (propertyTypeView != null) {
            EntityTypeView entityTypeView = vListEntityTypes.getSelectionModel().getSelectedItem();
            mMetadataManager.deletePropertyType(entityTypeView, propertyTypeView);
            mMainController.deliverUserMessage("Propriedade deletada, nao esqueca de salvar as modificacoes.");
        } else {
            mMainController.deliverUserMessage("Selecione uma propriedade antes.");
        }
    }
}