package com.big.instrumentation.util;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * This utility class encapsulates several convenience methods for operating on
 * the {@link Instrumentation} instance obtained from JVM.<br/>
 *
 * Created by patrick.kleindienst on 10.06.2015.
 *
 * @author patrick.kleindienst
 * 
 */
public class InstrumentationUtils {

	private static Instrumentation	instrumentation;

	/**
	 * Pass the {@link Instrumentation} object to the utility class.
	 * 
	 * @param instr
	 *            {@link Instrumentation} object to work on
	 */
	public static void setInstrumentation(Instrumentation instr) {
		instrumentation = instr;
	}

	/**
	 * Iterate through all the classes known to the {@link Instrumentation}
	 * object and search for a appropriate {@link Class} object by fully
	 * qualified name of a class.
	 * 
	 * @param className
	 *            fully qualified name of a certain class
	 * @return The {@link Class} object if a class named <code>className</code>
	 *         is known to the {@link Instrumentation} object, null otherwise.
	 */
	public static Class getLoadedClassByName(String className) {
		for (Class aClass : instrumentation.getAllLoadedClasses()) {
			if (aClass.getName().equalsIgnoreCase(className)) {
				return aClass;
			}
		}
		return null;
	}

	/**
	 * Iterate through all the classes known to the {@link Instrumentation}
	 * object and return a {@link List} of these residing in the
	 * <code>basePackage</code> passed as parameter. Furthermore a
	 * <code>ignoredPackage</code> parameter can be specified, acting as a
	 * additional filter for packages that should be ignored. Both parameters
	 * are optional.
	 * 
	 * @param basePackage
	 *            name of the base package where matching classes should reside
	 *            in
	 * @param ignoredPackage
	 *            name of package that should be ignored, e.g. a sub-package of
	 *            <code>basePackage</code>
	 *
	 * @return list of the names of all classes matching the criteria specified
	 *         by the parameters
	 */
	public static List<String> getLoadedClassNamesListByBasePackage(String basePackage, String ignoredPackage) {
		List<String> classNames = new ArrayList<>();
		for (Class aClass : instrumentation.getAllLoadedClasses()) {
			if (basePackage != null) {
				if (aClass.getName().startsWith(basePackage)) {
					if (ignoredPackage != null) {
						if (!aClass.getName().startsWith(ignoredPackage)) {
							classNames.add(aClass.getName());
						}
					} else {
						classNames.add(aClass.getName());
					}
				}
			} else {
				if (ignoredPackage != null) {
					if (!aClass.getName().startsWith(ignoredPackage)) {
						classNames.add(aClass.getName());
					}
				} else {
					classNames.add(aClass.getName());
				}
			}
		}
		return classNames;
	}
}
