package com.big.instrumentation.scan;

import org.objectweb.asm.*;

/**
 * Created by patrick.kleindienst on 11.06.2015.
 */
public class TransformerClassVisitor implements ClassVisitor {

	private static final String	TRANSFORMER_ANNOTATION	= "Lcom/big/instrumentation/annotation/Transformer;";

	private String				className;

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		className = name.replace("/", ".");
	}

	@Override
	public void visitSource(String s, String s1) {

	}

	@Override
	public void visitOuterClass(String s, String s1, String s2) {

	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (desc.equals(TRANSFORMER_ANNOTATION)) {
			try {
				this.getClass().getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void visitAttribute(Attribute attribute) {

	}

	@Override
	public void visitInnerClass(String s, String s1, String s2, int i) {

	}

	@Override
	public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
		return null;
	}

	@Override
	public void visitEnd() {

	}
}
