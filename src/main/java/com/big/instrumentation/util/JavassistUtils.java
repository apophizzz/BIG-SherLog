package com.big.instrumentation.util;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a utility class providing some convenience methods concerning <a
 * href="http://jboss-javassist.github.io/javassist/">Javassist</a>.<br/>
 *
 * Created by patrick.kleindienst on 02.06.2015.
 * 
 * @author patrick.kleindienst
 */
public class JavassistUtils {

	// #####################################################
	// # STATIC MEMBERS #
	// #####################################################

	private static ClassPool		classPool	= new ClassPool(true);
	private static Instrumentation	instr		= null;

	// #####################################################
	// # STATIC METHODS #
	// #####################################################

	/**
	 * Get all existing signatures for a certain <code>methodName</code>
	 * residing in a certain class.
	 * 
	 * @param fullQualifiedClassName
	 *            name of the class containing the method
	 *            <code>methodName</code>
	 * @param methodName
	 *            name of the method whose signatures should be searched
	 * @return {@link List} containing signatures of type {@link String}
	 */
	public static List<String> getSignaturesForMethod(String fullQualifiedClassName, String methodName) {
		prepareClassPool(InstrumentationUtils.getLoadedClassByName(fullQualifiedClassName));
		List<String> signatures = new ArrayList<>();
		try {
			CtClass ctClass = classPool.get(fullQualifiedClassName);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				if (ctMethod.getName().equalsIgnoreCase(methodName)) {
					signatures.add(ctMethod.getSignature());
				}
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return signatures;
	}

	/**
	 * Retrieving the bytecode of class <code>aClass</code> as byte array.
	 *
	 * @param aClass
	 *            class whose byte array representation should be returned
	 * @return byte array containing the bytecode of a <code>aClass</code>
	 */
	public static byte[] getBytecodeFromClass(Class aClass) {
		prepareClassPool(aClass);
		try {
			CtClass ctClass = classPool.get(aClass.getName());
			return ctClass.toBytecode();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Replaced by {@link JavassistUtils#prepareClassPool(Class)}, because this
	 * method does not need to step through all the loaded classes.
	 */
	// private static void prepareClassPool() {
	// if (instr != null) {
	// for (Class aClass : instr.getAllLoadedClasses()) {
	// classPool.appendClassPath(new LoaderClassPath(aClass.getClassLoader()));
	// }
	// }
	// }

	/**
	 * Utility method adding the classpath of a certain class
	 * {@link Instrumentation} to the existing {@link ClassPool}. This ensures
	 * that the the class is known to the <code>classPool</code> defined when a
	 * utility method is called with that class as parameter.
	 */
	private static void prepareClassPool(Class aClass) {
		classPool.appendClassPath(new LoaderClassPath(aClass.getClassLoader()));
	}

	/**
	 * Setup the static <code>instr</code> attribute with the
	 * {@link Instrumentation} object retrieved from JVM.
	 * 
	 * @param instrumentation
	 *            the {@link Instrumentation} object created by the JVM
	 */
	public static void setInstrumentation(Instrumentation instrumentation) {
		instr = instrumentation;
	}
}
