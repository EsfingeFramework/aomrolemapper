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
import java.util.HashMap;

import org.esfinge.aom.api.model.HasProperties;
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

public class AdapterTypesFactory {

	private static HashMap<String, Class> storedClasses = new HashMap<>();
	private static AdapterTypesFactory adapterTypeFactory;
	
	public static AdapterTypesFactory getInstance(){
		if(adapterTypeFactory == null){
			adapterTypeFactory = new AdapterTypesFactory();			
		}
		
		return adapterTypeFactory;
	}
	
	private AdapterTypesFactory() {
		storedClasses.clear();
	}
	
	public Object generate(IEntity entity, String propertiesFileName) throws Exception {		
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			IEntityType entityProperty = entity.getEntityType();
			
			String name = entityProperty.getName() + "AOMBeanAdapter";
			ClassConstructor.createClassAndConstructor(name, cw);
			
			if(propertiesFileName != null){
				PropertiesReaderJsonPattern pr = PropertiesReaderJsonPattern.getInstance(propertiesFileName);
				for(IProperty property : entityProperty.getProperties()){
					String annotationClassPath = pr.readProperty(property.getName());
					if(annotationClassPath != null){
						ClassConstructor.createAnnotationClass(annotationClassPath, cw);
					}
				}
			}
			
			for (IProperty p : entity.getProperties()) {
				ClassConstructor.createPrivateAttribute(p.getName(), cw);

				if (!p.getPropertyType().getType().toString().substring(6)
						.equals("java.lang.Object")) {
					String propertyType = p.getPropertyType().getType()
							.toString().substring(6);
					//ClassConstructor.createGetterWithSpecificProperty(name, cw, p.getName(),
						//	propertyType, null);
				}else{
					//ClassConstructor.createGetter(name, cw, p.getName(), null);
				}
			}

			DynamicClassLoader cl = new DynamicClassLoader();
			
			Class<?> clazz = cl.defineClass(name, cw.toByteArray());
			return clazz.getConstructor(HasProperties.class).newInstance(entityProperty);
	}	
	
	public void createComplexPropertyGetter(String name, ClassWriter cw,
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