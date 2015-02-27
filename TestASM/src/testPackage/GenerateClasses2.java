package testPackage;

import org.esfinge.aom.api.model.IEntity;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

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

public class GenerateClasses2 {

	public Object generate(String name, Integer number, IEntity entity)
			throws Exception {

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		createClassAndConstructor(name, cw, number);

		// for (IProperty p : entity.getProperties()) {
		// Class<?> type = Object.class;
		// if (p.getPropertyType() != null) {
		// type = (Class<?>) p.getPropertyType().getType();
		// }

		createPrivateAttribute("otherNumber", cw);

		createGetter(name, cw, "atl");
		// createGetter(String classname, ClassWriter cw, String prop)

		createSetter(name, cw, "bla");
		// createSetter(String name, ClassWriter cw, String prop)
		// }

		DynamicClassLoader cl = new DynamicClassLoader();
		Class clazz = cl.defineClass(name, cw.toByteArray());
		Object obj = clazz.newInstance();

		return obj;

	}

	protected void createClassAndConstructor(String name, ClassWriter cw,
			Integer number) {

		cw.visit(52, ACC_PUBLIC + ACC_SUPER, name, null, "java/lang/Object",
				null);

		// Foi feita a criacao do atributo private em um metodo separado
		// {
		// FieldVisitor fv = cw.visitField(ACC_PRIVATE, "number",
		// "Ljava/lang/Integer;",
		// null, null);
		// fv.visitEnd();
		// }

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
				null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V",
				false);

		mv.visitInsn(RETURN);

		mv.visitMaxs(1, 1);
		mv.visitEnd();

	}

	private void createPrivateAttribute(String property, ClassWriter cw) {

		FieldVisitor fv = cw.visitField(ACC_PRIVATE, property,
				"Ljava/lang/Integer;", null, null);
		fv.visitEnd();

	}

	private void createGetter(String name, ClassWriter cw, String prop) {

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getNumber",
				"()Ljava/lang/Integer;", null, null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "number", "Ljava/lang/Integer;");
		mv.visitInsn(ARETURN);

		mv.visitMaxs(1, 1);
		mv.visitEnd();

	}

	private void createSetter(String name, ClassWriter cw, String prop) {

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "setNumber",
				"(Ljava/lang/Integer;)V", null, null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, name, "number", "Ljava/lang/Integer;");

		mv.visitInsn(RETURN);

		mv.visitMaxs(2, 2);
		mv.visitEnd();

	}

}
