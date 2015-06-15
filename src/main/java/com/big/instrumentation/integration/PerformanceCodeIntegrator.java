package com.big.instrumentation.integration;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Created by patrick.kleindienst on 03.06.2015.
 */
public class PerformanceCodeIntegrator extends BaseCodeIntegrator {

	private static final String	SET_START		= "startTime = System.currentTimeMillis();";
	private static final String	SET_STOP		= "stopTime = System.currentTimeMillis();";
	private static final String	LOG_START		= PROVIDED_LOGGER + ".debug(\"Execution started at: \" + java.lang.String.valueOf(startTime));";
	private static final String	LOG_STOP		= PROVIDED_LOGGER + ".debug(\"Execution stopped at: \" + java.lang.String.valueOf(stopTime));";
	private static final String	LOG_TIME_DIFF	= PROVIDED_LOGGER + ".debug(\"Execution took \" + java.lang.String.valueOf(stopTime - startTime) + \" ms\");";

	@Override
	protected CtMethod enhanceMethodCode(CtClass ctClass, CtMethod ctMethod) {
		try {
			ctMethod.addLocalVariable("startTime", CtClass.longType);
			ctMethod.addLocalVariable("stopTime", CtClass.longType);
			ctMethod.insertBefore(SET_START + LOG_START);
			ctMethod.insertAfter(SET_STOP);
			ctMethod.insertAfter(LOG_STOP);
			ctMethod.insertAfter(LOG_TIME_DIFF);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
		return ctMethod;
	}
}
