package com.big.instrumentation.integration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.big.instrumentation.annotation.Transformer;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import com.big.instrumentation.util.InstrumentationUtils;

/**
 * Created by patrick.kleindienst on 10.06.2015.
 */
public class ParamMonitoringCodeIntegrator extends BaseCodeIntegrator {

	@Override
	protected CtMethod enhanceMethodCode(CtClass ctClass, CtMethod ctMethod) {
		try {
			String paramStatement = buildParamLoggingStatement(ctClass.getName(), ctMethod);
			ctMethod.insertAfter(paramStatement + "System.out.println(\"It worked!\");");
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
		return ctMethod;
	}

	private String buildParamLoggingStatement(String className, CtMethod ctMethod) {
		StringBuilder stringBuilder = new StringBuilder();

		try {
			Class<?> aClass = InstrumentationUtils.getLoadedClassByName(className);
			Method method = aClass.getDeclaredMethod(ctMethod.getName(), ctMethodParamsToClassArray(ctMethod));
			for (Class<?> param : method.getParameterTypes()) {
				stringBuilder.append(PROVIDED_LOGGER + ".info(\"Parameter " + param.getName() + " has value: \"" + param.getName() + ");");
			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	private Class[] ctMethodParamsToClassArray(CtMethod ctMethod) {
		List<Class> paramClassesList = new ArrayList<>();
		try {
			CtClass[] parameterTypes = ctMethod.getParameterTypes();
			for (CtClass parameterType : parameterTypes) {
				paramClassesList.add(InstrumentationUtils.getLoadedClassByName(parameterType.getName()));
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		Class[] classes = new Class[paramClassesList.size()];
		return paramClassesList.toArray(classes);
	}
}
