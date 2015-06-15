package com.big.instrumentation.jmx;

import com.big.instrumentation.transform.BaseTransformer;
import java.util.List;

/**
 * An interface defining {@link JmxInstrumentationService} as a MBean and making
 * it accessible through Java Management Extensions.<br/>
 * 
 * Created by patrick.kleindienst on 05.06.2015.
 * 
 * @author patrick.kleindienst
 * 
 */
public interface JmxInstrumentationServiceMBean {

	// #####################################################
	// # CONCERN: BASE PACKAGE #
	// #####################################################

	/**
	 * Defines the base package of classes that may be involved in further
	 * operations.
	 * 
	 * @param basePackage
	 *            name of the package that should serve as base package
	 */
	void setBasePackage(String basePackage);

	/**
	 * Returns the current base package settings.
	 * 
	 * @return current base package
	 */
	String getBasePackage();

	// #####################################################
	// # CONCERN: CLASS & METHOD INFO #
	// #####################################################

	/**
	 * Creates a list of all the classes specified under the current base
	 * package.
	 *
	 * @return loaded classes residing in specified base package
	 */
	List<String> listClassNamesByBasePackage();

	/**
	 * Creates a list of all the methods residing in a specific class.
	 *
	 * @param className
	 *            name of the class for which method names shall be displayed
	 * @return {@link List} of method names
	 */
	List<String> listMethodNamesForClass(String className);

	/**
	 * Provides a list of all existing signatures for a method inside of a
	 * specific class.
	 *
	 * @param className
	 *            name of the class the specified method is residing in
	 * @param methodName
	 *            name of method whose existing signatures shall be displayed
	 * @return {@link List} containing all existing signatures.
	 */
	List<String> listSignaturesForMethod(String className, String methodName);

	// #####################################################
	// # CONCERN: INSTRUMENTATION #
	// #####################################################

	/**
	 * Performs an instrumentation and applies bytecode changes with respect to
	 * the transformation settings.
	 *
	 * @param className
	 *            name of the class the instrumentation should be applied to
	 * @param methodName
	 *            name of the method the instrumentation should be applied to
	 * @param methodSignature
	 *            signature of the method the instrumentation should be applied
	 *            to (in case of overloaded methods)
	 */
	void instrumentMethod(String className, String methodName, String methodSignature);

	/**
	 * Performs an instrumentation and applies bytecode changes with respect to
	 * the transformation settings. Since method signatures are disregarded in
	 * this case all matching methods are instrumented in case of overloading.
	 * 
	 * @param className
	 *            name of the class the instrumentation should be applied to
	 * @param methodName
	 *            name of the method the instrumentation should be applied to
	 */
	void instrumentMethod(String className, String methodName);

	/**
	 * Resets the loaded class specified by the <code>className</code> parameter
	 * to it's original state.
	 *
	 * @param className
	 *            name of the class whose defaults should be restored.
	 */
	void resetClassTransformation(String className);

	// #####################################################
	// # CONCERN: AVAILABLE TRANSFORMATIONS & SELECTION #
	// #####################################################

	/**
	 * Iterate through all loaded classes and search for subclasses of
	 * {@link BaseTransformer}. The base class itself is ignored.
	 *
	 * @return list of already loaded subclasses of class
	 *         {@link BaseTransformer}
	 */
	List<String> listAvailableTransformations();

	/**
	 * Registers the selected {@link BaseTransformer} implementation for further
	 * instrumentation operations.
	 * 
	 * @param index
	 *            Index of a {@link BaseTransformer} that should be applied
	 */
	void selectTransformer(int index);

}
