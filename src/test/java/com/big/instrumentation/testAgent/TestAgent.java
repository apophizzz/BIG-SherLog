package com.big.instrumentation.testAgent;

import java.lang.instrument.Instrumentation;

import com.big.instrumentation.service.InstrumentationService;
import com.big.instrumentation.transform.PerformanceTransformer;

/**
 * Created by patrick.kleindienst on 03.06.2015.
 */
public class TestAgent {

	public static InstrumentationService	instrumentationService;

	public static void premain(String agentArgs, Instrumentation instrumentation) {
		PerformanceTransformer transformer = new PerformanceTransformer();
		instrumentationService = new InstrumentationService(instrumentation, transformer);
	}
}
