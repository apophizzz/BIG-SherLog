package com.big.sherlog.scan;

import org.objectweb.asm.*;

/**
 * This implementation of {@link ClassVisitor} checks if a currently visited
 * class is annotated with
 * {@link com.big.sherlog.annotation.Transformer}. The main advantage of
 * using the <a href="http://asm.ow2.org/">ASM library</a> is that a class does
 * not have to be loaded for our annotation analysis. The idea to use
 * {@link ClassVisitor} is inspired by Arno Haase's JAXenter article <a href=
 * "https://jaxenter.de/classpath-scan-im-eigenbau-aus-der-java-trickkiste-12963"
 * >Classpath-Scan im Eigenbau [Aus der Java-Trickkiste]</a>.<br/>
 * 
 * Created by patrick.kleindienst on 11.06.2015.
 * 
 * @author patrick.kleindienst
 */
public class TransformerClassVisitor extends ClassVisitor
{



	// #####################################################
	// # STATIC MEMBERS #
	// #####################################################

	private static final String	TRANSFORMER_ANNOTATION	= "Lcom/big/sherlog/annotation/Transformer;";

	// #####################################################
	// # INSTANCE MEMBERS #
	// #####################################################

	private String				className;

	public TransformerClassVisitor(int i)
	{
		super(i);
	}

	public TransformerClassVisitor(int i, ClassVisitor classVisitor)
	{
		super(i, classVisitor);
	}

	// #####################################################
	// # INSTANCE METHODS #
	// #####################################################

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		className = name.replace("/", ".");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public void visitSource(String s, String s1) {
		return;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public void visitOuterClass(String s, String s1, String s2) {
		return;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Checks if the currently visited class has annotation
	 * {@link com.big.sherlog.annotation.Transformer}.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public void visitAttribute(Attribute attribute) {
		return;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public void visitInnerClass(String s, String s1, String s2, int i) {
		return;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method is not needed for our purposes.
	 * </p>
	 */
	@Override
	public void visitEnd() {
		return;
	}
}
