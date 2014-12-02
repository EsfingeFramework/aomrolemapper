package usingAdapter;
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
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;

import org.apache.commons.beanutils.PropertyUtils;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GenerateClasses {

	// public Object generate(String name, Map<String, Object> map)
	public Object generate(IEntity entity)
			throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		createClassAndConstructor(cw);

		String className = entity.getEntityType().getName() + "BeanAdapter";
		
		//className and properties are OK !
		//System.out.println("className " + className);
		//System.out.println("Properties " + entity.getProperties().toString());
		
		for(IProperty p : entity.getProperties()){
			
			createGetter(className, cw, p.getName(), (Class<?>) p.getPropertyType().getType());
		  //createGetter(String classname, ClassWriter cw, String prop, Class<?> propertyClass)
			
			createSetter(p.getName(), cw, p.getName());
		  //createSetter(String name, ClassWriter cw, String prop) 

		}

		DynamicClassLoader cl = new DynamicClassLoader();
//		Class clazz = cl.defineClass(name, cw.toByteArray());
		Class clazz = cl.defineClass(className, cw.toByteArray());
		Object obj = clazz.newInstance();
//
//		for (String prop : map.keySet()) {
//			PropertyUtils.setProperty(obj, prop, map.get(prop));
//		}

		return obj;

	}

	protected void createClassAndConstructor(ClassWriter cw) {

		/*
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, name, null, "java/lang/Object",
				null);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
				null);

		mv.visitCode();
		// Label l0 = new Label();
		// mv.visitLabel(l0);
		// mv.visitLineNumber(3, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V",
				false);
		mv.visitInsn(RETURN);
		// Label l1 = new Label();
		// mv.visitLabel(l1);
		// mv.visitLocalVariable("this", n, null, l0, l1, 0);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	*/
		
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, "usingAdapter/FuncionarioBeanAdapter", null, "java/lang/Object", null);
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
				null);
		
	//	cw.visitSource("FuncionarioBeanAdapter.java", null);

//		{
//		fv = cw.visitField(ACC_PRIVATE, "entity", "Lorg/esfinge/aom/api/model/IEntity;", null, null);
//		fv.visitEnd();
//		}
		
		//mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/esfinge/aom/api/model/IEntity;)V", null, null);
		mv.visitCode();
//		Label l0 = new Label();
//		mv.visitLabel(l0);
//		mv.visitLineNumber(11, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
//		Label l1 = new Label();
//		mv.visitLabel(l1);
//		mv.visitLineNumber(12, l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, "usingAdapter/FuncionarioBeanAdapter", "entity", "Lorg/esfinge/aom/api/model/IEntity;");
//		Label l2 = new Label();
//		mv.visitLabel(l2);
//		mv.visitLineNumber(13, l2);
		mv.visitInsn(RETURN);
//		Label l3 = new Label();
//		mv.visitLabel(l3);
//		mv.visitLocalVariable("this", "LusingAdapter/FuncionarioBeanAdapter;", null, l0, l3, 0);
//		mv.visitLocalVariable("entity", "Lorg/esfinge/aom/api/model/IEntity;", null, l0, l3, 1);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}

	private void createSetter(String name, ClassWriter cw, String prop) {
		/*
		String propM = prop.substring(0, 1) + prop.substring(1);
		MethodVisitor mv = mv = cw.visitMethod(ACC_PUBLIC, "get" + propM,
				"()Ljava/lang/Object;", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, prop, "Ljava/lang/Object;");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
		*/
		
		String propM = prop.substring(0, 1) + prop.substring(1);
		
		MethodVisitor mv = mv = cw.visitMethod(ACC_PUBLIC, "get" + propM,
				"()Ljava/lang/Object;", null, null);
		
		//mv = cw.visitMethod(ACC_PUBLIC, "setAge", "(Ljava/lang/Integer;)V", null, null);
		mv.visitCode();
//		Label l0 = new Label();
//		Label l1 = new Label();
//		Label l2 = new Label();
//		mv.visitTryCatchBlock(l0, l1, l2, "org/esfinge/aom/exceptions/EsfingeAOMException");
//		mv.visitLabel(l0);
//		mv.visitLineNumber(25, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity", "Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn("age");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/esfinge/aom/api/model/IEntity", "setProperty", "(Ljava/lang/String;Ljava/lang/Object;)V", true);
//		mv.visitLabel(l1);
//		mv.visitLineNumber(26, l1);
//		Label l3 = new Label();
//		mv.visitJumpInsn(GOTO, l3);
//		mv.visitLabel(l2);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"org/esfinge/aom/exceptions/EsfingeAOMException"});
		mv.visitVarInsn(ASTORE, 2);
//		Label l4 = new Label();
//		mv.visitLabel(l4);
//		mv.visitLineNumber(27, l4);
		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);
//		mv.visitLabel(l3);
//		mv.visitLineNumber(29, l3);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitInsn(RETURN);
//		Label l5 = new Label();
//		mv.visitLabel(l5);
//		mv.visitLocalVariable("this", "LusingAdapter/FuncionarioBeanAdapter;", null, l0, l5, 0);
//		mv.visitLocalVariable("a", "Ljava/lang/Integer;", null, l0, l5, 1);
//		mv.visitLocalVariable("e", "Lorg/esfinge/aom/exceptions/EsfingeAOMException;", null, l4, l3, 2);
		mv.visitMaxs(3, 3);
		mv.visitEnd();
		
		
	}

	private void createGetter(String classname, ClassWriter cw, String prop, Class<?> propertyClass) {

		
		String propertyname = prop.substring(0, 1).toUpperCase() + prop.substring(1);
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get"+propertyname,
				"()Ljava/lang/Integer;", null, null);
		//trocar integer pelo class
		
	//	mv = cw.visitMethod(ACC_PUBLIC, "getAge", "()Ljava/lang/Integer;", null, null);
		mv.visitCode();
//		Label l0 = new Label();
//		Label l1 = new Label();
//		Label l2 = new Label();
//		mv.visitTryCatchBlock(l0, l1, l2, "org/esfinge/aom/exceptions/EsfingeAOMException");
//		mv.visitLabel(l0);
//		mv.visitLineNumber(17, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, classname, "entity", "Lorg/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn("age");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/esfinge/aom/api/model/IEntity", "getProperty", "(Ljava/lang/String;)Lorg/esfinge/aom/api/model/IProperty;", true);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/esfinge/aom/api/model/IProperty", "getValue", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
//		mv.visitLabel(l1);
		mv.visitInsn(ARETURN);
//		mv.visitLabel(l2);
//		mv.visitLineNumber(18, l2);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"org/esfinge/aom/exceptions/EsfingeAOMException"});
		mv.visitVarInsn(ASTORE, 1);
//		Label l3 = new Label();
//		mv.visitLabel(l3);
//		mv.visitLineNumber(19, l3);
		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);
//		Label l4 = new Label();
//		mv.visitLabel(l4);
//		mv.visitLocalVariable("this", "LusingAdapter/FuncionarioBeanAdapter;", null, l0, l4, 0);
//		mv.visitLocalVariable("e", "Lorg/esfinge/aom/exceptions/EsfingeAOMException;", null, l3, l4, 1);
		mv.visitMaxs(3, 2);
		mv.visitEnd();
		
		
		
		
	}
}
