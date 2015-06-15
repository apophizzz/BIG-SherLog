package com.big.instrumentation.logger;

import org.apache.log4j.Logger;

/**
 * This class sets up a {@link Logger} instance using <a
 * href="http://logging.apache.org/log4j/1.2/">Apache log4j</a>. This logger can
 * be used for integrating logging statements in existing code.<br/>
 * 
 * Created by patrick.kleindienst on 03.06.2015.
 * 
 * @author patrick.kleindienst
 * 
 */
public class LoggerProvider {

	// #####################################################
	// # STATIC MEMBERS #
	// #####################################################

	private static final String	LOGGER_NAME	= "BIG-SherLog-Logger";

	public static Logger		LOGGER		= Logger.getLogger(LOGGER_NAME);

}
