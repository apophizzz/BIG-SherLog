package com.big.sherlog.transform;

import com.big.sherlog.integration.BaseCodeIntegrator;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * This class provides a base implementation of the {@link ClassFileTransformer}
 * interface. The {@link BaseTransformer#transform} method defines a callback,
 * invoked by the {@link java.lang.instrument.Instrumentation} object if an
 * implementation of {@link BaseTransformer} was registered.<br/>
 * 
 * 
 * Created by patrick.kleindienst on 02.06.2015.
 * 
 * @author patrick.kleindienst
 * 
 */
public abstract class BaseTransformer implements ClassFileTransformer {

	// #####################################################
	// # INSTANCE MEMBERS #
	// #####################################################

	private ClassLoader			classLoader;
	private String				className;
	private String				methodName;
	private String				methodSignature;
	private BaseCodeIntegrator	codeIntegrator;

	// #####################################################
	// # CONSTRUCTORS #
	// #####################################################

	public BaseTransformer(ClassLoader classLoader, String className, String methodName, String methodSignature, BaseCodeIntegrator codeIntegrator) {
		this.classLoader = classLoader;
		this.className = className.replace("/", ".");
		this.methodName = methodName;
		this.methodSignature = methodSignature;
		this.codeIntegrator = codeIntegrator;
	}

	public BaseTransformer(BaseCodeIntegrator codeIntegrator) {
		this.codeIntegrator = codeIntegrator;
	}

	public BaseTransformer() {

	}

	// #####################################################
	// # INSTANCE METHODS #
	// #####################################################

	/**
	 * This method is an implementation of the {@link ClassFileTransformer}
	 * contract. After assuring that the class given as a parameter is really
	 * the class we want to transform (identified by class name and class
	 * loader), the task is delegated to the {@link BaseCodeIntegrator}
	 * implementation.
	 *
	 * @param loader
	 *            class loader of the class provided for transformation
	 * @param classname
	 *            name of the class provided for transformation
	 * @param classBeingRedefined
	 *            class object of the class provided for transformation (not
	 *            used)
	 * @param protectionDomain
	 *            protection domain of the class (not used)
	 * @param classfileBuffer
	 *            byte array representing the content of the .class-file that
	 *            belongs to the class we want to transform
	 * @return
	 * @throws IllegalClassFormatException
	 */
	@Override
	public byte[] transform(ClassLoader loader, String classname, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		classname = classname.replace("/", ".");
		if (classname.equals(this.className) && loader.equals(this.classLoader)) {
			return codeIntegrator.performCodeIntegration(className, methodName, methodSignature, classLoader, classfileBuffer);
		}
		return classfileBuffer;
	}

	// #####################################################
	// # GETTER & SETTER #
	// #####################################################

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className.replace("/", ".");
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}

	public BaseCodeIntegrator getCodeIntegrator() {
		return codeIntegrator;
	}

	public void setCodeIntegrator(BaseCodeIntegrator codeIntegrator) {
		this.codeIntegrator = codeIntegrator;
	}
}
