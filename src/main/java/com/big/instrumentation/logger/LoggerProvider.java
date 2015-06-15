package com.big.instrumentation.logger;

import org.apache.log4j.Logger;

/**
 * This class sets up a {@link Logger} instance using <a
 * href="http://logging.apache.org/log4j/1.2/">Apache log4j</a>. This logger can
 * be used for integrating logging statements in existing code.
 * 
 * <p>
 * Created by patrick.kleindienst on 03.06.2015.
 * </p>
 * 
 * @author patrick.kleindienst
 * 
 */
public class LoggerProvider {

	public static Logger	LOGGER	= Logger.getLogger("ByteMinerLogger");

}
