package br.com.bernardorufino.esfinge.genexample.test;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;
import org.esfinge.aom.model.factories.EntityTypeFactory;
import org.esfinge.aom.model.factories.PropertyTypeFactory;

import java.util.List;

public class Client {

    private static ModelManager sManager = ModelManager.getInstance();

    public static void main(String[] args) throws EsfingeAOMException, ClassNotFoundException {
//        createEntityType("Editor", "name", String.class, "department", String.class, "accessLevel", Integer.class);
//        createEntity("Editor", "name", "Joao", "department", "Staff", "accessLevel", 20);
//        createEntity("Editor", "name", "Maria", "department", "Funcs", "accessLevel", 2);
//        createEntity("Editor", "name", "Rob", "department", "Funcs", "accessLevel", 2);
//        createEntity("Editor", "name", "Smith", "department", "Staff", "accessLevel", 30);
//        int mode = 2;
//        if (mode == 0) {
//            createEntityType("Editor", "name", String.class, "department", String.class, "accessLevel", Integer.class);
//            createEntity("Editor", "name", "Fernando", "department", "Sales", "accessLevel", 10);
//        } else if (mode == 1) {
//            IEntityType editor = sManager.getEntityType("br.com.bernardorufino.esfinge.genexample.model", "Editor");
//            editor.removePropertyType("name");
//            sManager.save(editor);
//            readEntities("Editor");
//        } else {
//            IEntityType editor = sManager.getEntityType("br.com.bernardorufino.esfinge.genexample.model", "Editor");
//            editor.addPropertyType(PropertyTypeFactory.createPropertyType("name", String.class));
//            sManager.removeEntityType(editor);
//        }
        readEntities("Editores");
        addProperryType("Editores", "bolhado", Integer.class);
        readEntities("Editores");
    }

    public static void createEntityType(String name, Object... params) throws EsfingeAOMException {
        IEntityType entityType = EntityTypeFactory.createEntityType("br.com.bernardorufino.esfinge.genexample.model",
                                                                    name);
        for (int i = 0, n = params.length; i < n; i += 2) {
            String propertyName = (String) params[i];
            Class<?> clazz = (Class<?>) params[i + 1];
            IPropertyType propertyType = PropertyTypeFactory.createPropertyType(propertyName, clazz);
            entityType.addPropertyType(propertyType);
        }
        sManager.save(entityType);
    }

    public static void addProperryType(String typeName, String propertyName,
                                       Class<?> clazz) throws EsfingeAOMException {
        IEntityType type = sManager.getEntityType("br.com.bernardorufino.esfinge.genexample.model", typeName);
        IPropertyType propertyType = PropertyTypeFactory.createPropertyType(propertyName, clazz);
        type.addPropertyType(propertyType);
        sManager.save(type);
    }

    public static void createEntity(String typeName, Object... properties) throws EsfingeAOMException {
        IEntityType type = sManager.getEntityType("br.com.bernardorufino.esfinge.genexample.model", typeName);
        IEntity entity = type.createNewEntity();
        for (int i = 0, n = properties.length; i < n; i += 2) {
            String propertyName = (String) properties[i];
            Object value = properties[i + 1];
            entity.setProperty(propertyName, value);
        }
        sManager.save(entity);
    }

    public static void readEntities(String name) throws EsfingeAOMException {
        IEntityType editor = sManager.getEntityType("br.com.bernardorufino.esfinge.genexample.model", name);
        sManager.loadModel();
        List<IEntity> entities = sManager.getEntitiesForType(editor);
        System.out.println("Total of " + entities.size() + " entities of type " + name);
        int i = 1;
        for (IEntity entity : entities) {
            System.out.println("-- #" + i++);
            for (IProperty property : entity.getProperties()) {
                System.out.println("---- " + property.getPropertyType().getName() + ": " + property.getValue());
            }
        }
    }

    public static void readEntityTypes() throws EsfingeAOMException {
        List<IEntityType> entityTypes = sManager.loadModel();
        System.out.println("total of " + entityTypes.size() + " entityTypes");
        for (IEntityType entityType : entityTypes) {
            System.out.println("-- " + entityType.getName());
            for (IPropertyType propertyType : entityType.getPropertyTypes()) {
                System.out.println("---- " + propertyType.getName() + ": " + propertyType.getType());
            }
        }
    }

}
