package br.inpe.dga.factory;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import br.inpe.dga.utils.DynamicClassLoader;
import br.inpe.dga.utils.ObjectPrinter;
import br.inpe.dga.utils.PropertiesReaderJsonPattern;

public class AdapterFactory {
	private static HashMap<String, Class> storedClasses = new HashMap<>();
	private static AdapterFactory adapterFactory;
	private final String metadataFileName;
	
	public static AdapterFactory getInstance(String annotationMapFileName){
		if(adapterFactory == null){
			adapterFactory = new AdapterFactory(annotationMapFileName);			
		}
		return adapterFactory;
	}
	
	private AdapterFactory(String annotationMapFileName) {
		storedClasses.clear();
		metadataFileName = annotationMapFileName;
	}
	
	public Object generate(IEntity entity) throws Exception {		
		if (!existInStoredClasses(entity)) {
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			PropertiesReaderJsonPattern pr = PropertiesReaderJsonPattern.getInstance(metadataFileName);
			IEntityType entityType = entity.getEntityType();
			String name = entityType.getName() + "AOMBeanAdapter";
			ClassConstructor.createClassAndConstructor(name, cw);
			ClassConstructor.createPrivateAttribute(null, cw);
			
			//Verifica proprietades do entityType, busca propriedades no arquivo e anota a classe
			for(IProperty metadataEntity : entityType.getProperties()){
				String annotationClassPath = pr.readProperty(metadataEntity.getName());
				if(annotationClassPath != null){
					ClassConstructor.createAnnotationClass(annotationClassPath, cw);
				}
			}
			
			for (IProperty p : entity.getProperties()) {
				List<String> annotationClassPaths = new ArrayList<String>();
				Map<String, Object> annotationParameters = new HashMap<String, Object>();
				//Verifica propriedades da propertyType, busca propiedades no arquivo, e anota o get
				for(IProperty metadataPropertyType : entityType.getPropertyType(p.getName()).getProperties()){
					String annotationClassPath = pr.readProperty(metadataPropertyType.getName());					
						if(annotationClassPath != null && annotationClassPath.length() != 0){
							annotationClassPaths.add(annotationClassPath);
							//Considera somente um parâmetro por anotação
							String[] metadataParameters = pr.readPropertyParameters(metadataPropertyType.getName());
							if(metadataParameters!= null){
								for(String metadataParameter : metadataParameters){
									Map<String, Object> parameters = null;
									try{
										parameters = (Map<String, Object>)metadataPropertyType.getValue();
									}catch(ClassCastException e){
										annotationParameters.put(metadataParameter, metadataPropertyType.getValue());
										break;
									}									
									annotationParameters.put(metadataParameter, parameters.get(metadataParameter));
								}
							}
							//annotationParameters.put(pr.readPropertyParameter(metadataPropertyType.getName()), metadataPropertyType.getValue());
						}
				}
				
				if(IEntityType.class.isAssignableFrom(p.getPropertyType().getType().getClass())){					
					String propertyType = IEntityType.class.toString();
					ClassConstructor.createComplexPropertyGetter(name, cw, p.getName(), propertyType, annotationClassPaths, annotationParameters);				
				} else if (!p.getPropertyType().getType().toString().substring(6)
						.equals("java.lang.Object")) {
					String propertyType = p.getPropertyType().getType()
							.toString().substring(6);
					ClassConstructor.createGetterWithSpecificProperty(name, cw, p.getName(), propertyType, annotationClassPaths, annotationParameters);
				}else{
					ClassConstructor.createGetter(name, cw, p.getName(), annotationClassPaths, annotationParameters);
				}
			}

			DynamicClassLoader cl = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
			
			Class<?> clazz = cl.defineClass(name, cw.toByteArray());
			addStoredClass(entity, clazz);			
			return clazz.getConstructor(IEntity.class).newInstance(entity);
		}

		return getInstanceFromStoredClasses(entity);
	}
	

	private Class addStoredClass(IEntity entity, Class clazz)
			throws EsfingeAOMException {
		return storedClasses.put(entity.getEntityType().getName(), clazz);
	}

	private Object getInstanceFromStoredClasses(IEntity entity)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, EsfingeAOMException {
		Class clazz = getStoredClass(entity);
		return clazz.getConstructor(IEntity.class).newInstance(entity);
	}

	private boolean existInStoredClasses(IEntity entity)
			throws EsfingeAOMException {
		return storedClasses.containsKey(entity.getEntityType().getName());
	}

	private Class getStoredClass(IEntity entity) throws EsfingeAOMException {
		Class clazz;
		clazz = storedClasses.get(entity.getEntityType().getName());
		return clazz;
	}
	
	public Object generate(IEntity entity, boolean forceClassGeneration) throws Exception {	
		Object obj = null;
		
		if(forceClassGeneration){
			storedClasses.remove(entity.getEntityType().getName()); 
		}
		return generate(entity);
	}
	
	private void createPrivateAttribute(String property, ClassWriter cw) {
		FieldVisitor fv = cw.visitField(ACC_PRIVATE, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;", null, null);
		fv.visitEnd();
	}

	protected void createClassAndConstructor(String name, ClassWriter cw) {

		cw.visit(52, ACC_PUBLIC + ACC_SUPER, name, null, "java/lang/Object",
				null);

		//cw.visitSource("TemplateAdapter.java", null);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>",
				"(Lorg/esfinge/aom/api/model/IEntity;)V", null, null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V",
				false);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, name, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;");

		mv.visitInsn(RETURN);

		mv.visitMaxs(2, 2);
		mv.visitEnd();
		
		// ------------------------------Empty Constructor
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V",
				false);

		mv.visitInsn(RETURN);

		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private void createGetter(String name, ClassWriter cw, String prop) {

		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
				"()Ljava/lang/Object;", null, null);

		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"org/esfinge/aom/api/model/IEntity", "getProperty",
				"(Ljava/lang/String;)Lorg/esfinge/aom/api/model/IProperty;",
				true);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"org/esfinge/aom/api/model/IProperty", "getValue",
				"()Ljava/lang/Object;", true);

		mv.visitInsn(ARETURN);

		mv.visitVarInsn(ASTORE, 1);

		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException",
				"<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);

		// mv.visitMaxs(3, 2);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	private void createSetter(String name, ClassWriter cw, String prop) {

		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set" + propCaptalized,
				"(Ljava/lang/Object;)V", null, null);
		mv.visitCode();

		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2,
				"org/esfinge/aom/exceptions/EsfingeAOMException");
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"org/esfinge/aom/api/model/IEntity", "setProperty",
				"(Ljava/lang/String;Ljava/lang/Object;)V", true);
		mv.visitLabel(l1);
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, 2);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException",
				"<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l3);
		mv.visitInsn(RETURN);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "L" + name + ";", null, l0, l5, 0);
		mv.visitLocalVariable("a", "Ljava/lang/Object;", null, l0, l5, 1);
		mv.visitLocalVariable("e",
				"Lorg/esfinge/aom/exceptions/EsfingeAOMException;", null, l4,
				l3, 2);
		mv.visitMaxs(3, 3);
		mv.visitEnd();

	}

	private void createGetterWithSpecificProperty(String name, ClassWriter cw,
			String prop, String propertyType) {

		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		String specificPropType = ("()L" + propertyType + ";")
				.replace(".", "/");

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
				specificPropType, null, null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"org/esfinge/aom/api/model/IEntity", "getProperty",
				"(Ljava/lang/String;)Lorg/esfinge/aom/api/model/IProperty;",
				true);

		mv.visitMethodInsn(INVOKEINTERFACE,
				"org/esfinge/aom/api/model/IProperty", "getValue",
				"()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, propertyType.replace(".", "/"));

		mv.visitInsn(ARETURN);

		mv.visitVarInsn(ASTORE, 1);

		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException",
				"<init>", "(Ljava/lang/Throwable;)V", false);

		mv.visitInsn(ATHROW);

		mv.visitMaxs(3, 2);
		mv.visitEnd();

	}

	private void createSetterWithSpecificProperty(String name, ClassWriter cw,
			String prop, String propertyType) {

		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		String specificPropType = "(L" + propertyType.replace(".", "/") + ";)V";

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set" + propCaptalized,
				specificPropType, null, null);
		// "(Ljava/lang/Integer;)V"
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2,
				"org/esfinge/aom/exceptions/EsfingeAOMException");
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"org/esfinge/aom/api/model/IEntity", "setProperty",
				"(Ljava/lang/String;Ljava/lang/Object;)V", true);
		mv.visitLabel(l1);
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, 2);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException",
				"<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l3);
		mv.visitInsn(RETURN);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "L" + name + ";", null, l0, l5, 0);
		mv.visitLocalVariable("a", "L" + propertyType.replace(".", "/") + ";",
				null, l0, l5, 1);
		mv.visitLocalVariable("e",
				"Lorg/esfinge/aom/exceptions/EsfingeAOMException;", null, l4,
				l3, 2);
		mv.visitMaxs(3, 3);
		mv.visitEnd();
	}
	
	
	private void createComplexPropertyGetter(String name, ClassWriter cw,
			String propName, String propertyType) {
			
		String propCaptalized = propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		propertyType = propertyType.replace("interface", "");
		propertyType = propertyType.replaceAll(" ", "");
		String specificPropType = "()L" + propertyType.replace(".", "/") + ";";
		
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
				specificPropType, null, null);
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
		mv.visitLabel(l0);
		mv.visitLineNumber(17, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity", "Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(propName);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/esfinge/aom/api/model/IEntity", "getProperty", "(Ljava/lang/String;)Lorg/esfinge/aom/api/model/IProperty;", true);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/esfinge/aom/api/model/IProperty", "getValue", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "org/esfinge/aom/api/model/IEntity");
		mv.visitVarInsn(ASTORE, 1);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(18, l3);
		mv.visitMethodInsn(INVOKESTATIC, "br/inpe/dga/factory/AdapterFactory", "getInstance", "()Lbr/inpe/dga/factory/AdapterFactory;", false);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "br/inpe/dga/factory/AdapterFactory", "generate", "(Lorg/esfinge/aom/api/model/IEntity;)Ljava/lang/Object;", false);
		mv.visitLabel(l1);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l2);
		mv.visitLineNumber(19, l2);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
		mv.visitVarInsn(ASTORE, 1);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(20, l4);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLineNumber(21, l5);
		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLocalVariable("this", "Lbr/inpe/dga/adapter/TemplateAdapter;", null, l0, l6, 0);
		mv.visitLocalVariable("entityProp", "Lorg/esfinge/aom/api/model/IEntity;", null, l3, l2, 1);
		mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l4, l6, 1);
		mv.visitMaxs(3, 2);
		mv.visitEnd();
	}
}
