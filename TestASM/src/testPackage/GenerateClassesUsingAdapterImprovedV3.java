package testPackage;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.HashMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class GenerateClassesUsingAdapterImprovedV3 {

	private static HashMap<String, Class> storedClasses = new HashMap<>();
	
	
	public GenerateClassesUsingAdapterImprovedV3() {
		storedClasses.clear();
	}
	
	public Object generate(IEntity entity)
			throws Exception {

		Class clazz = null;
				
		if (storedClasses.containsKey(entity.getEntityType().getName())) {
			clazz = storedClasses.get(entity.getEntityType().getName());
		} else {

			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

			String name = entity.getEntityType().getName() + "AOMBeanAdapter";

			createPrivateAttribute(null, cw);
			createClassAndConstructor(name, cw);

			for (IProperty p : entity.getProperties()) {
				Class<?> type = Object.class;
				if (p.getPropertyType() != null) {
					type = (Class<?>) p.getPropertyType().getType();
					
				}

				if (!p.getPropertyType().getType().toString().substring(6)
						.equals("java.lang.Object")) {

					String propertyType = p.getPropertyType().getType()
							.toString().substring(6);

					createGetterWithSpecificProperty(name, cw, p.getName(),
							propertyType);

					createSetterWithSpecificProperty(name, cw, p.getName(),
							propertyType);

				} else {

					createGetter(name, cw, p.getName());

					createSetter(name, cw, p.getName());
				}
			}

			DynamicClassLoader cl = new DynamicClassLoader();
			clazz = cl.defineClass(name, cw.toByteArray());
			storedClasses.put(entity.getEntityType().getName(), clazz);
		}

		Object obj = clazz.getConstructor(IEntity.class).newInstance(entity);
		return obj;

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

		cw.visitSource("TestClassBeanAdapter.java", null);

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

}
