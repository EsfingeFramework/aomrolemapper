package br.com.bernardorufino.esfinge.genexample.view.data;


import br.com.bernardorufino.esfinge.genexample.model.DataManager;
import br.com.bernardorufino.esfinge.genexample.model.MetadataManager;
import br.com.bernardorufino.esfinge.genexample.model.views.EntityTypeView;
import br.com.bernardorufino.esfinge.genexample.model.views.EntityView;
import br.com.bernardorufino.esfinge.genexample.model.views.PropertyView;
import br.com.bernardorufino.esfinge.genexample.util.AbstractMutableChangeListener;
import br.com.bernardorufino.esfinge.genexample.util.FxUtils;
import br.com.bernardorufino.esfinge.genexample.util.MutableChangeListener;
import br.com.bernardorufino.esfinge.genexample.util.MutableObservableList;
import br.com.bernardorufino.esfinge.genexample.view.base.TabController;
import br.com.bernardorufino.esfinge.genexample.view.main.MainController;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.esfinge.aom.api.model.IEntityType;

import java.net.URL;
import java.util.ResourceBundle;

// FIXME: When adding elements to the beginning of the choice box it breaks, see new entity button click event
public class DataTabController extends TabController implements Initializable {

    @FXML private Button vButtonNewUser;
    @FXML private Button vButtonDeleteUser;
    @FXML private Button vButtonSaveUser;
    @FXML private TableView<PropertyView> vPropertiesTable;
    @FXML private TableColumn<PropertyView, String> vColPropertyName;
    @FXML private TableColumn<PropertyView, String> vColPropertyValue;
    @FXML private ListView<EntityView> vListEntities;
    @FXML private ChoiceBox<EntityTypeView> vChoiceUserType;

    private MainController mMainController;
    private final DataManager mDataManager = DataManager.getInstance();
    private final MetadataManager mMetadataManager = MetadataManager.getInstance();

    private Property<EntityTypeView> mEntityTypeView;
    private MutableObservableList<EntityView> mEntities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEntityTypeChoices();
        setupEntities();
        setupPropertiesTable();
    }

    @Override
    public void onTabSelected() {
        for (EntityTypeView entityTypeView : mMainController.getEntityTypes()) {
            IEntityType entityType = entityTypeView.getEntityType();
            for (EntityView entityView : mDataManager.getEntities(entityType)) {
                entityView.reload();
            }
        }
        mMainController.commitReload();
    }

    private void setupEntities() {
        vListEntities.getSelectionModel().selectedItemProperty().addListener(mEntitySelectedListener);
    }

    private void setupPropertiesTable() {
        vColPropertyName.setCellValueFactory(new PropertyValueFactory<PropertyView, String>("name"));
        vColPropertyName.setCellFactory(TextFieldTableCell.<PropertyView>forTableColumn());
        vColPropertyValue.setCellValueFactory(new PropertyValueFactory<PropertyView, String>("valueString"));
        vColPropertyValue.setCellFactory(TextFieldTableCell.<PropertyView>forTableColumn());
    }

    private ChangeListener<EntityTypeView> mEntityTypeChangeListener = new ChangeListener<EntityTypeView>() {
        @Override
        public void changed(ObservableValue<? extends EntityTypeView> observable,
                            EntityTypeView oldType, EntityTypeView newType) {
            if (newType != null) updateEntitiesList(newType.getEntityType());
        }
    };

    private void updateEntitiesList(IEntityType entityType) {
        mEntities = mDataManager.getEntities(entityType);
        vListEntities.setItems(null);
        vListEntities.setItems(mEntities);
        mEntities.addMutableChangeListener(mEntityUpdateListener);
    }

    // Ugly hack
    private MutableChangeListener<EntityTypeView> mEntityTypeUpdateListener = new AbstractMutableChangeListener<EntityTypeView>() {
        @Override
        public void onBeforeMutableChange(MutableObservableList<EntityTypeView> list, EntityTypeView item) {
            vChoiceUserType.getSelectionModel().clearSelection();
            vChoiceUserType.setItems(FXCollections.<EntityTypeView>emptyObservableList());
        }
        @Override
        public void onAfterMutableChange(MutableObservableList<EntityTypeView> list, EntityTypeView item) {
            vChoiceUserType.setItems(list);
            FxUtils.forceSelect(vChoiceUserType, item);
        }
    };

    private void setupEntityTypeChoices() {
        mEntityTypeView = vChoiceUserType.valueProperty();
        mEntityTypeView.addListener(mEntityTypeChangeListener);
        MutableObservableList<EntityTypeView> entityTypes = mMetadataManager.getEntityTypes();
        entityTypes.addMutableChangeListener(mEntityTypeUpdateListener);
        vChoiceUserType.setItems(entityTypes);
        vChoiceUserType.getSelectionModel().selectFirst();
    }

    // Dirty Hack
    private MutableChangeListener<EntityView> mEntityUpdateListener = new AbstractMutableChangeListener<EntityView>() {
        @Override
        public void onAfterMutableChange(MutableObservableList<EntityView> list, EntityView item) {
            FxUtils.forceSelect(vListEntities, item);
        }
    };

    private ChangeListener<? super EntityView> mEntitySelectedListener = new ChangeListener<EntityView>() {
        @Override
        public void changed(ObservableValue<? extends EntityView> observable,
                            EntityView oldEntity, EntityView newEntity) {
            if (newEntity != null) bindEntityProperties(newEntity);
        }
    };

    private void bindEntityProperties(EntityView entity) {
        vPropertiesTable.setItems(entity.getPropertyViews());
    }

    public void setMainController(MainController mainController) {
        mMainController = mainController;
    }

    @FXML
    public void saveUserClick(ActionEvent actionEvent) {
        EntityView entityView = vListEntities.getSelectionModel().getSelectedItem();
        if (mDataManager.save(entityView)) {
            mMainController.deliverUserMessage("Usuario salvo com sucesso.");
        } else {
            mMainController.deliverUserMessage("Houve um erro ao salvar usuario.");
        }
    }

    @FXML
    public void newUserClick(ActionEvent actionEvent) {
//        MutableObservableList<EntityTypeView> items = (MutableObservableList<EntityTypeView>) vChoiceUserType.getItems();
//        vChoiceUserType.getSelectionModel().clearSelection();
//        vChoiceUserType.setItems(FXCollections.<EntityTypeView>observableArrayList());
//        items.add(0, new Debug());
//        vChoiceUserType.setItems(items);
//        vChoiceUserType.getSelectionModel().selectFirst();
//        if (true) return;
        EntityView entityView = mDataManager.create(mEntityTypeView.getValue());
        mEntities.add(entityView);
        FxUtils.forceSelect(vListEntities, entityView);
        mMainController.deliverUserMessage("Nova entidade criada, nao esqueca de salva-la.");
    }

    @FXML
    public void deleteUserClick(ActionEvent actionEvent) {
        EntityView entityView = vListEntities.getSelectionModel().getSelectedItem();
        if (mDataManager.delete(entityView)) {
            FxUtils.remove(vListEntities, entityView);
            mMainController.deliverUserMessage("Usuario deletado com sucesso.");
        } else {
            mMainController.deliverUserMessage("Nao foi possivel deletar usuario.");
        }
    }

}

