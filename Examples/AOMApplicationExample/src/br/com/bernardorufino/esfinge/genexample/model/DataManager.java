package br.com.bernardorufino.esfinge.genexample.model;

import br.com.bernardorufino.esfinge.genexample.model.views.EntityTypeView;
import br.com.bernardorufino.esfinge.genexample.model.views.EntityView;
import br.com.bernardorufino.esfinge.genexample.model.views.PropertyView;
import br.com.bernardorufino.esfinge.genexample.util.MutableObservableList;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;


public class DataManager {

    // Thread-safe, lazy initialization with class holder.
    private static class InstanceHolder {
        private static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private final ModelManager mManager = ModelManager.getInstance();

    private DataManager() {
        /* Prevents outside instantiation */
    }

    public boolean delete(EntityView entityView) {
        try {
            mManager.removeEntity(entityView.getEntity());
            return true;
        } catch (EsfingeAOMException e) {
            return false;
        }
    }

    public boolean save(EntityView entityView) {
        try {
            IEntity entity = entityView.getEntity();
            for (PropertyView propertyView : entityView.getPropertyViews()) {
                entity.setProperty(propertyView.getName(), propertyView.getValue());
            }
            mManager.save(entity);
            entityView.reload();

            return true;
        } catch (EsfingeAOMException e) {
            return false;
        }
    }


    public MutableObservableList<EntityView> getEntities(IEntityType entityType) {
        try {
            MutableObservableList<EntityView> entityViews = new MutableObservableList<>();
            for (IEntity entity : mManager.getEntitiesForType(entityType)) {
                entityViews.add(new EntityView(entity));
            }
            return entityViews;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    public EntityView create(EntityTypeView entityTypeView) {
        try {
            IEntity entity = entityTypeView.getEntityType().createNewEntity();
            EntityView entityView = new EntityView(entity);
            entityView.setUnsaved(true);
            return entityView;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }

    }

}
