package com.big.sherlog.integration;

import com.big.sherlog.logger.LoggerProvider;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Provided implementation of {@link BaseCodeIntegrator}, inserting code for
 * performance measuring of method executions.<br/>
 *
 * Created by patrick.kleindienst on 03.06.2015.
 *
 * @author patrick.kleindienst
 */
public class PerformanceCodeIntegrator extends BaseCodeIntegrator {

	// #####################################################
	// # STATIC MEMBERS #
	// #####################################################

	private static final String	SET_START		= "startTime = System.currentTimeMillis();";
	private static final String	SET_STOP		= "stopTime = System.currentTimeMillis();";
	private static final String	LOG_START		= PROVIDED_LOGGER + ".debug(\"Execution started at: \" + java.lang.String.valueOf(startTime));";
	private static final String	LOG_STOP		= PROVIDED_LOGGER + ".debug(\"Execution stopped at: \" + java.lang.String.valueOf(stopTime));";
	private static final String	LOG_TIME_DIFF	= PROVIDED_LOGGER + ".debug(\"Execution took \" + java.lang.String.valueOf(stopTime - startTime) + \" ms\");";

	// #####################################################
	// # INSTANCE METHODS #
	// #####################################################

	/**
	 * Adding some code to create performance measurement log statements
	 * 
	 * @param ctMethod
	 *            method to be modified
	 * @return the modified {@link CtMethod} which can be re-attached to it's
	 *         declaring class after modification
	 */
	@Override
	protected CtMethod enhanceMethodCode(CtMethod ctMethod) {
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
