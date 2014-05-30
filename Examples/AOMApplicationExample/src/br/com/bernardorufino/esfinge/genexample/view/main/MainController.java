package br.com.bernardorufino.esfinge.genexample.view.main;

import br.com.bernardorufino.esfinge.genexample.model.views.EntityTypeView;
import br.com.bernardorufino.esfinge.genexample.view.base.BaseController;
import br.com.bernardorufino.esfinge.genexample.view.base.TabController;
import br.com.bernardorufino.esfinge.genexample.view.data.DataTabController;
import br.com.bernardorufino.esfinge.genexample.view.metadata.MetadataTabController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class MainController extends BaseController implements Initializable {

    @FXML private TabPane vTabPane;
    @FXML private Text vTextUserMessage;
    @FXML private GridPane mDataTab;
    @FXML private DataTabController mDataTabController;
    @FXML private GridPane mMetadataTab;
    @FXML private MetadataTabController mMetadataTabController;
    private Queue<EntityTypeView> mEntityTypeViewQueue = new LinkedList<>();

    private Map<Node, TabController> mTabs = new HashMap<Node, TabController>();

    private void populateTabs() {
        mTabs.put(mDataTab, mDataTabController);
        mTabs.put(mMetadataTab, mMetadataTabController);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        mDataTabController.setMainController(this);
        mMetadataTabController.setMainController(this);
        populateTabs();
        setupTabPane();
    }

    private void setupTabPane() {
        SelectionModel<Tab> model = vTabPane.getSelectionModel();
        model.selectedItemProperty().addListener(mTabChangeListener);
        model.clearSelection();
        model.selectFirst();
    }

    public void addEntityTypeToReload(EntityTypeView entityTypeView) {
        mEntityTypeViewQueue.add(entityTypeView);
    }

    public Iterable<EntityTypeView> getEntityTypes() {
        return mEntityTypeViewQueue;
    }

    public void commitReload() {
        mEntityTypeViewQueue.clear();
    }

    private ChangeListener<? super Tab> mTabChangeListener = new ChangeListener<Tab>() {
        @Override
        public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
            if (newTab == null) return;
            Node tab = newTab.getContent();
            mTabs.get(tab).onTabSelected();
        }
    };

    public void deliverUserMessage(String message) {
        vTextUserMessage.setText(message);
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        mDataTabController.setStage(stage);
        mMetadataTabController.setStage(stage);
    }
}
