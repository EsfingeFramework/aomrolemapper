package net.sf.esfinge.aom.model.dynamic.utils;

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
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassConstructor{	
	private static String adapterFactoryPath = "net/sf/esfinge/aom/model/dynamic/factory/";
	
	private static void putAnnotationParameters(
			Map<String, Object> annotationParameters, AnnotationVisitor av) {
		Iterator iterator = annotationParameters.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Object> mapEntry = (Map.Entry) iterator.next();
			String parameter = (String) mapEntry.getKey();
		    Object value = mapEntry.getValue();
		    if(parameter != null){		    	
		    	if(value instanceof Enum){
		    		
		    		if (parameter.equals("fetch")) {		    					    		
		    			av.visitEnum(parameter, "L" + value.getClass().getName().replace(".", "/") + ";", value.toString());
		    		}else{	    	
		    			AnnotationVisitor av1 = av.visitArray(parameter);
		    			av1.visitEnum(null, "L" + value.getClass().getName().replace(".", "/") + ";", value.toString());
		    			av1.visitEnd();
		    		}
		    	}else{
		    		av.visit(parameter, value);
		    	}
		    }
		}
	}
	
	public static void createAnnotationClass(String annotationClassPathName, Map<String,Object> annotationParameters, ClassWriter cw) {
		AnnotationVisitor av = cw.visitAnnotation(
				"L" + annotationClassPathName.replace(".", "/") + ";",
				true);
		if(annotationParameters.size()!=0){
			putAnnotationParameters(annotationParameters, av);
		}
		av.visitEnd();
	}
	
	public static void createAnnotationField(String annotationClassPathName, Map<String, Object> annotationParameters, MethodVisitor fv) {
		AnnotationVisitor av = fv.visitAnnotation(
				"L" + annotationClassPathName.replace(".", "/") + ";",
				true);
		if(annotationParameters.size()!=0){
			putAnnotationParameters(annotationParameters, av);
		}
		av.visitEnd();		
	}
	
	public static void createAnnotationMethod(String annotationClassPathName,  Map<String,Object> annotationParameters, MethodVisitor mv) {
		AnnotationVisitor av = mv.visitAnnotation(
				"L" + annotationClassPathName.replace(".", "/") + ";",
				true);
		if(annotationParameters.size()!=0){
			putAnnotationParameters(annotationParameters, av);
		}
		av.visitEnd();
	}
	
	public static void createPrivateAttribute(String propertyName, ClassWriter cw) {
		FieldVisitor fv = cw.visitField(ACC_PRIVATE, "entity",
				"Lnet/sf/esfinge/aom/api/model/IEntity;", null, null);
		fv.visitEnd();
	}

	public static void createClassAndConstructor(String name, ClassWriter cw) {
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, name, null, "java/lang/Object",
				null);
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>",
				"(Lnet/sf/esfinge/aom/api/model/IEntity;)V", null, null);
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V",
				false);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, name, "entity",
				"Lnet/sf/esfinge/aom/api/model/IEntity;");

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

	public static MethodVisitor createGetter(String name, ClassWriter cw, String prop) {
		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized,
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
				name,
				"hasProperty",
				"Lnet/sf/esfinge/aom/api/model/HasProperties;");
		mv.visitLdcInsn(prop);
		mv.visitMethodInsn(
				INVOKEINTERFACE,
				"net/sf/esfinge/aom/api/model/HasProperties",
				"getProperty",
				"(Ljava/lang/String;)Lnet/sf/esfinge/aom/api/model/IProperty;",
				true);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"net/sf/esfinge/aom/api/model/IProperty", "getValue",
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
		return mv;
	}
	
	public static void createSetter(String name, ClassWriter cw, String prop) {
		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);
		
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set" + propCaptalized,
				"(Ljava/lang/Object;)V", null, null);

		mv.visitCode();

		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2,
				"net/sf/esfinge/aom/exceptions/EsfingeAOMException");
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity",
				"Lnet/sf/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"net/sf/esfinge/aom/api/model/IEntity", "setProperty",
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
				"Lnet/sf/esfinge/aom/exceptions/EsfingeAOMException;", null, l4,
				l3, 2);
		mv.visitMaxs(3, 3);
		mv.visitEnd();
	}
	
	public static MethodVisitor createGetterWithSpecificProperty(String name, ClassWriter cw,
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
				"Lnet/sf/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"net/sf/esfinge/aom/api/model/IEntity", "getProperty",
				"(Ljava/lang/String;)Lnet/sf/esfinge/aom/api/model/IProperty;",
				true);

		mv.visitMethodInsn(INVOKEINTERFACE,
				"net/sf/esfinge/aom/api/model/IProperty", "getValue",
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
		return mv;
	}
	
	public static void createSetterWithSpecificProperty(String name, ClassWriter cw,
			String prop, String propertyType) {

		String propCaptalized = prop.substring(0, 1).toUpperCase()
				+ prop.substring(1);

		String specificPropType = "(L" + propertyType.replace(".", "/") + ";)V";

		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set" + propCaptalized,
				specificPropType, null, null);
		
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2,
				"net/sf/esfinge/aom/exceptions/EsfingeAOMException");
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity",
				"Lnet/sf/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(prop);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE,
				"net/sf/esfinge/aom/api/model/IEntity", "setProperty",
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
				"Lnet/sf/esfinge/aom/exceptions/EsfingeAOMException;", null, l4,
				l3, 2);
		mv.visitMaxs(3, 3);
		mv.visitEnd();
	}
	
	public static MethodVisitor createComplexPropertyGetter(String name, ClassWriter cw,
			String propName, String propertyType) {			
		String propCaptalized = propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		propertyType = propertyType.replace("interface", "");
		propertyType = propertyType.replaceAll(" ", "");
		String specificPropType = "()L" + propertyType.replace(".", "/") + ";";
		//String specificPropType = "()Ljava/lang/Object;";
		
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
		mv.visitFieldInsn(GETFIELD, name, "entity", "Lnet/sf/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(propName);
		mv.visitMethodInsn(INVOKEINTERFACE, "net/sf/esfinge/aom/api/model/IEntity", "getProperty", "(Ljava/lang/String;)Lnet/sf/esfinge/aom/api/model/IProperty;", true);
		mv.visitMethodInsn(INVOKEINTERFACE, "net/sf/esfinge/aom/api/model/IProperty", "getValue", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "net/sf/esfinge/aom/api/model/IEntity");
		mv.visitVarInsn(ASTORE, 1);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(18, l3);
		mv.visitMethodInsn(INVOKESTATIC, adapterFactoryPath + "AdapterFactory", "getInstance", "()L" + adapterFactoryPath + "AdapterFactory;", false);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, adapterFactoryPath + "AdapterFactory", "generate", "(Lnet/sf/esfinge/aom/api/model/IEntity;)Ljava/lang/Object;", false);
		mv.visitTypeInsn(CHECKCAST, propertyType.replace(".", "/"));
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
		mv.visitLocalVariable("this", "L" + name + ";", null, l0, l6, 0);
		mv.visitLocalVariable("entityProp", "Lnet/sf/esfinge/aom/api/model/IEntity;", null, l3, l2, 1);
		mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l4, l6, 1);
		mv.visitMaxs(3, 2);
		mv.visitEnd();
		return mv;
	}

	public static void createComplexPropertySetter(String name, ClassWriter cw,
			String propName, String propertyType) {
		String propCaptalized = propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		propertyType = propertyType.replace("interface", "");
		propertyType = propertyType.replaceAll(" ", "");
		String specificPropType = "(L" + propertyType.replace(".", "/") + ";)V";
		//String specificPropType = "()Ljava/lang/Object;";
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set" + propCaptalized, 
				"(Ljava/lang/Object;)V", null, null);
		//MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get" + propCaptalized, "(Ljava/lang/Object;)V", null, null);

		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
		mv.visitLabel(l0);
		mv.visitLineNumber(29, l0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitTypeInsn(CHECKCAST, "net/sf/esfinge/aom/api/model/IEntity");
		mv.visitVarInsn(ASTORE, 2);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(30, l3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, name, "entity", "Lnet/sf/esfinge/aom/api/model/IEntity;");
		mv.visitLdcInsn(propName);
		mv.visitMethodInsn(INVOKEINTERFACE, "net/sf/esfinge/aom/api/model/IEntity", "getProperty", "(Ljava/lang/String;)Lnet/sf/esfinge/aom/api/model/IProperty;", true);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(31, l4);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKEINTERFACE, "net/sf/esfinge/aom/api/model/IProperty", "setValue", "(Ljava/lang/Object;)V", true);
		mv.visitLabel(l1);
		mv.visitLineNumber(33, l1);
		Label l5 = new Label();
		mv.visitJumpInsn(GOTO, l5);
		mv.visitLabel(l2);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
		mv.visitVarInsn(ASTORE, 2);
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLineNumber(34, l6);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);
		Label l7 = new Label();
		mv.visitLabel(l7);
		mv.visitLineNumber(35, l7);
		mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V", false);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l5);
		mv.visitLineNumber(37, l5);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitInsn(RETURN);
		Label l8 = new Label();
		mv.visitLabel(l8);
		mv.visitLocalVariable("this", "L" + name + ";", null, l0, l8, 0);
		mv.visitLocalVariable("entity", "Ljava/lang/Object;", null, l0, l8, 1);
		mv.visitLocalVariable("entityProp", "Lnet/sf/esfinge/aom/api/model/IEntity;", null, l3, l1, 2);
		mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l6, l5, 2);
		mv.visitMaxs(3, 3);
		mv.visitEnd();		
	}
}