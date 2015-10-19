package br.inpe.dga.factory;

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
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassConstructor{	
	public static void createAnnotationClass(String annotationClassPathName, ClassWriter cw) {
		AnnotationVisitor av = cw.visitAnnotation(
				"L" + annotationClassPathName.replace(".", "/") + ";",
				true);
		av.visitEnd();
	}
	
	public static void createAnnotationMethod(String annotationClassPathName,  Map<String,Object> annotationParameters, MethodVisitor mv) {
		AnnotationVisitor av = mv.visitAnnotation(
				"L" + annotationClassPathName.replace(".", "/") + ";",
				true);
		if(annotationParameters.size()!=0){
			Iterator iterator = annotationParameters.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, Object> mapEntry = (Map.Entry) iterator.next();
				String parameter = (String) mapEntry.getKey();
			    Object value = mapEntry.getValue();
			    if(parameter != null){
			    	av.visit(parameter, value);
			    }
			}
		}
		av.visitEnd();
	}
	
	public static void createPrivateAttribute(String propertyName, ClassWriter cw) {
		FieldVisitor fv = cw.visitField(ACC_PRIVATE, "entity",
				"Lorg/esfinge/aom/api/model/IEntity;", null, null);
		fv.visitEnd();
	}

	public static void createClassAndConstructor(String name, ClassWriter cw) {
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

	public static void createGetter(String name, ClassWriter cw, String prop, List<String> annotationClassPaths,  Map<String,Object> annotationParameters) {
		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
				"()Ljava/lang/Object;", null, null);
		for(String annotationClassPath : annotationClassPaths){		
			createAnnotationMethod(annotationClassPath, annotationParameters, mv);
		}
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
		mv.visitLabel(l0);
		mv.visitLineNumber(22, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD,
				name,
				"hasProperty",
				"Lorg/esfinge/aom/api/model/HasProperties;");
		mv.visitLdcInsn(prop);
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
				"L"+name+";", null,
				l0, l6, 0);
		mv.visitLocalVariable("valueProperty", "Ljava/lang/Object;", null,
				l3, l2, 1);
		mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l4, l6, 1);
		mv.visitMaxs(3, 2);
		mv.visitEnd();
	}
	
	public static void createGetterWithSpecificProperty(String name, ClassWriter cw,
			String prop, String propertyType, List<String> annotationClassPaths,  Map<String,Object> annotationParameters) {
		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		String specificPropType = ("()L" + propertyType + ";")
				.replace(".", "/");
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
				specificPropType, null, null);
		
		for(String annotationClassPath : annotationClassPaths){		
			createAnnotationMethod(annotationClassPath, annotationParameters, mv);
		}
		
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
	
	public static void createComplexPropertyGetter(String name, ClassWriter cw,
			String propName, String propertyType, List<String> annotationClassPaths, Map<String,Object> annotationParameters) {			
		String propCaptalized = propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		propertyType = propertyType.replace("interface", "");
		propertyType = propertyType.replaceAll(" ", "");
		String specificPropType = "()L" + propertyType.replace(".", "/") + ";";
		
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
				specificPropType, null, null);
		for(String annotationClassPath : annotationClassPaths){		
			createAnnotationMethod(annotationClassPath, annotationParameters, mv);
		}
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