package com.big.instrumentation.exception;

/**
 * A {@link RuntimeException} that is thrown if something goes wrong during a
 * transformation process.<br/>
 * 
 * Created by patrick.kleindienst on 02.06.2015.
 * 
 * @author patrick.kleindienst
 */
public class RuntimeInstrumentationException extends RuntimeException {

	public RuntimeInstrumentationException(String message) {
		super(message);
	}
}
