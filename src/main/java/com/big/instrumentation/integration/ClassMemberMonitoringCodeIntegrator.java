package com.big.instrumentation.integration;

import com.big.instrumentation.util.InstrumentationUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import java.lang.reflect.Field;

/**
 * Provided implementation of {@link BaseCodeIntegrator} for monitoring the
 * values of a class' instance variables (e.g. injected beans in Java EE/Spring
 * environment). <br/>
 *
 * Created by patrick.kleindienst on 11.06.2015.
 * 
 * @author patrick.kleindienst
 */
public class ClassMemberMonitoringCodeIntegrator extends BaseCodeIntegrator {

	// #####################################################
	// # INSTANCE METHODS #
	// #####################################################

	/**
	 * Invoke member logging builder method and add code to
	 * <code>ctMethod</code>.
	 * 
	 * @param ctMethod
	 *            method to be changed
	 * @return the customized {@link CtMethod} object
	 */
	@Override
	protected CtMethod enhanceMethodCode(CtMethod ctMethod) {
		try {
			ctMethod.insertAfter(buildLoggingStatement(ctMethod.getDeclaringClass()));
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
		return ctMethod;
	}

	/**
	 * This utility method uses a {@link StringBuilder} instance to create
	 * logging statements which provide information about the class members and
	 * their current values.
	 *
	 * @param ctClass
	 *            class whose members should be logged
	 * @return the final logging statement
	 */
	private String buildLoggingStatement(CtClass ctClass) {
		StringBuilder builder = new StringBuilder();
		Class loadedClass = InstrumentationUtils.getLoadedClassByName(ctClass.getName());
		for (Field field : loadedClass.getDeclaredFields()) {
			builder.append(PROVIDED_LOGGER + ".debug(\"Found class member '" + field.getName() + "' of type " + field.getType().getName() + " with value: \"  + " + field.getName() + ");");
		}
		return builder.toString();
	}
}
