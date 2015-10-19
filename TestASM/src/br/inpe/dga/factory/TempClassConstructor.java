package br.inpe.dga.factory;

import java.util.*;
import org.objectweb.asm.*;

public class TempClassConstructor implements Opcodes {

	public static byte[] dump() throws Exception {

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(52, ACC_PUBLIC + ACC_SUPER,
				"br/inpe/dga/adapter/TemplateEntityTypeAdapter", null,
				"java/lang/Object", null);

		//cw.visitSource("TemplateEntityTypeAdapter.java", null);

		{
			av0 = cw.visitAnnotation(
					"Lorg/esfinge/aom/model/rolemapper/metadata/annotations/Entity;",
					true);
			av0.visitEnd();
		}
		
		{
			fv = cw.visitField(ACC_PRIVATE, "entityProperty",
					"Lorg/esfinge/aom/api/model/HasProperties;", null, null);
			fv.visitEnd();
		}
		
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>",
					"(Lorg/esfinge/aom/api/model/HasProperties;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(16, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
					"()V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(17, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD,
					"br/inpe/dga/adapter/TemplateEntityTypeAdapter",
					"entityProperty",
					"Lorg/esfinge/aom/api/model/HasProperties;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(18, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this",
					"Lbr/inpe/dga/adapter/TemplateEntityTypeAdapter;", null,
					l0, l3, 0);
			mv.visitLocalVariable("entityProperty",
					"Lorg/esfinge/aom/api/model/HasProperties;", null, l0, l3,
					1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getPersistence",
					"()Ljava/lang/Object;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
			mv.visitLabel(l0);
			mv.visitLineNumber(22, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD,
					"br/inpe/dga/adapter/TemplateEntityTypeAdapter",
					"entityProperty",
					"Lorg/esfinge/aom/api/model/HasProperties;");
			mv.visitLdcInsn("mainContact");
			mv.visitMethodInsn(
					INVOKEINTERFACE,
					"org/esfinge/aom/api/model/HasProperties",
					"getProperty",
					"(Ljava/lang/String;)Lorg/esfinge/aom/api/model/IProperty;",
					true);
			mv.visitMethodInsn(INVOKEINTERFACE,
					"org/esfinge/aom/api/model/IProperty", "getValue",
					"()Ljava/lang/Object;", true);
			mv.visitVarInsn(ASTORE, 1);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(23, l3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLabel(l1);
			mv.visitInsn(ARETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(24, l2);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1,
					new Object[] { "java/lang/Exception" });
			mv.visitVarInsn(ASTORE, 1);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(25, l4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception",
					"printStackTrace", "()V", false);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(26, l5);
			mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException",
					"<init>", "(Ljava/lang/Throwable;)V", false);
			mv.visitInsn(ATHROW);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLocalVariable("this",
					"Lbr/inpe/dga/adapter/TemplateEntityTypeAdapter;", null,
					l0, l6, 0);
			mv.visitLocalVariable("valueProperty", "Ljava/lang/Object;", null,
					l3, l2, 1);
			mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l4, l6, 1);
			mv.visitMaxs(3, 2);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}