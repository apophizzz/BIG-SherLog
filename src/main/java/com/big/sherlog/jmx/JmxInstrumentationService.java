package com.big.sherlog.jmx;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.big.sherlog.annotation.SherlogTransformer;
import com.big.sherlog.scan.TransformerClasspathScanner;
import com.big.sherlog.service.InstrumentationService;
import com.big.sherlog.transform.BaseTransformer;
import com.big.sherlog.transform.ClassMemberMonitoringTransformer;
import com.big.sherlog.util.InstrumentationUtils;
import com.big.sherlog.util.JavassistUtils;

/**
 * This class is exposed as an MBean class through
 * {@link JmxInstrumentationServiceMBean} interface and defines the operations
 * that can be performed via <a
 * href="http://docs.oracle.com/javase/tutorial/jmx/">Java Management
 * Extensions</a>, using e.g. JConsole as a JMX tool.<br/>
 *
 * Created by patrick.kleindienst on 05.06.2015.
 * 
 * @author patrick.kleindienst
 * 
 */
public class JmxInstrumentationService implements JmxInstrumentationServiceMBean {

	// #####################################################
	// # STATIC MEMBERS #
	// #####################################################

	private static final String										IGNORED_PACKAGE	= "com.big.sherlog";

	// #####################################################
	// # INSTANCE MEMBERS #
	// #####################################################

	private String													basePackage;
	private InstrumentationService									instrumentationService;
	private List<Class<? extends BaseTransformer>>					transformerList;
	private Map<Class<? extends BaseTransformer>, BaseTransformer>	transformerCache;

	public JmxInstrumentationService(Instrumentation instrumentation) {
		this.instrumentationService = new InstrumentationService(instrumentation, new ClassMemberMonitoringTransformer());
		this.transformerCache = new HashMap<>();
	}

	// #####################################################
	// # CONCERN: BASE PACKAGE #
	// #####################################################

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBasePackage() {
		return basePackage;
	}

	// #####################################################
	// # CONCERN: CLASS & METHOD INFO #
	// #####################################################

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> listClassNamesByBasePackage() {
		return InstrumentationUtils.getLoadedClassNamesListByBasePackage(basePackage, IGNORED_PACKAGE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> listMethodNamesForClass(String className) {
		List<String> methodNames = new ArrayList<>();
		Class loadedClass = InstrumentationUtils.getLoadedClassByName(className);

		for (Method method : loadedClass.getDeclaredMethods()) {
			methodNames.add(method.getName());
		}
		return methodNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> listSignaturesForMethod(String className, String methodName) {
		List<String> signatures = new ArrayList<>();
		Class loadedClass = InstrumentationUtils.getLoadedClassByName(className);

		for (Method method : loadedClass.getDeclaredMethods()) {
			if (method.getName().equalsIgnoreCase(methodName)) {
				signatures.addAll(JavassistUtils.getSignaturesForMethod(loadedClass.getName(), methodName));
			}
		}
		return signatures;
	}

	// #####################################################
	// # CONCERN: INSTRUMENTATION #
	// #####################################################

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void instrumentMethod(String className, String methodName, String methodSignature) {
		instrumentationService.doInstrumentation(className, methodName, methodSignature);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void instrumentMethod(String className, String methodName) {
		instrumentationService.doInstrumentation(className, methodName, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetClassTransformation(String className) {
		for (Class aClass : instrumentationService.getInstrumentation().getAllLoadedClasses()) {
			if (aClass.getName().equalsIgnoreCase(className)) {
				instrumentationService.restoreClass(aClass);
			}
		}
	}

	// #####################################################
	// # CONCERN: AVAILABLE TRANSFORMATIONS & SELECTION #
	// #####################################################

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> listAvailableTransformations() {
		List<String> transformersAvailable = new ArrayList<>();

		if (transformerList == null) {
			setupTransformerList();
		}

		for (int i = 0; i < transformerList.size(); i++) {
			transformersAvailable.add("[" + i + "] " + transformerList.get(i).getSimpleName());
		}
		return transformersAvailable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectTransformer(int index) {
		if (transformerList == null) {
			setupTransformerList();
		}
		instrumentationService.setTransformer(findTransformer(index));
	}

	// ###########################################################################
	// # UTIL METHODS #
	// ###########################################################################

	/**
	 * Convenience method for fetching a certain {@link BaseTransformer}
	 * implementation. If no instance has been created yet, an object is
	 * instantiated via Reflection API and afterwards gets saved in a cache. If
	 * it's already existing, it's directly fetched from this cache.
	 *
	 * @param index
	 *            list index of {@link BaseTransformer} instance
	 * @return selected {@link BaseTransformer} instance
	 */
	private BaseTransformer findTransformer(int index) {
		if (isValidTransformerIndex(index)) {
			if (!transformerCache.containsKey(transformerList.get(index))) {
				try {
					BaseTransformer baseTransformer = transformerList.get(index).newInstance();
					transformerCache.put(transformerList.get(index), baseTransformer);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return transformerCache.get(transformerList.get(index));
		}
		return null;
	}

	private boolean isValidTransformerIndex(int index) {
		if (index >= 0 && index < transformerList.size()) {
			return true;
		}
		return false;
	}

	/**
	 * Convenience method for collecting all currently loaded
	 * {@link BaseTransformer} implementations. A classpath scan is executed at
	 * the beginning to make sure that every {@link BaseTransformer} subclass
	 * annotated with {@link SherlogTransformer} is
	 * actually loaded.
	 *
	 */
	private void setupTransformerList() {
		TransformerClasspathScanner.loadTransformerClasses();
		transformerList = new ArrayList<>();
		for (Class aClass : instrumentationService.getInstrumentation().getAllLoadedClasses()) {
			if (BaseTransformer.class.isAssignableFrom(aClass) && !aClass.getName().equalsIgnoreCase(BaseTransformer.class.getName())) {
				transformerList.add(aClass);
			}
		}
	}

}
