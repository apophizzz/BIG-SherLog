package com.big.sherlog.testAgent;

import java.lang.instrument.Instrumentation;

import com.big.sherlog.service.InstrumentationService;
import com.big.sherlog.transform.PerformanceTransformer;

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
