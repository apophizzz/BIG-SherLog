package com.big.sherlog.service;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import com.big.sherlog.exception.RuntimeInstrumentationException;
import com.big.sherlog.transform.BaseTransformer;

/**
 * This class is a convenience class, encapsulating an {@link Instrumentation}
 * object as well as a {@link BaseTransformer} implementation as members. It
 * provides a simple interface for transforming and restoring classes or
 * switching transformers, hiding complex tasks like adding or removing
 * transformers on the {@link Instrumentation} object.<br/>
 * 
 * Created by patrick.kleindienst on 02.06.2015.
 * 
 * @author patrick.kleindienst
 */
public class InstrumentationService {

	// #####################################################
	// # INSTANCE MEMBERS #
	// #####################################################

	private Instrumentation	instrumentation;
	private BaseTransformer	transformer;

	// #####################################################
	// # CONSTRUCTORS #
	// #####################################################

	public InstrumentationService(Instrumentation instrumentation, BaseTransformer transformer) {
		this.instrumentation = instrumentation;
		this.transformer = transformer;
	}

	// #####################################################
	// # INSTANCE METHODS #
	// #####################################################

	/**
	 * Check if class is known to the {@link Instrumentation} member and
	 * delegate to the actual {@link InstrumentationService#instrument} method.
	 * 
	 * @param className
	 *            name of the class to be transformed
	 * @param methodName
	 *            name of the method that should be affected
	 * @param methodSignature
	 *            signature of the method for further specification in case of
	 *            overloaded methods (may be <code>null</code>)
	 */
	public void doInstrumentation(String className, String methodName, String methodSignature) {
		for (Class aClass : instrumentation.getAllLoadedClasses()) {
			if (aClass.getName().equals(className)) {
				instrument(aClass, methodName, methodSignature);
				return;
			}
		}
		throw new RuntimeInstrumentationException("Couldn't find loaded class for name: " + className);
	}

	/**
	 * Reset a class definition to it's original state. The 'original state' is
	 * the state a class had after it's definition has initially been loaded
	 * from the according class file right after application startup. The
	 * re-transformation is nothing but an ordinary transformation, however
	 * using no transformer.
	 * 
	 * @param aClass
	 *            class to be restored
	 */
	public void restoreClass(Class aClass) {
		try {
			instrumentation.retransformClasses(aClass);
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convenience method for setting up a {@link BaseTransformer}
	 * implementation before being invoked by the {@link Instrumentation}
	 * object.
	 * 
	 * @param loader
	 *            class loader of a class the be transformed
	 * @param className
	 *            name of the class to be transformed
	 * @param methodName
	 *            name of the method to be transformed
	 * @param methodSignature
	 *            signature of the method to be transformed, in case of
	 *            overloaded methods (may be null)
	 */
	private void initializeTransformer(ClassLoader loader, String className, String methodName, String methodSignature) {
		if (transformer != null) {
			transformer.setClassLoader(loader);
			transformer.setClassName(className);
			transformer.setMethodName(methodName);
			transformer.setMethodSignature(methodSignature);
		} else {
			throw new RuntimeInstrumentationException("Transformer value must not be null!");
		}
	}

	/**
	 * This method induces the setup, registration and de-registration of the
	 * {@link BaseTransformer} implementation provided. Moreover, it initiates
	 * the transformation process on the {@link Instrumentation} object.
	 *
	 * @param aClass
	 *            the class to be transformed
	 * @param methodName
	 *            the method to be transformed
	 * @param methodSignature
	 *            signature information in case of overloaded methods (may be
	 *            <code>null</code>)
	 */
	private void instrument(Class aClass, String methodName, String methodSignature) {
		if (instrumentation != null) {
			initializeTransformer(aClass.getClassLoader(), aClass.getName(), methodName, methodSignature);
			instrumentation.addTransformer(transformer, true);
			try {
				instrumentation.retransformClasses(aClass);
			} catch (UnmodifiableClassException e) {
				e.printStackTrace();
			} finally {
				instrumentation.removeTransformer(transformer);
			}

		} else {
			throw new RuntimeInstrumentationException("Instrumentation value must not be null!");
		}
	}

	// #####################################################
	// # GETTERS & SETTERS #
	// #####################################################

	public Instrumentation getInstrumentation() {
		return instrumentation;
	}

	public void setInstrumentation(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
	}

	public BaseTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(BaseTransformer transformer) {
		this.transformer = transformer;
	}
}
