package com.big.instrumentation.service;

import com.big.instrumentation.exception.RuntimeInstrumentationException;
import com.big.instrumentation.scan.TransformerClasspathScanner;
import com.big.instrumentation.transform.BaseTransformer;
import com.big.instrumentation.util.JavassistUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Created by patrick.kleindienst on 02.06.2015.
 */
public class InstrumentationService {

	private Instrumentation	instrumentation;
	private BaseTransformer	transformer;

	public InstrumentationService(Instrumentation instrumentation, BaseTransformer transformer) {
		this.instrumentation = instrumentation;
		this.transformer = transformer;
		JavassistUtils.setInstrumentation(instrumentation);
	}

	public void doInstrumentation(String className, String methodName, String methodSignature) {
		for (Class aClass : instrumentation.getAllLoadedClasses()) {
			if (aClass.getName().equals(className)) {
				instrument(aClass, aClass.getClassLoader(), methodName, methodSignature);
				return;
			}
		}
		throw new RuntimeInstrumentationException("Couldn't find loaded class for name: " + className);
	}

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

	private void instrument(Class aClass, ClassLoader loader, String methodName, String methodSignature) {
		if (instrumentation != null) {
			initializeTransformer(loader, aClass.getName(), methodName, methodSignature);
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

	public void restoreClass(Class aClass) {
		try {
			instrumentation.retransformClasses(aClass);
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}
	}

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
