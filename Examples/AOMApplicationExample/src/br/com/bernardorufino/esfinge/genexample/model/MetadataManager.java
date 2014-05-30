package br.com.bernardorufino.esfinge.genexample.model;

import br.com.bernardorufino.esfinge.genexample.model.views.EntityTypeView;
import br.com.bernardorufino.esfinge.genexample.model.views.EntityView;
import br.com.bernardorufino.esfinge.genexample.model.views.PropertyTypeView;
import br.com.bernardorufino.esfinge.genexample.util.MutableObservableList;
import br.com.bernardorufino.esfinge.genexample.util.TypeConverter;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;
import org.esfinge.aom.model.factories.EntityTypeFactory;
import org.esfinge.aom.model.factories.PropertyTypeFactory;

import java.util.List;

public class MetadataManager {

    private static final String ENTITY_TYPE_PACKAGE = "br.com.bernardorufino.esfinge.genexample.model";
    private static final String DEFAULT_NAME = "Unnamed";

    // Thread-safe, lazy initialization with class holder.
    private static class InstanceHolder {
        private static final MetadataManager INSTANCE = new MetadataManager();
    }

    public static MetadataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private MutableObservableList<EntityTypeView> mEntityTypeViews;

    private final ModelManager mManager = ModelManager.getInstance();

    private MetadataManager() {
        /* Prevents outside instantiation */
    }

    public boolean save(EntityTypeView entityTypeView) {
        try {
            IEntityType entityType = entityTypeView.getEntityType();
            entityType.setName(entityTypeView.getStrName());
            for (PropertyTypeView propertyTypeView : entityTypeView.getPropertyTypeViews()) {
                IPropertyType propertyType = propertyTypeView.getPropertyType();
                propertyType.setName(propertyTypeView.getName());
                propertyType.setType(propertyTypeView.getType());
            }
            mManager.save(entityType);
            entityTypeView.setUnsaved(false);
            return true;
        } catch (EsfingeAOMException e) {
            return false;
        }
    }

    public boolean delete(EntityTypeView entityTypeView) {
        try {
            mManager.removeEntityType(entityTypeView.getEntityType());
            return true;
        } catch (EsfingeAOMException e) {
            return false;
        }
    }

    public EntityTypeView create() {
        try {
            IEntityType entityType = EntityTypeFactory.createEntityType(ENTITY_TYPE_PACKAGE, DEFAULT_NAME);
            EntityTypeView entityTypeView = new EntityTypeView(entityType);
            entityTypeView.setUnsaved(true);
            return entityTypeView;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    public boolean createPropertyType(EntityTypeView entityTypeView, String name, String typeName) {
        try {
            Class<?> type = TypeConverter.getType(typeName);
            if (type == null) return false;
            IPropertyType propertyType = PropertyTypeFactory.createPropertyType(name, type);
            PropertyTypeView propertyTypeView = new PropertyTypeView(propertyType);
            entityTypeView.getEntityType().addPropertyType(propertyType);
            entityTypeView.addPropertyTypeView(propertyTypeView);
            return true;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }

    public MutableObservableList<EntityTypeView> getEntityTypes() {
        return getEntityTypes(false);
    }

    public void deletePropertyType(EntityTypeView entityTypeView, PropertyTypeView propertyTypeView) {
        try {
            IEntityType entityType = entityTypeView.getEntityType();
            // Load entities beforehand or else deleting the property will cause an error when retrieving fresh
            // entities, see observations.txt
            deleteEntitiesProperty(entityType, propertyTypeView.getName());

            entityTypeView.removePropertyTypeView(propertyTypeView);
            entityType.removePropertyType(propertyTypeView.getPropertyType().getName());
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }

    }

    private void deleteEntitiesProperty(IEntityType entityType, String propertyName) {
        try {
            for (EntityView entityView : DataManager.getInstance().getEntities(entityType)) {
                IEntity entity = entityView.getEntity();
                entity.removeProperty(propertyName);
                mManager.save(entity);
                entityView.reload();
            }
        } catch (EsfingeAOMException e) {
            e.printStackTrace();
        }
    }

    public MutableObservableList<EntityTypeView> getEntityTypes(boolean reload) {
        if (mEntityTypeViews != null && !reload) return mEntityTypeViews;
        try {
            mEntityTypeViews = new MutableObservableList<>();
            List<IEntityType> entityTypes = mManager.loadModel();
            for (IEntityType entityType : entityTypes) {
                mEntityTypeViews.add(new EntityTypeView(entityType));
            }
            return mEntityTypeViews;
        } catch (EsfingeAOMException e) {
            AssertionError error = new AssertionError();
            error.initCause(e);
            throw error;
        }
    }
}
